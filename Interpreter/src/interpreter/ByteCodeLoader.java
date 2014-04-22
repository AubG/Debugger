/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interpreter;

import interpreter.bytecodes.ByteCode;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.Vector;

/**
 *
 * @author Aub
 */
public class ByteCodeLoader {

    String srcFile = "";

    public ByteCodeLoader(String programFile) throws IOException {
        srcFile = System.getProperty("user.dir") + "/" + programFile;
        //srcFile = new BufferedReader(new FileReader(programFile));
    }

    public Program loadCodes() throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, FileNotFoundException {
        Program prog = new Program();
        Vector<String> byteCodeInstance = new Vector<String>();
        ByteCode byteCode;
        BufferedReader in = new BufferedReader(new FileReader(srcFile));
        int position = 0;

        do {
            String codeLine = in.readLine();
            Scanner tokenizer = new Scanner(codeLine);
            String code = tokenizer.next();
            String codeClass = CodeTable.get(code);
            
            //Checking for Debug Byte Codes
            if (codeClass.startsWith("Debug")) {
                byteCode = (ByteCode) (Class.forName("interpreter.bytecodes.debugbytecodes." + codeClass).newInstance());
            } else {
                byteCode = (ByteCode) (Class.forName("interpreter.bytecodes." + codeClass).newInstance());
            }

            while (tokenizer.hasNext()) {
                byteCodeInstance.add(tokenizer.next());
            }

            byteCode.init(byteCodeInstance);
            prog.addByteCode(byteCode);

            if (codeClass.equals("LabelCode")) {
                prog.hashAddress(byteCodeInstance, position);
            }

            byteCodeInstance.clear();
            position++;

        } while (in.ready());

        prog.changeAddr();

        return prog;

    }
}
