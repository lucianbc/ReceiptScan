#!/bin/bash
for f in ./*/*.tex; do aspell --lang=ro --mode=tex check $f; done