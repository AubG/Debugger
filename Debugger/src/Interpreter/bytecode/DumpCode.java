package Interpreter.ByteCode;

import Interpreter.VirtualMachine;
import java.util.*;

public class DumpCode extends ByteCode {
    private boolean isOn;
    
    public void init(Vector<String> args) {
        if (args.firstElement().equals("ON"))
            isOn = true;
        else
            isOn = false;
    }
    
    public void execute(VirtualMachine vm) {
        vm.setDumpOn(isOn);
    }
}