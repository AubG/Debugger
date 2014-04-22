/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package interpreter.bytecodes.debugbytecodes;

import interpreter.bytecodes.ByteCode;
import interpreter.*;
import interpreter.debugger.DebugVirtualMachine;
import java.util.Vector;

/**
 *
 * @author Aub
 */
public class DebugLineCode extends ByteCode {
    int lineNumber;
    
    @Override
    public void execute(VirtualMachine vm) {
        DebugVirtualMachine dvm = (DebugVirtualMachine) vm;
        dvm.setCrrntLine(lineNumber);
    }

    @Override
    public void init(Vector<String> args) {
        lineNumber = Integer.valueOf(args.get(0));
    }
    
    public int getLineNum(){
        return lineNumber;
    }
    
    @Override
    public String toString() {
        return "LINE " + lineNumber;
    }
}
