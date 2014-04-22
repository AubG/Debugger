package Interpreter.ByteCode;

import Interpreter.VirtualMachine;
import java.util.*;

public class ReturnCode extends ByteCode{
    String label;
    protected int val;

    public void execute(VirtualMachine vm) {  
        vm.setPC(vm.popRA());
        vm.popFrame();
        val = vm.peekRunStack();
    }

    public void init(Vector<String> args) {
        if(args.size() > 0)
            label = args.get(0);
        else
            label = null;
    }
    
    @Override
    public String toString(){
        return ("RETURN" + " " + label);
    }
}
