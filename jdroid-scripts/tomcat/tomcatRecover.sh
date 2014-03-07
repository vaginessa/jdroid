#! /bin/sh

# crontab -e
# monitor tomcat every 15 minutes
# */15 * * * * sudo sh /path/to/tomcatRecover.sh

LOG_PATH=$1
SERVICE=/etc/init.d/tomcat
STOPPED_MESSAGE="Tomcat servlet engine is not running."

if [ "`$SERVICE status`" = "$STOPPED_MESSAGE" ]
then
	$SERVICE start >> $LOG_PATH/recovery.$(date +"%Y-%m-%d.%H:%M").log
fi
