package Interpreter.Debugger;

import Interpreter.*;
import Interpreter.ByteCode.*;
import Interpreter.ByteCode.DebugByteCodes.*;
import java.io.*;
import java.util.*;

public class DebugVirtualMachine extends VirtualMachine {

    private Vector<Source> lineCode;

    //Dont need this until milestone 5
    private Vector<String> trace;

    private Stack<FunctionEnvironmentRecord> FERStack;
    private Source srcLine;
    private FunctionEnvironmentRecord fer;
    private Stack<Integer> startFunc;
    private Stack<Integer> endFunc;
    private boolean isStepOut, isStepOver, isStepIn, isTraceOn;
    private int stackSize;

    public DebugVirtualMachine(Program program) {
        super(program);
        lineCode = new Vector<Source>();

        //Dont need this until Milestone 5
        trace = new Vector<String>();

        FERStack = new Stack<FunctionEnvironmentRecord>();
        startFunc = new Stack<Integer>();
        endFunc = new Stack<Integer>();
        fer = new FunctionEnvironmentRecord();
        runStack = new RunTimeStack();
        returnAddr = new Stack();
        pc = 0;
        isRunning = true;
        
        //None of this necessary until milestone 4 and/or 5
        isStepOut = false;
        isStepOver = false;
        isStepIn = false;
        isTraceOn = false;
    }

    @Override
    public void executeProgram() {
        while (isRunning) {
            ByteCode code = program.getCode(pc);
            if (code instanceof DebugLineCode) {
                DebugLineCode temp = (DebugLineCode) code;
                if (temp.getLine() > 0 && lineCode.get(temp.getLine() - 1).getIsBreakptSet()) {
                    code.execute(this);
                    pc++;
                    code = program.getCode(pc);
                    if (code instanceof DebugFunctionCode) {
                        code.execute(this);
                        pc++;
                        code = program.getCode(pc);
                        while (code instanceof DebugFormalCode) {
                            code.execute(this);
                            pc++;
                            code = program.getCode(pc);
                        }
                    }
                    break;
                }
            }
            if (code instanceof DebugFunctionCode) {
                DebugFunctionCode temp = (DebugFunctionCode) code;
                startFunc.push(temp.getStart());
                endFunc.push(temp.getEnd());
            }

            //All part of milestones 4 and 5
            if (isStepOut) {
                if (FERStack.size() == stackSize - 1) {
                    isStepOut = false;
                    break;
                }
            }
            if (isStepOver) {
                if (FERStack.size() == stackSize && code instanceof DebugLineCode) {
                    code.execute(this);
                    pc++;
                    isStepOver = false;
                    break;
                } else if (FERStack.size() < stackSize) {
                    isStepOver = false;
                    break;
                }
            }
            if (isStepIn) {
                if (FERStack.size() == stackSize && code instanceof DebugLineCode) {
                    code.execute(this);
                    pc++;
                    isStepIn = false;
                    break;
                } else if (FERStack.size() > stackSize) {
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
            //End of milestone 4 and 5 stuff
            //Everything below this must be put into milestone 3

            code.execute(this);
            pc++;
        }
    }

    public void loadSource(String sourceFile) throws FileNotFoundException, IOException {
        BufferedReader reader = new BufferedReader(new FileReader(sourceFile));
        String nextLine;
        //int count = -1;

        while (reader.ready()) {
            //count++;
            srcLine = new Source();
            nextLine = reader.readLine();
            srcLine.setSourceLine(nextLine);
            lineCode.add(srcLine);
        }
    }

    public void enterSymbol(String var, int offset) {
        fer.enterID(var, offset);
    }

    public void popSymbol(int offset) {
        fer.popSymbols(offset);
    }

    public void setCrrntLine(int currentLine) {
        fer.setCrrntLine(currentLine);
    }

    public void setFunc(String name, int start, int end) {
        fer.setName(name);
        fer.setStrtLine(start);
        fer.setEndLine(end);
    }

    public void newFER() {
        FERStack.push(fer);
        fer = new FunctionEnvironmentRecord();
        fer.beginScope();
    }

    public void endFER() {
        fer = FERStack.pop();
    }

    public Vector<Source> getLineCode() {
        return lineCode;
    }

    public void setIsRunning(boolean running) {
        isRunning = running;
    }

    public boolean setBrk(Vector<Integer> breaks) {
        for (Integer line : breaks) {
            if (!(program.isValidBrk(line))) {
                return false;
            }
            lineCode.get(line - 1).isBreakptSet(true);
        }
        return true;
    }

    public void clrBrk(Vector<Integer> breaks) {
        for (Integer line : breaks) {
            lineCode.get(line - 1).isBreakptSet(false);
        }
    }

    public void clearAll() {
        for (Source line : lineCode) {
            line.isBreakptSet(false);
        }
    }

    public Vector<Integer> displayFunc() {
        if (startFunc.size() == 0 && endFunc.size() == 0) {
            return null;
        }
        Vector<Integer> currentFunc = new Vector<Integer>();
        currentFunc.add(startFunc.peek() - 1);
        currentFunc.add(endFunc.peek() - 1);
        return currentFunc;
    }

    public boolean getIsRunning() {
        return isRunning;
    }

    public String[][] displayVar() {
        String[][] variables = fer.getVar();
        for (int i = 0; i < variables.length; i++) {
            String offset = variables[i][1];
            variables[i][1] = (runStack.getValue(Integer.parseInt(offset)));
        }
        return variables;
    }

    public int getLine() {
        return fer.getCrrntLine();
    }

    //Not Necessary until milestone 4 and/or 5
    public void step(boolean out, boolean over, boolean in) {
        stackSize = FERStack.size();
        isStepOut = out;
        isStepOver = over;
        isStepIn = in;
        executeProgram();
    }

    //Not necessary until milestone 5
    public boolean getIsTraceOn() {
        return isTraceOn;
    }

    //Not Necessary until milestone 5
    public void setIsTraceOn(boolean isTraceOn) {
        this.isTraceOn = isTraceOn;
    }

    public String getValue(int offset) {
        return runStack.getValue(offset);
    }

    public int getPC() {
        return pc;
    }

    public ByteCode getCode(int pc) {
        return program.getCode(pc);
    }

    //Not Necessary until milestone 5
    public void addTrace(String function) {
        trace.add(function);
    }

    public int getFERStackSize() {
        return FERStack.size();
    }

    //Not Necessary until milestone 5
    public Vector<String> getTrace() {
        return trace;
    }

    public String getFuncName() {
        return fer.getName();
    }

    //Not Necessary until Milestone 5
    public void clearTrace() {
        trace.clear();
    }

    public Vector<String> printStack() {
        Vector<String> callStack = new Vector<String>();
        callStack.add(fer.getName() + ": " + fer.getCrrntLine());
        Object[] stack = FERStack.toArray();
        for (Object FER : stack) {
            FunctionEnvironmentRecord temp = (FunctionEnvironmentRecord) FER;
            callStack.add(temp.getName() + ": " + temp.getCrrntLine());
        }
        return callStack;
    }
}
