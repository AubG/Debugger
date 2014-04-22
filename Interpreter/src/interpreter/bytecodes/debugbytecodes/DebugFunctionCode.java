/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package interpreter.bytecodes.debugbytecodes;

import interpreter.VirtualMachine;
import interpreter.bytecodes.ByteCode;
import interpreter.debugger.DebugVirtualMachine;
import java.util.Vector;

/**
 *
 * @author Aub
 */
public class DebugFunctionCode extends ByteCode {
 private String name, start, end;

 @Override
    public void execute(VirtualMachine vm) {
        DebugVirtualMachine dvm = (DebugVirtualMachine) vm;
        dvm.setFunc(name, getStart(), getEnd());
    }

    @Override
    public void init(Vector<String> args){
        name = args.get(0);
        start = args.get(1);
        end = args.get(2);
    }

    @Override
    public String toString() {
        return "FUNCTION " + name + " " + getStart() + " " + getEnd();
    }

    /**
     * @return the start
     */
    public int getStart() {
        return Integer.parseInt(start);
    }

    /**
     * @return the end
     */
    public int getEnd() {
        return Integer.parseInt(end);
    }
}
