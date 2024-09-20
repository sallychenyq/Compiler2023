package Node;

public class MainFuncDefNode {
    private BlockNode blockNode;
    public MainFuncDefNode(BlockNode blockNode){
        this.blockNode=blockNode;
    }
    public void print(){
        blockNode.print();
        //System.out.println("<MainFuncDef>");
    }

    public BlockNode getBlockNode() {
        return blockNode;
    }
}
