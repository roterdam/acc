package com.acc.parser;

import com.acc.constants.Kind;
import com.acc.data.Code;
import com.acc.data.Keyword;
import com.acc.data.Result;
import com.acc.data.Token;
import com.acc.exception.SyntaxErrorException;
import com.acc.structure.SymbolTable;
import com.acc.util.Tokenizer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prabhuk on 2/12/2015.
 */
public class TypeDeclaration extends Parser {

    public TypeDeclaration(Code code, Tokenizer tokenizer, SymbolTable symbolTable) {
        super(code, tokenizer, symbolTable);
    }

    @Override
    public Result parse() {
        Result x = new Result();
        Kind kind;
        Token next = tokenizer.next();
        List<Integer> dimensions = null;
        if (next.isKeyword() && ((Keyword) next).isVar()) {
            x.kind(Kind.VAR);
            //$TODO$ set address
        } else if (next.isKeyword() && ((Keyword) next).isArray()) {
            x.kind(Kind.ARRAY);
            dimensions = new ArrayList<Integer>();
            next = tokenizer.next();
            while (next.getToken().equals("[")) {
                next = tokenizer.next();
                if (!next.isConstant()) {
                    throw new SyntaxErrorException("Number expected for array dimension declaration. Found [" + next.getToken() + "] instead");
                }
                dimensions.add(Integer.parseInt(next.getToken()));
                next = tokenizer.next();
                if (!isClosedBracket(next)) {
                    throw new SyntaxErrorException("Symbol \"]\" expected for array declaration. Found [" + next.getToken() + "] instead");
                }
                next = tokenizer.next();
            }
            if (dimensions.size() == 0) {
                throw new SyntaxErrorException("Symbol \"[\" expected for array declaration. Found [" + next.getToken() + "] instead");
            }
            x.setDimensions(dimensions);
            tokenizer.previous(); // fixing the tokenizer to refer back to the previous variable
        } else if (next.isKeyword() && (((Keyword) next).isProcedure() || ((Keyword) next).isFunction())) {
            x.kind(Kind.PROCEDURE);
        } else {
            x.kind(null);
//            throw new SyntaxErrorException("Keyword \"var\" or \"array\" expected for type declaration. Found [" + next.getToken() + "] instead");
        }
        return x;
    }

    private boolean isClosedBracket(Token next) {
        return next.getToken().equals("]");
    }

}
