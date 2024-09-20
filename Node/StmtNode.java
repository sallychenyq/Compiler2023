package Node;

import Token.Token;

import java.util.ArrayList;

public class StmtNode {

    private Token ident;
    private String string;
    private StmtType type;
    private ArrayList<ExpNode> expList;
    private StmtNode stmtNode;
    private ForStmtNode forStmtNode1,forStmtNode2;
    private CondNode condNode;
    private ArrayList<StmtNode> stmtList;
    private ExpNode expNode;
    private LValNode lValNode;
    private BlockNode blockNode;
    private Token ret;
    private int BreakId;
    private int ContinueId;
    private int StmtId;

    public enum StmtType {
        LValExp, Exp, Block, If, For, Return, LValGetint, Printf,Continue,Break
    }
    public StmtNode(StmtType type,LValNode lValNode,ExpNode expNode) {
        // LVal '=' Exp ';'
        this.type = type;
        this.lValNode = lValNode;
        this.expNode = expNode;
    }
    public StmtNode(StmtType type,ExpNode expNode) {
        // [Exp] ';'
        this.type = type;
        this.expNode = expNode;

    }
    public StmtNode(StmtType type,ExpNode expNode,Token ret){
        // 'return' [Exp] ';'
        this.type = type;
        this.expNode = expNode;
        this.ret=ret;
    }
    public StmtNode(StmtType type,BlockNode blockNode) {
        // Block
        this.type = type;
        this.blockNode = blockNode;
    }

    public StmtNode(StmtType type,CondNode condNode, ArrayList<StmtNode> stmtList) {
        // 'if' '(' Cond ')' Stmt [ 'else' Stmt ]
        this.type = type;
        this.condNode = condNode;
        this.stmtList = stmtList;
    }

    public StmtNode(StmtType type,ForStmtNode forStmtNode1,ForStmtNode forStmtNode2, CondNode condNode,StmtNode stmtNode) {
        // 'for' '(' [ForStmt] ';' [Cond] ';' [ForStmt] ')' Stmt
        this.type = type;
        this.forStmtNode1=forStmtNode1;
        this.forStmtNode2=forStmtNode2;
        this.condNode = condNode;
        this.stmtNode = stmtNode;
    }

    public StmtNode(StmtType type,LValNode lValNode) {
        // LVal '=' 'getint' '(' ')' ';'
        this.type = type;
        this.lValNode = lValNode;
    }

    public StmtNode(StmtType type,ArrayList<ExpNode> expList,String string) {
        // 'printf' '(' FormatString { ',' Exp } ')' ';'
        this.type = type;
        this.expList=expList;
        this.string=string;
    }
    public StmtNode(StmtType type,Token ident){
        // 'break' ';'| 'continue' ';'
        this.type=type;
        this.ident=ident;
    }

    public void print(){
        switch (type) {
            case LValExp:
                // LVal '=' Exp ';'
                lValNode.print();
                expNode.print();
                break;
            case Exp:
                // [Exp] ';'
                if (expNode != null) expNode.print();
                break;
            case Block:
                // Block
                blockNode.print();
                break;
            case If:
                // 'if' '(' Cond ')' Stmt [ 'else' Stmt ]
                condNode.print();
                for (StmtNode stmtNode1:stmtList)
                    stmtNode1.print();

                break;
            case For:
                //'for' '(' [ForStmt] ';' [Cond] ';' [ForStmt] ')' Stmt
                //for (int i = 0; i < forStmtList.size()&&forStmtList.size()!=0; i++) {
                if (forStmtNode1!=null){
                    forStmtNode1.print();
                }

                if (condNode!=null) condNode.print();
                if (forStmtNode2!=null){
                    forStmtNode2.print();
                }
                stmtNode.print();
                break;
            case Return:
                // 'return' [Exp] ';'
                if (expNode != null) {
                    expNode.print();
                }
                break;
            case LValGetint:
                // LVal '=' 'getint' '(' ')' ';'
                lValNode.print();
                break;
            case Printf:
                // 'printf' '(' FormatString { ',' Exp } ')' ';'
                for (int i = 0; i < expList.size()&&expList.size()!=0; i++) {
                    expList.get(i).print();
                }
                break;
            default:

        }
       // System.out.println("<Stmt>");
    }
    public StmtType getType() {
        return type;
    }
    public Token getRet(){return ret;}
    public Token getIdent(){return ident;}
    public LValNode getLValNode() {
        return lValNode;
    }
    public ExpNode getExpNode() {
        return expNode;
    }
    public BlockNode getBlockNode() {
        return blockNode;
    }
    public CondNode getCondNode() {
        return condNode;
    }
    public ArrayList<StmtNode> getStmtNodes() {
        return stmtList;
    }
    //public boolean getElseToken() {    }
    public ArrayList<ExpNode> getExpNodes() {
        return expList;
    }
    public String getFormatString() {
        return string;
    }
    public ForStmtNode getForStmtNode1(){
        return forStmtNode1;
    }
    public ForStmtNode getForStmtNode2(){
        return forStmtNode2;
    }
    public StmtNode getStmtNode(){
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

    public int getStmtId(){
        return StmtId;
    }
    public void setStmtId(int stmtId){
        this.StmtId=stmtId;
    }

}
