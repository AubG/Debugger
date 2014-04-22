/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package interpreter.debugger;

/**
 *
 * @author Aub
 */
public class Source {
    private String sourceLine;
    private boolean isBreakpoint;
    
    //Constructor
    public Source(){
        sourceLine = null;
        isBreakpoint = false;
    }
    
    /**
     *
     * @param sourceLine
     */
    public void setSourceLine(String sourceLine){
        this.sourceLine = sourceLine;
    }
    
    /**
     *
     * @return
     */
    public String getSourceLine(){
        return this.sourceLine;
    }
    
    /**
     *
     * @param isOn
     */
    public void isBreakpoint(Boolean isOn){
        isBreakpoint = isOn;
    }
    
    /**
     *
     * @return
     */
    public boolean getIsBreakpoint(){
        return isBreakpoint;
    }
}
