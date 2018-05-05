#include "gtest/gtest.h"

#include "Equation.hpp"

std::string eval(const std::string &expression) {
    Equation::Equation eq;
    eq.Set(expression);
    return eq.Evaluate();
}

/*std::string erase_space(const std::string &s) {
  std::string s2(s);
  s2.erase(std::remove_if(s2.begin(), s2.end(), std::isspace), s2.end());
  return s2;
}*/

TEST(Equation, Numbers
) {
EXPECT_DOUBLE_EQ(atof(eval("4").c_str()),
4.0);
EXPECT_DOUBLE_EQ(atof(eval("-54.2").c_str()),
-54.2);
}

TEST(Equation, Addition
) {
EXPECT_DOUBLE_EQ(atof(eval("4+3").c_str()),
7.0);
EXPECT_DOUBLE_EQ(atof(eval("4-3").c_str()),
1.0);
EXPECT_DOUBLE_EQ(atof(eval("-4+3-2").c_str()),
-3.0);
}

TEST(Equation, Multiplication
) {
EXPECT_DOUBLE_EQ(atof(eval("4*3").c_str()),
12.0);
EXPECT_DOUBLE_EQ(atof(eval("4/3").c_str()),
4.0 / 3.0);
}
