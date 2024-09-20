package Node;

public class ConstExpNode {
    private AddExpNode addExpNode;
    private String value;

    public ConstExpNode(AddExpNode addExpNode) {
        this.addExpNode = addExpNode;
    }
    public void print() {
        addExpNode.print();
        //System.out.println("<ConstExp>");
    }

    public AddExpNode getAddExpNode() {
        return addExpNode;
    }
    public void setValue(String value){
        this.value=value;
    }
    public String getValue(){
        return value;
    }
}
