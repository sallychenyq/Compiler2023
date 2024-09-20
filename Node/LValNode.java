package Node;

import Symbol.VarSymbol;
import Token.Token;

import java.net.ContentHandler;
import java.util.ArrayList;

public class LValNode {
    private ArrayList<ExpNode> expList;
    private Token ident;
    private VarSymbol varSymbol;
    private String regId;
    private String value;
    private boolean change;
    private int type;

    public LValNode(ArrayList<ExpNode> expList,Token ident,boolean change){
        this.expList=expList;
        this.ident=ident;
        this.change=change;
    }
    public void print(){
        for (ExpNode expNode:expList){
            expNode.print();
        }
        //System.out.println("<LVal>");
    }
    public boolean changevalue(){
        return change;
    }
    public ArrayList<ExpNode> getExpNodes() {
        return expList;
    }

    public Token getIdent() {
        return ident;
    }
    public VarSymbol getVarInfo(){
        return varSymbol;
    }
    public void setVarInfo(VarSymbol varSymbol){
        this.varSymbol=varSymbol;
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
