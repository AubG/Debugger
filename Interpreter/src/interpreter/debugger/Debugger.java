/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interpreter.debugger;

import interpreter.ByteCodeLoader;
import interpreter.debugger.ui.*;
import interpreter.Interpreter;
import interpreter.Program;
import java.io.IOException;

/**
 *
 * @author Aub
 */
public class Debugger {

    private DebugUI ui;
    private DebugVirtualMachine dvm;
    private Interpreter interpreter;

    /**
     *
     * @param interpreter
     */
    public Debugger(Interpreter interpreter) {
        this.interpreter = interpreter;
    }

    /**
     *
     * @param sourceFile
     * @param bcl
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     */
    public void debugRun(String sourceFile, ByteCodeLoader bcl) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        Program prog = bcl.loadCodes();
        dvm = new DebugVirtualMachine(prog);
        dvm.loadSource(sourceFile);
        ui = new DebugUI(dvm);
        do {
            
            if(!dvm.getIsIntrinsic()){
                ui.dumpSource();
            } else {
                dvm.setIntrinsic(false);
            }
            
            ui.userCommand();
        } while (dvm.getIsRunning());
    }

}
