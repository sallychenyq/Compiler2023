package Node;

import java.util.ArrayList;

public class FuncFParamsNode {
    private ArrayList<FuncFParamNode> funcFParamList;
    private String value;

    public FuncFParamsNode(ArrayList<FuncFParamNode> funcFParamList){
        this.funcFParamList=funcFParamList;
    }
    public void print(){
        funcFParamList.get(0).print();
        for (int i = 1; i < funcFParamList.size(); i++) {
            //IOUtils.write(commas.get(i - 1).toString());
            funcFParamList.get(i).print();
        }
        //System.out.println("<FuncFParams>");
    }

    public ArrayList<FuncFParamNode> getFuncFParamNodes() {
        return funcFParamList;
    }
    public String getValue(){
        return value;
    }

    public void setValue(String value){
        this.value=value;
    }
}
