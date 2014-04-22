package Interpreter.ByteCode;

import Interpreter.VirtualMachine;
import java.util.*;

public class StoreCode extends ByteCode{
    private String n;
    private String id;
    private int value;
    
    public void execute(VirtualMachine vm) {
        value = vm.store(Integer.parseInt(n));    
        
    }

    public void init(Vector<String> args) {
        n = args.get(0);
        id = args.get(1);
    }
    
    @Override
    public String toString() {
        return "STORE " + Integer.parseInt(n) + " " + id + "   " + id + " = " + value;
    }
}