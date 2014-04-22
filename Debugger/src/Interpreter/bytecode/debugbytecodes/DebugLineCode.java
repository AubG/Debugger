package Interpreter.ByteCode.DebugByteCodes;

import Interpreter.*;
import Interpreter.ByteCode.*;
import Interpreter.Debugger.DebugVirtualMachine;
import java.util.Vector;

public class DebugLineCode extends ByteCode {
    private String n;

    public void execute(VirtualMachine vm) {
        DebugVirtualMachine dvm = (DebugVirtualMachine) vm;
        dvm.setCrrntLine(Integer.parseInt(n));
    }

    public void init(Vector<String> args){
        n = args.get(0);
    }

    @Override
    public String toString() {
        return "LINE " + Integer.parseInt(n);
    }
    
    public int getLine(){
        return Integer.parseInt(n);
    }
}
