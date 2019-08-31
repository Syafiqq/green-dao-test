#!/usr/bin/env bash
adb shell "run-as package.name chmod 666 /data/data/package.name/databases/file"
adb exec-out run-as package.name cat databases/file > newOutFileName
adb shell "run-as package.name chmod 600 /data/data/package.name/databases/file"