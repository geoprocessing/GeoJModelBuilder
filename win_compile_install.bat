:: tested on Maven 3.3.3, JDK 1.8
@echo off

:: change working directory
cd /d %~dp0

:: compile the parent
call mvn clean install

:: compile GeoJModelBuilderUI module
cd GeoJModelBuilderUI
:: update the dependencies
call mvn dependency:copy-dependencies -DoutputDirectory=lib
call mvn install -Pplugin
call mvn install -Prepo

pause