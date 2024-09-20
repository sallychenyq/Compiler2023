package Symbol;

import Token.Token;

public class Symbol {

    public int id;		// 当前单词对应的poi。
    //ident,return,const值,"&num",for,break,continue,
    public int level;//block//public int tableId; 	// 当前单词所在的符号表编号。
    public Token token; 	// 当前单词所对应的字符串。sym.getstr()
    public int type; 		// 0 -> a, 1 -> a[], 2 -> a[][], -1 -> func

    //public int num=1,table=0;

    int StmtId=0;
    int YesId=0;
    int NoId=0;
    int BreakId=0;
    int ContinueId=0;
    String returnType="";
    VarSymbol varSymbol= new VarSymbol();
    FuncSymbol funcSymbol=new FuncSymbol();

    public Symbol(Token token, int type) {

        this.token = token;
        this.type = type;
    }

    public Token getIdent() {
        return token;
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
    public void setLevel(int level){
        this.level=level;
    }
    public int getLevel(){
        return level;
    }

    public int getBreakId(){
        return BreakId;
    }
    public void setBreakId(int breakId){
        BreakId=breakId;
    }
    public int getContinueId(){
        return ContinueId;
    }
    public void setContinueId(int continueId){
        ContinueId=continueId;
    }

    public int getStmtId(){
        return StmtId;
    }
    public void setStmtId(int stmtId){
        StmtId=stmtId;
    }
    public int getYesId(){
        return YesId;
    }
    public int getNoId(){
        return NoId;
    }
    public void setYesId(int yesId){
        YesId=yesId;
    }
    public void setNoId(int noId){
        NoId=noId;
    }

    /*public AstNode changeNode(){
        AstNode s = this.child.get(this.child.size()-1);
        this.child.remove(this.child.size()-1);
        return s;
    }*/

}