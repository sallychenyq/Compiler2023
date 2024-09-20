package Node;

public class LAndExpNode {
    private EqExpNode eqExpNode;
    private LAndExpNode landExpNode;
    private String value;
    private int YesId;
    private int NoId;
    private int StmtId;

    public LAndExpNode(EqExpNode eqExpNode,LAndExpNode landExpNode){
        this.eqExpNode= eqExpNode;
        this.landExpNode=landExpNode;
    }
    public void print(){
        if (landExpNode!=null)
            landExpNode.print();
        else if(eqExpNode!=null)
            eqExpNode.print();
       // System.out.println("<LAndExp>");
    }

    public LAndExpNode getLAndExpNode() {
        return landExpNode;
    }

    public EqExpNode getEqExpNode() {
        return eqExpNode;
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
