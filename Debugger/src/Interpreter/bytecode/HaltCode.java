package Interpreter.ByteCode;

import Interpreter.VirtualMachine;
import java.util.*;

public class HaltCode extends ByteCode{
    public void execute(VirtualMachine vm) { 
        vm.halt();
    }
    
    public void init(Vector<String> args){
        
    }
    
    @Override
    public String toString(){
        return ("HALT");
    }
}
