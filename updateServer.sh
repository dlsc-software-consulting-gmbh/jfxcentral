#!/bin/bash

set -e

# SERVER=server1.java4browser.com
SERVER=$1
KEY_FILE=~/.ssh/second-server.pem
PROJECT=jfxcentral

echo "Updating server: '$SERVER'"
mvn -P jpro jpro:release

ssh -i $KEY_FILE -t ubuntu@$SERVER "rm jpro/$PROJECT-jpro.zip"  || true
scp -i $KEY_FILE target/jfxcentral-jpro.zip   ubuntu@$SERVER:jpro/

echo "STOPING OLD SERVER"
ssh -i $KEY_FILE -t ubuntu@$SERVER "./jpro/$PROJECT-jpro/bin/stop.sh"  || true
ssh -i $KEY_FILE -t ubuntu@$SERVER "rm -r ./jpro/$PROJECT-jpro"  || true


ssh -i $KEY_FILE -t ubuntu@$SERVER "cd jpro ; unzip $PROJECT-jpro.zip"
echo "STARTING NEW SERVER"

# export PATH=$PATH:/home/ubuntu/jdk-11.0.1/bin
# export PATH=/home/ubuntu/jdk-11.0.1/bin:$PATH

ssh -i $KEY_FILE  ubuntu@$SERVER "./jpro/$PROJECT-jpro/bin/restart-background.sh  > /dev/null 2> /dev/null < /dev/null &"
# ssh -i $KEY_FILE  ubuntu@$SERVER "export PATH=/home/ubuntu/jdk-11.0.1/bin:$PATH ; ./jpro/$PROJECT-jpro/bin/restart.sh  > /dev/null 2> /dev/null < /dev/null &"
echo "FINISHED UPDATING '$SERVER'"