#include <jni.h>
#include <string>
#include <sstream>
#include <complex>

#include "Equation.hpp"

std::string ConvertJString(JNIEnv *env, jstring str) {
    if (!str) std::string();

    const jsize len = env->GetStringUTFLength(str);
    const char *strChars = env->GetStringUTFChars(str, (jboolean *) 0);

    std::string Result(strChars, len);

    env->ReleaseStringUTFChars(str, strChars);

    return Result;
}

Equation::StatePtr state = std::make_shared<Equation::DefaultState>();
Equation::NodePtr ANS;

extern "C"
JNIEXPORT jstring JNICALL
Java_de_lennart_1oymanns_calculator_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */, jstring expr, bool numeric) {
    Equation::Equation eq;
    std::stringstream errorstream;
    bool s = eq.Set(ConvertJString(env, expr), errorstream);
    if (!s) {
        return env->NewStringUTF(errorstream.str().c_str());
    }
    try {
        std::string result = eq.Evaluate(numeric, state);
    } catch (const std::exception &e) {
        std::string expr = "error: ";
        expr += e.what();
        return env->NewStringUTF(expr.c_str());
    }

    std::string latex = eq.ToLatex();
    auto it = std::find(latex.begin(), latex.end(), '\\');
    while (it != latex.end()) {
        auto it2 = latex.insert(it, '\\');
        it = std::find(it2 + 2, latex.end(), '\\');
    }
    state->SetVariable("ANS", eq.Node());
    ANS = eq.Node();
    return env->NewStringUTF(latex.c_str());
}

extern "C"
JNIEXPORT void JNICALL
Java_de_lennart_1oymanns_calculator_MainActivity_SetVariableJNI(
        JNIEnv *env,
        jobject /* this */, jstring name) {
    if (!ANS) {
        return;
    }
    state->SetVariable(ConvertJString(env, name), ANS->clone());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_de_lennart_1oymanns_calculator_MainActivity_GetAnsJNI(
        JNIEnv *env,
        jobject /* this */) {
    if (!ANS) { return env->NewStringUTF(""); }
    return env->NewStringUTF(ANS->ToString().c_str());
}