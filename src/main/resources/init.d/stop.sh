#!/bin/sh

echo "Shutting down ${project.artifactId} ...";

PID=`cat /tmp/${project.artifactId}.pid | cut -d: -f1`
kill -HUP $PID
sleep 3

if [ "`ps -o comm= -p $PID | wc -l`" -gt 0 ]; then
  while [ "`ps -o comm= -p $PID | wc -l`" -gt 0 ]; do
    echo "${project.artifactId} is still running, killing ...";
    kill -9 $PID;
    sleep 1;
  done;

  echo "${project.artifactId} has been killed with [-9]";
else
  echo "${project.artifactId} has been killed with [HUP]";
fi;

rm /tmp/${project.artifactId}.pid
