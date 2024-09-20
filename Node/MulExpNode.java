package Node;

import Symbol.VarSymbol;

import java.util.ArrayList;

public class MulExpNode {
    private ArrayList<String> ops;
    private UnaryExpNode unaryExpNode;
    private ArrayList<UnaryExpNode> mulExpNodes;
    private String regId;
    private String value;
    private VarSymbol varSymbol;
    private int type;

    public MulExpNode(UnaryExpNode unaryExpNode, ArrayList<UnaryExpNode> mulExpNodes, ArrayList<String> ops){
        this.unaryExpNode=unaryExpNode;
        this.mulExpNodes=mulExpNodes;
        this.ops=ops;
    }
    public void print(){
        if (mulExpNodes.size()!=0)
            for (UnaryExpNode i:mulExpNodes){
                i.print();
            }

        else if(unaryExpNode!=null)
            unaryExpNode.print();
        //System.out.println("<MulExp>");
    }

    public UnaryExpNode getUnaryExpNode() {
        return unaryExpNode;
    }

    public ArrayList<UnaryExpNode> getMulExpNode() {
        return mulExpNodes;
    }

    public ArrayList<String> getOperator() {
        return ops;
    }

    //public Object getStr() {    }
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
