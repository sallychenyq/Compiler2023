package Symbol;

public class VarSymbol {
    private int kind;		// 1 -> const, 0 -> var
    private int dim;//shuzu维度
    String value="";//这个寄存器存的数值？？？
    String regId="";//地址寄存器编号
    //变量：类型，大小，数组维度，          每一维度大小// KeyValue key = new KeyValue();
    int d1=0;//第一个维度数（例如，a[3]就是3）
    int d2=0;//第二个weidu数（例如，a[2][4]就是4）
    String AddrType="i32";//i32orvoid  这里是取址的类型，主要用于函数调用各种维度时使用
    String intVal="";//0维常数初值存储
    String [] d1Value = null;//1维常数初值存储
    String [][] d2Value = null;//2维常数初值存储
    private int level;//所在的层数
    private Symbol symbol;
    private boolean type;

    /*public VarSymbol() {
        this.dim = 0;
        this.kind = kind;
    }*/
    public Symbol getsymbol(){
        return symbol;
    }
    public void setSymbol(Symbol symbol){
        this.symbol=symbol;
    }
    public boolean gettype(){
        return type;
    }
    public void settype(boolean type){
        this.type=type;
    }
    public int getDim(){//shuzuweidu
        return dim;
    }

    public void setDim(int dim){
        this.dim=dim;
    }
    public int getKind(){
        return kind;
    }
    public void setKind(int kind){
        this.kind=kind;
    }
    public void setLevel(int level){
        this.level=level;
    }
    public int getLevel(){
        return level;
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

    public void setD1(int d1){
        this.d1=d1;
        //if(d1==0){d1Value = null;}
        if (d1!=0){d1Value = new String[d1];}
    }

    public void setD2(int d2){
        this.d2=d2;
        //if(this.d1==0){d2Value = null;}
        if (this.d1!=0){d2Value = new String[this.d1][d2];}
    }
    public int getD1(){
        return d1;
    }//第一个维度数

    public int getD2(){
        return d2;
    }

    public void setAddrType(String addType){
        AddrType=addType;
    }

    public String getAddrType(){
        return AddrType;
    }


    public void setD1Value(String[] d1Value){
        this.d1Value=d1Value;
    }

    public void setD2Value(String[][] d2Value){
        this.d2Value=d2Value;
    }

    public String[] getD1Value(){
        return d1Value;
    }

    public String[][] getD2Value(){
        return d2Value;
    }

    public void setIntVal(String intVal){
        this.intVal=intVal;
    }//0维常数初值

    public String getIntVal(){
        return intVal;
    }

}
