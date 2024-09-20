package Node;

import Symbol.FuncSymbol;
import Symbol.VarSymbol;
import Token.Token;

import java.net.ContentHandler;
import java.util.ArrayList;

public class UnaryExpNode {
    private PrimaryExpNode primaryExpNode;
    private ArrayList<ExpNode> funcRParamsNode;
    private String unaryOpNode;
    private UnaryExpNode unaryExpNode;
    private Token ident;
    private VarSymbol varSymbol;
    private String value;
    private String regId;
    private FuncSymbol funcSymbol;
    private int type;

    public UnaryExpNode(PrimaryExpNode primaryExpNode,ArrayList<ExpNode> funcRParamsNode,UnaryExpNode unaryExpNode,
                        String unaryOpNode,Token ident){
        this.primaryExpNode=primaryExpNode;
        this.funcRParamsNode=funcRParamsNode;
        this.unaryOpNode=unaryOpNode;
        this.unaryExpNode=unaryExpNode;
        this.ident=ident;
    }
    public void print(){
        if(primaryExpNode!=null){
            primaryExpNode.print();
        }else if(unaryExpNode!=null){
            unaryExpNode.print();
            //}else if(unaryOpNode!=null){
            //unaryOpNode.print();
        }else{
            for (ExpNode expNode:funcRParamsNode){
                expNode.print();
            }
        }
        //System.out.println("<UnaryExp>");
    }

    public PrimaryExpNode getPrimaryExpNode() {
        return primaryExpNode;
    }

    public Token getIdent() {
        return ident;
    }

    public ArrayList<ExpNode> getFuncRParamsNode() {
        return funcRParamsNode;
    }

    public String getUnaryOpNode() {
        return unaryOpNode;
    }

    public UnaryExpNode getUnaryExpNode() {
        return unaryExpNode;
    }
    public VarSymbol getVarInfo(){
        return varSymbol;
    }
    public void setVarInfo(VarSymbol varSymbol){
        this.varSymbol=varSymbol;
    }
    public FuncSymbol getFuncInfo(){
        return funcSymbol;
    }

    public void setFuncInfo(FuncSymbol funcSymbol){
        this.funcSymbol=funcSymbol;
    }
    public String getRegId(){//下面这四个都是寄存器编号 “%0”
        return regId;
    }

    public String getValue(){
        return value;
    }

    public void setValue(String value){
        this.value=value;
    }

    public void setRegId(String regId){
        this.regId=regId;
    }
    public int getType(){
        return type;
    }
    public void setType(int type){
        this.type=type;
    }
}
