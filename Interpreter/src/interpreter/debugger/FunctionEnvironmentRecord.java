/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interpreter.debugger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * <
 * pre>
 * Binder objects group 3 fields 1. a value 2. the next link in the chain of ids
 * in the current scope 3. the next link of a previous Binder for the same
 * identifier in a previous scope
 * </pre>
 */
class Binder {

    private Object offset;
    private String prevtop;   // prior symbol in same scope
    private Binder tail;      // prior binder for same symbol
    // restore this when closing scope

    Binder(Object v, String p, Binder t) {
        offset = v;
        prevtop = p;
        tail = t;
    }

    Object getValue() {
        return offset;
    }

    String getPrevtop() {
        return prevtop;
    }

    Binder getTail() {
        return tail;
    }
}

/**
 * <
 * pre>
 * The Table class is similar to java.util.Dictionary, except that each key must
 * be a Symbol and there is a scope mechanism.
 *
 * Consider the following sequence of events for table t: t.put(Symbol("a"),5)
 * t.beginScope() t.put(Symbol("b"),7) t.put(Symbol("a"),9)
 *
 * ids will have the key/value pairs for Symbols "a" and "b" as:
 *
 * Symbol("a") -> Binder(9, Symbol("b") , Binder(5, null, null) ) (the second
 * field has a reference to the prior Symbol added in this scope; the third
 * field refers to the Binder for the Symbol("a") included in the prior scope)
 * Binder has 2 linked lists - the second field contains list of ids added to
 * the current scope; the third field contains the list of Binders for the
 * Symbols with the same string id - in this case, "a"
 *
 * Symbol("b") -> Binder(7, null, null) (the second field is null since there
 * are no other ids to link in this scope; the third field is null since there
 * is no Symbol("b") in prior scopes)
 *
 * top has a reference to Symbol("a") which was the last symbol added to current
 * scope
 *
 * Note: What happens if a symbol is defined twice in the same scope??
 * </pre>
 */
class Table {

    private java.util.HashMap<String, Binder> ids = new java.util.HashMap<>();
    private String top;    // reference to last symbol added to
    // current scope; this essentially is the
    // start of a linked list of ids in scope
    private Binder marks;  // scope mark; essentially we have a stack of
    // marks - push for new scope; pop when closing
    // scope

    public Table() {
    }

    /**
     * Gets the object associated with the specified symbol in the Table.
     */
    public Object get(String key) {
        Binder e = ids.get(key);
        return e.getValue();
    }

    /**
     * Puts the specified value into the Table, bound to the specified
     * Symbol.<br>
     * Maintain the list of ids in the current scope (top);<br>
     * Add to list of ids in prior scope with the same string identifier
     */
    public void put(String key, Object value) {
        ids.put(key, new Binder(value, top, ids.get(key)));
        top = key;
    }

    /**
     * Remembers the current state of the Table; push new mark on mark stack
     */
    public void beginScope() {
        marks = new Binder(null, top, marks);
        top = null;
    }

    /**
     * Restores the table to what it was at the most recent beginScope that has
     * not already been ended.
     */
    public void popIds(int num) {
        for (int i = 0; i < num; i++) {
            Binder e = ids.get(top);
            if (e.getTail() != null) {
                ids.put(top, e.getTail());
            } else {
                ids.remove(top);
            }
            top = e.getPrevtop();
        }
    }

    public String[][] getVar() {
        Iterator<String> set = ids.keySet().iterator();
        String[][] variables = new String[ids.size()][2];
        for (int i = 0; i < ids.size(); i++) {
            variables[i][0] = set.next();
            variables[i][1] = ids.get(variables[i][0]).getValue().toString();
        }
        return variables;
    }

    /**
     * @return a set of the Table's ids.
     */
    public java.util.Set<String> keys() {
        return ids.keySet();
    }
}

/**
 *
 * @author Aub
 */
public class FunctionEnvironmentRecord {

    private Table ids;
    private int startLine, endLine, currentLine;
    private String name;

