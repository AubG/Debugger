package Interpreter.ByteCode;

import Interpreter.VirtualMachine;
import java.util.*;

public class WriteCode extends ByteCode{
    public void execute(VirtualMachine vm) {
        System.out.println(vm.write()); 
    }

    public void init(Vector<String> args) {

    }
    
    @Override
    public String toString(){
        return ("WRITE");
    }
}