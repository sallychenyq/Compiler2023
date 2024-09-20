package Node;

import java.util.ArrayList;

public class RelExpNode {
    private ArrayList<String> ops;
    private AddExpNode addExpNode;
    private ArrayList<AddExpNode> relExpNodes;
    private String value;
    private String regId;

    public RelExpNode(AddExpNode addExpNode, ArrayList<AddExpNode> relExpNodes, ArrayList<String> ops){
        this.addExpNode= addExpNode;
        this.relExpNodes=relExpNodes;
        this.ops=ops;
    }
    public void print(){
        if (relExpNodes.size()!=0){
            for(AddExpNode i:relExpNodes){
                i.print();
            }
        }
        else if(addExpNode!=null)
            addExpNode.print();
        //System.out.println("<RelExp>");
    }

    public AddExpNode getAddExpNode() {
        return addExpNode;
    }

    public ArrayList<AddExpNode> getRelExpNode() {
        return relExpNodes;
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
