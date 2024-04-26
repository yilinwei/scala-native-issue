#!/usr/bin/env bash

clang test.c -L ./target/scala-2.13 -lfoo-out -lstdc++ -lm

