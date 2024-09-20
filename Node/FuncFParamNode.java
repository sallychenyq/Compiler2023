package Node;

import Symbol.VarSymbol;
import Token.Token;

import java.util.ArrayList;

public class FuncFParamNode {
    private int not0wei;
    private ArrayList<ConstExpNode> constExpList;
    private Token ident;
    private VarSymbol varSymbol;
    private String regId;
    private String value;

    public FuncFParamNode(ArrayList<ConstExpNode> constExpList, Token ident, int not0wei,VarSymbol symbol){
        this.constExpList=constExpList;
        this.ident=ident;
        this.not0wei=not0wei;
        this.varSymbol=symbol;
    }
    public void print(){
        if (constExpList.size() > 0) {
            constExpList.get(0).print();
            for (int i = 1; i < constExpList.size(); i++) {
                //IOUtils.write(commas.get(i - 1).toString());
                constExpList.get(i-1).print();
            }
        }
        //System.out.println("<FuncFParam>");
    }

    public Token getIdent() {
        return ident;
    }

    public ArrayList<ConstExpNode> getConstExpNodes() {
        return constExpList;
    }
    public int getNot0wei(){
        return not0wei;
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

   // public CharSequence getLeftBrackets() {    }
}
