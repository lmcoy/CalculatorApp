//
//  Copyright © 2017 Lennart Oymanns. All rights reserved.
//

#include <iostream>

#include "Equation.hpp"

int main(int argc, const char *argv[]) {

    Equation::Equation eq;
    eq.Set("5");
    // eq.WriteInternalRepToStream(std::cout);
    std::cout << eq.Evaluate();
    std::cout << "\n";

    return 0;
}
