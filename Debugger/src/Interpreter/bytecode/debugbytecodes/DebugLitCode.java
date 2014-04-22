package Interpreter.ByteCode.DebugByteCodes;

import Interpreter.*;
import Interpreter.ByteCode.*;
import Interpreter.Debugger.DebugVirtualMachine;
import java.util.Vector;

public class DebugLitCode extends LitCode {

    public void execute(VirtualMachine vm) {
        DebugVirtualMachine dvm = (DebugVirtualMachine) vm;
        super.execute(dvm);
        if(getAddSym()){
            int offset = dvm.getRunStackSize();
            dvm.enterSymbol(getID(), offset); 
        }
    }
}
