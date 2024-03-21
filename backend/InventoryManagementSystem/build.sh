#!/bin/sh

cd ./InventoryManagementSystem-Base
./gradlew clean build publishToMavenLocal
cd ..


cd ./AuthMicroService
./gradlew clean build
cd ..

cd ./ProductMicroService
./gradlew clean build
cd ..

cd ./SaleMicroService
./gradlew clean build
cd ..


docker-compose build
docker-compose up -d
