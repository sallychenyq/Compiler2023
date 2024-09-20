package Node;
import java.util.ArrayList;
public class CompUnitNode {
    private ArrayList<DeclNode> declList;
    private ArrayList<FuncDefNode> funcDefList;
    private MainFuncDefNode mainFuncDefNode;

    public CompUnitNode(ArrayList<DeclNode> declList, ArrayList<FuncDefNode> funcDefList,MainFuncDefNode mainFuncDefNode){
        this.declList = declList;
        this.funcDefList = funcDefList;
        this.mainFuncDefNode = mainFuncDefNode;
    }
    public void print() {
        for(int i=0;i<declList.size()&&declList.size()>0;i++)//for (DeclNode declNode : declList) {
            declList.get(i).print();
        //}
        for(int i=0;i<funcDefList.size()&&funcDefList.size()>0;i++)//for (FuncDefNode funcDefNode : funcDefList) {
            funcDefList.get(i).print();
        //}
        mainFuncDefNode.print();
        //System.out.println("<CompUnit>");//IOUtils.write(Parser.nodeType.get(NodeType.CompUnit));
    }

    public ArrayList<DeclNode> getDeclNodes() {
        return declList;
    }

    public ArrayList<FuncDefNode> getFuncDefNodes() {
        return funcDefList;
    }

    public MainFuncDefNode getMainFuncDefNode() {
        return mainFuncDefNode;
    }
}
