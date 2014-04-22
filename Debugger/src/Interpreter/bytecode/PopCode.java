package Interpreter.ByteCode;

import Interpreter.VirtualMachine;
import java.util.*;

public class PopCode extends ByteCode{
    String n;
    
    public void execute(VirtualMachine vm) {    
        vm.pop(Integer.parseInt(n));
    }
    
    public void init(Vector<String> args){
        n = args.get(0);
    }
    @Override
    public String toString(){
        return ("POP" +  " " + n);
    }
    
    public int getOffset(){
        return Integer.parseInt(n);
    }
}
