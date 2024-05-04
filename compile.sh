#!/usr/bin/env bash

ar t target/scala-2.13/libfoo.a | grep unwind | xargs ar d target/scala-2.13/libfoo.a
clang -Wl,-E test.c -L ./target/scala-2.13 -lfoo -lstdc++ -lm -lgcc -lgcc_s
