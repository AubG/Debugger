package Interpreter.Debugger;

public class Source {
    private String sourceLine;
    private boolean isBreakptSet;

    public Source(){
        sourceLine = null;
        isBreakptSet = false;
    }

    public void setSourceLine(String sourceLine){
        this.sourceLine = sourceLine;
    }

    public String getSourceLine(){
        return sourceLine;
    }

    public void isBreakptSet(boolean isOn){
        isBreakptSet = isOn;
    }

    public boolean getIsBreakptSet(){
        return isBreakptSet;
    }
}
