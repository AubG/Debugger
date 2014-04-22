package Interpreter.ByteCode;

import Interpreter.VirtualMachine;
import java.util.*;

public class LabelCode extends ByteCode{
    String label;
    
    public void execute(VirtualMachine vm) {
        
    }

    public void init(Vector<String> args) {
        label = args.get(0);
    }
    
    @Override
    public String toString(){
        return ("LABEL" + " " + label);
    }
}