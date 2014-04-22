package Interpreter.ByteCode.DebugByteCodes;

import Interpreter.*;
import Interpreter.ByteCode.CallCode;
import Interpreter.Debugger.DebugVirtualMachine;
import java.util.Vector;

public class DebugCallCode extends CallCode {

    public void execute(VirtualMachine vm) {
        DebugVirtualMachine dvm = (DebugVirtualMachine) vm;
        super.execute(dvm);
        dvm.newFER();
    }
}
