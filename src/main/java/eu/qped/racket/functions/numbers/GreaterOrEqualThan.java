package eu.qped.racket.functions.numbers;

import eu.qped.racket.buildingBlocks.Expression;

import java.util.List;

public class GreaterOrEqualThan extends Expression {

    @Override
    public String evaluate(Expression e) {
        return evaluate(e.getRest(super.getId()));
        //return evaluate(e.getNext(id), e.getNext(id+1));
    }

    @Override
    public String evaluate(List<Expression> list) {
        Boolean first = true;
        float value = 0;
        for (Expression e : list) {
            float valueNow = Float.valueOf(e.evaluate(this));
            if (first) {
                value = valueNow;
                first = false;
                continue;
            }

            if (!(value >= valueNow))
                return Boolean.toString(false);
            value = valueNow;
        }
        return Boolean.toString(true);
    }

    @Override
    public String toString() {
        return "GreaterThanOrEqualThan" + "(" + super.getId() + ")";
    }
}
