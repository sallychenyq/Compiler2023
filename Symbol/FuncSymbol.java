package Symbol;

import java.util.ArrayList;

public class FuncSymbol {
    private String returnType;//1:int,0:void
    private int paramnum;
    int depth;
    private ArrayList<Integer> paramtype=new ArrayList<>();
    private boolean type;

    public void setReturnType(String returnType){
        this.returnType=returnType;
    }

    public String getReturnType(){
        return returnType;
    }
    public int getParamnum(){
        return paramnum;
    }
    public void setParamnum(int paramnum){
        this.paramnum=paramnum;
    }
    public ArrayList<Integer> getParamtype(){
        return paramtype;
    }public void setParamtype(ArrayList<Integer> paramtype){
        this.paramtype=paramtype;
    }
    public void setDepth(int depth){
        this.depth=depth;
    }public int getDepth(){
        return depth;
    }public boolean gettype(){
        return type;
    }
    public void settype(boolean type){
        this.type=type;
    }
    //函数：返回值类型，           参数类型及顺序, 作用域
    // func：
    // 	retype		// 0 -> int, 1 -> void
    //	paramNum	// 参数数量
    // 	paramTypeList	// 参数类型
}
