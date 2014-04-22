package Interpreter.ByteCode;

import Interpreter.VirtualMachine;
import java.util.*;

public class LitCode extends ByteCode{
    String n;
    String id;
    private boolean add = false;
    
    public void execute(VirtualMachine vm) {  
        vm.pushLit(Integer.parseInt(n));
    }

    public void init(Vector<String> args) {
        n = args.get(0);
        if(args.size() > 1){
            id = args.get(1);
            add = true;
        }
    }
    
    @Override
    public String toString(){
        return ("LIT" + " " + n + " " + id);
    }
    
    public String getID(){
        return id;
    }
    
    public boolean getAddSym(){
        return add;
    }
}