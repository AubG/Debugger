package Interpreter;

import java.util.*;
import Interpreter.ByteCode.*;
import Interpreter.ByteCode.DebugByteCodes.DebugLineCode;

public class Program {
    private static ArrayList<ByteCode> codeList;
    private static HashMap<String,Integer> addrTable = new HashMap<String,Integer>();
    
    public Program(){
        codeList = new ArrayList<ByteCode>();
    }
    
    public static void addByteCode(ByteCode byteCode){
        codeList.add(byteCode);
    }
    
    public static void hashAddress(Vector<String> label, int position){
        addrTable.put(label.get(0), position);
    }
    
    public static void changeAddr(){
        ByteCode byteCode;
        
        for(int i = 0 ; i < codeList.size() ; i++){
            byteCode = codeList.get(i);
            
            if(byteCode instanceof FalseBranchCode){
                 ((FalseBranchCode)byteCode).setAddr(addrTable.get(((FalseBranchCode)byteCode).getLabel()));
            }
            if(byteCode instanceof GoToCode){
                 ((GoToCode)byteCode).setAddr(addrTable.get(((GoToCode)byteCode).getLabel()));
            }
            if(byteCode instanceof CallCode){
                 ((CallCode)byteCode).setAddr(addrTable.get(((CallCode)byteCode).getLabel()));
            }
        }
        
    }
    
    public ByteCode getCode(int pc){
        return codeList.get(pc);
        
    }
    
    //Code Necessary for the debugger not needed for Interpreter
    public boolean isValidBrk(int breakP){
        ByteCode temp;
        for(int i = 0 ; i < codeList.size() ; i++){
            temp = codeList.get(i);
            if(temp instanceof DebugLineCode){
                int line = ((DebugLineCode)temp).getLine();
                if(breakP == line)
                    return true;
            }
        }
        return false;
    }
}
