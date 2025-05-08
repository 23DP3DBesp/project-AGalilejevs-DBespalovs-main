@echo off
REM
cd /d %~dp0

echo
call mvnw clean package

echo 
call mvnw exec:java -Dexec.mainClass="lv.rvt.Main"

pause