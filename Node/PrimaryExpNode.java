package Node;

import Symbol.VarSymbol;

public class PrimaryExpNode {
    private ExpNode expNode = null;
    private LValNode lValNode = null;
    private String numberNode = null;
    private VarSymbol varSymbol;
    private String regId;
    private String value;
    private int type;

    public PrimaryExpNode(ExpNode expNode,LValNode lValNode,String numberNode){
        this.expNode=expNode;
        this.lValNode=lValNode;
        this.numberNode=numberNode;
    }
    public void print() {
        if (expNode != null) {
            expNode.print();
        } else if (lValNode != null) {
            lValNode.print();
        } //else {
          //  System.out.println(numberNode);
        //}
        //System.out.println("<PrimaryExp>");
    }

    public ExpNode getExpNode() {
        return expNode;
    }

    public LValNode getLValNode() {
        return lValNode;
    }

    public String getNumberNode() {
        return numberNode;
    }
    public void setVarInfo(VarSymbol varSymbol){
        this.varSymbol=varSymbol;
    }
    public VarSymbol getVarInfo(){
        return varSymbol;
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
