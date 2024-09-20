package Node;

import java.util.ArrayList;

public class VarDeclNode {
    private ArrayList<VarDefNode> varDefList;
    public VarDeclNode(ArrayList<VarDefNode> varDefList){
        this.varDefList=varDefList;
    }
    public void print(){
        varDefList.get(0).print();
        for (int i = 1; i < varDefList.size(); i++) {
            //IOUtils.write(commas.get(i - 1).toString());
            varDefList.get(i).print();
        }
        //System.out.println("<VarDef>");
    }

    public ArrayList<VarDefNode> getVarDefNodes() {
        return varDefList;
    }
}
