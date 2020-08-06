#!/usr/bin/env bash

rm -rf dist
mkdir tmp
javac -cp 'dist/*' -d tmp `find src -iname '*.java'`
cd tmp
jar cvf KvjBin.jar *
cd ..
mkdir dist
mv tmp/KvjBin.jar dist
rm -rf tmp
