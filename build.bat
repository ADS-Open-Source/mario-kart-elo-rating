@echo off

set back=%cd%

cd %back%\mleko & ng build  & cd %back% & mvn clean install
