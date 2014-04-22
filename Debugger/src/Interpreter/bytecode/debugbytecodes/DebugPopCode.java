package Interpreter.ByteCode.DebugByteCodes;

import Interpreter.*;
import Interpreter.ByteCode.*;
import Interpreter.Debugger.DebugVirtualMachine;
import java.util.Vector;

public class DebugPopCode extends PopCode {

    public void execute(VirtualMachine vm) {
        DebugVirtualMachine dvm = (DebugVirtualMachine) vm;
        super.execute(dvm);
        dvm.popSymbol(getOffset());
    }
}
