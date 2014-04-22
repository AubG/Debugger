package Interpreter.Debugger;

import java.util.*;

/** <pre>
 *  Binder objects group 3 fields
 *  1. a offset
 *  2. the next link in the chain of ids in the current scope
 *  3. the next link of a previous Binder for the same identifier
 *     in a previous scope
 *  </pre>
*/
class Binder {
  private Object offset;
  private String prevtop;   // prior symbol in same scope
  private Binder tail;      // prior binder for same symbol
                            // restore this when closing scope
  Binder(Object v, String p, Binder t) {
	offset=v; prevtop=p; tail=t;
  }

  Object getOffset() { return offset; }
  String getPrevtop() { return prevtop; }
  Binder getTail() { return tail; }
}


/** <pre>
 * The Table class is similar to java.util.Dictionary, except that
 * each key must be a Symbol and there is a scope mechanism.
 *
 * Consider the following sequence of events for table t:
 * t.put(Symbol("a"),5)
 * t.beginScope()
 * t.put(Symbol("b"),7)
 * t.put(Symbol("a"),9)
 * 
 * ids will have the key/value pairs for Symbols "a" and "b" as:
 * 
 * Symbol("a") ->
 *     Binder(9, Symbol("b") , Binder(5, null, null) )
 * (the second field has a reference to the prior Symbol added in this
 * scope; the third field refers to the Binder for the Symbol("a")
 * included in the prior scope)
 * Binder has 2 linked lists - the second field contains list of ids
 * added to the current scope; the third field contains the list of
 * Binders for the Symbols with the same string id - in this case, "a"
 * 
 * Symbol("b") ->
 *     Binder(7, null, null)
 * (the second field is null since there are no other ids to link
 * in this scope; the third field is null since there is no Symbol("b")
 * in prior scopes)
 * 
 * top has a reference to Symbol("a") which was the last symbol added
 * to current scope
 * 
 * Note: What happens if a symbol is defined twice in the same scope??
 * </pre>
*/
class Table {

  private java.util.HashMap<String,Binder> ids = new java.util.HashMap<String,Binder>();
  private String top;    // reference to last symbol added to
                         // current scope; this essentially is the
                         // start of a linked list of ids in scope
  private Binder marks;  // scope mark; essentially we have a stack of
                         // marks - push for new scope; pop when closing
                         // scope

  public Table(){}


 /**
  * Gets the object associated with the specified symbol in the Table.
  */
  public Object get(String key) {
	Binder e = ids.get(key);
	return e.getOffset();
  }

 /**
  * Puts the specified offset into the Table, bound to the specified Symbol.<br>
  * Maintain the list of ids in the current scope (top);<br>
  * Add to list of ids in prior scope with the same string identifier
  */
  public void put(String key, Object value) {
	ids.put(key, new Binder(value, top, ids.get(key)));
	top = key;
  }

 /**
  * Remembers the current state of the Table; push new mark on mark stack
  */
  public void beginScope() {
    marks = new Binder(null, top, marks);
    top = null;
  }

 /**
  * Restores the table to what it was at the most recent beginScope
  *	that has not already been ended.
  */
  public void popSymbols(int n) {
	for(int i = 0 ; i < n ; i++){
	   Binder e = ids.get(top);
	   if (e.getTail()!=null) 
               ids.put(top,e.getTail());
	   else 
               ids.remove(top);
           top = e.getPrevtop();
	}
  }
    
  public String[][] getVar(){
      Iterator<String> set = ids.keySet().iterator();
      String[][] variables = new String[ids.size()][2];
      for(int i = 0 ; i < ids.size() ; i++){
          variables[i][0] = set.next();
          variables[i][1] = ids.get(variables[i][0]).getOffset().toString();
      }
      return variables;
  }

  /**
   * @return a set of the Table's ids.
   */
  public java.util.Set<String> keys() {return ids.keySet();}
}


public class FunctionEnvironmentRecord {
    private Table ids;
    private int strtLine, endLine, crrntLine;
    private String name;
    
    public FunctionEnvironmentRecord(){
        ids = new Table();
    }

    /**
     * @return the strtLine
     */
    public int getStrtLine() {
        return strtLine;
    }

    /**
     * @param strtLine the strtLine to set
     */
    public void setStrtLine(int strtLine) {
        this.strtLine = strtLine;
    }

    /**
     * @return the endLine
     */
    public int getEndLine() {
        return endLine;
    }

    /**
     * @param endLine the endLine to set
     */
    public void setEndLine(int endLine) {
        this.endLine = endLine;
    }

    /**
     * @return the crrntLine
     */
    public int getCrrntLine() {
        return crrntLine;
    }

    /**
     * @param crrntLine the crrntLine to set
     */
    public void setCrrntLine(int crrntLine) {
        this.crrntLine = crrntLine;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
    public void setFunc(String name, int strt, int end){
        this.name = name;
        strtLine = strt;
        endLine = end;
    }
    
    public void enterID(String id, int value){
        ids.put(id, value);
    }
    
    public void popSymbols(int n){
        ids.popSymbols(n);
    }
    
    public void beginScope(){
        ids.beginScope();
    }
    
    public String[][] getVar(){
        return ids.getVar();
    }
    
    public void dump(){
        Iterator<String> i = ids.keys().iterator();
        String id;
        Object offset;
        int j = 1;
        System.out.print("(<");
        while(i.hasNext()){                
            id = i.next();
            offset = ids.get(id);
            System.out.print(id + "/" + offset);
            if(j < ids.keys().size())
                System.out.print(",");
            j++;
        }
        System.out.print(">,");
        if(name == null)
            System.out.print("-,");
        else
            System.out.print(name + ",");
        if(strtLine == 0)
            System.out.print("-,");
        else 
            System.out.print(strtLine + ",");
        if(endLine == 0)
            System.out.print("-,");
        else 
            System.out.print(endLine + ",");
        if(crrntLine == 0)
            System.out.print("-");
        else 
            System.out.print(crrntLine);
        System.out.println(")");
    }
}
