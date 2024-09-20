package Node;

import Symbol.VarSymbol;
import Token.Token;
import java.util.ArrayList;

public class ConstDefNode {
    private ArrayList<ConstExpNode> constExpList;
    private ConstInitValNode constInitValNode;
    VarSymbol varSymbol;
    Token ident;
    String value="";
    String regId="";//地址寄存器编号

    public ConstDefNode(ArrayList<ConstExpNode> constExpList, ConstInitValNode constInitValNode,Token ident,VarSymbol symbol){
        this.constExpList=constExpList;
        this.constInitValNode=constInitValNode;
        this.ident=ident;
        this.varSymbol=symbol;
    }
    public void print() {

        for (int i = 0; i < constExpList.size(); i++) {
            //IOUtils.write(leftBrackets.get(i).toString());
            constExpList.get(i).print();
            //IOUtils.write(rightBrackets.get(i).toString());
        }
        //IOUtils.write(equalToken.toString());
        constInitValNode.print();
        //System.out.println("<ConstDef>");
    }

    public Token getIdent() {
        return ident;
    }

    public ArrayList<ConstExpNode> getConstExpNodes() {
        return constExpList;
    }

    public ConstInitValNode getConstInitValNode() {
        return constInitValNode;
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
