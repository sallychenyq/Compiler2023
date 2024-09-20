package Node;

import java.util.ArrayList;

public class EqExpNode {
    private ArrayList<String> ops;
    private ArrayList<RelExpNode> eqExpNodes;
    private RelExpNode relExpNode;
    private String value;
    private String regId;

    public EqExpNode(ArrayList<RelExpNode> eqExpNodes, RelExpNode relExpNode, ArrayList<String> ops){
        this.eqExpNodes= eqExpNodes;
        this.relExpNode=relExpNode;
        this.ops=ops;
    }
    public void print(){
        if (relExpNode!=null)
            relExpNode.print();
        else if(eqExpNodes.size()!=0)
            for(RelExpNode i:eqExpNodes){
                i.print();
            }
        //System.out.println("<EqExp>");
    }

    public RelExpNode getRelExpNode() {
        return relExpNode;
    }

    public ArrayList<RelExpNode> getEqExpNode() {
        return eqExpNodes;
    }

    public ArrayList<String> getOperator() {
        return ops;
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
}
