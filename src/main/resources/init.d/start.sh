#!/bin/sh

basepath=/opt/${project.artifactId}/bin

if [ -f /tmp/${project.artifactId}.pid ]; then
  	echo "${project.artifactId} is currently running";
else
 	$basepath/${project.artifactId} & 
 	echo $! > /tmp/${project.artifactId}.pid;
fi;