    /**
     * Does not need to be here after the first Milestone of the Debugger.
     * (Assumption)
     *
     * @param args
     */
//    public static void main(String args[]) {
//        String srcFile;
//        try {
//            srcFile = System.getProperty("user.dir") + "/" + args[0];
//            (new FunctionEnvironmentRecord()).readSrcFile(srcFile);
//        } catch (java.io.IOException e) {
//            System.out.println("****" + e);
//        }
//    }

    /**
     * Reads the programFile given through a command-line arg to the main method
     * of the FunctionEnvironmentRecord Class. Disperses the commands
     * accordingly.
     *
     * @param programFile
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void readSrcFile(String programFile) throws FileNotFoundException, IOException {
        BufferedReader in = new BufferedReader(new FileReader(programFile));
        do {
            String srcFileLine = in.readLine();
            Scanner tokenizer = new Scanner(srcFileLine);
            String cmd = tokenizer.next();

            while (tokenizer.hasNext()) {
                switch (cmd) {
                    case "BS":
                        System.out.print(cmd + "\t\t");
                        beginScope();
                        dump();
                        break;
                    case "Function":
                        System.out.print(cmd);
                        String tempName = tokenizer.next();
                        int tempStart = Integer.valueOf(tokenizer.next());
                        int tempEnd = Integer.valueOf(tokenizer.next());
                        System.out.print(" " + tempName + " " + tempStart + " " + tempEnd + "\t\t");
                        setFunc(tempName, tempStart, tempEnd);
                        dump();
                        break;
                    case "Line":
                        System.out.print(cmd);
                        int tempCurrent = Integer.valueOf(tokenizer.next());
                        System.out.print(" " + tempCurrent + "\t\t\t");
                        setCurrentLine(tempCurrent);
                        dump();
                        break;
                    case "Enter":
                        System.out.print(cmd);
                        String tempId = tokenizer.next();
                        int tempNum = Integer.valueOf(tokenizer.next());
                        System.out.print(" " + tempId + " " + tempNum + "\t\t");
                        enterID(tempId,
                                tempNum);
                        dump();
                        break;
                    case "Pop":
                        System.out.print(cmd);
                        int tempNumm = Integer.valueOf(tokenizer.next());
                        System.out.print(" " + tempNumm + "\t\t\t");
                        popIds(tempNumm);
                        dump();
                        break;
                    default:
                        System.out.println("**** Error Not Recognized command");
                        break;
                }
            }

        } while (in.ready());
    }

    public FunctionEnvironmentRecord() {
        ids = new Table();
    }

    public int getStartLine() {
        return startLine;
    }

    public void setStartLine(int startLine) {
        this.startLine = startLine;
    }

    public int getEndLine() {
        return endLine;
    }

    public void setEndLine(int endLine) {
        this.endLine = endLine;
    }

    public int getCurrentLine() {
        return currentLine;
    }

    public void setCurrentLine(int currentLine) {
        this.currentLine = currentLine;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFunc(String name, int start, int end) {
        this.name = name;
        this.startLine = start;
        this.endLine = end;
    }

    public void enterID(String id, int value) {
        ids.put(id, value);
    }

    public void popIds(int num) {
        ids.popIds(num);
    }

    public void beginScope() {
        ids.beginScope();
    }

    public String[][] getVar() {
        return ids.getVar();
    }

    public void dump() {
        Iterator<String> i = ids.keys().iterator();
        String id;
        Object offset;
        int j = 1;
        System.out.print("(<");
        while (i.hasNext()) {
            id = i.next();
            offset = ids.get(id);
            System.out.print(id + "/" + offset);
            if (j < ids.keys().size()) {
                System.out.print(",");
            }
            j++;
        }
        System.out.print(">,");
        if (name == null) {
            System.out.print("-,");
        } else {
            System.out.print(name + ",");
        }
        if (startLine == 0) {
            System.out.print("-,");
        } else {
            System.out.print(startLine + ",");
        }
        if (endLine == 0) {
            System.out.print("-,");
        } else {
            System.out.print(endLine + ",");
        }
        if (currentLine == 0) {
            System.out.print("-");
        } else {
            System.out.print(currentLine);
        }
        System.out.println(")");
    }
}
