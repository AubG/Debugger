package Interpreter.ByteCode;

import java.util.*;
import Interpreter.*;

public class ArgsCode extends ByteCode{
    String n;
    
    public void execute(VirtualMachine vm) {
        vm.setFrame(Integer.parseInt(n));
    }

    public void init(Vector<String> args) {
        n = args.get(0);
    }
    
    @Override
    public String toString(){
        return ("ARGS" + " " + n);
    }
}