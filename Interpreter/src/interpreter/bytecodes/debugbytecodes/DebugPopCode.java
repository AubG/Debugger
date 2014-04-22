/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package interpreter.bytecodes.debugbytecodes;

import interpreter.*;
import interpreter.bytecodes.PopCode;
import interpreter.debugger.DebugVirtualMachine;

/**
 *
 * @author Aub
 */
public class DebugPopCode extends PopCode {
    
    @Override
    public void execute(VirtualMachine vm) {
        DebugVirtualMachine dvm = (DebugVirtualMachine) vm;
        super.execute(dvm);
        dvm.popSymbol(getOffset());
    }
}
