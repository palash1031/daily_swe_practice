@echo off
rem Compiles everything and runs every *Test class in src\test\java. Needs JDK 17+.
setlocal enabledelayedexpansion
cd /d "%~dp0"

set CP=..\lib\junit-4.13.2.jar;..\lib\hamcrest-core-1.3.jar
if exist out rmdir /s /q out
mkdir out
javac -d out -cp "%CP%" src\main\java\*.java src\test\java\*.java
if errorlevel 1 exit /b 1

set TESTS=
for %%f in (src\test\java\*Test.java) do set TESTS=!TESTS! %%~nf
java -cp "out;%CP%" org.junit.runner.JUnitCore!TESTS!
