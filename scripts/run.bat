@echo off
REM Décompression des fichiers volumineux
echo Décompression des fichiers volumineux...
powershell -Command "Expand-Archive -Path apt.dat.zip -DestinationPath . -Force"
powershell -Command "Expand-Archive -Path libjfxwebkit.so.zip -DestinationPath .\lib -Force"

REM Compilation
echo Compilation des fichiers Java...
javac --module-path lib --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.media,javafx.web -cp "src;lib/*;ingescape/jar/*;ingescape/libs/*" -d bin src\main\*.java src\AgentCB\*.java src\XPlaneConnect\*.java src\XPlaneConnect\discovery\*.java src\uk\me\jstott\jcoord\**\*.java

REM Exécution
echo Lancement de l'application...
java --module-path lib --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.media,javafx.web -cp "bin;lib/*;ingescape/jar/*;ingescape/libs/*" main.Main
