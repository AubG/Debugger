package Interpreter.ByteCode;

import java.util.*;
import Interpreter.*;

public abstract class ByteCode {
    public abstract void execute(VirtualMachine vm);
    public abstract void init(Vector<String> args);
}
