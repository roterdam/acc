package com.acc.data;

import com.acc.constants.OperationCode;
import com.acc.structure.ControlFlowGraph;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by prabhuk on 1/14/2015.
 * This holds the generated code
 */
public class Code {

    /*
     * List of instructions for the input program
     */
    private final List<Instruction> instructions = new LinkedList<Instruction>();
    private static ControlFlowGraph controlFlowGraph = ControlFlowGraph.getDominatorTree();


    /**
     * @return Returns the current program counter value
     */
    public int getPc() {
        return instructions.size();
    }

    /**
     * @param instruction - Takes an instruction and appends to the output code
     * @return Returns the current program counter value
     */
    public int addCode(Instruction instruction) {
        instruction.setLocation(getPc());
        instructions.add(instruction);
        if(instruction.getOpcode() == OperationCode.PHI) {
            return instructions.size();
        }
        controlFlowGraph.addInstruction(instruction, this);
        return instructions.size();
    }



    public void Fixup(int location) {
        instructions.get(location).FixUp(getPc() - location);
        //$TODO$ this is not updated within the basicblock. Need to fix it in the basic block as well. Use instr number range?
    }

    public void Fixlink(Result follow) {

    }


    public List<Instruction> getInstructions() {
        return instructions;
    }
}
