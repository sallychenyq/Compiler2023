package Node;

public class CondNode {
    private LOrExpNode lOrExpNode;
    private String value;
    private int StmtId;
    private int YesId;
    private int NoId;

    public CondNode(LOrExpNode lOrExpNode) {
        this.lOrExpNode = lOrExpNode;
    }
    public void print() {
        lOrExpNode.print();
        //System.out.println("<Cond>");
    }

    public LOrExpNode getLOrExpNode() {
        return lOrExpNode;
    }
    public void setValue(String value){
        this.value=value;
    }
    public String getValue(){
        return value;
    }
    public int getStmtId(){
        return StmtId;
    }
    public void setStmtId(int stmtId){
        this.StmtId=stmtId;
    }
    public int getYesId(){
        return YesId;
    }
    public int getNoId(){
        return NoId;
    }
    public void setYesId(int yesId){
        this.YesId=yesId;
    }
    public void setNoId(int noId){
        this.NoId=noId;
    }
}
