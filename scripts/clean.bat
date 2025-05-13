@echo off
echo Cleaning project...

REM Supprimer les répertoires de compilation
rmdir /s /q bin
rmdir /s /q classes

REM Supprimer les fichiers temporaires et de log
del /q *.log
del /q *.tmp
del /q *.bak
del /q *.swp
del /q *~
del /q apt.dat
del /q structure.txt

echo Done.
pause
