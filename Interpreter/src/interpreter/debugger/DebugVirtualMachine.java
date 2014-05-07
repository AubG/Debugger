/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interpreter.debugger;

import interpreter.*;
import interpreter.bytecodes.ByteCode;
import interpreter.bytecodes.ReadCode;
import interpreter.bytecodes.WriteCode;
import interpreter.bytecodes.debugbytecodes.*;
import java.io.*;
import java.util.*;

/**
 *
 * @author Aub
 */
public class DebugVirtualMachine extends VirtualMachine {

    private Vector<Source> lineCode;
    private Stack<FunctionEnvironmentRecord> FERstack;
    private Source srcLine;
    private FunctionEnvironmentRecord fer;
    private Stack<Integer> startFunc;
    private Stack<Integer> endFunc;

    //Holds Function Trace strings, and return trace strings
    private Vector<String> trace;

    //Boolean Party
    private boolean isStepOut, isStepIn, isStepOver, isIntrinsic, isTraceOn;

    //Stack size used by Steps
    private int stackSize;

    /**
     *
     * @param program
     */
    public DebugVirtualMachine(Program program) {
        super(program);

        trace = new Vector();

        lineCode = new Vector<>();
        FERstack = new Stack<>();
        startFunc = new Stack<>();
        endFunc = new Stack<>();
        fer = new FunctionEnvironmentRecord();
        runStack = new RunTimeStack();
        returnAddr = new Stack();
        pc = 0;

        isStepOut = false;
        isStepIn = false;
        isStepOver = false;
        isIntrinsic = false;
        isTraceOn = false;
        isRunning = true;
    }

    @Override
    public void executeProgram() {
        while (isRunning) {
            ByteCode code = program.getCode(pc);

            //Checking for potential breakpoint
            if (code instanceof DebugLineCode) {
                DebugLineCode tempLine = (DebugLineCode) code;

                //retrieving boolean isBreakpoint from Source object
                if (tempLine.getLineNum() > 0 && lineCode.get(tempLine.getLineNum() - 1).getIsBreakpoint()) {
                    
                    isStepOut = false;
                    isStepIn = false;
                    isStepOver = false;
                    
                    code.execute(this);
                    pc++;
                    code = program.getCode(pc);
 
                    //Executing the expected following function code
                    if (code instanceof DebugFunctionCode) {
                        code.execute(this);
                        pc++;
                        code = program.getCode(pc);

                        //Exectuing the expected formal(s) which may or may not be there
                        while (code instanceof DebugFormalCode) {
                            code.execute(this);
                            pc++;
                            code = program.getCode(pc);
                        }
                    }
                    //Returning control to the Debugger and then UI userCommand
                    break;
                }
            }
            
            //Find the new functionCode and save the start and end for later printing (FUNC)
            if (code instanceof DebugFunctionCode) {
                DebugFunctionCode temp = (DebugFunctionCode) code;
                startFunc.push(temp.getStart());
                endFunc.push(temp.getEnd());
            }

            if (isStepOut) {
                if (FERstack.size() == stackSize - 1) {
                    isStepOut = false;
                    break;
                }
            }

            if (isStepOver) {
                  if (FERstack.size() == stackSize && code instanceof DebugLineCode) {
                    code.execute(this);
                    pc++;
                    isStepOver = false;
                    break;
                } else if (FERstack.size() < stackSize) {
                    isStepOver = false;
                    break;
                }
            }

            if (isStepIn) {

                if (FERstack.size() == stackSize && code instanceof DebugLineCode) {
                    code.execute(this);
                    pc++;
                    isStepIn = false;
                    code = program.getCode(pc);
                     
                    //Executing the potential function code
                    if (code instanceof DebugFunctionCode) {
                        code.execute(this);
                        pc++;
                        code = program.getCode(pc);

                        //Exectuing the expected formal(s) which may or may not be there
                        while (code instanceof DebugFormalCode) {
                            code.execute(this);
                            pc++;
                            code = program.getCode(pc);
                        }
                    }
                    break;
                    
                } else if (FERstack.size() > stackSize) {
                    for (int i = 0; i < 2; i++) {
                        code.execute(this);
                        pc++;
                        code = program.getCode(pc);
                    }
                    while (code instanceof DebugFormalCode) {
                        code.execute(this);
                        pc++;
                        code = program.getCode(pc);
                    }
                    isStepIn = false;
                    break;
                }
            }

            code.execute(this);
            pc++;
        }
    }

