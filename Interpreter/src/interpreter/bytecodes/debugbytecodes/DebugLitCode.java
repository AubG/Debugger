/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package interpreter.bytecodes.debugbytecodes;

import interpreter.*;
import interpreter.bytecodes.LitCode;
import interpreter.debugger.DebugVirtualMachine;

/**
 *
 * @author Aub
 */
public class DebugLitCode extends LitCode{
    
    @Override
    public void execute(VirtualMachine vm){
        DebugVirtualMachine dvm = (DebugVirtualMachine) vm;
        super.execute(dvm);
        if(getAddSym()){
            int offset = dvm.getRunStackSize();
            dvm.enterSymbol(getID(), offset); 
        }
    }
}
