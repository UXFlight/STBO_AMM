@echo off
setlocal

echo Cleaning project...

REM Supprimer les répertoires de compilation
if exist bin rmdir /s /q bin
if exist classes rmdir /s /q classes

REM Supprimer les fichiers temporaires et de log
del /q *.log 2>nul
del /q *.tmp 2>nul
del /q *.bak 2>nul
del /q *.swp 2>nul
del /q *~ 2>nul
del /q apt.dat 2>nul
del /q .\lib\libjfxwebkit.so 2>nul

echo Done.

endlocal