    /**
     *
     * @param sourceFile
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void loadSource(String sourceFile) throws FileNotFoundException, IOException {
        BufferedReader reader = (new BufferedReader(new FileReader(sourceFile)));
        String nextLine;

        while (reader.ready()) {
            srcLine = new Source();
            nextLine = reader.readLine();
            srcLine.setSourceLine(nextLine);
            lineCode.add(srcLine);
        }
    }

    /**
     *
     * @param currentLine
     */
    public void setCrrntLine(int currentLine) {
        fer.setCurrentLine(currentLine);
    }

    /**
     *
     * @param name
     * @param start
     * @param end
     */
    public void setFunc(String name, int start, int end) {
        fer.setName(name);
        fer.setStartLine(start);
        fer.setEndLine(end);
    }

    /**
     *
     * @return
     */
    public Vector<Source> getLineCode() {
        return lineCode;
    }

    /**
     *
     * @return
     */
    public int getLine() {
        return fer.getCurrentLine();
    }

    /**
     * Calls on isValidBrk if false sends false to setBrk in UI if true sets
     * breakpoints at values in Vector breaks then returns true
     *
     * @param breaks
     * @return
     */
    public boolean setBrks(Vector<Integer> breaks) {
        for (Integer line : breaks) {
            if (!(program.isValidBrk(line))) {
                return false;
            }
            lineCode.get(line - 1).isBreakpoint(true);
        }
        return true;
    }

    /**
     *
     * @param breaks
     */
    public void clrBrks(Vector<Integer> breaks) {
        for (Integer line : breaks) {
            lineCode.get(line - 1).isBreakpoint(false);
        }
    }

    /**
     *
     * @param running
     */
    public void setIsRunning(boolean running) {
        isRunning = running;
    }

    /**
     *
     * @return
     */
    public boolean getIsRunning() {
        return isRunning;
    }

    /**
     *
     */
    public void newFER() {
        FERstack.push(fer);
        fer = new FunctionEnvironmentRecord();
        fer.beginScope();
    }

    /**
     *
     */
    public void endFER() {
        fer = FERstack.pop();
    }

    /**
     * Changed the ( - 1 ) on both peeks might have to replace that
     *
     * @return
     */
    public Vector<Integer> displayFunc() {
        if (startFunc.size() == 0 && endFunc.size() == 0) {
            return null;
        }
        Vector<Integer> currentFunc = new Vector<Integer>();
        currentFunc.add(startFunc.peek());
        currentFunc.add(endFunc.peek());
        return currentFunc;
    }

    /**
     *
     * @return
     */
    public String[][] displayVars() {
        String[][] variables = fer.getVar();
        for (int i = 0; i < variables.length; i++) {
            String offset = variables[i][1];
            variables[i][1] = (runStack.getValue(Integer.parseInt(offset)));
        }
        return variables;
    }

    /**
     *
     * @param var
     * @param offset
     */
    public void enterSymbol(String var, int offset) {
        fer.enterID(var, offset);
    }

    /**
     *
     * @param offset
     */
    public void popSymbol(int offset) {
        fer.popIds(offset);
    }

    public void step(boolean out, boolean over, boolean in) {

        stackSize = FERstack.size();
        isStepOut = out;
        isStepIn = in;
        isStepOver = over;
        executeProgram();
    }

    public void setIntrinsic(boolean flag) {
        isIntrinsic = flag;
    }

    public boolean getIsIntrinsic() {
        return isIntrinsic;
    }

    public void clearTrace() {
        trace.clear();
    }

    public void addTrace(String function) {
        trace.add(function);
    }

    public boolean getIsTraceOn() {
        return isTraceOn;
    }

    public Vector<String> getTrace() {
        return trace;
    }

    public void setIsTraceOn(boolean isTraceOn) {
        this.isTraceOn = isTraceOn;
    }

    public int getFERStackSize() {
        return FERstack.size();
    }

    public String getFuncName() {
        return fer.getName();
    }

    public String getValue(int offset) {
        return runStack.getValue(offset);
    }

    public ByteCode getCode(int pc) {
        return program.getCode(pc);
    }

    public int getPC() {
        return pc;
    }
    
    public Vector<String> printStack(){
        Vector<String> callStack = new Vector<String>();
        callStack.add(fer.getName() + ": " + fer.getCurrentLine());
        Object[] stack = FERstack.toArray();
        for (Object FER : stack) {
            FunctionEnvironmentRecord temp = (FunctionEnvironmentRecord) FER;
            callStack.add(temp.getName() + ": " + temp.getCurrentLine());
        }
        return callStack;
    }
}
