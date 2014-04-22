package Interpreter.ByteCode.DebugByteCodes;

import Interpreter.*;
import Interpreter.ByteCode.*;
import Interpreter.Debugger.DebugVirtualMachine;
import java.util.Vector;

public class DebugReturnCode extends ReturnCode {
    
    public void execute(VirtualMachine vm) {
        DebugVirtualMachine dvm = (DebugVirtualMachine) vm;
        if(dvm.getIsTraceOn()){
            String name = "";
            for(int i = 0 ; i < dvm.getFERStackSize() ; i++)
                name += "    ";
            name += "exit: " + dvm.getFuncName() + ": " + val;
            dvm.addTrace(name);
        }
        super.execute(dvm);
        dvm.endFER();
    }
}
