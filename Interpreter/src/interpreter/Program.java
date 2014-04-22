/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package interpreter;

import java.util.*;
import interpreter.bytecodes.*;
import interpreter.bytecodes.debugbytecodes.DebugLineCode;

/**
 *
 * @author Aub
 */
public class Program {
    private static ArrayList<ByteCode> byteCodeList;
    private static HashMap<String,Integer> addrTable = new HashMap<>();
    
    public Program(){
        byteCodeList = new ArrayList<>();
    }
    
    public static void addByteCode(ByteCode byteCode){
        byteCodeList.add(byteCode);
    }
    
    public static void hashAddress(Vector<String> label, int position){
        addrTable.put(label.get(0), position);
    }
    
    public static void changeAddr(){
        ByteCode byteCode;
        
        for(int i = 0; i < byteCodeList.size() ; i++ ) {
            byteCode = byteCodeList.get(i);
            
            if(byteCode instanceof FalseBranchCode){
                ((FalseBranchCode) byteCode).setAddr(addrTable.get(((FalseBranchCode) byteCode).getLabel()));
            }
            if(byteCode instanceof CallCode){
                ((CallCode) byteCode).setAddr(addrTable.get(((CallCode) byteCode).getLabel()));
            }
            if(byteCode instanceof GoToCode){
                ((GoToCode) byteCode).setAddr(addrTable.get(((GoToCode) byteCode).getLabel()));
            }
        }
    }
    
    public ByteCode getCode(int pc){
        return byteCodeList.get(pc);
    }

    /**
     * Added this method for DVM
     * Checks whether breakpoint is valid before 
     * the DVM sets it.
     * @param breakP
     * @return
     */
    public boolean isValidBrk(int breakP){
        ByteCode temp;
        for(int i = 0 ; i < byteCodeList.size() ; i++){
            temp = byteCodeList.get(i);
            if(temp instanceof DebugLineCode){
                int line = ((DebugLineCode)temp).getLineNum();
                if(breakP == line)
                    return true;
            }
        }
        return false;
    }
}
