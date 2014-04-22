package Interpreter.ByteCode.DebugByteCodes;

import Interpreter.*;
import Interpreter.ByteCode.*;
import Interpreter.Debugger.DebugVirtualMachine;
import java.util.Vector;

public class DebugFormalCode extends ByteCode {
    private String var, offset;

    public void execute(VirtualMachine vm) {
        DebugVirtualMachine dvm = (DebugVirtualMachine) vm;
        dvm.enterSymbol(var, dvm.getRunStackSize() - Integer.parseInt(offset));
    }

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