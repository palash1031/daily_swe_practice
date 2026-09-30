@echo off
rem Compiles the main code and runs Main. Needs JDK 17+.
cd /d "%~dp0"

if exist out rmdir /s /q out
mkdir out
javac -d out src\main\java\*.java
if errorlevel 1 exit /b 1
java -cp out Main
