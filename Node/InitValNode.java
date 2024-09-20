package Node;

import Symbol.VarSymbol;

import java.util.ArrayList;

public class InitValNode {
    private ArrayList<InitValNode> initValList;
    private ExpNode expNode;
    private VarSymbol varSymbol;
    private String value;
    private String regId;

    public InitValNode(ArrayList<InitValNode> initValList,ExpNode expNode){
        this.initValList=initValList;
        this.expNode=expNode;
    }
    public void print(){
        if (expNode != null) {
            expNode.print();
        } else {
            if (initValList.size() > 0) {
                for (int i = 0; i < initValList.size(); i++) {
                    initValList.get(i).print();
                    //if (i != initValList.size() - 1) {
                    //    IOUtils.write(commas.get(i).toString());
                    //}
                }
            }
        }
        //System.out.println("<InitVal>");
    }

    public ExpNode getExpNode() {
        return expNode;
    }

    public ArrayList<InitValNode> getInitValNodes() {
        return initValList;
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
