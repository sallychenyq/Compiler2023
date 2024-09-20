package Node;

import Symbol.VarSymbol;
import Token.Token;

import java.util.ArrayList;

public class VarDefNode {
    private ArrayList<ConstExpNode> constExpList;
    private InitValNode initValNode;
    private VarSymbol varSymbol;
    private Token ident;
    private String value;
    private String regId;

    public VarDefNode(ArrayList<ConstExpNode> constExpList, InitValNode initValNode, Token ident,VarSymbol symbol){
        this.initValNode=initValNode;
        this.constExpList=constExpList;
        this.ident=ident;
        this.varSymbol=symbol;
    }
    public void print(){
        for (int i = 0; i < constExpList.size(); i++) {
            //IOUtils.write(leftBrackets.get(i).toString());
            constExpList.get(i).print();
        }
        if (initValNode != null) {
            //IOUtils.write(assign.toString());
            initValNode.print();
        }
       // System.out.println("<VarDef>");
    }

    public Token getIdent() {
        return ident;
    }

    public ArrayList<ConstExpNode> getConstExpNodes() {
        return constExpList;
    }

    public InitValNode getInitValNode() {
        return initValNode;
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

}
