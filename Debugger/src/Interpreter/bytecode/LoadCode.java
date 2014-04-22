package Interpreter.ByteCode;

import Interpreter.VirtualMachine;
import java.util.*;

public class LoadCode extends ByteCode{
    String n;
    String id;
    
    public void execute(VirtualMachine vm) {
        vm.load(Integer.parseInt(n));
    }

    public void init(Vector<String> args) {
        n = args.get(0);
        id = args.get(1);
    }
    
    @Override
    public String toString(){
        return ("LOAD" + " " + n + " " + id);
    }
}
