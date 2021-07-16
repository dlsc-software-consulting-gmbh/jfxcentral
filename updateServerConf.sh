#!/bin/bash

set -e

# SERVER=server1.java4browser.com
SERVER=$1
KEY_FILE=~/.ssh/second-server.pem
NGINX_CONF=jfx-central.com.nginx.conf


 ssh -i $KEY_FILE -t ubuntu@$SERVER "rm $NGINX_CONF"  || true
 ssh -i $KEY_FILE -t ubuntu@$SERVER "rm .htpasswd"  || true
 scp -r -i $KEY_FILE conf/nginx/$NGINX_CONF ubuntu@$SERVER:.
 scp -r -i $KEY_FILE conf/nginx/.htpasswd ubuntu@$SERVER:.
 ssh -i $KEY_FILE -t ubuntu@$SERVER "sudo cp -f $NGINX_CONF /etc/nginx/conf.d"  || true


echo "RESTARTING NGINX"
ssh -i $KEY_FILE -t ubuntu@$SERVER  "sudo service nginx restart"
echo "FINISHED update conf $SERVER"
