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
     * Prints the raw code input as commandline arguement
     * Points at the current line you are on if you break
     * Puts star at beginning of any line /w a breakpoint
     */
    public void dumpSource() {
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
        System.out.print("UI>> ");
        String thisLine = scanner.next();
        switch (thisLine) {
            case "SET":
                setBrk();
                userCommand();
                break;
            case "CLEAR":
                clrBrk();
                userCommand();
                break;
            case "FUNC":
                displayFunc();
                System.out.println();
                userCommand();
                break;
            case "CONTINUE":
                dvm.executeProgram();
                break;
            case "QUIT":
                dvm.setIsRunning(false);
                break;
            case "VARS":
                displayVars();
                userCommand();
                break;
            case "?":
                System.out.println();
                help();
                userCommand();
                break;
            default:
                System.out.println("\n**** ERROR: Invalid Command type \"?\" for list of Commands\n");
                userCommand();
                break;
        }
    }

    // Prints the command list
    void help() {
        System.out.println("\n"
                + "----======----||All Currently Available Commands||----======----\n"
                + "     HELP  |?|   to display this list.\n"
                + "      SET  |S|   and the number lines followed by zero to set breakpoints. (i.e. 3 5 6 0)\n"
                + "    CLEAR  |C|   and the number lines followed by zero to clear breakpoints. (i.e. 5 6 0)\n"
                + "     FUNC  |F|   to display the current function.\n"
                + " CONTINUE |CONT| to continue execution.\n"
                + "     QUIT  |Q|   to quit execution.\n"
                + "     VARS  |V|   to display the variables in the current function.\n"
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
                    + "  blocks, while, if, return, assign\n");
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
            for (int i = currentFunc.get(0); i < currentFunc.get(1); i++) {
                System.out.println(src.get(i).getSourceLine());
            }
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
}
