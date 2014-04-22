package Interpreter;

import Interpreter.ByteCode.*;
import java.io.*;
import java.util.*;

public class ByteCodeLoader {
    BufferedReader srcFile;
    
    public ByteCodeLoader(String programFile) throws IOException {
        srcFile = new BufferedReader(new FileReader(programFile));        
    }
    
    public Program loadCodes() throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException{
        Program program = new Program();
        Vector<String> args = new Vector<String>();
        int i = 0;
        ByteCode byteCode;
        
        do {
            String codeLine = srcFile.readLine();
            Scanner tokenizer = new Scanner(codeLine);
            String code = tokenizer.next();
            String codeClass = CodeTable.get(code);
            
           //Specifically for the Debugger Not necessary yet
            
            if(codeClass.startsWith("Debug"))
                byteCode = (ByteCode) (Class.forName("Interpreter.ByteCode.DebugByteCodes." + codeClass).newInstance());
            else
                byteCode = (ByteCode) (Class.forName("Interpreter.ByteCode." + codeClass).newInstance());
            
            
            while(tokenizer.hasNext()){
                args.add(tokenizer.next());
            }
            byteCode.init(args);
            program.addByteCode(byteCode);
            if(codeClass.equals("LabelCode"))
                program.hashAddress(args, i);
            args.clear();
            i++;
            
        } while(srcFile.ready());
        
        program.changeAddr();
        
        return program;
    }
}
