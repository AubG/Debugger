package Interpreter;

import java.util.*;

public class RunTimeStack {
    Stack<Integer> framePointers;
    Vector<Integer> runStack;
    
    public RunTimeStack(){
        framePointers = new Stack<Integer>();
        runStack = new Vector<Integer>();
        framePointers.push(0);
    }
    
   public void dump(){
       Iterator<Integer> temp = framePointers.iterator();
       int i = 0;

       if((framePointers.size()) == 1){
           System.out.print("[");
           while(i < (runStack.size() - 1)){
               System.out.print(runStack.get(i++) + ",");
           }
           System.out.print(runStack.get(i) + "]");
       } else {
           temp.next();

           while (temp.hasNext()) {
               System.out.print("[");
               int j = temp.next() - 1;
               while (i < j) {
                   System.out.print(runStack.get(i++) + ",");
               }
               System.out.print(runStack.get(j) + "]");
               i++;
           }

           System.out.print("[");
           while(i < (runStack.size()-1)){
               System.out.print(runStack.get(i++) + ",");
           }
           System.out.print(runStack.get(i) + "]");
       }
       System.out.println();
    }
    
    public int peek(){
        return runStack.get(runStack.size() - 1);
    }
    
    public int push(int i){
        runStack.add(i);
        return i;
    }
    
    public Integer push(Integer i){
        runStack.add(i);
        return i;
    }
    
    public int pop(){
        return runStack.remove(runStack.size() - 1);
    }
    
    public void newFrameAt(int offset){
        framePointers.push(runStack.size() - offset);
    }    
    
    public void popFrame(){
        int top = (runStack.size() - 1);
        Integer temp = runStack.remove(top);
        top--;
        Integer frame = framePointers.pop();
        for(int i = top; i >= frame; i--)
            runStack.remove(i);
        runStack.add(temp);
    }
    
    public int store(int offset){
        int temp = runStack.remove(runStack.size() - 1);
        runStack.set(offset + framePointers.peek(), temp);
        return temp;
    }
    
    public int load(int offset){
        runStack.add(runStack.get(offset + framePointers.peek()));
        return runStack.get(offset + framePointers.peek());
    }    
    
    public Vector<Integer> getArgs() {
        int frameIndex = framePointers.peek();
        Vector<Integer> args = new Vector<Integer>();
        
        while (frameIndex < runStack.size()) {
            int var = runStack.get(frameIndex);
            args.add(var);
            frameIndex++;
        }
        
        return args;
    }
    
    public String getValue(int i){
        if(i == 0)
            return runStack.get(i).toString();
        else
            return runStack.get(i-1).toString();
    }
    
    
}

