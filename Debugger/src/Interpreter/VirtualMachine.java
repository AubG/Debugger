package Interpreter;

import java.util.*;
import Interpreter.ByteCode.*;

public class VirtualMachine {
    protected Program program;
    protected RunTimeStack runStack;
    protected Stack<Integer> returnAddr;
    protected int pc;
    protected boolean isRunning;
    protected boolean dumpState;
    
    public VirtualMachine(Program prgrm){
        program = prgrm;
    }
    
    public void executeProgram(){
        pc = 0;
        runStack = new RunTimeStack();
        returnAddr = new Stack();
        isRunning = true;
        dumpState = false;
        while(isRunning){
            ByteCode code = program.getCode(pc);
            code.execute(this);
            if (!(code instanceof DumpCode)) {    
                if (dumpState) {
                    System.out.println(code.toString());               
                    runStack.dump();   
                }
            }
            pc++;      
        }
    }
    
    public void setFrame(int args){
        runStack.newFrameAt(args);
    }
    
    public void setDumpOn(boolean state) {
        dumpState = state;
    }
    
    public void changeAddr(int target){
        returnAddr.push(pc);
        pc = target;
    }
    
    public void bop(String binOp){
        int op2 = runStack.pop();
        int op1 = runStack.pop();
        
        if(binOp.equals("+"))
            runStack.push(op1 + op2);
        if(binOp.equals("-"))
            runStack.push(op1 - op2);
        if(binOp.equals("*"))
            runStack.push(op1 * op2);
        if(binOp.equals("/"))
            runStack.push(op1 / op2);
        if(binOp.equals("=="))
            if(op1 == op2)
                runStack.push(1);
            else
                runStack.push(0);            
        if(binOp.equals("!="))
            if(op1 != op2)
                runStack.push(1);
            else
                runStack.push(0);
        if(binOp.equals("<"))
            if(op1 < op2)
                runStack.push(1);
            else
                runStack.push(0);
        if(binOp.equals("<="))
            if(op1 <= op2)
                runStack.push(1);
            else
                runStack.push(0);
        if(binOp.equals(">"))
            if(op1 > op2)
                runStack.push(1);
            else
                runStack.push(0);
        if(binOp.equals(">="))
            if(op1 >= op2)
                runStack.push(1);
            else
                runStack.push(0);
        if(binOp.equals("|"))
            if(op1 == 0 || op2 == 0)
                runStack.push(0);
            else
                runStack.push(1);
        if(binOp.equals("&"))
            if(op1 == 1 && op2 == 1)
                runStack.push(1);
            else
                runStack.push(0);      
    }
    
    public void fBranch(int target){
        if(runStack.pop() == 0)
            pc = target;           
    }
    
    public void goTo(int target){
        pc = target;
    }
    
    public void halt(){
        isRunning = false;
    }
    
    public void pushLit(int lit){
        runStack.push(lit);
    }
    
    public void load(int offset){
        runStack.load(offset);
    }
    
    public void pop(int n){
        for(int i = 0 ; i < n ; i++)
            runStack.pop();
    }
    
    public void read(int n){
        runStack.push(n);
    }
    
   
    
    public int store(int off){
        return runStack.store(off);
    }
    
    public int write(){
        return (runStack.peek());
    }
    
    public int popRA(){
        return returnAddr.pop();
    }
    
    public void setPC(int addr){
        pc = addr;
    }
    
    public void popFrame(){
        runStack.popFrame();
    }
    
    public int peekRunStack(){
        return runStack.peek();
    }
    
    public Vector<Integer> getArgs() {
        return runStack.getArgs();
    }
    
    public int getRunStackSize(){
        return runStack.runStack.size();
    }
}
