package Interpreter.Debugger.UI;

import Interpreter.Debugger.*;
import java.util.*;

public class UI {

    private DebugVirtualMachine dvm;
    private Vector<Source> src;
    private Scanner scanner;
    int currentLine;

    public UI(DebugVirtualMachine dvm) {
        this.dvm = dvm;
        src = dvm.getLineCode();
        scanner = new Scanner(System.in);
        currentLine = 1;
    }

    public void dumpSource() {
       //Not necessary until Milestone 4 and/or 5
        if (dvm.getIsTraceOn()) {
            System.out.println();
            Vector<String> temp = dvm.getTrace();
            for (int i = 0; i < temp.size(); i++) {
                System.out.println(temp.get(i));
            }
            dvm.clearTrace();
        }
        //End of Unnecesary code
        
        currentLine = dvm.getLine();
        int i = 0;
        System.out.println();
        for (Source line : src) {
            if (line.getIsBreakptSet()) {
                System.out.print("*");
            } else {
                System.out.print(" ");
            }
            System.out.print(String.format("%2d", (src.indexOf(line) + 1)) + " ");
            if (currentLine == i + 1) {
                System.out.println(line.getSourceLine() + "         <------");
            } else {
                System.out.println(line.getSourceLine());
            }
            i++;
        }
    }

    public void userCommand() {
        System.out.print("\nEnter a command or enter '?' for help: ");
        String input = scanner.next();

        if (input.equalsIgnoreCase("set")) {
            setBrk();
        } else if (input.equalsIgnoreCase("clear")) {
            clrBrk();
        } else if (input.equalsIgnoreCase("func")) {
            displayFunc();
        } else if (input.equalsIgnoreCase("continue")) {
            dvm.executeProgram();
        } else if (input.equalsIgnoreCase("quit")) {
            dvm.setIsRunning(false);
        } else if (input.equalsIgnoreCase("vars")) {
            displayVar();
        } else if (input.equalsIgnoreCase("?")) {
            help();
        } else if (input.equalsIgnoreCase("stepout")) {
            step(true, false, false);
        } else if (input.equalsIgnoreCase("stepover")) {
            step(false, true, false);
        } else if (input.equalsIgnoreCase("stepin")) {
            step(false, false, true);
        } else if (input.equalsIgnoreCase("traceON")) {
            trace(true);
        } else if (input.equalsIgnoreCase("traceOFF")) {
            trace(false);
        } else if (input.equalsIgnoreCase("stack")) {
            printStack();
        } else if (input.equalsIgnoreCase("breaks")) {
            breakSet();
        } else {
            System.out.println("\n'" + input + "' is not a command.");
        }
    }

    void help() {
        System.out.println("\nCommands available:\n"
                + "      SET and the number lines followed by zero to set breakpoints. (i.e. 3 5 6 0)\n"
                + "    CLEAR and the number lines followed by zero to clear breakpoints. (i.e. 5 6 0)\n"
                + "     FUNC to display the current function.\n"
                + " CONTINUE to continue execution.\n"
                + "     QUIT to quit execution.\n"
                + "     VARS to display the variables in the current function.\n"
                + "  STEPOUT to step out of the current function.\n"
                + " STEPOVER to step over the current line.\n"
                + "   STEPIN to step into the current function.\n"
                + "  TRACEON to turn the tracing function on.\n"
                + " TRACEOFF to turn the tracing function off.\n"
                + "    STACK to print the current call stack.\n"
                + "   BREAKS to display the current breakpoints.");
    }

    void setBrk() {
        Vector<Integer> breaks = new Vector<Integer>();
        int nextBrk = 1;
        while (nextBrk != 0) {
            nextBrk = scanner.nextInt();
            if (nextBrk != 0) {
                breaks.add(nextBrk);
            }
        }
        if (!dvm.setBrk(breaks)) {
            System.out.println("\nOne or more breakpoint(s) is/are invalid. Try again.");
        }
        System.out.print("Breakpoints set at ");
        for (int point : breaks) {
            System.out.print(point + ", ");
        }
        System.out.println("\n");
    }

    void clrBrk() {
        Vector<Integer> breaks = new Vector<Integer>();
        int nextBrk = 1;
        while (nextBrk != 0) {
            nextBrk = scanner.nextInt();
            if (nextBrk != 0) {
                breaks.add(nextBrk);
            }
        }
        if (breaks.size() < 1) {
            dvm.clearAll();
        } else {
            dvm.clrBrk(breaks);
        }
        System.out.print("Cleared breakpoints at ");
        for (int point : breaks) {
            System.out.print(point + ", ");
        }
        System.out.println("\n");
    }

    void displayFunc() {
        Vector<Integer> currentFunc = dvm.displayFunc();
        if (currentFunc != null) {
            for (int i = currentFunc.get(0); i < currentFunc.get(1); i++) {
                System.out.println("\n" + src.get(i).getSourceLine());
            }
        } else {
            dumpSource();
        }
    }

    void displayVar() {
        String[][] variables = dvm.displayVar();
        System.out.println();
        for (int i = 0; i < variables.length; i++) {
            System.out.println("ID: " + variables[i][0] + "     Value: " + variables[i][1]);
        }
    }

    //Not Necessary until Milestone 4 and/or 5
    void step(boolean out, boolean over, boolean in) {
        dvm.step(out, over, in);
    }

    //Not Necessary until MileStone 4 and/or 5
    void trace(boolean isTraceOn) {
        dvm.setIsTraceOn(isTraceOn);
    }
    //Not Necessary until Milestone 4 and/or 5
    void printStack() {
        System.out.println();
        Vector<String> callStack = dvm.printStack();
        for (int i = callStack.size() - 1; i >= 0; i--) {
            System.out.println(callStack.get(i));
        }
        System.out.println("\nPrinted the current Call Stack.");
    }
    
    void breakSet() {
        int count = 0;
        for (int i = 0; i < src.size(); i++) {
            if ((src.get(i).getIsBreakptSet())) {
                if (count == 0) {
                    System.out.print("Breakpoints currently set at line(s): ");
                }
                System.out.print((i + 1) + " ");
                count++;
            }
        }
        if (count == 0) {
            System.out.print("There are no breakpoints set.");
        }
        System.out.println();
    }
}
