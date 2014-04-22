package Interpreter.ByteCode.DebugByteCodes;

import Interpreter.*;
import Interpreter.ByteCode.*;
import Interpreter.Debugger.DebugVirtualMachine;
import java.util.Vector;

public class DebugFunctionCode extends ByteCode {
    private String name, start, end;

    public void execute(VirtualMachine vm) {
        DebugVirtualMachine dvm = (DebugVirtualMachine) vm;
        dvm.setFunc(name, getStart(), getEnd());
        if(dvm.getIsTraceOn()){
            String function = "";
            for(int i = 0 ; i < dvm.getFERStackSize() ; i++)
                function += "    ";
            int temp = name.indexOf("<");
            if(temp < 0)
                function += name + "( ";
            else
                function += name.substring(0, temp) + "( ";
            int pc = dvm.getPC() + 1;
            ByteCode code = dvm.getCode(pc);
            while(code instanceof DebugFormalCode){
                function += dvm.getValue(dvm.getRunStackSize() - Integer.parseInt(((DebugFormalCode)code).getOffset())) + " ";
                code = dvm.getCode(++pc);
            }
            dvm.addTrace(function + ")");
        }
    }

    public void init(Vector<String> args){
        name = args.get(0);
        start = args.get(1);
        end = args.get(2);
    }

    @Override
    public String toString() {
        return "FUNCTION " + name + " " + getStart() + " " + getEnd();
    }

    /**
     * @return the start
     */
    public int getStart() {
        return Integer.parseInt(start);
    }

    /**
     * @return the end
     */
    public int getEnd() {
        return Integer.parseInt(end);
    }
}
