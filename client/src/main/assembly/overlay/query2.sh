#!/bin/bash

MAIN_CLASS="ar.edu.itba.pod.client.query.Query2Client"

java "$@" -cp 'lib/jars/*'  $MAIN_CLASS $*