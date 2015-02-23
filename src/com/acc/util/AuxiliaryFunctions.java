package com.acc.util;

import com.acc.constants.Condition;
import com.acc.constants.Kind;
import com.acc.constants.OperationCode;
import com.acc.data.*;
import com.acc.memory.RegisterAllocator;
import com.acc.structure.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by prabhuk on 1/14/2015.
 * The auxilary methods to cre
 */
public class AuxiliaryFunctions {

    public static void BJ(Code code, int loc) {
        final Result x = new Result(Kind.CONSTANT);
        x.value(loc);
        addInstruction(OperationCode.bra, code, x, null, null);
    }

    public static void FJLink(Code code, Result follow) {
//        final Symbol symbol = new Symbol(String.valueOf(x.fixupLoc()), code.getPc(), null, false, x.fixupLoc());
//        symbol.setResult(x);
        Result equal = new Result(Kind.CONSTANT);
        equal.value(0);
        Result branchLocation = new Result(Kind.CONSTANT);
        branchLocation.value(0);
        addInstruction(OperationCode.beq, code, equal, branchLocation, null);
        follow.fixupLoc(code.getPc() - 1);
    }

    public static void CJF(Code code, Result currentState, SymbolTable symbolTable) {
        Result x = new Result(Kind.INTERMEDIATE);
        x.setIntermediateLoation(code.getPc() - 1);
        Result y = new Result(Kind.CONSTANT);
        y.value(0);
        AuxiliaryFunctions.addInstruction(Condition.getNegatedInstruction(currentState.condition()), code, x, y, symbolTable);
        currentState.fixupLoc(code.getPc() - 1);
    }
    

    
    /*
     * Combines x & y and resultant result obj is maintained in x.
     */
    public static void combine(Code code, String op, Result x, Result y) {
        if (x.kind().isConstant() && y.kind().isConstant()) {
            if (op.equals(OperationCode.add)) {
                x.value(x.value() + y.value());
            } else if (op.equals(OperationCode.sub)) {
                x.value(x.value() - y.value());
            } else if (op.equals(OperationCode.mul)) {
                x.value(x.value() * y.value());
            } else if (op.equals(OperationCode.div)) {
                x.value(x.value() / y.value());
            } else {
                throw new UnsupportedOperationException("Combine cannot process Operation code [" + op + "]");
            }
        }
    }


    public static void declareSymbol(String symbolName, SymbolTable symbolTable, SymbolType type, List<Integer> arrayDimensions) {
        final Symbol s;
        if (type == SymbolType.ARRAY) {
            s = new Symbol(symbolName, -1, arrayDimensions.size(), null);
            final int dimensionCount = arrayDimensions.size();
            int[] dimensionsArray = new int[dimensionCount];
            for (int i = 0; i < arrayDimensions.size(); i++) {
                dimensionsArray[i] = arrayDimensions.get(i);
            }
            s.setArrayDimension(dimensionCount);
            s.setArrayValue(Array.newInstance(Integer.class, dimensionsArray));
        } else {
            s = new Symbol(symbolName, -1,null);
        }
        symbolTable.addSymbol(s);
    }

    public static void removeInstruction(Code code, BasicBlock node) {
        final List<Instruction> instructions = node.getInstructions();
        final List<Instruction> remove = new ArrayList<Instruction>();
        for (Instruction instruction : instructions) {
            if(instruction.isPhi()) {
                PhiInstruction phi = (PhiInstruction) instruction;
                if(phi.canIgnore()) {
                    remove.add(instruction);
                }
            }
        }
        for (Instruction instruction : remove) {
            code.removeCode(instruction);
            node.getInstructions().remove(instruction);
            node.removePhiInstruction(instruction.getSymbol().getName());
        }
    }

    public static void addInstruction(int op, Code code, Result x, Result y, SymbolTable symbolTable) {
        final Instruction instruction = new Instruction(op, x, y, code.getPc());
        if(x.kind().isVariable() || x.kind().isArray()) {
            final Symbol recentOccurence = symbolTable.getRecentOccurence(x.getVariableName());
            instruction.setSymbol(recentOccurence);
        }

        code.addCode(instruction);
    }

    public static void addMoveInstruction(Code code, Result x, Result y, SymbolTable symbolTable) {
        addToSymbolTable(code, symbolTable, x);
        addInstruction(OperationCode.move, code, x, y, symbolTable);
    }

    private static void addToSymbolTable(Code code, SymbolTable symbolTable, Result x) {
        final Symbol recent = symbolTable.getRecentOccurence(x.getVariableName());
        final Symbol symbol;
        if(recent.getType().isArray()) {
            symbol = new Symbol(recent.getName(), code.getPc(), recent.getArrayDimension(), Symbol.cloneValue(recent.getValue()));
        } else {
            symbol = new Symbol(recent.getName(), code.getPc(), recent.getValue());
        }
        symbolTable.addSymbol(symbol);
    }
}
