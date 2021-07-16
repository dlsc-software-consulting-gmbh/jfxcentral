#!/usr/bin/env bash

declare -a arr=("server1.jfx-ensemble.com" "server2.jfx-ensemble.com" "server3.jfx-ensemble.com")

for i in "${arr[@]}"
do
   echo "UPDATING ALL $i"
   ./updateServer.sh $i
   ./updateServerConf.sh $i
   echo "FINISHED ALL FOR $i"
done
