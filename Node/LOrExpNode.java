package Node;

public class LOrExpNode {
    private LOrExpNode lorExpNode;
    private LAndExpNode landExpNode;
    private String value;
    private int NoId;
    private int YesId;
    private int StmtId;

    public LOrExpNode(LOrExpNode lorExpNode,LAndExpNode landExpNode){
        this.lorExpNode= lorExpNode;
        this.landExpNode=landExpNode;
    }
    public void print(){
        if (landExpNode!=null)
            landExpNode.print();
        else if(lorExpNode!=null)
            lorExpNode.print();
        //System.out.println("<LOrExp>");
    }

    public LOrExpNode getLOrExpNode() {
        return lorExpNode;
    }

    public LAndExpNode getLAndExpNode() {
        return landExpNode;
    }

    public String getValue(){
        return value;
    }

    public void setValue(String value){
        this.value=value;
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
    public int getStmtId(){
        return StmtId;
    }
    public void setStmtId(int stmtId){
        this.StmtId=stmtId;
    }
}
