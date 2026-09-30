#!/bin/sh
# Compiles the main code and runs Main. Needs JDK 17+.
set -e
cd "$(dirname "$0")"

rm -rf out
mkdir -p out
javac -d out src/main/java/*.java
java -cp out Main
