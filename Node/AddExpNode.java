package Node;

import Symbol.VarSymbol;

import java.util.ArrayList;

public class AddExpNode {
    private ArrayList<String> ops;
    private ArrayList<MulExpNode> addExpNodes;
    private MulExpNode mulExpNode;
    private String value;
    private VarSymbol varSymbol;
    private String regId;
    private int type;

    //public Token.Token sym;
    public AddExpNode(ArrayList<MulExpNode> addExpNodes, MulExpNode mulExpNode, ArrayList<String> ops){
        this.addExpNodes= addExpNodes;
        this.mulExpNode=mulExpNode;
        this.ops=ops;
    }
    public void print(){
        if (mulExpNode!=null) {
            mulExpNode.print();
            //System.out.println(sym.getType()+" "+sym.getStr());
        }else if(addExpNodes.size()!=0)
            for (MulExpNode i:addExpNodes){
                i.print();
            }
        //System.out.println("<AddExp>");
    }

    public MulExpNode getMulExpNode() {
        return mulExpNode;
    }

    public ArrayList<MulExpNode> getAddExpNode() {
        return addExpNodes;
    }

    public ArrayList<String> getOperator() {
        return ops;
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
