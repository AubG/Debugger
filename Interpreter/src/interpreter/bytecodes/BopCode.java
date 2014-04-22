package interpreter.bytecodes;


import interpreter.VirtualMachine;
import java.util.*;

public class BopCode extends ByteCode{
    String binOp;
    
    @Override
    public void execute(VirtualMachine vm) {  
        vm.bop(binOp);
    }

    @Override
    public void init(Vector<String> args) {
        binOp = args.get(0);
    }
    
    @Override
    public String toString(){
        return ("BOP" + " " + binOp);
    }
}
