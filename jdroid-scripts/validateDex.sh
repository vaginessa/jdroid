#!/bin/sh

# Count the methods in dex file. Dex files allows to have until 65536 methods maximum.

DEX_PATH=$1

# Initialize error var
status="OK"
# Method count threshold
threshold=64000

# Calculate method count
methodcount=`(cat $DEX_PATH | head -c 92 | tail -c 4 | hexdump -e '1/4 "%d\n"')`

# Verify thresold
if [ $methodcount -gt $threshold ]
then
        status="WARNING"
fi

# Show method count
methodcountmsg='Method count in '$DEX_PATH': '$methodcount' of 65536'
echo "*************************************************************************"
echo "* Status: $status. $methodcountmsg"
echo "*************************************************************************"
