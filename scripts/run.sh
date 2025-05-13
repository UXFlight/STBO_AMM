#!/bin/bash
CP="bin:lib/*:ingescape/jar/*:ingescape/libs/*"
MODULES="javafx.controls,javafx.fxml,javafx.graphics,javafx.media,javafx.web"

# Décompresser les fichiers volumineux
echo "Décompression des fichiers volumineux..."
unzip -o apt.dat.zip -d ./
unzip -o libjfxwebkit.so.zip -d ./lib

# Compilation
echo "Compilation des fichiers Java..."
javac --module-path lib --add-modules $MODULES -cp "src:$CP" -d bin \
  src/main/*.java src/AgentCB/*.java src/XPlaneConnect/*.java \
  src/XPlaneConnect/discovery/*.java src/uk/me/jstott/jcoord/**/*.java

# Exécution
echo "Lancement de l'application..."
java --module-path lib --add-modules $MODULES -cp "$CP" main.Main
