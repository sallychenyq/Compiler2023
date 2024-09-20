package Node;

import java.util.ArrayList;

public class ConstDeclNode {
    private ArrayList<ConstDefNode> constDefList;
    public ConstDeclNode(ArrayList<ConstDefNode> constDefList){
        this.constDefList=constDefList;
    }
    public void print() {
        for (int i = 0; i < constDefList.size(); i++) {
            constDefList.get(i).print();
        }
        //System.out.println("<ConstDecl>");
    }

    public ArrayList<ConstDefNode> getConstDefNodes() {
        return constDefList;
    }
}
