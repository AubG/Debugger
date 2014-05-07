/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interpreter.debugger.ui;

import java.util.*;
import interpreter.debugger.*;

/**
 *
 * @author Aub
 */
public class DebugUI {

    private DebugVirtualMachine dvm;
    private Vector<Source> src;
    private Scanner scanner;
    int currentLine;
    boolean firstPrint = true;

    public DebugUI(DebugVirtualMachine dvm) {
        this.dvm = dvm;
        src = dvm.getLineCode();
        scanner = new Scanner(System.in);
        currentLine = 1;
    }

    /**
     * Prints the raw code input as commandline arguement Points at the current
     * line you are on if you break Puts star at beginning of any line /w a
     * breakpoint
     */
    public void dumpSource() {

        if (dvm.getIsTraceOn()) {
            System.out.println();
            Vector<String> temp = dvm.getTrace();

            for (int i = 0; i < temp.size(); i++) {
                System.out.println(temp.get(i));
            }

            dvm.clearTrace();
        }

        currentLine = dvm.getLine();
        int i = 0;
        System.out.println();
        for (Source line : src) {
            if (line.getIsBreakpoint()) {
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
        System.out.println();
        // Printing the Commands for the first and only time without being told
        if (firstPrint == true) {
            help();
            firstPrint = false;
        }
    }

    /**
     * Handles all of the users commands
     */
    public void userCommand() {
        //make if else block for all the commands or switch
        System.out.print("Type ? for help\n>> ");
        String thisLine = scanner.next();
        switch (thisLine) {
            case "s":
                setBrk();
                userCommand();
                break;
            case "clr":
                clrBrk();
                userCommand();
                break;
            case "f":
                displayFunc();
                System.out.println();
                userCommand();
                break;
            case "c":
                dvm.executeProgram();
                break;
            case "q":
                dvm.setIsRunning(false);
                break;
            case "v":
                displayVars();
                userCommand();
                break;
            case "?":
                System.out.println();
                help();
                userCommand();
                break;
            case "so":
                dvm.step(true, false, false);
                break;
            case "si":
                dvm.step(false, false, true);
                break;
            case "sv":
                dvm.step(false, true, false);
                break;
            case "to":
                dvm.setIsTraceOn(true);
                break;
            case "tf":
                dvm.setIsTraceOn(false);
                break;
            case "cs":
                printStack();
                break;
            default:
                System.out.println("\n**** ERROR: Invalid Command type \"?\" for list of Commands\n");
                userCommand();
                break;
        }
    }

    // Prints the command list
    // Shorten the commands (SIGNIFICANTLY)
    void help() {
        System.out.println("\n"
                + "----======----||All Currently Available Commands||----======----\n"
                + "     ?  -|-  to display this list.\n"
                + "     s  -|-  and the number lines then zero to end input. (i.e. S 3 5 6 0)\n"
                + "   clr  -|-  and the number lines then zero to end input. (i.e. C 5 6 0)\n"
                + "     f  -|-  to display the current function.\n"
                + "     c  -|-  to continue execution.\n"
                + "     q  -|-  to quit execution.\n"
                + "     v  -|-  to display the variables in the current function.\n"
                + "    so  -|-  to step out of the current function.\n"
                + "    si  -|-  to step into the next function.\n"
                + "    sv  -|-  to step over the next function.\n"
                + "    to  -|-  to turn function tracing on.\n"
                + "    tf  -|-  to turn function tracing off.\n"
                + "    cs  -|-  to print the call Stack.\n"
                + "----------------------------------------------------------------\n\n");
    }

    // Sets all breakpoints, sends array of breakpoints to dvm
    // dvm deals with checking if valid breakpoint 
    void setBrk() {
        Vector<Integer> breaks = new Vector<>();
        while (scanner.hasNext()) {
            int nextBrk = scanner.nextInt();
            if (nextBrk == 0) {
                break;
            } else {
                breaks.add(nextBrk);
            }
        }
        if (!dvm.setBrks(breaks)) {
            System.out.println("\n**** One or more Breakpoint(s) are invalid. Try Again.\n\n"
                    + "Breakpoints can only be set at following instructions:\n"
                    + "                     blocks, while, if, return, assign\n");
        } else {
            System.out.print("Breakpoint(s) set at: ");
            for (int point : breaks) {
                System.out.print(point + " ");
            }
        }
        System.out.println("\n");
    }

    // Sends vector of breakpoints to dvm
    // dvm deals with clearing breakpoints
    void clrBrk() {
        //clear breakpoints
        Vector<Integer> breaks = new Vector<>();
        while (scanner.hasNext()) {
            int temp = scanner.nextInt();
            if (temp == 0) {
                break;
            } else {
                breaks.add(temp);
            }
        }

        dvm.clrBrks(breaks);

        System.out.print("Cleared breakpoints at: ");
        for (int point : breaks) {
            System.out.print(point + " ");
        }
        System.out.println("\n");
    }

    // Gets function lines from dvm
    // Prints source lines
    // if not in any function(s) should print entire program
    void displayFunc() {
        //Print the current function we are in
        Vector<Integer> currentFunc = dvm.displayFunc();
        if (currentFunc != null) {
            currentLine = dvm.getLine();
            System.out.println();
            for (int i = currentFunc.get(0); i < currentFunc.get(1); i++) {
                
                    if (src.get(i).getIsBreakpoint()) {
                        System.out.print("*");
                    } else {
                        System.out.print(" ");
                    }
                    
                    System.out.print(String.format("%2d", (src.indexOf(src.get(i)) + 1)) + " ");
                    
                    if (currentLine == i + 1) {
                        System.out.println(src.get(i).getSourceLine() + "         <------");
                    } else {
                        System.out.println(src.get(i).getSourceLine());
                    }               
            }
            System.out.println();

        } else {
            dumpSource();
        }
    }

    // Print all the vars currently initialized
    // Goes to dvm to deal with getting values
    void displayVars() {
        //Call on dvm method displayVars
        String[][] variables = dvm.displayVars();
        System.out.println();
        for (int i = 0; i < variables.length; i++) {
            System.out.println("ID: " + variables[i][0] + "     Value: " + variables[i][1]);
        }
        System.out.println();
    }

    void printStack() {
        System.out.println();
        Vector<String> callStack = dvm.printStack();
        for (int i = callStack.size() - 1; i > 0; i--) {
            if (i == callStack.size() - 1) {
                System.out.println(callStack.get(0));
            }
            System.out.println(callStack.get(i));

        }
        System.out.println("\nPrinted the current Call Stack.");
    }
}
