package Node;


import Symbol.VarSymbol;

public class ExpNode {
    private AddExpNode addExpNode;
    private VarSymbol varSymbol;
    private String value;
    private String regId;
    private int type;

    public ExpNode(AddExpNode addExpNode) {
        this.addExpNode = addExpNode;
    }
    public void print() {
        addExpNode.print();
        //System.out.println("<Exp>");
    }

    public AddExpNode getAddExpNode() {
        return addExpNode;
    }
    public VarSymbol getVarInfo(){
        return varSymbol;
    }
    public void setVarInfo(VarSymbol varSymbol){
        this.varSymbol=varSymbol;
    }
    public String getRegId(){//下面这四个都是寄存器编号 “%0”
        return regId;
    }

    public String getValue(){
        return value;
    }

    public void setValue(String value){
        this.value=value;
    }

    public void setRegId(String regId){
        this.regId=regId;
    }
    public int getType(){
        return type;
    }
    public void setType(int type){
        this.type=type;
    }
}
