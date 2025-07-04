@echo off
setlocal enabledelayedexpansion

REM Définir les variables
set "CP=bin;lib/*;ingescape/jar/*;ingescape/libs/*"
set "JAVAFX=javafx-sdk-21\lib"
set "MODULES=javafx.controls,javafx.fxml,javafx.graphics,javafx.media,javafx.web"

REM Décompression des fichiers volumineux
echo Décompression des fichiers volumineux...
if exist "apt.dat.zip" (
    powershell -Command "Expand-Archive -Path 'apt.dat.zip' -DestinationPath '.' -Force"
) else (
    echo Warning: apt.dat.zip not found, skipping extraction
)

REM Extraire libjfxwebkit.so sans sous-dossier
if exist "libjfxwebkit.so.zip" (
    mkdir temp_extract >nul 2>&1
    powershell -Command "Expand-Archive -Path 'libjfxwebkit.so.zip' -DestinationPath 'temp_extract' -Force"
    for /r "temp_extract" %%f in (libjfxwebkit.so) do (
        move /Y "%%f" "lib\" >nul
    )
    rmdir /s /q temp_extract
) else (
    echo Warning: libjfxwebkit.so.zip not found, skipping extraction
)

REM Compilation
echo Compilation des fichiers Java...

set "SOURCES="

for %%f in (src\main\*.java src\AgentCB\*.java src\XPlaneConnect\*.java src\XPlaneConnect\discovery\*.java) do (
    set "SOURCES=!SOURCES! %%f"
)

for /R src\uk\me\jstott\jcoord %%f in (*.java) do (
    set "SOURCES=!SOURCES! %%f"
)

javac --module-path "%JAVAFX%;lib" --add-modules %MODULES% -cp "src;%CP%" -d bin !SOURCES!

REM Exécution
echo Lancement de l'application...
java --module-path "%JAVAFX%" --add-modules %MODULES% ^
     -cp "%CP%" ^
     -Dprism.order=sw ^
     -Dprism.verbose=true ^
     main.Main

endlocal
