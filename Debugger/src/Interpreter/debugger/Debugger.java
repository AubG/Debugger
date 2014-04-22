package Interpreter.Debugger;

import Interpreter.ByteCodeLoader;
import Interpreter.Debugger.UI.*;
import Interpreter.Interpreter;
import Interpreter.Program;
import java.io.IOException;

public class Debugger {
    private UI ui;
    private DebugVirtualMachine dvm;
    private Interpreter interpreter;
    
    public Debugger(Interpreter interpreter){
        this.interpreter = interpreter;
    }
    
    public void debugRun(String sourceFile, ByteCodeLoader bcl) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
            Program program = bcl.loadCodes();
            dvm = new DebugVirtualMachine(program);
            dvm.loadSource(sourceFile);
            UI ui = new UI(dvm);
            do{
                ui.dumpSource();
                ui.userCommand();
            }while(dvm.getIsRunning());
        }
}
