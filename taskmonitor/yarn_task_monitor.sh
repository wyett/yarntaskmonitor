#!/bin/bash

#source evn
#source /root/.bash_profile


#global var
RUNNING_USER=hadoop2
YARN_PRE=${HADOOP_HOME}/bin
YARN_BASE=${YARN_PRE}/yarn
SPARK_PRE=${SPARK_HOME}/dba_program
SPARK_CONF=${SPARK_HOME}/conf/spark-defaults.conf
COMMIT1=${SPARK_PRE}/spark_submit_mysqlslowlog_yarn.sh
COMMIT2=${SPARK_PRE}/spark_submit_mysqldbstatus_yarn.sh
COMMIT3=${SPARK_PRE}/spark_submit_mongodbslowlog_yarn.sh

#$JAVA_HOME/bin/java -jar /opt/wyett/taskmonitor-1.0-SNAPSHOT.jar -cp /opt/wyett/lib/
JAVACMD=${JAVA_HOME}/bin/java
WORKDIR=$(cd `dirname $0`; pwd)
JARPACK=taskmonitor-1.0-SNAPSHOT.jar 

# LOG
LOGFILE=/var/log/taskmonitor.log

#############################################
#tool func
execCMD() {
  declare _cmd=$1
  su - "${RUNNING_USER}" -c "${_cmd}"
}

execYarnCMD() {
  declare _f=$1
  declare _cmd="${YARN_BASE} application -${_f}"
  su - "${RUNNING_USER}" -c "${_cmd}"
}

log() {
  declare _log=$1
  echo "$(date +%F@%T) $_log" >> $LOGFILE
}

# get yarn task
getYarnRunningTaskList() {
  declare _f="list"
  execYarnCMD 'list' | grep '^application'
}


getYarnRunningTaskIdByQueue() {
  declare _q=$1
  execYarnCMD 'list' \
    | grep '^application' \
    | grep "${_q}" \
    | awk '{print $1}' 
}

# kill task
killYarnTaskById() {
  declare _id=$1

  if [ -n ${_id} ];then
    execCMD "${YARN_BASE} application -kill ${_id}"
  fi
}

# commit task
commitTask() {
  declare _q=$1
  if [ "$_q" = "mysqlslowlog" ];then
    execCMD "$(enableMySqlSlowlogConf)"
    execCMD "${COMMIT1}"
  fi
  if [ "$_q" = "mysqldbstatus" ];then
    execCMD "$(disableMySqlSlowlogConf)"
    execCMD "${COMMIT2}"
  fi
  if [ "$_q" = "mongodbslowlog" ];then
    execCMD "$(disableMySqlSlowlogConf)"
    execCMD "${COMMIT3}"
  fi
}


enableMySqlSlowlogConf() {
  sed -i '/^#spark.yarn.jars.*mysqlslowlog.*/s/^#*//g' ${SPARK_CONF}
  sed -i '/spark.yarn.jars.*spark.*/s/^/#/g' ${SPARK_CONF}
}

disableMySqlSlowlogConf() {
  sed -i '/^spark.yarn.jars.*mysqlslowlog.*/s/^/#/g' ${SPARK_CONF}
  sed -i '/spark.yarn.jars.*spark.*/s/^#*//g' ${SPARK_CONF}
}

castCollToQueue() {
  declare _c=$1
  if [ ${_c} = 'mysql_slowlog' ];then
    echo "mysqlslowlog"
  fi

  if [ ${_c} = 'db_query_response' ];then
    echo "mysqldbstatus"
  fi
}
############################################

#$JAVA_HOME/bin/java -jar /opt/wyett/taskmonitor-1.0-SNAPSHOT.jar -cp /opt/wyett/lib/

getDruidData() {
  ${JAVACMD} -jar ${JARPACK} -cp ./lib/ \
        | grep "^MainMonitor result" \
        | cut -d ":" -f 2 | sed "s/\[//g;s/\]//g"
}


############################################

recommitTask() {
  declare _c=$1
  declare _q=$(castCollToQueue ${_c})

  # get task id
  if [ -n "${_q}" ];then
    taskId=$(getYarnRunningTaskIdByQueue "${_q}")
  fi

  if [ -n ${taskId} ];then
    killYarnTaskById ${taskId}
  fi

  commitTask "${_q}"
}

main() {
  declare _d=$(getDruidData)
  echo "${_d}"

  if [ -z "${_d}" ];then
    log "yarn task normal"
  fi

  if [ -n "${_d}" ];then
    echo ${_d} | sed 's/\"//g' | tr "," "\n" | while read TNAME
    do
      log "restart task ${TNAME}"
      recommitTask $TNAME
    done
  fi
}

main

#eof
