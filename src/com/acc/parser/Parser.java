package com.acc.parser;

import com.acc.constants.Kind;
import com.acc.data.Code;
import com.acc.data.Result;
import com.acc.data.SSACode;
import com.acc.data.Token;
import com.acc.exception.SyntaxErrorException;
import com.acc.structure.Symbol;
import com.acc.structure.SymbolTable;
import com.acc.structure.SymbolType;
import com.acc.util.AuxiliaryFunctions;
import com.acc.util.Printer;
import com.acc.util.Tokenizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by prabhuk on 1/24/2015.
 */
public abstract class Parser {
    protected Code code;
    protected SSACode ssaCode;
    protected Tokenizer tokenizer;
    private SymbolTable symbolTable;
    private static final SymbolTable globalTable = SymbolTable.getGlobalSymbolTable();
    private final Map<String, List<String>> procedureArguments = new HashMap<String, List<String>>();

    public Parser(Code code, Tokenizer tokenizer) {
        this.code = code;
        this.tokenizer = tokenizer;
        this.ssaCode = ssaCode;
    }

    public SSACode getSsaCode() {
        return ssaCode;
    }

    public static SymbolTable getGlobalTable() {
        return globalTable;
    }

    public Code getCode() {
        return code;
    }

    public SymbolTable getSymbolTable() {
        if (symbolTable != null) {
            return symbolTable;
        }
        return globalTable;
    }

    public void removeLocalSymbolTable() {
        symbolTable = null;
    }

    public void setSymbolTable(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }

    public Map<String, List<String>> getProcedureArguments() {
        return procedureArguments;
    }

    public List<String> getArgumentNamesForProcedure(String procedureName) {
        return procedureArguments.get(procedureName);
    }

    /**
     * For the given procedureName, stores the list of argument names in order.
     * throws SyntaxErrorException when duplicate argument name is passed
     *
     * @param procedureName
     * @param argumentName
     * @return true on successful addition of argument name, false otherwise
     */
    public boolean addArgumentNameForProcedure(String procedureName, String argumentName) {
        if (procedureArguments.get(procedureName) == null) {
            procedureArguments.put(procedureName, new ArrayList<String>());
        }

        List<String> args = procedureArguments.get(procedureName);
        if (args.contains(argumentName)) {
            throw new SyntaxErrorException("Duplicate argument name: [" + argumentName + "] in the procedure [" + procedureName + "]");
        }

        return args.add(argumentName);
    }


    public abstract Result parse();

    protected List<Result> accumulateArrayIdentifiers(Symbol recent) {
        List<Result> arrayIdentifiers = new ArrayList<Result>();
        if (recent.getType().isArray()) {
            for (int i = 0; i < recent.getArrayDimension(); i++) {
                handleOpenBoxBracket();
                arrayIdentifiers.add(new Expression(code, tokenizer).parse());
                handleCloseBoxBracket();
            }
        }
        return arrayIdentifiers;
    }

    private void handleOpenBoxBracket() {
        final Token openBoxBracket = tokenizer.next();
        if (!openBoxBracket.getToken().equals("[")) {
            throw new SyntaxErrorException("Expected '['. Found[" + openBoxBracket.getToken() + "] instead");
        }
    }

    private void handleCloseBoxBracket() {
        final Token closeBoxBracket = tokenizer.next();
        if (!closeBoxBracket.getToken().equals("]")) {
            throw new SyntaxErrorException("Expected ']'. Found[" + closeBoxBracket.getToken() + "] instead");
        }
    }
}
