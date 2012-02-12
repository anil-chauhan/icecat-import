@echo off

rem *** Bat file for building ICECat import modules ***
rem *** Author: Anykey Skovorodkin ***

echo Compile api...

call ant -buildfile .\java\build.xml BuildAll
IF ERRORLEVEL 1 GOTO FAIL

echo publishing API files to plugins
copy .\java\packages\* .\plugins\emission_framework\lib\

echo Compile plugins...

call ant -buildfile .\plugins\emission_framework\build.xml BuildAll
IF ERRORLEVEL 1 GOTO FAIL

echo **************************************
echo All projects compiled without errors
GOTO OK

:FAIL
echo ************************************************
echo ICECat import projects compiled with errors

:OK