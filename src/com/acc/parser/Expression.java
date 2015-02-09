package com.acc.parser;

import com.acc.constants.OperationCode;
import com.acc.data.Code;
import com.acc.data.Operator;
import com.acc.data.Result;
import com.acc.data.Token;
import com.acc.util.AuxiliaryFunctions;
import com.acc.util.Tokenizer;

/**
 * Created by prabhuk on 1/24/2015.
 */
public class Expression extends Parser {

    public Expression(Code code, Tokenizer tokenizer) {
        super(code, tokenizer);
    }

    @Override
    public Result parse() {
        Result x, y;
        final Term term = new Term(code, tokenizer);
        x = term.parse();
        while (tokenizer.hasNext()) {
            final Token next = tokenizer.next();
            Operator nextOperator;
            while (next.tokenType().isOperator()) {
                nextOperator = (Operator) next;
                int instructionCode;
                if (nextOperator.value().isPlus()) {
                    instructionCode = OperationCode.ADD;
                } else if (nextOperator.value().isMinus()) {
                    instructionCode = OperationCode.SUB;
                } else {
                    break;
                }
                y = term.parse();
                AuxiliaryFunctions.combine(code, instructionCode, x, y);
            }
        }
        return x;
    }
}
