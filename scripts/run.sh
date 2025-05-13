#!/bin/bash
CP="bin:lib/*:ingescape/jar/*:ingescape/libs/*"
MODULES="javafx.controls,javafx.fxml,javafx.graphics,javafx.media,javafx.web"

# Extract large files
echo "Extracting large files..."
if [ -f apt.dat.zip ]; then
  unzip -o apt.dat.zip -d ./
else
  echo "Warning: apt.dat.zip not found, skipping extraction"
fi

# Extract libjfxwebkit.so to ./lib, avoiding nested lib
if [ -f libjfxwebkit.so.zip ]; then
  mkdir -p temp_extract
  unzip -o libjfxwebkit.so.zip -d temp_extract
  find temp_extract -name libjfxwebkit.so -exec mv {} ./lib/ \;
  rm -rf temp_extract
else
  echo "Warning: libjfxwebkit.so.zip not found, skipping extraction"
fi

# Compilation
echo "Compiling Java files..."
javac --module-path lib --add-modules "$MODULES" -cp "src:$CP" -d bin \
  src/main/*.java src/AgentCB/*.java src/XPlaneConnect/*.java \
  src/XPlaneConnect/discovery/*.java src/uk/me/jstott/jcoord/**/*.java

# Execution
echo "Launching the application..."
java --module-path lib --add-modules "$MODULES" -cp "$CP" main.Main