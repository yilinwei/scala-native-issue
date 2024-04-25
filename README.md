Clang version 15.0.7
OS: Linux

# Running

The environment is bundled with a nix flake

```sh
sbt nativeLink
./compile.sh
./a.out
```

# Output
```
terminate called after throwing an instance of 'scalanative::ExceptionWrapper'
terminate called recursively
terminate called after throwing an instance of 'scalanative::ExceptionWrapper'
terminate called recursivelyAborted (core dumped)
```
