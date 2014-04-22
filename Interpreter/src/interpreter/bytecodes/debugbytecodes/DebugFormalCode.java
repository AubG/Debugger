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
public class DebugFormalCode extends ByteCode {
    private String var, offset;
    
    @Override
    public void execute(VirtualMachine vm) {
        DebugVirtualMachine dvm = (DebugVirtualMachine) vm;
        dvm.enterSymbol(var, dvm.getRunStackSize() - Integer.parseInt(offset));
    }

    @Override
    public void init(Vector<String> args){
        var = args.get(0);
        offset = args.get(1);
    }

    @Override
    public String toString() {
        return "FORMAL " + var + " " + offset;
    }
    
    public String getOffset(){
        return offset;
    }
    
}
