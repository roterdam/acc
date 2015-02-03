package com.acc.data;

import com.acc.structure.BasicBlock;

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
    private final List<Integer> instructions = new LinkedList<Integer>();
    private final List<BasicBlock> basicBlocks = new LinkedList<BasicBlock>();
    private BasicBlock currentBlock;

    public Code() {
        currentBlock = new BasicBlock();
        basicBlocks.add(currentBlock);
    }

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
    public int addCode(int instruction) {
        instructions.add(instruction);
        currentBlock.addToBlock(instruction);
        if(isBranchInstruction(instruction))
        {
            addBasicBlock();
        }
        return instructions.size();
    }

    /*
     * Creates a new Basic Block to which instructions will be added in the future
     */

    public void addBasicBlock() {
        currentBlock = new BasicBlock();
        basicBlocks.add(currentBlock);
    }

    /*
     * @return Returns the current Basic Block
     */
    public BasicBlock getCurrentBlock() {
        return currentBlock;
    }

}
