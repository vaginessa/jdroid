#!/bin/sh

FILE=$1

if [ -f "$FILE" ]
then
	todo=`cat "$FILE" | grep "TODO"`

	exitCode=0
	if [ -n "$todo" ]
	then
	
		echo "**************************************************************"
		echo "* Status: ERROR. Missing translations on $FILE"
		echo "**************************************************************"
		cat "$FILE" | grep "TODO"
	
		exit 1
	fi

echo "**************************************************************"
echo "* Status: OK. Not Missing translations on $FILE"
echo "**************************************************************"

fi
exit 0
	
