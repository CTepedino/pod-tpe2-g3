#!/bin/bash

MAIN_CLASS="ar.edu.itba.pod.client.query.Query3AlternativeClient"

java "$@" -cp 'lib/jars/*'  $MAIN_CLASS $*