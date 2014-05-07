/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interpreter.bytecodes.debugbytecodes;

import interpreter.bytecodes.ReturnCode;
import interpreter.*;
import interpreter.debugger.DebugVirtualMachine;

/**
 *
 * @author Aub
 */
public class DebugReturnCode extends ReturnCode {

    @Override
    public void execute(VirtualMachine vm) {
        DebugVirtualMachine dvm = (DebugVirtualMachine) vm;

        super.execute(dvm);

        if (dvm.getIsTraceOn()) {
            String dontTrace = dvm.getFuncName().toLowerCase();
            String name = "";

            //Dont want to trace those pesky Main, reads and write
            if (dontTrace.equals("main") || dontTrace.equals("read") || dontTrace.equals("write")) {
                return;
            }
            
            for (int i = 0; i < dvm.getFERStackSize(); i++) {
                name += "   ";
            }
            name += "exit " + dvm.getFuncName() + ": " + val;
            dvm.addTrace(name);
        }

        dvm.endFER();
    }
}
