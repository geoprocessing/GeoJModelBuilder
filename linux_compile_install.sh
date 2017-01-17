#!/bin/bash
mvn clean install
cd GeoJModelBuilderUI
mvn dependency:copy-dependencies -DoutputDirectory=lib
mvn install -Pplugin
mvn install -Prepo
echo finished


