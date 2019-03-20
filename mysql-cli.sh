#! /bin/bash -e

docker run $* -it \
   --name mysqlterm --rm \
   -e MYSQL_HOST=$DOCKER_HOST_IP \
   mysql:5.7.13 \
   sh -c 'exec mysql -h"$MYSQL_HOST"  -uroot -prootpassword -o eventuate'
