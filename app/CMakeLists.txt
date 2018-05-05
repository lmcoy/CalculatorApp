# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)
set (CMAKE_CXX_STANDARD 11)

INCLUDE_DIRECTORIES( "/usr/local/include/" )

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

add_library( # Sets the name of the library.
             native-lib

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
             src/main/cpp/native-lib.cpp
             src/main/cpp/Equation.cpp
             src/main/cpp/Factor.cpp
             src/main/cpp/Function.cpp
             src/main/cpp/Lexer.cpp
             src/main/cpp/Number.cpp
             src/main/cpp/Parser.cpp
             src/main/cpp/Power.cpp
             src/main/cpp/State.cpp
             src/main/cpp/Summand.cpp
             src/main/cpp/Token.cpp
             src/main/cpp/TwoOp.cpp
             src/main/cpp/UnaryMinus.cpp
             src/main/cpp/Variable.cpp
             src/main/cpp/Derivative.cpp
             src/main/cpp/MathFunction.cpp
             src/main/cpp/UserFunction.cpp
             src/main/cpp/NumberRepr.cpp
             src/main/cpp/ComplexNumber.cpp
             )

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
                       native-lib

                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib} )