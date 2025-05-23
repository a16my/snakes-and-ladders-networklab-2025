#!/bin/bash

echo " Building CLIENT..."

# Replace mainClass with client
sed -i '' 's/<mainClass>.*<\/mainClass>/<mainClass>client.SnakesAndLaddersGame<\/mainClass>/' pom.xml

# Build the client JAR
mvn clean package

# Rename it for clarity
cp target/project1-1.0-SNAPSHOT.jar target/client.jar

echo " Client JAR built: target/client.jar"

