{
  description = "A very basic flake";

  inputs = {
    flake-utils.url = github:numtide/flake-utils;
    nixpkgs.url = github:NixOS/nixpkgs/nixos-23.11;
  };

  outputs = { self, nixpkgs, flake-utils }:
    flake-utils.lib.eachDefaultSystem(system: let
      pkgs = import nixpkgs { inherit system; };
    in {
      devShell = with pkgs;
        mkShell {
          packages = [
            llvmPackages_15.clang
            llvmPackages_15.lldb
            pkg-config
            sbt
            openjdk
          ];
        };
    });
}
