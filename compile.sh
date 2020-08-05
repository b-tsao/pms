#!/usr/bin/env bash

mkdir tmp
javac -cp 'dist/*' -d tmp `find src -iname '*.java'`
cd tmp
jar cvf argonms.jar *
cd ..
mv tmp/argonms.jar .
rm -rf tmp
