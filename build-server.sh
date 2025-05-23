#!/bin/bash

echo "📦 Building SERVER..."

# Replace mainClass with server
sed -i '' 's/<mainClass>.*<\/mainClass>/<mainClass>server.GameServer<\/mainClass>/' pom.xml

# Build the server JAR
mvn clean package

# Rename it
cp target/project1-1.0-SNAPSHOT.jar target/server.jar

echo "✅ Server JAR built: target/server.jar"

