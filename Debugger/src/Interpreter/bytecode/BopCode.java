package Interpreter.ByteCode;

import Interpreter.VirtualMachine;
import java.util.*;

public class BopCode extends ByteCode{
    String binOp;
    
    public void execute(VirtualMachine vm) {  
        vm.bop(binOp);
    }

    public void init(Vector<String> args) {
        binOp = args.get(0);
    }
    
    @Override
    public String toString(){
        return ("BOP" + " " + binOp);
    }
}
