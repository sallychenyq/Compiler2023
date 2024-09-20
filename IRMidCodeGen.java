import Node.*;
import Symbol.*;
import Token.Token;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class IRMidCodeGen{
    private CompUnitNode compUnitNode;
    int level=0;
    //AstNode Rootast;
    int regId=1;
    int nowtag=0;
    boolean ret, infor,blockret,lastret;//hasret,
    ArrayList <Symbol> stack = new ArrayList<>();//AstNode
    HashMap <String,Symbol> global= new HashMap <> ();
    //SymbolTable stack=new SymbolTable(0);
    ArrayList<Integer> paramtype=new ArrayList<>();
    PrintWriter pw;

    //FileWriter fw=null;
    public IRMidCodeGen(CompUnitNode compUnitNode) {
        this.compUnitNode = compUnitNode;
    }
   /* public void generating(){
        generate(this.Rootast);
    }
    public void generate(AstNode ast){
        if(ast.getIdent().equals("<ConstDef>")){ConstDef(ast);}
        else
    }*/

    public void visitCompUnit() {
        //try {
            init();
            SymbolManager.getInstance().init();//addTable(); // 创建新的符号表//addSymbolAndConstTable();
            Symbol symgetint = new Symbol(new Token("GETINTTK", "getint", 0), -1);
            //?????SymbolTable.addSymbol(symgetint); // 符号注册getInstance().
            FuncSymbol funcgetint = symgetint.getFuncInfo();
            symgetint.setFuncInfo(funcgetint);
            // CompUnit -> {Decl} {FuncDef} MainFuncDef
            for (DeclNode declNode : compUnitNode.getDeclNodes()) {
                if (declNode.constORvar() == 0) {
                    visitConstDecl(declNode.getConstDecl());
                } else {
                    visitVarDecl(declNode.getVarDecl());
                }
            }
            for (FuncDefNode funcDefNode : compUnitNode.getFuncDefNodes()) {
                visitFuncDef(funcDefNode);
            }
            visitMainFuncDef(compUnitNode.getMainFuncDefNode());
//        } catch (Exception e){
//            throw new RuntimeException("Runtime Error");
//        }
    }

    private void visitConstDecl(ConstDeclNode constDeclNode) {
        // ConstDecl -> 'const' BType ConstDef { ',' ConstDef } ';'
       // tmpType = "i32";
        for (ConstDefNode constDefNode : constDeclNode.getConstDefNodes()) {
            visitConstDef(constDefNode);
        }
    }
    public void visitConstDef(ConstDefNode a){
        // ConstDef -> Ident { '[' ConstExp ']' } '=' ConstInitVal
        //ArrayList<AstNode> a=ast.getChild();
        //AstNode ident = a.get(0);
        Symbol symbol =null;
        VarSymbol k = new VarSymbol();//ident

        if(a.getConstExpNodes().size()==0){
            symbol=new Symbol(a.getIdent(), 0);
            k=symbol.getVarInfo();
            if(level!=0){
                pw.println("    %r"+this.regId+" = alloca i32");pw.flush();
                k.setValue("%r"+this.regId);
                k.setRegId("%r"+this.regId);
                this.regId++;
            }
            //k=symbol.getVarInfo();
            k.setDim(0);
            a.getConstInitValNode().setVarInfo(k);
            k.setKind(1);if (k.getRegId().equals("")) k.setRegId(a.getRegId());
            symbol.setVarInfo(k);
            visitConstInitVal(a.getConstInitValNode());
            k.setValue(a.getConstInitValNode().getVarInfo().getValue());
            if (SymbolManager.getInstance().addVarDef(symbol,k)==-1){
                ErrorHandler.addError(new ErrorHandler(symbol.getIdent().getLineNum(),'b'));

            }else{
            //symbol = SymbolManager.getInstance().findSymbol(a.getIdent().getStr(), true);
            if(level==0){
                pw.println("@"+a.getIdent().getStr()+" = dso_local constant i32 "+k.getValue());pw.flush();
            }
            else{
                pw.println("    store i32 "+a.getConstInitValNode().getValue()+", i32* "+k.getRegId());pw.flush();
            }}
        }
        else if(a.getConstExpNodes().size()==1){
            int l=this.level;
            this.level=0;
            visitConstExp(a.getConstExpNodes().get(0));
            this.level=l;
            symbol=new Symbol(a.getIdent(), 1);
            k=symbol.getVarInfo();
            if(level!=0){
                pw.println("    %r"+this.regId+" = alloca ["+a.getConstExpNodes().get(0).getValue()+" x i32]");pw.flush();
                a.setValue("%r"+this.regId);
                a.setRegId("%r"+this.regId);
                this.regId++;
            }
            if (a.getVarInfo().getDim()!=-1) k.setDim(1);
            else k.setDim(-1);
            k.setD1(Integer.parseInt(a.getConstExpNodes().get(0).getValue()));
            a.getConstInitValNode().setVarInfo(k);
            k.setKind(1);if (k.getRegId().equals("")) k.setRegId(a.getRegId());
            symbol.setVarInfo(k);
            //symbol = SymbolManager.getInstance().findSymbol(a.getIdent().getStr(), true);
            visitConstInitVal(a.getConstInitValNode());

            k.setValue(a.getConstInitValNode().getVarInfo().getValue());
            if (SymbolManager.getInstance().addVarDef(symbol,k)==-1){
                ErrorHandler.addError(new ErrorHandler(symbol.getIdent().getLineNum(),'b'));

            }else {

                if (level == 0) {
                    pw.print("@" + a.getIdent().getStr() + " = dso_local constant [" + k.getD1() + " x i32] [");
                    pw.flush();
                    String[] d1v = k.getD1Value();
                    for (int i = 0; i < k.getD1() - 1; i++) {
                        pw.print("i32 " + d1v[i] + ", ");
                        pw.flush();
                    }
                    pw.println("i32 " + k.getD1Value()[k.getD1() - 1] + "]");
                    pw.flush();
                } else {
                    String[] d1v = k.getD1Value();
                    for (int i = 0; i < k.getD1(); i++) {
                        if (!(d1v[i].equals("NuLL"))) {
                            pw.println("    %r" + this.regId + " = getelementptr [" + k.getD1() + " x i32], [" + k.getD1() + " x i32]*" +
                                    k.getRegId() + ", i32 0, i32 " + i);
                            pw.flush();
                            pw.println("    store i32 " + d1v[i] + ", i32* %r" + this.regId);
                            pw.flush();
                            this.regId++;
                        }
                    }
                }
            }
        }
        else if(a.getConstExpNodes().size()==2){
            int l=this.level;
            this.level=0;
            visitConstExp(a.getConstExpNodes().get(0));
            visitConstExp(a.getConstExpNodes().get(1));
            this.level=l;
            symbol=new Symbol(a.getIdent(),2);
            k=symbol.getVarInfo();
            if(level!=0){
                pw.println("    %r"+this.regId+" = alloca ["+a.getConstExpNodes().get(0).getValue()+" x [ "+
                        a.getConstExpNodes().get(1).getValue() +" x i32]]");pw.flush();
                k.setValue("%r"+this.regId);
                k.setRegId("%r"+this.regId);
                this.regId++;
            }
            if (a.getVarInfo().getDim()!=-1) k.setDim(2);
            else k.setDim(-1);
            k.setD1(Integer.parseInt(a.getConstExpNodes().get(0).getValue()));
            k.setD2(Integer.parseInt(a.getConstExpNodes().get(1).getValue()));
            a.getConstInitValNode().setVarInfo(k);
            k.setKind(1);if (k.getRegId().equals("")) k.setRegId(a.getRegId());
            symbol.setVarInfo(k);
            //symbol = SymbolManager.getInstance().findSymbol(a.getIdent().getStr(), true);
            visitConstInitVal(a.getConstInitValNode());
            if (SymbolManager.getInstance().addVarDef(symbol,k)==-1){
                ErrorHandler.addError(new ErrorHandler(symbol.getIdent().getLineNum(),'b'));

            }else{
            if(level==0){
                pw.print("@"+a.getIdent().getStr()+" = dso_local constant ["+k.getD1()+" x ["+k.getD2()+" x i32]] [[");pw.flush();
                String [][]d2v = k.getD2Value();
                for(int i=0;i<k.getD1()-1;i++){
                    pw.print(k.getD2()+" x i32] [");pw.flush();
                    for(int j=0;j<k.getD2()-1;j++){
                        pw.print("i32 "+d2v[i][j]+", ");pw.flush();
                    }
                    pw.print("i32 "+k.getD2Value()[i][k.getD2()-1]+"], [");pw.flush();
                }
                pw.println(k.getD2()+" x i32] [");pw.flush();
                for(int j=0;j<k.getD2()-1;j++){
                    pw.print("i32 "+d2v[k.getD1()-1][j]+", ");pw.flush();
                }
                pw.println("i32 "+k.getD2Value()[k.getD1()-1][k.getD2()-1]+"]]");pw.flush();
            }
            else{
                String [][]d2v = k.getD2Value();
                for(int i=0;i<k.getD1();i++){
                    for(int j=0;j<k.getD2();j++){
                        if(!(d2v[i][j].equals("NuLL"))){
                            pw.println("    %r"+this.regId+" = getelementptr ["+k.getD1()+" x ["+k.getD2()+" x i32]], ["+
                                    k.getD1()+" x ["+k.getD2()+" x i32]]*"+k.getRegId()+", i32 0, i32 "+i+", i32 "+j);pw.flush();
                            pw.println("    store i32 "+d2v[i][j]+", i32* %r"+this.regId);pw.flush();
                            this.regId++;
                        }
                    }
                }
            }}
        }//k.setKind(1);
        a.setRegId(k.getRegId());//a.setVarInfo(k);//ident.setVarInfo(k);
        if(level==0){
            if(global.get(a.getIdent().getStr())==null&&symbol!=null){
                global.put(a.getIdent().getStr(),symbol);
            }else {
                //Error.addError(new Error(a.getIdent().getLineNum(),'b'));
            }

        }
        /*else{
            symbol.setLevel(this.level);
            boolean noteq=false;
            if(stack.size()==0){
                stack.add(symbol);
            }else{
                for (int j = stack.size()-1; j >= 0; j--) {

                    if (stack.get(j).getIdent().getStr().equals(symbol.token.getStr()) ){//&& stack.get(j).getLevel() == 1) {
                        Error.addError(new Error(symbol.getIdent().getLineNum(),'b'));
                        noteq=false;
                        break;
                    }
                    else{
                        noteq=true;
                    }
                }}if(noteq) stack.add(symbol);
        }*/
    }
    public void visitConstInitVal(ConstInitValNode ast){
        // ConstInitVal -> ConstExp| '{' [ ConstInitVal { ',' ConstInitVal } ] '}'
        //ArrayList<AstNode> a=ast.getChild();
        VarSymbol k= ast.getVarInfo();
        if(k.getDim()==0){
            visitConstExp(ast.getConstExpNode());
            ast.getVarInfo().setValue(ast.getConstExpNode().getValue());
            ast.setValue(ast.getConstExpNode().getValue());
        }
        else if(k.getDim()==1){
            int j=0;
            String[] d1v = k.getD1Value();//new String[k.getD1()];
            for(int i=0;i<ast.getConstInitValNodes().size();i++){
                VarSymbol k1=new VarSymbol();//ast.getConstInitValNodes().get(i).getVarInfo();
                k1.setDim(0);
                ast.getConstInitValNodes().get(i).setVarInfo(k1);
                visitConstInitVal(ast.getConstInitValNodes().get(i));
                k1.setValue(ast.getConstInitValNodes().get(i).getValue());
                //ast.setValue(ast.getConstInitValNodes().get(i).getValue());
                d1v[j]=ast.getConstInitValNodes().get(i).getValue();
                j++;
            }
            if(j<k.getD1()){
                for(;j<k.getD1();j++){
                    if(this.level!=0){d1v[j]="NuLL";}
                    else{d1v[j]="0";}
                }
            }
            k.setD1Value(d1v);
        }
        else if(k.getDim()==2){
            int m=0;
            String[][] d2v = k.getD2Value();//new String[k.getD1()][k.getD2()];
            //if(ast.getConstInitValNodes().getChild().get(0).getIdent().equals("{")){
                for(int i=0;i<ast.getConstInitValNodes().size();i++){
                    VarSymbol k1=new VarSymbol();//ast.getConstInitValNodes().get(i).getVarInfo();
                    k1.setDim(1);
                    k1.setD1(k.getD2());
                    ast.getConstInitValNodes().get(i).setVarInfo(k1);
                    visitConstInitVal(ast.getConstInitValNodes().get(i));
                    k1.setValue(ast.getConstInitValNodes().get(i).getValue());
                    String[] d=ast.getConstInitValNodes().get(i).getVarInfo().getD1Value();
                    if (ast.getConstInitValNodes().get(i).getVarInfo().getD1() >= 0)
                        System.arraycopy(d, 0, d2v[m], 0, ast.getConstInitValNodes().get(i).getVarInfo().getD1());
                    //for d2v[m][n]
                    m++;
                }
                k.setD2Value(d2v);
            /*}
            else{
                int j=0;
                for(int i=1;i<a.size()-1;i+=2){
                    KeyValue k1=a.get(i).getKey();
                    k1.setDim(0);
                    a.get(i).setKey(k1);
                    generate(a.get(i));
                    d2v[0][j]=a.get(i).getValue();
                    j++;
                }
                if(j<k.getD1()){
                    for(;j<k.getD1();j++){
                        if(this.level!=0){d2v[0][j]="NuLL";}
                        else{d2v[0][j]="0";}
                    }
                }
            }
            if(m<k.getD1()){
                for(;m<k.getD1();m++){
                    for(int n=0;n<k.getD2();n++){
                        if(this.level!=0){d2v[m][n]="NuLL";}
                        else{d2v[m][n]="0";}
                    }
                }
            }*/
        }
    }

    private void visitVarDecl(VarDeclNode varDeclNode) {
        // VarDecl -> BType VarDef { ',' VarDef } ';'
        //tmpType = "i32";
        for (VarDefNode varDefNode : varDeclNode.getVarDefNodes()) {
            visitVarDef(varDefNode);
        }
    }
    public void visitVarDef(VarDefNode a){
        // VarDef -> Ident { '[' ConstExp ']' } [ '=' InitVal ]
        //ArrayList<AstNode> a=ast.getChild();
        //AstNode ident = a.get(0);
        Symbol symbol=null;
        VarSymbol k;//=null;

        if(a.getConstExpNodes().size()==0){
            symbol=new Symbol(a.getIdent(),0);
            k=symbol.getVarInfo();
            if(level!=0){
                pw.println("    %r"+this.regId+" = alloca i32");pw.flush();
                k.setValue("%r"+this.regId);
                k.setRegId("%r"+this.regId);
                this.regId++;
            }
           // symbol=new Symbol(ident.getIdent(), 0);
            //k=symbol.getVarInfo();
            k.setDim(0);

            if(a.getInitValNode()!=null){
                a.getInitValNode().setVarInfo(k);
                visitInitVal(a.getInitValNode());
                k.setValue(a.getInitValNode().getVarInfo().getValue());
                if(level!=0){
                    pw.println("    store i32 "+a.getInitValNode().getValue()+", i32* "+k.getRegId());pw.flush();
                }
            } else {
                k.setValue("0");

            }k.setKind(0);

            symbol.setVarInfo(k);
            if (SymbolManager.getInstance().addVarDef(symbol,k)==-1){
                ErrorHandler.addError(new ErrorHandler(symbol.getIdent().getLineNum(),'b'));

            }else{
            if(level==0){
                pw.println("@"+a.getIdent().getStr()+" = dso_local global i32 "+k.getValue());pw.flush();
            }}
        }
        else if(a.getConstExpNodes().size()==1){
            int l=this.level;
            this.level=0;
            visitConstExp(a.getConstExpNodes().get(0));
            this.level=l;
            symbol=new Symbol(a.getIdent(),1);
            k=symbol.getVarInfo();
            if(level!=0){
                pw.println("    %r"+this.regId+" = alloca ["+a.getConstExpNodes().get(0).getValue()+" x i32]");
                k.setValue("%r"+this.regId);
                k.setRegId("%r"+this.regId);
                this.regId++;
            }
            if (a.getVarInfo().getDim()!=-1) k.setDim(1);
            else k.setDim(-1);
            k.setD1(Integer.parseInt(a.getConstExpNodes().get(0).getValue()));
            String []d1v = k.getD1Value();
            if(a.getInitValNode()!=null){
                a.getInitValNode().setVarInfo(k);
                visitInitVal(a.getInitValNode());
            }
            else {
                for(int i=0;i<k.getD1();i++){
                    if(level==0){d1v[i]="0";}
                    else{d1v[i]="NuLL";}
                }
                k.setD1Value(d1v);
            }
            k.setKind(0);
            symbol.setVarInfo(k);
            if (SymbolManager.getInstance().addVarDef(symbol,k)==-1){
                ErrorHandler.addError(new ErrorHandler(symbol.getIdent().getLineNum(),'b'));

            }else{
            if(level==0){
                if(a.getInitValNode()!=null){
                    pw.print("@"+a.getIdent().getStr()+" = dso_local global ["+k.getD1()+" x i32] [");pw.flush();
                    d1v=k.getD1Value();
                    for(int i=0;i<k.getD1()-1;i++){
                        pw.print("i32 "+d1v[i]+", ");//pw.flush();
                    }
                    pw.println("i32 "+k.getD1Value()[k.getD1()-1]+"]");pw.flush();
                }
                else{
                    pw.println("@"+a.getIdent().getStr()+" = dso_local global ["+k.getD1()+" x i32] zeroinitializer");pw.flush();
                }
            }
            else{
                d1v = k.getD1Value();
                for(int i=0;i<k.getD1();i++){
                    if(!(d1v[i].equals("NuLL"))){
                        pw.println("    %r"+this.regId+" = getelementptr ["+k.getD1()+" x i32], ["+k.getD1()+" x i32]*"+k.getRegId()+", i32 0, i32 "+i);
                        pw.println("    store i32 "+d1v[i]+", i32* %r"+this.regId);
                        this.regId++;
                    }
                }
            }}
        }
        else if(a.getConstExpNodes().size()==2){
            int l=this.level;
            this.level=0;
            visitConstExp(a.getConstExpNodes().get(0));
            visitConstExp(a.getConstExpNodes().get(1));
            this.level=l;
            symbol=new Symbol(a.getIdent(),2);
            k=symbol.getVarInfo();
            if(level!=0){
                pw.println("    %r"+this.regId+" = alloca ["+a.getConstExpNodes().get(0).getValue()+" x [ "+a.getConstExpNodes().get(1).getValue() +" x i32]]");
                k.setValue("%r"+this.regId);
                k.setRegId("%r"+this.regId);
                this.regId++;
            }
            if (a.getVarInfo().getDim()!=-1) k.setDim(2);
            else k.setDim(-1);
            k.setD1(Integer.parseInt(a.getConstExpNodes().get(0).getValue()));
            k.setD2(Integer.parseInt(a.getConstExpNodes().get(1).getValue()));
            String [][]d2v = k.getD2Value();//new String[k.getD1()][k.getD2()];
            if(a.getInitValNode()!=null){
                a.getInitValNode().setVarInfo(k);
                visitInitVal(a.getInitValNode());
            }
            else {
                for(int i=0;i<k.getD1();i++){
                    for(int j=0;j<k.getD2();j++){
                        if(level==0){
                            d2v[i][j]="0";
                        }
                        else{
                            d2v[i][j]="NuLL";
                        }
                    }
                }
                k.setD2Value(d2v);
            }k.setKind(0);
            symbol.setVarInfo(k);
            if (SymbolManager.getInstance().addVarDef(symbol,k)==-1){
                ErrorHandler.addError(new ErrorHandler(symbol.getIdent().getLineNum(),'b'));

            }
            if(level==0){
                if(a.getInitValNode()!=null){
                    pw.print("@"+a.getIdent().getStr()+" = dso_local global ["+k.getD1()+" x ["+k.getD2()+" x i32]] [[");
                    d2v=k.getD2Value();
                    for(int i=0;i<k.getD1()-1;i++){
                        pw.print(k.getD2()+" x i32] [");
                        for(int j=0;j<k.getD2()-1;j++){
                            pw.print("i32 "+d2v[i][j]+", ");
                        }
                        pw.print("i32 "+k.getD2Value()[i][k.getD2()-1]+"], [");pw.flush();
                    }
                    pw.print(k.getD2()+" x i32] [");pw.flush();
                    for(int j=0;j<k.getD2()-1;j++){
                        pw.print("i32 "+d2v[k.getD1()-1][j]+", ");
                    }
                    pw.println("i32 "+k.getD2Value()[k.getD1()-1][k.getD2()-1]+"]]");pw.flush();
                }
                else{
                    pw.println("@"+a.getIdent().getStr()+" = dso_local global ["+k.getD1()+" x ["+k.getD2()+" x i32]] zeroinitializer");
                }
            }
            else{
                d2v = k.getD2Value();
                for(int i=0;i<k.getD1();i++){
                    for(int j=0;j<k.getD2();j++){
                        if(!(d2v[i][j].equals("NuLL"))){
                            pw.println("    %r"+this.regId+" = getelementptr ["+k.getD1()+" x ["+k.getD2()+" x i32]], ["+k.getD1()+" x ["+k.getD2()+" x i32]]*"+k.getRegId()+", i32 0, i32 "+i+", i32 "+j);
                            pw.println("    store i32 "+d2v[i][j]+", i32* %r"+this.regId);
                            this.regId++;
                        }
                    }
                }
            }
        }
        //k.setKind(0);
        //symbol.setVarInfo(k);//ident.setVarInfo(k);
        if(symbol!=null){
        if(level==0){
            if(global.get(a.getIdent().getStr())==null){
                global.put(a.getIdent().getStr(),symbol);
            }else {
                //Error.addError(new Error(a.getIdent().getLineNum(),'b'));
            }
        }
        else{
            symbol.setLevel(this.level);
            //boolean noteq=false;
            /*if(stack.size()==0){
                stack.add(symbol);
            }else{
                for (int j = stack.size()-1; j >= 0; j--) {

                    if (stack.get(j).getIdent().getStr().equals(symbol.token.getStr()) ){//&& stack.get(j).getLevel() == 1) {
                        Error.addError(new Error(symbol.getIdent().getLineNum(),'b'));
                        noteq=false;
                        break;
                    }
                    else{
                        noteq=true;
                    }
                }}if(noteq) stack.add(symbol);*/
        }}
    }

    public void visitInitVal(InitValNode ast){
        // InitVal → Exp | '{' [ InitVal { ',' InitVal } ] '}
        //ArrayList<AstNode> a=ast.getChild();
        VarSymbol k= ast.getVarInfo();
        if(k.getDim()==0){
            visitExp(ast.getExpNode());
            ast.getVarInfo().setValue(ast.getExpNode().getValue());
            ast.setValue(ast.getExpNode().getValue());
            ast.setVarInfo(k);
        }
        else if(k.getDim()==1){
            int j=0;
            String[] d1v = k.getD1Value();//new String[k.getD1()];
            for(int i=0;i<ast.getInitValNodes().size();i++){

                VarSymbol k1=new VarSymbol();//ast.getInitValNodes().get(i).getVarInfo();
                k1.setDim(0);
                //k1.setD1(k.getD2());
                ast.getInitValNodes().get(i).setVarInfo(k1);
                visitInitVal(ast.getInitValNodes().get(i));
                d1v[j]=ast.getInitValNodes().get(i).getValue();
                j++;
            }
            if(j<k.getD1()){
                for(;j<k.getD1();j++){
                    if(this.level!=0){d1v[j]="NuLL";}
                    else{d1v[j]="0";}

                }
            }
            k.setD1Value(d1v);
        }
        else if(k.getDim()==2){
            int m=0;
            String[][] d2v = k.getD2Value();//new String[k.getD1()][k.getD2()];
            //if(a.get(1).getChild().get(0).getIdent().equals("{")){
                for(int i=0;i<ast.getInitValNodes().size();i++){

                    VarSymbol k1=new VarSymbol();//ast.getInitValNodes().get(i).getVarInfo();
                    k1.setDim(1);
                    k1.setD1(k.getD2());
                    ast.getInitValNodes().get(i).setVarInfo(k1);
                    visitInitVal(ast.getInitValNodes().get(i));
                    String[] d=ast.getInitValNodes().get(i).getVarInfo().getD1Value();
                    for(int n=0;n<ast.getInitValNodes().get(i).getVarInfo().getD1();n++){
                        d2v[m][n]=d[n];
                    }
                    m++;
                }
                k.setD2Value(d2v);
            /*}
            else{
                int j=0;
                for(int i=1;i<a.size()-1;i+=2){
                    KeyValue k1=a.get(i).getKey();
                    k1.setDim(0);
                    a.get(i).setKey(k1);
                    generate(a.get(i));
                    d2v[0][j]=a.get(i).getValue();
                    j++;
                }
                if(j<k.getD1()){
                    for(;j<k.getD1();j++){
                        if(this.level!=0){d2v[0][j]="NuLL";}
                        else{d2v[0][j]="0";}
                    }
                }
            }*/
            if(m<k.getD1()){
                for(;m<k.getD1();m++){
                    for(int n=0;n<k.getD2();n++){
                        if(this.level!=0){d2v[m][n]="NuLL";}
                        else{d2v[m][n]="0";}
                    }
                }
            }
        }
    }
    public void visitFuncDef(FuncDefNode ast){
        // FuncDef -> FuncType Ident '(' [FuncFParams] ')' Block
        //ArrayList<AstNode> a=ast.getChild();
        String Type = ast.getFuncType();
        Symbol symbol=new Symbol(ast.getIdent(),-1);
        FuncSymbol sym=symbol.getFuncInfo();
        if(Type.equals("int")){Type="i32";ret=true;}
        else if(Type.equals("void")){Type="void";ret=false;}//hasret=true;
        sym.setReturnType(Type);

        pw.print("define dso_local "+Type+" @"+ast.getIdent().getStr());pw.flush();

        //if(a.get(2).getIdent().equals("(")){
            pw.print("(");pw.flush();
        boolean hasret=false;
            if(ast.getFuncFParamsNode()!=null){
                this.regId=0;
                SymbolManager.getInstance().addTable();
                int num=visitFuncFParams(ast.getFuncFParamsNode());
                sym.setParamnum(num);sym.setParamtype(paramtype);
                symbol.setFuncInfo(sym);
                if(global.get(ast.getIdent().getStr())==null){
                    global.put(ast.getIdent().getStr(),symbol);
                }
                if (SymbolManager.getInstance().addFuncDef(symbol,sym)==-1){
                    ErrorHandler.addError(new ErrorHandler(ast.getIdent().getLineNum(),'b'));

                }
                pw.print(") {\n");pw.flush();
                blockret=true;
                hasret=visitBlock(ast.getBlockNode());
                paramtype=new ArrayList<>();
                //if(ret&&)  {//&&blockret
                //    ErrorHandler.addError(new ErrorHandler(ast.getBlockNode().getRight().getLineNum(),'g'));
                    //flag=true;
                //}hasret=false;

            }else{
                pw.print(") {\n");pw.flush();this.regId=1;
                symbol.setFuncInfo(sym);
                if(global.get(ast.getIdent().getStr())==null){
                    global.put(ast.getIdent().getStr(),symbol);
                }else {
                    //Error.addError(new Error(ast.getIdent().getLineNum(),'b'));
                }
                if (SymbolManager.getInstance().addFuncDef(symbol,sym)==-1){
                    ErrorHandler.addError(new ErrorHandler(ast.getIdent().getLineNum(),'b'));
                }
                SymbolManager.getInstance().addTable();
                blockret=true;
                hasret=visitBlock(ast.getBlockNode());
                //hasret=false;
            }
        if(ret&&!hasret)  {//&&blockret
            ErrorHandler.addError(new ErrorHandler(ast.getBlockNode().getRight().getLineNum(),'g'));
            //flag=true;
        }
       // }
        if(Type.equals("void")){
            this.nowtag+=1;
            pw.println("    ret void");pw.flush();
            this.nowtag-=1;
        }
        pw.println("}");pw.flush();

    }
    public void visitMainFuncDef(MainFuncDefNode ast){
        // MainFuncDef -> 'int' 'main' '(' ')' Block
        pw.println("\ndefine dso_local i32 @main() {");pw.flush();
        this.regId=1;ret=true;//hasret=false;
        SymbolManager.getInstance().addTable();
        blockret=true;//hasret=true;
        boolean hasret=visitBlock(ast.getBlockNode());//Block
        pw.println("}");pw.flush();

        if(ret&&!hasret)  {//&&blockret
                  ErrorHandler.addError(new ErrorHandler(ast.getBlockNode().getRight().getLineNum(),'g'));
            //flag=true;
        }//hasret=false;
    }
    public int visitFuncFParams(FuncFParamsNode ast) {
        // FuncFParams -> FuncFParam { ',' FuncFParam }
        //ArrayList<AstNode> a=ast.getChild();
        //this.regId=0;
        paramtype.add(visitFuncFParam(ast.getFuncFParamNodes().get(0)));
        int i;
        for (i = 1; i < ast.getFuncFParamNodes().size(); i++) {
            pw.print(", ");
            pw.flush();
            paramtype.add(visitFuncFParam(ast.getFuncFParamNodes().get(i)));
        }
        this.regId++;
        return i;
    }
    public int visitFuncFParam(FuncFParamNode ast) {
        // BType Ident [ '[' ']' { '[' ConstExp ']' }]
        //ArrayList<AstNode> a=ast.getChild();
        Symbol symbol = new Symbol(null,0);
        VarSymbol k=new VarSymbol();

        if (ast.getNot0wei()==0) {
            symbol=new Symbol(ast.getIdent(), 0);
            symbol.setLevel(1);
            //k=symbol.getVarInfo();

            pw.print("i32 %r" + this.regId);pw.flush();
            k.setValue("%r" + this.regId);
            k.setAddrType("i32");
            this.regId++;
            //symbol = new Symbol(ast.getIdent(), 0);
            //symbol.setVarInfo(ast.getVarInfo());
            symbol.setVarInfo(k);


        } else if (ast.getNot0wei()==1) {
            symbol=new Symbol(ast.getIdent(), 1);
            symbol.setLevel(1);
            pw.print("i32* %r" + this.regId);pw.flush();
            k=symbol.getVarInfo();
            k.setValue("%r" + this.regId);
            k.setAddrType("i32*");
            if (k.getDim()!=-1) k.setDim(1);
            else k.setDim(-1);
            k.setD1(0);
            this.regId++;
            symbol.setVarInfo(k);

        } else if (ast.getConstExpNodes() != null) {
            symbol=new Symbol(ast.getIdent(), 2);
            symbol.setLevel(1);
            k=symbol.getVarInfo();
            visitConstExp(ast.getConstExpNodes().get(0));
            pw.print("[" + ast.getConstExpNodes().get(0).getValue() + " x i32] *%r" + this.regId);pw.flush();
            k.setValue("%r" + this.regId);
            if (symbol.getVarInfo().getDim()!=-1) k.setDim(2);
            else k.setDim(-1);
            k.setD1(0);
            k.setD2(Integer.parseInt(ast.getConstExpNodes().get(0).getValue()));
            k.setAddrType("[" + ast.getConstExpNodes().get(0).getValue() + " x i32]*");
            this.regId++;
            symbol.setVarInfo(k);

        }if (SymbolManager.getInstance().addVarDef(symbol,k)==-1){
            ErrorHandler.addError(new ErrorHandler(ast.getIdent().getLineNum(),'b'));
        }
        ast.setVarInfo(k);
        /*boolean noteq=false;
        if(stack.size()==0){
            stack.add(symbol);
        }else{
        for (int j = stack.size()-1; j >= 0; j--) {

            if (stack.get(j).getIdent().getStr().equals(symbol.token.getStr()) ){//&& stack.get(j).getLevel() == 1) {
                Error.addError(new Error(symbol.getIdent().getLineNum(),'b'));
                noteq=false;
                break;
            }
            else{
                noteq=true;
            }
        }}if(noteq) stack.add(symbol);*/
        return symbol.type;
    }
    boolean flag=true;
    public boolean visitBlock(BlockNode ast){
        // Block -> '{' { BlockItem } '}'//ConstDecl | VarDecl  | Stmt
        //ArrayList<AstNode> a=ast.getChild();
        //if(ast.getBlockItemNodes().size()==0) {
           // if (ast.getLeft().equals("{")) {
        boolean hasret = false;
                if (level == 0) {
                    nowtag += 1;
                }
                level += 1;
        //SymbolManager.getInstance().addTable();
                if (level == 1) {
                    List<Symbol> table=SymbolManager.getInstance().getCurSymbolTable().getTable().values().stream().toList();//Symbol symbol=SymbolManager.getInstance().findSymbol(,false);
                    for (int j =table.size() - 1; j >= 0; j--) {
                        if (table.get(j).getVarInfo().getRegId().equals("") && table.get(j).getLevel() == 1) {
                            pw.println("    %r" + this.regId + " = alloca " + table.get(j).getVarInfo().getAddrType());
                            pw.flush();
                            table.get(j).getVarInfo().setRegId("%r" + this.regId);
                            //String space="* ";
                            //if(stack.get(j).getVarInfo().getAddrType()=="i32*") space=" * ";
                            pw.println("    store " + table.get(j).getVarInfo().getAddrType() + " " + table.get(j).getVarInfo().getValue() +
                                    ", " + table.get(j).getVarInfo().getAddrType() + " * " + table.get(j).getVarInfo().getRegId());
                            pw.flush();
                            this.regId++;
                        }
                    }
                }

        for(int i=0;i<ast.getBlockItemNodes().size();i++){
            ast.getBlockItemNodes().get(i).setContinueId(ast.getContinueId());
            ast.getBlockItemNodes().get(i).setBreakId(ast.getBreakId());
            if (i==ast.getBlockItemNodes().size()-1) {
                if (ast.getBlockItemNodes().get(i).getStmtNode()!=null&&ast.getBlockItemNodes().get(i).getStmtNode().getType()!= StmtNode.StmtType.Return){
                    if(blockret){

                        if (!lastret) {lastret=true;
                            flag=false;}
                    }else {

                        flag=true;
                    }
                }else flag=true;
                if(ret&&!flag&&blockret)  {//&&blockret
                   // ErrorHandler.addError(new ErrorHandler(ast.getRight().getLineNum(),'g'));
                    flag=true;
                }
            }
            hasret=visitBlockItem(ast.getBlockItemNodes().get(i));
            //if(lastret) flag=false;

        } //   } else if (ast.getRight().equals("}")) {
        if(ast.getBlockItemNodes().size()==0){
            if(ret&&blockret)  {//&&blockret
              //  ErrorHandler.addError(new ErrorHandler(ast.getRight().getLineNum(),'g'));
            }
        }
        lastret=false;
        //blockret=true;
        SymbolManager.getInstance().traceBackFather();
        /*for (int j = stack.size() - 1; j >= 0; j--) {
                    if (stack.get(j).getLevel() == this.level) {
                        stack.remove(j);
                    }
                }*/
                level -= 1;
                if (level == 0) {
                    nowtag -= 1;
                }
            //}else{

        //}
        return hasret;
    }

    private boolean visitBlockItem(BlockItemNode blockItemNode) {
        // BlockItem -> Decl | Stmt
        boolean hasret = false;
        if (blockItemNode.getDeclNode() != null) {
            if(blockItemNode.getDeclNode().constORvar()==0){
                visitConstDecl(blockItemNode.getDeclNode().getConstDecl());
            }else {
                visitVarDecl(blockItemNode.getDeclNode().getVarDecl());
            }
        } else {
            blockItemNode.getStmtNode().setContinueId(blockItemNode.getContinueId());
            blockItemNode.getStmtNode().setBreakId(blockItemNode.getBreakId());
            hasret=visitStmt(blockItemNode.getStmtNode());
        }
        return hasret;
    }
    public boolean visitStmt(StmtNode ast) {
        // Stmt -> LVal '=' Exp ';'
        //	| [Exp] ';'
        //	| Block
        //	| 'if' '(' Cond ')' Stmt [ 'else' Stmt ]
        //	| 'for' '(' [ForStmt] ';' [Cond] ';' [ForStmt] ')' Stmt
        //	| 'break' ';' | 'continue' ';'
        //	| 'return' [Exp] ';'
        //	| LVal '=' 'getint' '(' ')' ';'
        //	| 'printf' '(' FormatString { ',' Exp } ')' ';'
        int end=0;boolean hasret=false;
        switch (ast.getType()) {
            case Block:
                ast.getBlockNode().setContinueId(ast.getContinueId());
                ast.getBlockNode().setBreakId(ast.getBreakId());
                ast.setBreakId(ast.getBreakId());
                ast.setContinueId(ast.getContinueId());
                blockret=false;
                SymbolManager.getInstance().addTable();
                visitBlock(ast.getBlockNode());
                break;
            case Return:
                hasret=true;//if (lastret)
                if (ast.getExpNode() == null) {
                    pw.println("    ret void");pw.flush();
                } else {
                    if(!ret) {
                       ErrorHandler.addError(new ErrorHandler(ast.getRet().getLineNum(),'f'));
                    }
                    visitExp(ast.getExpNode());//Exp
                    pw.println("    ret i32 " + ast.getExpNode().getValue());pw.flush();
                }
                break;
            case LValExp:
                if(visitLVal(ast.getLValNode())==1){//LVal
                    ErrorHandler.addError(new ErrorHandler(ast.getLValNode().getIdent().getLineNum(),'h'));
                }
                visitExp(ast.getExpNode());//Exp
                pw.println("    store i32 " + ast.getExpNode().getValue() + ", i32* " + ast.getLValNode().getRegId());
                pw.flush();
                break;
            case LValGetint:
                if(visitLVal(ast.getLValNode())==1){//LVal
                ErrorHandler.addError(new ErrorHandler(ast.getLValNode().getIdent().getLineNum(),'h'));
                }
                pw.println("    %r" + (this.regId) + " = call i32 @getint()");
                pw.flush();
                pw.println("    store i32 " + "%r" + (this.regId) + ", i32* " + ast.getLValNode().getRegId());
                pw.flush();
                this.regId++;
               // pw.println("    %"+this.regId+" = load i32, i32* "+ast.getLValNode().getRegId());pw.flush();
                break;
            case Exp:
                if (ast.getExpNode()!=null) visitExp(ast.getExpNode());
                break;

            case Printf:
                int parNum = 0;
                String s = ast.getFormatString();
                for (int i = 1; i < s.length() - 1; i++) {
                    if (s.charAt(i) == '%' && s.charAt(i + 1) == 'd') {
                        i++;
                        if (!Parser.noteq) visitExp(ast.getExpNodes().get(parNum));
                        parNum++;
                    }
                }
                parNum = 0;
                for (int i = 1; i < s.length() - 1; i++) {
                    if (s.charAt(i) == '%' && s.charAt(i + 1) == 'd') {
                        i++;
                        if (!Parser.noteq) pw.println("    call void @putint(i32 " + ast.getExpNodes().get(parNum).getValue() + ")");
                        pw.flush();
                        parNum++;
                    } else if (s.charAt(i) == '\\' && s.charAt(i + 1) == 'n') {
                        i++;
                        pw.println("    call void @putch(i32 10)");
                        pw.flush();

                    } else {
                        pw.println("    call void @putch(i32 " + (int) s.charAt(i) + ")");
                        pw.flush();
                    }
                }
                break;
            case If:
                pw.println("    br label %r" + this.regId);
                pw.flush();
                pw.println("\nr" + this.regId + ":");
                pw.flush();
                ast.getCondNode().setYesId(this.regId + 1);
                int YesId = this.regId + 1;
                int NoId = 0;
                int StmtId;
                if (ast.getStmtNodes().size() > 1) {
                    ast.getCondNode().setNoId(this.regId + 2);
                    ast.getCondNode().setStmtId(this.regId + 3);
                    ast.getStmtNodes().get(0).setStmtId(this.regId + 3);
                    ast.getStmtNodes().get(0).setContinueId(ast.getContinueId());
                    ast.getStmtNodes().get(0).setBreakId(ast.getBreakId());
                    ast.getStmtNodes().get(1).setStmtId(this.regId + 3);
                    ast.getStmtNodes().get(1).setContinueId(ast.getContinueId());
                    ast.getStmtNodes().get(1).setBreakId(ast.getBreakId());
                    NoId = this.regId + 2;
                    StmtId = this.regId + 3;
                    this.regId += 4;
                } else {
                    ast.getCondNode().setNoId(this.regId + 2);
                    ast.getCondNode().setStmtId(this.regId + 2);
                    ast.getStmtNodes().get(0).setStmtId(this.regId + 2);
                    ast.getStmtNodes().get(0).setContinueId(ast.getContinueId());
                    ast.getStmtNodes().get(0).setBreakId(ast.getBreakId());
                    StmtId = this.regId + 2;
                    this.regId += 3;
                }
                visitCond(ast.getCondNode());
                pw.println("\nr" + YesId + ":");
                pw.flush();
                //StmtId = ast.getCondNode().getLOrExpNode().getStmtId();
                ast.getStmtNodes().get(0).setStmtId(StmtId);
                visitStmt(ast.getStmtNodes().get(0));
                if (ast.getStmtNodes().size() > 1) {
                    pw.println("\nr" + NoId + ":");
                    pw.flush();
                    visitStmt(ast.getStmtNodes().get(1));
                }
                pw.println("\nr" + StmtId + ":");
                pw.flush();
        /*}
        else if(a.get(0).getIdent().equals("while")){
            pw.println("    br label %"+this.regId+"\n");
            pw.println("\nv"+this.regId+":\n");
            int YesId = this.regId+1;
            int StmtId=this.regId+2;
            a.get(2).setYesId(this.regId+1);
            a.get(2).setNoId(this.regId+2);
            a.get(2).setStmtId(this.regId+2);
            a.get(4).setStmtId(this.regId);
            a.get(4).setBreakId(this.regId+2);
            a.get(4).setContinueId(this.regId);
            this.regId+=3;
            generate(a.get(2));
            pw.println("\nv"+YesId+":\n");
            generate(a.get(4));
            pw.println("\nv"+StmtId+":\n");*/
                break;

            case For:
                //	| 'for' '(' [ForStmt] ';' [Cond] ';' [ForStmt] ')' Stmt
                pw.println("    br label %r" + this.regId);
                pw.flush();
                pw.println("\nr" + this.regId + ":");
                pw.flush();

                int CondId = this.regId + 1;
                YesId = this.regId + 2;
                NoId = this.regId + 3;
                StmtId = this.regId + 4;
                this.regId += 5;
                int for2 = 0;int count = 0;
                //int i = 0;
                boolean hasForStmt1 = false;
                boolean hasCond = false;

                int size=4;if(ast.getForStmtNode1()!=null) {
                    size+=1;count=size-1;
            }else count=size-1;
                if(ast.getCondNode()!=null) {
                    size+=1;
                }
                for(int i=0;i<size;i++){
//                if(count>=2){break;}
//                if(a.get(i).getIdent().equals(";")){count+=1;}
                if (i==count-2&&ast.getForStmtNode1() != null) {
                    hasForStmt1 = true;
                    ast.getForStmtNode1().setStmtId(CondId);
                    visitForStmt(ast.getForStmtNode1());
                }

                if (i==count&&ast.getCondNode() != null) {
                    pw.println("\nr" + CondId + ":");
                    pw.flush();
                    hasCond = true;
                    ast.getCondNode().setYesId(YesId);
                    ast.getCondNode().setNoId(NoId);
                    ast.getCondNode().setStmtId(CondId);
                    visitCond(ast.getCondNode());
                }
                if (i>count-2&&i<size-1&&!hasForStmt1) {
                        pw.println("    br label %r" + CondId);
                        pw.flush();
                }
                if (i>=count&&!hasCond) {
                    pw.println("\nr" + CondId + ":");
                    pw.println("    br label %r" + YesId);
                    pw.flush();
                }
                }
                //for(;i<a.size();i++){
                //if(a.get(i).getIdent().equals("<forStmt>")){for2=i;}
                if (ast.getStmtNode() != null) {
                    pw.println("\nr" + YesId + ":");
                    pw.flush();
                    ast.getStmtNode().setStmtId(StmtId);
                    ast.getStmtNode().setBreakId(NoId);
                    ast.getStmtNode().setContinueId(StmtId);
                    ast.setContinueId(StmtId);
                    ast.setBreakId(NoId);
                    infor=true;
                    visitStmt(ast.getStmtNode());
                }

                pw.println("\nr" + StmtId + ":");
                pw.flush();
                if ( ast.getForStmtNode2() == null) {//!hasForStmt1 &&
                    pw.println("    br label %r" + CondId);
                    pw.flush();
                } else if (ast.getForStmtNode2() != null) {
                    ast.getForStmtNode2().setStmtId(CondId);
                    visitForStmt(ast.getForStmtNode2());
                }
                end = NoId;
                pw.println("\nr" + NoId + ":");
                pw.flush();
                ast.setStmtId(ast.getBreakId());
                infor=false;
                break;

            case Continue:
                ast.setStmtId(ast.getContinueId());
                if (!infor){
                    ErrorHandler.addError(new ErrorHandler(ast.getIdent().getLineNum(),'m'));
                }
                break;

            case Break:
                ast.setStmtId(ast.getBreakId());
                if (!infor){
                    ErrorHandler.addError(new ErrorHandler(ast.getIdent().getLineNum(),'m'));
                }
                break;
            default:
                break;//throw new RuntimeException("Unknown StmtNode type: " + ast.getType());
        }
        if (ast.getStmtId() != 0 && end != ast.getStmtId()) {
            pw.println("    br label %r" + ast.getStmtId());pw.flush();
        }
        return hasret;
    }
    public void visitForStmt (ForStmtNode ast){
        // ForStmt → LVal '=' Exp
        //ArrayList<AstNode> a=ast.getChild();
        visitLVal(ast.getLvalNode());//LVal
        visitExp(ast.getExpNode());//Exp
        pw.println("    store i32 "+ast.getExpNode().getValue()+", i32* "+ast.getLvalNode().getRegId());pw.flush();
        pw.println("    br label %r"+ast.getStmtId());
    }

    public void visitExp(ExpNode ast){
        // Exp -> AddExp
        //ArrayList<AstNode> a=ast.getChild();
        visitAddExp(ast.getAddExpNode());//AddExp
        ast.setValue(ast.getAddExpNode().getValue());
        ast.setRegId(ast.getAddExpNode().getRegId());
        ast.setVarInfo(ast.getAddExpNode().getVarInfo());
        ast.setType(ast.getAddExpNode().getType());
    }


    public void visitCond(CondNode ast){
        // Cond -> LOrExp
        ast.getLOrExpNode().setNoId(ast.getNoId());
        ast.getLOrExpNode().setYesId(ast.getYesId());
        ast.getLOrExpNode().setStmtId(ast.getStmtId());
        visitLOrExp(ast.getLOrExpNode());//LOrExp
        ast.setStmtId(ast.getLOrExpNode().getStmtId());
        ast.setValue("%r"+this.regId);
        this.regId++;
    }
    public int visitLVal(LValNode ast){
        // LVal -> Ident {'[' Exp ']'}
        //ArrayList<AstNode> a=ast.getChild();
        Token ident = ast.getIdent();
        VarSymbol k = new VarSymbol();//ast.getVarInfo();

        int check=0;boolean isdef = false;
        //for(int i=stack.size()-1;i>=0;i--){
            //symbol.setVarInfo(stack.get(i).getVarInfo());

        //if (check==0){
        Symbol symbol = SymbolManager.getInstance().findSymbol(ast.getIdent().getStr(), true);///????
            //if(stack.get(i).token.getStr().equals(ident.getStr())){
                if (symbol!=null){
                    // check=1;// isdef=true;
                    if (symbol.type==-1){
                        ErrorHandler.addError(new ErrorHandler(ident.getLineNum(),'c'));
                        return -1;
                    }else{
                        k=symbol.getVarInfo();
                    }
                }
                else {
                    //symbol = SymbolManager.getInstance().findSymbol(ast.getIdent().getStr(), true);
                    //if (symbol!=null)k=symbol.getVarInfo();
                    ErrorHandler.addError(new ErrorHandler(ident.getLineNum(),'c'));
                    return -1;
                }//if(k.getKind()==1)

        if (global.get(ast.getIdent().getStr())==null||global.get(ast.getIdent().getStr()).getIdent()!=symbol.getIdent()) {
            if (ast.getExpNodes().size() == 0) {

                if (k.getDim() == 0) {//int a;
                    if (!k.gettype()) ast.setType(0);
                    else ast.setType(-1);
                    //pw.println("    %"+this.regId+" = load i32, i32* "+stack.get(i).getVarInfo().getRegId());pw.flush();
                    //if (!ast.changevalue()){
                    pw.println("    %r" + this.regId + " = load i32, i32* " + k.getRegId());
                    pw.flush();
                    //}
                    k.setAddrType("i32");
                    ast.setValue("%r" + this.regId);
                    ast.setRegId(k.getRegId());
                    this.regId++;

                } else if (k.getDim() == 1) {
                    if (!k.gettype())ast.setType(1);
                    else ast.setType(-1);
                    if (k.getD1() != 0) {//int a[2]{func(a)}
                        pw.println("    %r" + this.regId + " = getelementptr [" + k.getD1() + " x i32], [" + k.getD1() + " x i32]*" +
                                k.getRegId() + ", i32 0, i32 0");
                        pw.flush();
                        k.setAddrType("i32*");
                        ast.setValue("%r" + (this.regId));
                        ast.setRegId("%r" + (this.regId));
                        this.regId += 2;////?
                    } else {//func(int a[]){func(a)}
                        k.setAddrType("i32*");
                        pw.println("    %r" + this.regId + " = load " + k.getAddrType() + ", " + k.getAddrType() + " * " +
                                k.getRegId());
                        pw.flush();
                        ast.setValue("%r" + (this.regId));
                        ast.setRegId("%r" + (this.regId));
                        this.regId += 1;
                    }

                } else if (k.getDim() == 2) {
                    if (!k.gettype())ast.setType(2);
                    else ast.setType(-1);
                    if (k.getD1() != 0) {//int a[2][3]{func(a)}
                        pw.println("    %r" + this.regId + " = getelementptr [" + k.getD1() + " x [" + k.getD2() + " x i32]], [" +
                                k.getD1() + " x [" + k.getD2() + " x i32]]*" + k.getRegId() + ", i32 0, i32 0");
                        pw.flush();
                        k.setAddrType("[" + k.getD2() + " x i32]*");
                        ast.setValue("%r" + (this.regId));
                        ast.setRegId("%r" + (this.regId));
                        this.regId++;
                    } else {//func(int a[][3]){func(a)}
                        pw.println("    %r" + this.regId + " = load [" + k.getD2() + " x i32] *, [" + k.getD2() + " x i32]* * " +
                                k.getRegId());
                        pw.flush();
                        k.setAddrType("[" + k.getD2() + " x i32]*");
                        ast.setValue("%r" + (this.regId));
                        ast.setRegId(k.getRegId());
                        this.regId++;
                    }

                }check=1;
            } else if (ast.getExpNodes().size() == 1) {

                if (k.getDim() == 1) {
                    if (!k.gettype())ast.setType(0);
                    else ast.setType(-1);
                    if (k.getD1() != 0) {//int a[2]{func(a[1])}??????????
                        visitExp(ast.getExpNodes().get(0));
                        pw.println("    %r" + this.regId + " = getelementptr [" + k.getD1() + " x i32], [" + k.getD1() + " x i32]*" +
                                k.getRegId() + ", i32 0, i32 " + ast.getExpNodes().get(0).getValue());
                        pw.println("    %r" + (this.regId + 1) + " = load i32, i32* %r" + this.regId);
                        k.setAddrType("i32");
                        ast.setValue("%r" + (this.regId + 1));
                        ast.setRegId("%r" + (this.regId));
                        this.regId += 2;
                    } else {//func(int b,int a[]){func(a[2],xxx)}
                        visitExp(ast.getExpNodes().get(0));
                        pw.println("    %r" + this.regId + " = load i32*, i32* * " + k.getRegId());
                        pw.println("    %r" + (this.regId + 1) + " = getelementptr i32, i32* %r" + this.regId + ", i32 " + ast.getExpNodes().get(0).getValue());
                        pw.println("    %r" + (this.regId + 2) + " = load i32, i32* %r" + (this.regId + 1));
                        ast.setValue("%r" + (this.regId + 2));
                        ast.setRegId("%r" + (this.regId + 1));
                        k.setAddrType("i32");
                        this.regId += 3;
                    }
                } else if (k.getDim() == 2) {
                    if (!k.gettype())ast.setType(1);
                    else ast.setType(-1);
                    if (k.getD1() != 0) {//int a[2][3]{func(a[1])}
                        visitExp(ast.getExpNodes().get(0));
                        pw.println("    %r" + this.regId + " = mul i32 " + ast.getExpNodes().get(0).getValue() + ", " + k.getD2());
                        this.regId++;
                        pw.println("    %r" + this.regId + " = getelementptr [" + k.getD1() + " x [" + k.getD2() + " x i32]], [" +
                                k.getD1() + " x [" + k.getD2() + " x i32]]*" + k.getRegId() + ", i32 0, i32 0");
                        this.regId++;
                        pw.println("    %r" + this.regId + " = getelementptr [" + k.getD2() + " x i32], [" + k.getD2() + " x i32]* %r" + (this.regId - 1) + ", i32 0, i32 %r" + (this.regId - 2));
                        k.setAddrType("i32*");
                        ast.setValue("%r" + (this.regId));
                        ast.setRegId("%r" + (this.regId));
                        this.regId += 1;
                    } else {//func(int b[],int a[][3]){func(a[2],xxx)}
                        visitExp(ast.getExpNodes().get(0));
                        pw.println("    %r" + this.regId + " = load [" + k.getD2() + " x i32] *, [" + k.getD2() + " x i32]* * " + k.getRegId());
                        this.regId++;
                        pw.println("    %r" + this.regId + " = getelementptr [" + k.getD2() + " x i32], [" + k.getD2() + " x i32]* %r" + (this.regId - 1) + ", i32 " + ast.getExpNodes().get(0).getValue());
                        this.regId++;
                        pw.println("    %r" + this.regId + " = getelementptr [" + k.getD2() + " x i32], [" + k.getD2() + " x i32]* %r" + (this.regId - 1) + ", i32 0, i32 0");
                        k.setAddrType("i32*");
                        ast.setValue("%r" + (this.regId));
                        ast.setRegId("%r" + (this.regId));
                        this.regId++;
                    }
                }check=1;
            } else if (ast.getExpNodes().size() == 2) {
                if (!k.gettype()) ast.setType(0);
                else ast.setType(-1);
                visitExp(ast.getExpNodes().get(0));
                visitExp(ast.getExpNodes().get(1));
                if (k.getD1() != 0) {//int a[2][3]{func(a[1][2])}

                    pw.println("    %r" + this.regId + " = getelementptr [" + k.getD1() + " x [" + k.getD2() + " x i32]], [" +
                            k.getD1() + " x [" + k.getD2() + " x i32]]*" + k.getRegId() + ", i32 0, i32 " + ast.getExpNodes().get(0).getValue() + ", i32 " + ast.getExpNodes().get(1).getValue());
                    pw.println("    %r" + (this.regId + 1) + " = load i32, i32* %r" + this.regId);
                    k.setAddrType("i32");
                    ast.setValue("%r" + (this.regId + 1));
                    ast.setRegId("%r" + (this.regId));
                    this.regId += 2;
                } else {//func(int b,int a[][3]){func(a[2][2],xxx)}
                    pw.println("    %r" + this.regId + " = load [" + k.getD2() + " x i32] *, [" + k.getD2() + " x i32]* * " + k.getRegId());
                    this.regId++;
                    pw.println("    %r" + this.regId + " = getelementptr [" + k.getD2() + " x i32], [" + k.getD2() + " x i32]* %r" + (this.regId - 1) + ", i32 " + ast.getExpNodes().get(0).getValue());
                    this.regId++;
                    pw.println("    %r" + this.regId + " = getelementptr [" + k.getD2() + " x i32], [" + k.getD2() + " x i32]* %r" + (this.regId - 1) + ", i32 0, i32 " + ast.getExpNodes().get(1).getValue());
                    pw.println("    %r" + (this.regId + 1) + " = load i32, i32 *%r" + (this.regId));
                    k.setAddrType("i32");
                    ast.setValue("%r" + (this.regId + 1));
                    ast.setRegId("%r" + (this.regId));
                    this.regId += 2;
                }check=1;
            }
            //isdef=true;
            //     break;
            //}//else isdef=false;
        }else{
            if (global.get(ident.getStr())!=null){
                //isdef=true;
                k=global.get(ident.getStr()).getVarInfo();

            if(level>0){
                if (ast.getExpNodes().size() == 0) {

                    if (k.getDim() == 0) {
                        if (!k.gettype())ast.setType(0);
                        else ast.setType(-1);
                        if (!ast.changevalue() && level != 0) {    //if (>0){
                            pw.println("    %r" + this.regId + " = load i32, i32* @" + ident.getStr());
                            pw.flush();
                            //}
                            k.setAddrType("i32");
                            ast.setValue("%r" + this.regId);
                            ast.setRegId("@" + ident.getStr());//%reg<-@a,b=a
                            this.regId++;

                        } else {//////????????
                            ast.setValue(global.get(ident.getStr()).getVarInfo().getValue());
                            ast.setRegId("@" + global.get(ident.getStr()).getIdent().getStr());
                            //@a<-value,a=1200
                              if(ast.getExpNodes().size()==0){
                                ast.setValue(global.get(ident.getStr()).getVarInfo().getValue());
                                ast.setRegId("@" +global.get(ident.getStr()).getIdent().getStr());
                                //pw.println("    %r" + this.regId + " = load i32, i32* @" + ident.getStr());pw.flush();
                            }
                        }
                    }
                    else if(k.getDim()==1){
                        if (!k.gettype())ast.setType(1);
                        else ast.setType(-1);
                        pw.println("    %r"+this.regId+" = getelementptr ["+k.getD1()+" x i32], ["+k.getD1()+" x i32]* @"+
                                ident.getStr()+", i32 0, i32 0");
                        k.setAddrType("i32*");
                        ast.setValue("%r"+(this.regId));
                        ast.setRegId("%r"+(this.regId));
                        this.regId+=1;//?????????
                    }
                    else if(k.getDim()==2){
                        if (!k.gettype())ast.setType(2);
                        else ast.setType(-1);
                        pw.println("    %r"+this.regId+" = getelementptr ["+k.getD1()+" x ["+k.getD2()+" x i32]], ["+
                                k.getD1()+" x ["+k.getD2()+" x i32]]* @"+ident.getStr()+", i32 0, i32 0");
                        k.setAddrType("["+k.getD2()+" x i32]*");
                        ast.setValue("%r"+(this.regId));
                        ast.setRegId("%r"+(this.regId));
                        this.regId++;
                    }
                } else if(ast.getExpNodes().size()==1){

                    if(k.getDim()==1){
                        if (!k.gettype())ast.setType(0);
                        else ast.setType(-1);
                        visitExp(ast.getExpNodes().get(0));
                        pw.println("    %r"+this.regId+" = getelementptr ["+k.getD1()+" x i32], ["+k.getD1()+" x i32]* @"+
                                ident.getStr()+", i32 0, i32 "+ast.getExpNodes().get(0).getValue());
                        pw.println("    %r"+(this.regId+1)+" = load i32, i32* %r"+this.regId);
                        k.setAddrType("i32");
                        ast.setValue("%r"+(this.regId+1));
                        ast.setRegId("%r"+(this.regId));
                        this.regId+=2;
                    }
                    else if(k.getDim()==2){
                        if (!k.gettype())ast.setType(1);
                        else ast.setType(-1);
                        visitExp(ast.getExpNodes().get(0));
                        pw.println("    %r"+this.regId+" = mul i32 "+ast.getExpNodes().get(0).getValue()+", "+k.getD2());
                        this.regId++;
                        pw.println("    %r"+this.regId+" = getelementptr ["+k.getD1()+" x ["+k.getD2()+" x i32]], ["+k.getD1()+" x ["+k.getD2()+" x i32]]* @"+ident.getStr()+", i32 0, i32 0");
                        this.regId++;
                        pw.println("    %r"+this.regId+" = getelementptr ["+k.getD2()+" x i32], ["+k.getD2()+" x i32]* %r"+
                                (this.regId-1)+", i32 0, i32 %r"+(this.regId-2));
                        k.setAddrType("i32*");
                        ast.setValue("%r"+(this.regId));
                        ast.setRegId("%r"+(this.regId));
                        this.regId+=1;
                    }
                } else if(ast.getExpNodes().size()==2){
                    if (!k.gettype())ast.setType(0);
                    else ast.setType(-1);
                    visitExp(ast.getExpNodes().get(0));
                    visitExp(ast.getExpNodes().get(1));
                    pw.println("    %r"+this.regId+" = getelementptr ["+k.getD1()+" x ["+k.getD2()+" x i32]], ["+k.getD1()+
                            " x ["+k.getD2()+" x i32]]* @"+ident.getStr()+", i32 0, i32 "+ast.getExpNodes().get(0).getValue()+", i32 "+ast.getExpNodes().get(1).getValue());
                    pw.println("    %r"+(this.regId+1)+" = load i32, i32* %r"+this.regId);
                    k.setAddrType("i32");
                    ast.setValue("%r"+(this.regId+1));
                    ast.setRegId("%r"+(this.regId));
                    this.regId+=2;
                }ast.setVarInfo(k);
            }else {
                if (ast.getExpNodes().size() == 0) {
                    if (!k.gettype())ast.setType(0);
                    else ast.setType(-1);
                    ast.setValue(global.get(ident.getStr()).getVarInfo().getValue());///????
                } else if (ast.getExpNodes().size() == 1) {
                    if (!k.gettype())ast.setType(0);
                    else ast.setType(-1);
                    visitExp(ast.getExpNodes().get(0));
                    ast.setValue(global.get(ident.getStr()).getVarInfo().getD1Value()[Integer.parseInt(ast.getExpNodes().get(0).getValue())]);
                } else if (ast.getExpNodes().size() == 2) {
                    if (!k.gettype())ast.setType(0);
                    else ast.setType(-1);
                    visitExp(ast.getExpNodes().get(0));
                    visitExp(ast.getExpNodes().get(1));
                    ast.setValue(global.get(ident.getStr()).getVarInfo().getD2Value()[Integer.parseInt(ast.getExpNodes().get(0).getValue())][Integer.parseInt(ast.getExpNodes().get(1).getValue())]);
                }
            }

        }//else {
           // Error.addError(new Error(ident.getLineNum(),'c'));
           // }//}
        //if(!isdef){//??????
         //   Error.addError(new Error(ident.getLineNum(),'c'));
        }
        ast.setVarInfo(k);
        return k.getKind();
    }

    public void visitPrimaryExp(PrimaryExpNode ast){
        // PrimaryExp -> '(' Exp ')' | LVal | Number
        //ArrayList<AstNode> a=ast.getChild();
        if(ast.getNumberNode()!=null){
            //visitNumber(ast.getNumberNode());//Number
            ast.setValue(ast.getNumberNode());
            ast.setType(0);
            VarSymbol k=new VarSymbol();
            k.setAddrType("i32");
            ast.setVarInfo(k);
        }
        else if(ast.getExpNode()!=null){
            visitExp(ast.getExpNode());//Exp
            ast.setValue(ast.getExpNode().getValue());
            ast.setType(ast.getExpNode().getType());
        }
        else if(ast.getLValNode()!=null){
            visitLVal(ast.getLValNode());//LVal
            ast.setValue(ast.getLValNode().getValue());
            ast.setRegId(ast.getLValNode().getRegId());
            ast.setVarInfo(ast.getLValNode().getVarInfo());
            ast.setType(ast.getLValNode().getType());
        }
    }
    /*public void visitNumber(String ast){
        // Number -> IntConst
        //ArrayList<AstNode> a=ast.getChild();
        //ast.setValue(ast);
    }*/
    public void visitUnaryExp(UnaryExpNode ast){
        // UnaryExp -> PrimaryExp | Ident '(' [FuncRParams] ')' | UnaryOp UnaryExp
        //ArrayList<AstNode> a=ast.getChild();
        if(ast.getUnaryExpNode()!=null){
            visitUnaryExp(ast.getUnaryExpNode());//UnaryExp
            switch (ast.getUnaryOpNode()) {
                case "-":
                    if (level > 0) {
                        pw.println("    %r" + this.regId + " = sub i32 0, " + ast.getUnaryExpNode().getValue());
                        pw.flush();
                        ast.setRegId("%r" + this.regId);
                        ast.setValue("%r" + this.regId);
                        this.regId++;
                    } else {
                        ast.setValue(mathCalculate("0", "-", ast.getUnaryExpNode().getValue()));
                    }
                    break;
                case "+":
                    ast.setValue(ast.getUnaryExpNode().getValue());
                    break;
                case "!":
                    pw.println("    %r" + this.regId + " = icmp eq i32 0, " + ast.getUnaryExpNode().getValue());
                    pw.flush();
                    this.regId++;
                    pw.println("    %r" + this.regId + " = zext i1 %r" + (this.regId - 1) + " to i32");
                    pw.flush();
                    ast.setRegId("%r" + this.regId);
                    ast.setValue("%r" + this.regId);
                    this.regId++;
                    break;
            }
            ast.setType(ast.getUnaryExpNode().getType());
            ast.setVarInfo(ast.getUnaryExpNode().getVarInfo());
        }
        else if(ast.getPrimaryExpNode()!=null){
            visitPrimaryExp(ast.getPrimaryExpNode());//PrimaryExp
            ast.setValue(ast.getPrimaryExpNode().getValue());
            ast.setRegId(ast.getPrimaryExpNode().getRegId());
            ast.setVarInfo(ast.getPrimaryExpNode().getVarInfo());
            ast.setType(ast.getPrimaryExpNode().getType());
        }
        else {//if(a.size()>2&&a.get(1).getIdent().equals("(")){
            //String ident=;
            FuncSymbol k = new FuncSymbol();
            Symbol identGlobe = null;
            if (global.get(ast.getIdent().getStr())==null) {
                ErrorHandler.addError(new ErrorHandler(ast.getIdent().getLineNum(), 'c'));
            }else {
//if (global.get(ast.getIdent().getStr()) != null) {
            //    identGlobe = SymbolManager.getInstance().findSymbol(ast.getIdent().getStr(), true);
            //}//if(stack.get(i).token.getStr().equals(ident.getStr())){
                identGlobe = global.get(ast.getIdent().getStr());
                k = identGlobe.getFuncInfo();
                //if(k.getKind()==1)
                //
                if (k.getParamnum() != ast.getFuncRParamsNode().size()) {
                    ErrorHandler.addError(new ErrorHandler(ast.getIdent().getLineNum(), 'd'));
                }

                // ast.getFuncInfo().setReturnType(identGlobe.getFuncInfo().getReturnType());
                if (k.getReturnType().equals("i32")) {
                    if (!k.gettype()) ast.setType(0);
                    else ast.setType(-1);
                    if (ast.getFuncRParamsNode().size() == 0) {
                        pw.println("    %r" + this.regId + " = call i32 @" + ast.getIdent().getStr() + "()");
                        pw.flush();
                        ast.setValue("%r" + this.regId);
                        this.regId++;
                    } else if (k.getParamnum() == ast.getFuncRParamsNode().size()) {
                        StringBuilder value = new StringBuilder();
                        visitExp(ast.getFuncRParamsNode().get(0));
                        if (k.getParamtype().get(0) != ast.getFuncRParamsNode().get(0).getType()) {
                            ErrorHandler.addError(new ErrorHandler(ast.getIdent().getLineNum(), 'e'));
                        } else {
                            value.append(ast.getFuncRParamsNode().get(0).getVarInfo().getAddrType()).append(" ").append(ast.getFuncRParamsNode().get(0).getValue());
                        }

                        ast.setValue("%r" + this.regId);

                        for (int i = 1; i < ast.getFuncRParamsNode().size(); i++) {
                            visitExp(ast.getFuncRParamsNode().get(i));
                            if (k.getParamtype().get(i) != ast.getFuncRParamsNode().get(i).getType()) {
                                ErrorHandler.addError(new ErrorHandler(ast.getIdent().getLineNum(), 'e'));
                            } else {
                                value.append(", ").append(ast.getFuncRParamsNode().get(i).getVarInfo().getAddrType()).append(" ").append(ast.getFuncRParamsNode().get(i).getValue());
                            }

                            ast.setValue("%r" + this.regId);

                        }//
                        pw.println("    %r" + this.regId + " = call i32 @" + ast.getIdent().getStr() + "(" +
                                value + ")");
                        pw.flush();
                        this.regId++;
                    }
                    VarSymbol varSymbol = new VarSymbol();
                    varSymbol.setAddrType("i32");
                    ast.setVarInfo(varSymbol);
                } else if (k.getReturnType().equals("void")) {
                    ast.setType(-1);
                    if (ast.getFuncRParamsNode().size() == 0) {
                        pw.println("    call void @" + ast.getIdent().getStr() + "()");
                        pw.flush();
                    } else if (k.getParamnum() == ast.getFuncRParamsNode().size()) {
                        StringBuilder value = new StringBuilder();
                        visitExp(ast.getFuncRParamsNode().get(0));
                        if (k.getParamtype().get(0) != ast.getFuncRParamsNode().get(0).getType()) {
                            ErrorHandler.addError(new ErrorHandler(ast.getIdent().getLineNum(), 'e'));
                        } else {
                            value.append(ast.getFuncRParamsNode().get(0).getVarInfo().getAddrType()).append(" ").append(ast.getFuncRParamsNode().get(0).getValue());
                        }
                        for (int i = 1; i < ast.getFuncRParamsNode().size(); i++) {
                            visitExp(ast.getFuncRParamsNode().get(i));
                            if (k.getParamtype().get(i) != ast.getFuncRParamsNode().get(i).getType()) {
                                ErrorHandler.addError(new ErrorHandler(ast.getIdent().getLineNum(), 'e'));
                            } else {
                                value.append(", ").append(ast.getFuncRParamsNode().get(i).getVarInfo().getAddrType()).append(" ").append(ast.getFuncRParamsNode().get(i).getValue());

                            }
                        }
                        pw.println("    call void @" + ast.getIdent().getStr() + "(" +
                                value + ")");
                        pw.flush();

                    }//VarSymbol varSymbol=new VarSymbol();varSymbol.setAddrType("void");
                    //ast.setVarInfo(varSymbol);
                }//}else{
                //  Error.addError(new Error(ast.getIdent().getLineNum(),'c'));
            }
        }
    }
   /* public void visitFuncRParams(FuncRParamsNode ast){
        // FuncRParams -> Exp { ',' Exp }
        //ArrayList<AstNode> a=ast.getChild();
        visitExp(ast.getexp);
        StringBuilder Value=new StringBuilder(a.get(0).getKey().getAddrType()+" "+a.get(0).getValue());
        for(int i=2;i<a.size();i+=2){
            generate(a.get(i));
            Value.append(", ").append(a.get(i).getKey().getAddrType()).append(" ").append(a.get(i).getValue());
        }
        ast.setValue(Value.toString());
    }
    public void visitAddMulExp(AddExpNode ast){
        // AddExp -> MulExp | MulExp ('+' | '−') AddExp
        // MulExp → UnaryExp | MulExp ('*' | '/' | '%') UnaryExp
        //ArrayList<AstNode> a=ast.getChild();
        generate(a.get(0));//AddExp/MulExp
        String left=a.get(0).getValue();
        if(a.size()>1){
            for(int i=1;i<a.size();i+=2){
                String op=a.get(i).getIdent();
                generate(a.get(i+1));
                String right=a.get(i+1).getValue();
                String opt=Operator(op);
                if(level>0){
                    pw.println("    %"+this.regId+" = "+opt+" i32 "+left+", "+right+"\n");
                    a.get(i+1).setRegId("%"+this.regId);
                    a.get(i+1).setValue("%"+this.regId);
                    this.regId++;
                }
                else{
                    a.get(i+1).setValue(mathCalculate(left,op,right));
                }
                left=a.get(i+1).getValue();
            }
            ast.setValue(a.get(a.size()-1).getValue());
        }
        else{
            ast.setVarInfo(a.get(0).getKey());
            ast.setValue(left);
            ast.setRegId(a.get(0).getRegId());
        }
    }*/
    //5*3/2:add->mul->mul/unary->mul*unary /unary
    //           mul-> unary*mul->unary*unary/mul
    private void visitMulExp(MulExpNode mulExpNode) {
        // MulExp → UnaryExp | MulExp ('*' | '/' | '%') UnaryExp
        visitUnaryExp(mulExpNode.getUnaryExpNode());
        String left=mulExpNode.getUnaryExpNode().getValue();//x>1>0 x=2
        //if (value != null) {            tmpValue = buildFactory.buildBinary(curBlock, op, value, tmpValue);}
        if (mulExpNode.getMulExpNode().size()!=0) {
            String right ,opt;
            for(int i=0;i<mulExpNode.getMulExpNode().size();i++) {
                visitUnaryExp(mulExpNode.getMulExpNode().get(i));
                right=mulExpNode.getMulExpNode().get(i).getValue();
                opt=Operator(mulExpNode.getOperator().get(i));
                if(level>0){
                    pw.println("    %r"+this.regId+" = "+opt+" i32 "+left+", "+right);pw.flush();
                    mulExpNode.setRegId("%r"+this.regId);
                    mulExpNode.setValue("%r"+this.regId);
                    mulExpNode.getMulExpNode().get(i).setRegId("%r"+this.regId);
                    mulExpNode.getMulExpNode().get(i).setValue("%r"+this.regId);
                    left=mulExpNode.getMulExpNode().get(i).getValue();
                    this.regId++;
                    mulExpNode.setVarInfo(mulExpNode.getMulExpNode().get(i).getVarInfo());
                }
                else{

                    mulExpNode.setValue(mathCalculate(left,mulExpNode.getOperator().get(i),right));
                    mulExpNode.getMulExpNode().get(i).setValue(mathCalculate(left,mulExpNode.getOperator().get(i),right));
                    left = mulExpNode.getValue();//left=mulExpNode.getMulExpNode().get(i).getValue();
                }
            }//generate(a.get(i+1));


            //left=relExpNode.getRelExpNode().getValue();
            //relExpNode.setValue(a.get(a.size()-1).getValue());

        }else {
            mulExpNode.setVarInfo(mulExpNode.getUnaryExpNode().getVarInfo());
            mulExpNode.setValue(left);
            mulExpNode.setRegId(mulExpNode.getUnaryExpNode().getRegId());
            mulExpNode.setType(mulExpNode.getUnaryExpNode().getType());
        }
    }

    private void visitAddExp(AddExpNode addExpNode) {
        // AddExp -> MulExp | MulExp ('+' | '−') AddExp
        visitMulExp(addExpNode.getMulExpNode());
        String left = addExpNode.getMulExpNode().getValue();//x>1>0 x=2
        //if (value != null) {            tmpValue = buildFactory.buildBinary(curBlock, op, value, tmpValue);}
        if (addExpNode.getAddExpNode().size() != 0) {
            String right = "", opt = "";
            for (int i = 0; i < addExpNode.getAddExpNode().size(); i++) {
                visitMulExp(addExpNode.getAddExpNode().get(i));   //generate(a.get(i+1));

                right = addExpNode.getAddExpNode().get(i).getValue();
                opt = Operator(addExpNode.getOperator().get(i));
                if (level > 0) {
                    pw.println("    %r" + this.regId + " = " + opt + " i32 " + left + ", " + right);
                    pw.flush();
                    addExpNode.setRegId("%r" + this.regId);
                    addExpNode.setValue("%r" + this.regId);
                    addExpNode.getAddExpNode().get(i).setRegId("%r" + this.regId);
                    addExpNode.getAddExpNode().get(i).setValue("%r" + this.regId);
                    left = addExpNode.getAddExpNode().get(i).getValue();
                    addExpNode.setVarInfo(addExpNode.getAddExpNode().get(i).getVarInfo());
                    this.regId++;
                } else {
                    addExpNode.setValue(mathCalculate(left, addExpNode.getOperator().get(i), right));
                    left = addExpNode.getValue();
                    addExpNode.getAddExpNode().get(i).setValue(mathCalculate(left,addExpNode.getOperator().get(i),right));
                }
            }
            //left=relExpNode.getRelExpNode().getValue();
            //relExpNode.setValue(a.get(a.size()-1).getValue());

        } else {
            addExpNode.setVarInfo(addExpNode.getMulExpNode().getVarInfo());
            addExpNode.setValue(left);
            addExpNode.setRegId(addExpNode.getMulExpNode().getRegId());
            addExpNode.setType(addExpNode.getMulExpNode().getType());
        }
    }
    /*public void visitRelEqExp(EqExpNode ast){
        // RelExp -> AddExp | AddExp ('<' | '>' | '<=' | '>=') RelExp
        // EqExp -> RelExp | RelExp ('==' | '!=') EqExp
        //ArrayList<AstNode> a=ast.getChild();
        generate(a.get(0));//RelExp/EqExp
        String left=a.get(0).getValue();
        if(a.size()>1){

        }
        else{
            ast.setValue(left);
        }
    }*/
    private void visitRelExp(RelExpNode relExpNode) {
        // RelExp -> AddExp | AddExp ('<' | '>' | '<=' | '>=') RelExp
        visitAddExp(relExpNode.getAddExpNode());
        String left=relExpNode.getAddExpNode().getValue();//x>1>0 x=2
        //if (value != null) {            tmpValue = buildFactory.buildBinary(curBlock, op, value, tmpValue);}
        if (relExpNode.getRelExpNode().size()!=0) {
            String right = "",opt="";
            for(int i=0;i<relExpNode.getRelExpNode().size();i++) {
                visitAddExp(relExpNode.getRelExpNode().get(i));   //generate(a.get(i+1));

                right = relExpNode.getRelExpNode().get(i).getValue();
                opt = Operator(relExpNode.getOperator().get(i));
                if (level > 0) {
                    pw.println("    %r" + this.regId + " = icmp " + opt + " i32 " + left + ", " + right);pw.flush();
                    this.regId++;
                    pw.println("    %r" + this.regId + " = zext i1 %r" + (this.regId - 1) + " to i32");pw.flush();
                    relExpNode.setRegId("%r" + this.regId);
                    relExpNode.setValue("%r" + this.regId);
                    relExpNode.getRelExpNode().get(i).setRegId("%r" + this.regId);
                    relExpNode.getRelExpNode().get(i).setValue("%r" + this.regId);
                    left = relExpNode.getRelExpNode().get(i).getValue();
                    //relExpNode.setVarInfo(relExpNode.getRelExpNode().get(i).getVarInfo());
                    this.regId++;
                } else {
                    relExpNode.setValue(mathCalculate(left, relExpNode.getOperator().get(i), right));
                    relExpNode.getRelExpNode().get(i).setValue(mathCalculate(left, relExpNode.getOperator().get(i), right));
                    left = relExpNode.getValue();}
                //left=relExpNode.getRelExpNode().getValue();
                //relExpNode.setValue(a.get(a.size()-1).getValue());
            }
        }else {
                relExpNode.setValue(left);
            relExpNode.setRegId(relExpNode.getAddExpNode().getRegId());
            //relExpNode.setType(relExpNode.getRelExpNode().getType());
        }
    }

    private void visitEqExp(EqExpNode eqExpNode) {
        // EqExp -> RelExp | RelExp ('==' | '!=') EqExp
        visitRelExp(eqExpNode.getRelExpNode());
        String left=eqExpNode.getRelExpNode().getValue();
        if (eqExpNode.getEqExpNode().size()!=0) {
            String right = "",opt="";
            for(int i=0;i<eqExpNode.getEqExpNode().size();i++) {
                visitRelExp(eqExpNode.getEqExpNode().get(i));   //generate(a.get(i+1));

                right = eqExpNode.getEqExpNode().get(i).getValue();
                opt = Operator(eqExpNode.getOperator().get(i));
                if (level > 0) {
                pw.println("    %r"+this.regId+" = icmp "+opt+" i32 "+left+", "+right);pw.flush();
                this.regId++;
                pw.println("    %r"+this.regId+" = zext i1 %r"+(this.regId-1)+" to i32");pw.flush();
                eqExpNode.setRegId("%r"+this.regId);
                eqExpNode.setValue("%r"+this.regId);
                eqExpNode.getEqExpNode().get(i).setRegId("%r"+this.regId);
                eqExpNode.getEqExpNode().get(i).setValue("%r"+this.regId);
                    left = eqExpNode.getEqExpNode().get(i).getValue();
                    this.regId++;
            }
            else{
                eqExpNode.setValue(mathCalculate(left,eqExpNode.getOperator().get(i),right));
                eqExpNode.getEqExpNode().get(i).setValue(mathCalculate(left,eqExpNode.getOperator().get(i),right));
                    left = eqExpNode.getValue();
            }}
        }else {
            eqExpNode.setValue(left);
        }
    }
    public void visitLAndExp(LAndExpNode ast) {
        // LAndExp -> EqExp | EqExp '&&' LAndExp
        //???????？、?、?
        //for(int i=0;i<a.size()-2;i+=2){
        //ast.getEqExpNode().setYesId(ast.getYesId());
       // ast.getLAndExpNode().setStmtId(ast.getStmtId());
        //ast.getEqExpNode().setNoId(ast.getNoId());
        if(ast.getLAndExpNode()!=null){
          //ast.getLAndExpNode().setNoId(this.regId);
            //pw.println("\n"+(this.regId-1)+":");pw.flush();
        }
        visitEqExp(ast.getEqExpNode());
        ast.setStmtId(ast.getNoId());
        if(ast.getLAndExpNode()==null){
            ast.setValue(ast.getEqExpNode().getValue());

        } else {
            ast.getLAndExpNode().setYesId(ast.getYesId());
            ast.getLAndExpNode().setStmtId(ast.getStmtId());
            ast.getLAndExpNode().setNoId(ast.getNoId());///????
            //ast.setNoId(this.regId+1);
            //if (ast.getLAndExpNode().getLAndExpNode()==null) ast.getLAndExpNode().setNoId(ast.getNoId()+1);
            pw.println("    %r" + this.regId + " = icmp ne i32 0, " + ast.getEqExpNode().getValue());pw.flush();
            pw.println("    br i1 %r"+this.regId+", label %r"+(this.regId+1)+", label %r"+ast.getNoId());pw.flush();

            pw.println("\nr"+(this.regId+1)+":");pw.flush();
            this.regId += 2;
            visitLAndExp(ast.getLAndExpNode());//LAndExp
            ast.setStmtId(ast.getLAndExpNode().getStmtId());
            ast.setValue(ast.getLAndExpNode().getValue());
            //pw.println("\n" + (this.regId - 1) + ":");pw.flush();
            ///if(ast.getLAndExpNode()==null){
            //    ast.setValue(ast.getEqExpNode().getValue());
           // }
           // pw.println("    %"+this.regId+" = icmp ne i32 0, "+ast.getEqExpNode().getValue());
           // pw.println("    br i1 %"+this.regId+", label %"+ast.getYesId()+", label %"+ast.getNoId());
           // this.regId+=1;

        }

        //pw.println("\n"+ast.getLAndExpNode().getNoId()+":");pw.flush();
    }
    public void visitLOrExp(LOrExpNode ast){
        // LOrExp -> LAndExp | LAndExp '||' LOrExp
        //ArrayList<AstNode> a=ast.getChild();
        //for(int i=0;i<a.size()-2;i+=2){
        ast.getLAndExpNode().setYesId(ast.getYesId());
        ast.getLAndExpNode().setStmtId(ast.getStmtId());
        ast.getLAndExpNode().setNoId(ast.getNoId());
        if(ast.getLOrExpNode()!=null){
            ast.getLOrExpNode().setNoId(ast.getYesId()+1);
            //pw.println("\n"+(this.regId-1)+":");pw.flush();this.regId
        }
        //pw.println("\n"+ast.getLAndExpNode().getNoId()+":");pw.flush();
        visitLAndExp(ast.getLAndExpNode());//特殊
        ast.setStmtId(ast.getNoId());
        if (ast.getLOrExpNode()==null){//if(ast.getLAndExpNode().getLAndExpNode()==null)
            //ast.setNoId(ast.getYesId());
            pw.println("    %r"+this.regId+" = icmp ne i32 0, "+ast.getLAndExpNode().getValue());
            pw.println("    br i1 %r"+this.regId+", label %r"+ast.getYesId()+", label %r"+(ast.getNoId()));
            this.regId++;///????

            ast.setValue(ast.getLAndExpNode().getValue());
        }else {
            ast.getLOrExpNode().setYesId(ast.getYesId());
            ast.getLOrExpNode().setStmtId(ast.getStmtId());
            // ast.getLOrExpNode().setNoId(ast.getNoId());
            pw.println("    %r"+this.regId+" = icmp ne i32 0, "+ast.getLAndExpNode().getValue());
            pw.println("    br i1 %r"+this.regId+", label %r"+ast.getYesId()+", label %r"+(this.regId+1));

            pw.println("\nr"+(this.regId+1)+":");pw.flush();
            this.regId+=2;

            visitLOrExp(ast.getLOrExpNode());//LOrExp
            ast.setStmtId(ast.getLOrExpNode().getStmtId());
            ast.setValue(ast.getLOrExpNode().getValue());
            //pw.println("    %"+this.regId+" = icmp ne i32 0, "+ast.getLOrExpNode().getValue());pw.flush();
           // pw.println("    br i1 %"+this.regId+", label %"+ast.getYesId()+", label %"+(this.regId+1));pw.flush();
            //this.regId+=2;

        }


        /*int max=a.size()-1;
        if(a.get(max).getChild().size()==1){
            generate(a.get(max));//LOrExp
            pw.println("    %"+this.regId+" = icmp ne i32 0, "+a.get(max).getValue()+"\n");
            pw.println("    br i1 %"+this.regId+", label %"+ast.getYesId()+", label %"+ast.getNoId()+"\n");
            this.regId+=1;
        }
        else{
            a.get(max).setYesId(ast.getYesId());
            a.get(max).setStmtId(ast.getStmtId());
            a.get(max).setNoId(ast.getNoId());
            this.regId++;
            generate(a.get(max));//特殊
        }*/

    }
    public void visitConstExp(ConstExpNode ast){
            // ConstExp -> AddExp
            //ArrayList<AstNode> a=ast.getChild();
            //if(a.size()==1){
                visitAddExp(ast.getAddExpNode());
                ast.setValue(ast.getAddExpNode().getValue());
           // }
    }

    public String Operator(String op){
        String opt = switch (op) {
            case "+" -> "add";
            case "-" -> "sub";
            case "*" -> "mul";
            case "/" -> "sdiv";
            case "%" -> "srem";
            case "==" -> "eq";
            case "!=" -> "ne";
            case ">" -> "sgt";
            case ">=" -> "sge";
            case "<" -> "slt";
            case "<=" -> "sle";
            case "&&" -> "and";
            case "||" -> "or";
            default -> "";
        };
        return opt;
    }
    public String mathCalculate(String left,String op,String right){
        int a=Integer.parseInt(left);
        int b=Integer.parseInt(right);
        int ans = switch (op) {
            case "+" -> a + b;
            case "-" -> a - b;
            case "*" -> a * b;
            case "/" -> a / b;
            case "%" -> a % b;
            case "==" -> (a == b) ? 1 : 0;
            case "!=" -> (a != b) ? 1 : 0;
            case ">" -> (a > b) ? 1 : 0;
            case ">=" -> (a >= b) ? 1 : 0;
            case "<" -> (a < b) ? 1 : 0;
            case "<=" -> (a <= b) ? 1 : 0;
            default -> 0;
        };
        return ans+"";
    }
    public void init(){
        FileWriter fw=null;
        try {
            //fw=new FileWriter("./llvm_ir.txt",true);
            pw = new PrintWriter("./llvm_ir.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //pw=new PrintWriter(fw);

        pw.println("declare i32 @getint()");
        pw.flush();
        pw.println("declare void @putint(i32)");
        pw.flush();
        pw.println("declare void @putch(i32)");
        pw.flush();
        pw.println("declare void @putstr(i8*)");
        pw.flush();
    }
    /*public void pw.println(String str){
        FileWriter fw=null;
        try {
            fw=new FileWriter("llvm_ir.txt",true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        PrintWriter pw=new PrintWriter(fw);
        pw.print(str);
        pw.flush();
    }*/
}
