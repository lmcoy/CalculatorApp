package de.lennart_oymanns.calculator;

/**
 * Created by lo on 01.05.17.
 */

public class Result {
    private String expression = "";
    private String result = "";

    public Result(String expr, String res) {
        expression = expr;
        result = res;
    }

    public String GetExpression() {
        return expression;
    }

    public String GetResult() {
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Result) {
            Result o = (Result) obj;
            return expression.equals(o.expression) && result.equals(o.result);
        }
        return super.equals(obj);
    }
}
