#!/bin/bash
echo "Cleaning project..."
rm -rf bin/
rm -rf classes/
rm -f *.log *.tmp *.bak *.swp *~ ./lib/libjfxwebkit.so apt.dat
echo "Done."