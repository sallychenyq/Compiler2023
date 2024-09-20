package Node;

public class ForStmtNode {
    private LValNode lvalNode;
    private ExpNode expNode;
    private int StmtId;

    public ForStmtNode(LValNode lvalNode,ExpNode expNode){
        this.lvalNode=lvalNode;
        this.expNode=expNode;
    }
    public void print(){
        lvalNode.print();
        expNode.print();
        //System.out.println("<ForStmt>");
    }
    public LValNode getLvalNode(){
        return lvalNode;
    }
    public ExpNode getExpNode(){
        return expNode;
    }
    public int getStmtId(){
        return StmtId;
    }
    public void setStmtId(int stmtId){
        this.StmtId=stmtId;
    }
}
