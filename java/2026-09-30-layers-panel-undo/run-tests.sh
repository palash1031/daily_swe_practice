#!/bin/sh
# Compiles everything and runs every *Test class in src/test/java. Needs JDK 17+.
set -e
cd "$(dirname "$0")"

CP="../lib/junit-4.13.2.jar:../lib/hamcrest-core-1.3.jar"
rm -rf out
mkdir -p out
javac -d out -cp "$CP" src/main/java/*.java src/test/java/*.java

TESTS=$(cd src/test/java && ls *Test.java | sed 's/\.java$//')
java -cp "out:$CP" org.junit.runner.JUnitCore $TESTS
