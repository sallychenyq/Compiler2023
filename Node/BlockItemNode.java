package Node;

import java.util.ArrayList;

public class BlockItemNode {

    private DeclNode declNode;
    private StmtNode stmtNode;
    private int ContinueId;
    private int BreakId;

    public BlockItemNode(DeclNode declNode, StmtNode stmtNode){
        this.declNode=declNode;
        this.stmtNode=stmtNode;
    }
    public void print(){
        if (declNode != null) {
            declNode.print();
        } else if (stmtNode!=null){
            stmtNode.print();
        }

    }

    public DeclNode getDeclNode() {
        return declNode;
    }

    public StmtNode getStmtNode() {
        return stmtNode;
    }
    public int getBreakId(){
        return BreakId;
    }
    public void setBreakId(int breakId){
        this.BreakId=breakId;
    }
    public int getContinueId(){
        return ContinueId;
    }
    public void setContinueId(int continueId){
        this.ContinueId=continueId;
    }

}
