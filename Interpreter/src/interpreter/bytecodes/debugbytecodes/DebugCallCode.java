/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package interpreter.bytecodes.debugbytecodes;

import interpreter.*;
import interpreter.bytecodes.CallCode;
import interpreter.debugger.DebugVirtualMachine;
import java.util.Vector;

/**
 *
 * @author Aub
 */
public class DebugCallCode extends CallCode {
    
    @Override
    public void execute(VirtualMachine vm){
        DebugVirtualMachine dvm = (DebugVirtualMachine) vm;
        super.execute(dvm);
        dvm.newFER();
    }
    
    @Override
    public String toString(){
        return super.toString();
    }
    
}
