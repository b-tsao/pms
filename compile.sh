#!/usr/bin/env bash

mkdir tmp
javac -cp 'dist/*' -d tmp `find src -iname '*.java'`
cd tmp
jar cvf MapleVintage.jar *
cd ..
mv tmp/MapleVintage.jar dist
rm -rf tmp
