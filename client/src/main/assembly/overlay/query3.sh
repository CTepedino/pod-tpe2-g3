#!/bin/bash

MAIN_CLASS="ar.edu.itba.pod.client.query.Query3Client"

java "$@" -cp 'lib/jars/*'  $MAIN_CLASS $*