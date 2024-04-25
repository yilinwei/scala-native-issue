#include <assert.h>
#include "api.h"
int main() {
  assert(ScalaNativeInit() == 0);
  lib_bar();
}
