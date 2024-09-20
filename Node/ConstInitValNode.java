package Node;

import Symbol.VarSymbol;

import java.util.ArrayList;

public class ConstInitValNode {
    private ArrayList<ConstInitValNode> constInitValList;
    private ConstExpNode constExpNode;
    private VarSymbol varSymbol;
    private String regId;
    private String value;

    public ConstInitValNode(ArrayList<ConstInitValNode> constInitValList,ConstExpNode constExpNode){
        this.constExpNode=constExpNode;
        this.constInitValList=constInitValList;
    }
    public void print(){
        if (constExpNode != null) {
            constExpNode.print();
        } else {
            //IOUtils.write(leftBraceToken.toString());
            if (constInitValList.size() > 0) {
                constInitValList.get(0).print();
                for (int i = 1; i < constInitValList.size(); i++) {
                    //IOUtils.write(commas.get(i - 1).toString());
                    constInitValList.get(i).print();
                }
            }
            //IOUtils.write(rightBraceToken.toString());
        }
        //System.out.println("<ConstInitVal");
    }

    public ConstExpNode getConstExpNode() {
        return constExpNode;
    }

    public ArrayList<ConstInitValNode> getConstInitValNodes() {
        return constInitValList;
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
