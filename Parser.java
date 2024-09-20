import java.io.*;
import java.io.FileWriter;
import java.util.ArrayList;
import Node.*;
import Symbol.*;
import Token.Token;

public class Parser {
    public ArrayList<Token> wordList;
    public Token sym;
    public int index;
    public String parType;
    public CompUnitNode ast;
    public int paramnum;
    int num;
    public SymbolTable symbolTable;
    private boolean changevalue;
    public static boolean noteq;
    //private ArrayList<Item> logList;
    //public String toString() {
    //    return "<" + .toString() + ">";
    //}
    public Parser(ArrayList<Token> list) {
        this.wordList = list;
        this.index = 0;
    //    this.//logList = new ArrayList<>();
    }

    public void analyze() {
        File file =new File("./output.txt");
        FileWriter fileWriter = null;
        //try {
        //    fileWriter = new FileWriter(file);
        //    fileWriter.write("");
        //} catch (IOException e) {
        //    throw new RuntimeException(e);
        //}
        sym = wordList.get(index++);//NextSym();//nextSym();
        //Node.ConstExpNode.print();
        aCompUnit();//this.ast=
        //Error.printe();
        //SymbolTable.print();
        //ast.print();??????????
        //logList.add(new OrdinarySyntax(SynType.CompUnit));
    }
    public void NextSym(){

        System.out.println(sym.getType()+" "+sym.getStr()+" "+sym.getLineNum());
        if(index<wordList.size()) {
            sym = wordList.get(index++);//
        }
        else {
            sym=null;
        }
    }
     public CompUnitNode getAst(){
        return ast;
     }
    public CompUnitNode aCompUnit() {
        /// CompUnit -> {Decl} {FuncDef} MainFuncDef

        ArrayList<DeclNode> declList = new ArrayList<>();
        ArrayList<FuncDefNode> funcDefList = new ArrayList<>();
        MainFuncDefNode mainFuncDefNode = null;
       /* while (sym.getType().equals("CONSTTK")) {
            declList.add(aDecl());
        }        while (sym.getType().equals("VOIDTK")){
            funcDefList.add(aFuncDef());
            //mainFuncDefNode = aMainFuncDef();
        }*/
        while ((sym.getType().equals("INTTK")&&!wordList.get(index + 1).getType().equals("LPARENT"))||sym.getType().equals("CONSTTK")) {
            //sym = wordList.get(index++);System.out.println(sym+" 1");
             declList.add(aDecl());

        }
        while ((sym.getType().equals("INTTK")&&wordList.get(index).getType().equals("IDENFR")||sym.getType().equals("VOIDTK"))){
            funcDefList.add(aFuncDef());
        }
        mainFuncDefNode = aMainFuncDef(); //now == int

        System.out.println("<CompUnit>");
        ast=new CompUnitNode(declList, funcDefList, mainFuncDefNode);
        return ast;
    }

    // Decl → ConstDecl | VarDecl
    public DeclNode aDecl(){
        ConstDeclNode constDeclNode = null;
        VarDeclNode varDeclNode = null;
        int type=0;
        if (sym.getType().equals("CONSTTK")) {
            constDeclNode = aConstDecl();
        } else {
            type=1;
            varDeclNode = aVarDecl();
        }
        return new DeclNode(constDeclNode, varDeclNode,type);
    }
    //ConstDecl → 'const' BType ConstDef { ',' ConstDef } ';'
    public ConstDeclNode aConstDecl(){
        ArrayList<ConstDefNode> constDefList = new ArrayList<>();
        if (sym.getType().equals("CONSTTK")){
            NextSym();//sym = wordList.get(index++);
            if (sym.getType().equals("INTTK")) {
                NextSym();//sym = wordList.get(index++);
                constDefList.add(aConstDef());

                while (sym.getType().equals("COMMA")) {
                    NextSym();//sym = wordList.get(index++);
                    constDefList.add(aConstDef());
                }
                //sym=wordList.get(index++);//sym+"/"+ System.out.println(wordList.get(index));
                if (sym.getType().equals("SEMICN")) {
                    NextSym();//sym = wordList.get(index++);
                } //else {System.out.println(wordList.get(index-2).getLineNum()+" i");}
                else {
                    //System.out.println(sym);
                    ErrorHandler.addError(new ErrorHandler(wordList.get(index-2).getLineNum(), 'i'));
                }
            }
        }
        System.out.println("<ConstDecl>");
        return new ConstDeclNode(constDefList);
    }
    //ConstDef → Ident { '[' ConstExp ']' } '=' ConstInitVal
    public ConstDefNode aConstDef() {
        ArrayList<ConstExpNode> constExpList = new ArrayList<>();
        ConstInitValNode constInitValNode = null;
        int type = 0;
        Token token = null;
        VarSymbol symbol = new VarSymbol();
        if (sym.getType().equals("IDENFR")) {
            token = new Token(sym.getType(), sym.getStr(), sym.getLineNum());
            NextSym();//sym = wordList.get(index++);
            while (sym.getType().equals("LBRACK")) {
                type++;
                NextSym();//sym = wordList.get(index++);
                constExpList.add(aConstExp());
                if (sym.getType().equals("RBRACK")) {
                    NextSym();//sym = wordList.get(index++);
                } else {
                    ErrorHandler.addError(new ErrorHandler(wordList.get(index - 2).getLineNum(), 'k'));
                    symbol.settype(true);
                }
            }
            if (sym.getType().equals("ASSIGN")) {
                NextSym();//sym = wordList.get(index++);
                constInitValNode = aConstInitVal();
            }
        }
        //symbolTable.addSymbol(new Symbol(token, type, 1));
        //if(SymbolTable.findSymbol())
        //    Error.addError(new Error(wordList.get(index - 2).getLineNum(), 'b'));
        System.out.println("<ConstDef>");
        return new ConstDefNode(constExpList, constInitValNode, token, symbol);
    }
    //ConstInitVal → ConstExp|'{'[ ConstInitVal { ',' ConstInitVal } ] '}'
    public ConstInitValNode aConstInitVal(){
        ArrayList<ConstInitValNode> constInitValList = new ArrayList<>();
        ConstExpNode constExpNode=null;
        if(sym.getType().equals("LBRACE")){
            NextSym();//sym = wordList.get(index++);
            if (!sym.getType().equals("RBRACE")) {

                constInitValList.add(aConstInitVal());

                while (sym.getType().equals("COMMA")) {

                    NextSym();//sym = wordList.get(index++);
                    constInitValList.add(aConstInitVal());
                    //System.out.println("<ConstInitVal>?");
                }
                if (!sym.getType().equals("COMMA")){
                    NextSym();
                    System.out.println("<ConstInitVal>");//System.out.println("<ConstInitVal>?");
                }
            }else {
                NextSym();//sym = wordList.get(index++);
                //System.out.println(sym);
            }

        }else {
            constExpNode = aConstExp();
            System.out.println("<ConstInitVal>");
        }
        //System.out.println(sym+"?");

        return new ConstInitValNode(constInitValList,constExpNode);
    }
    //VarDecl → BType VarDef { ',' VarDef } ';'
    public VarDeclNode aVarDecl(){
        ArrayList<VarDefNode> varDefList=new ArrayList<>();
        if(sym.getType().equals("INTTK")){
            NextSym();//sym = wordList.get(index++);
            varDefList.add(aVarDef());
            while (sym.getType().equals("COMMA")) {
                NextSym();//sym=wordList.get(index++);
                varDefList.add(aVarDef());
            }
            if(sym.getType().equals("SEMICN")){/*执行完成*/
                NextSym();//sym = wordList.get(index++);
            }
            else{
                ErrorHandler.addError(new ErrorHandler(wordList.get(index-2).getLineNum(), 'i'));
                /*错误System.out.println(wordList.get(index-2).getLineNum()+" i");*/}
        }
        else{/*错误*/}//System.out.println(sym+" "+wordList.get(index));
        System.out.println("<VarDecl>");
        return new VarDeclNode(varDefList);
    }
//VarDef → Ident{'['ConstExp']'} | Ident{'['ConstExp']'} '=' InitVal
    public VarDefNode aVarDef(){
        ArrayList<ConstExpNode> constExpList = new ArrayList<>();
        InitValNode initValNode=null;
        int type=0;Token token=null;
        VarSymbol symbol = new VarSymbol();
        if(sym.getType().equals("IDENFR")){
            token=new Token(sym.getType(),sym.getStr(),sym.getLineNum());
            NextSym();//sym = wordList.get(index++);
            while (sym.getType().equals("LBRACK")){
                type++;
                NextSym();//sym = wordList.get(index++);
                constExpList.add(aConstExp());
                if (sym.getType().equals("RBRACK")) {
                    NextSym();//sym = wordList.get(index++);
                } else {
                    ErrorHandler.addError(new ErrorHandler(wordList.get(index-2).getLineNum(), 'k'));
                    symbol.settype(true);////wordList.add(new Token.Token("RBRACK","]", sym.getLineNum()));//errorCheckK(wordList.get(index - 2).getLineNum());
                    //System.out.println(sym.getType()+" "+sym.getStr());
                }
            }
            //symbolTable.addSymbol(new Symbol(token, type, 0));
            //if(SymbolTable.findSymbol())
            //                Error.addError(new Error(wordList.get(index - 2).getLineNum(), 'b'));
            if(sym.getType().equals("ASSIGN")){
                NextSym();//sym = wordList.get(index++);
                initValNode= aInitVal();
                /*执行完成*/
            }
            else{/*错误*/}
        }else{/*错误*/}
        System.out.println("<VarDef>");
        return new VarDefNode(constExpList,initValNode,token,symbol);
    }

    //InitVal → Exp | '{' [ InitVal { ',' InitVal } ] '}'
    public InitValNode aInitVal(){
        ArrayList<InitValNode> initValList = new ArrayList<>();
        ExpNode expNode=null;
        if (sym.getType().equals("LBRACE")) {
            NextSym();//sym = wordList.get(index++);
            if (!sym.getType().equals("RBRACE")) {
                initValList.add(aInitVal());
                while (sym.getType().equals("COMMA")) {
                    NextSym();//sym = wordList.get(index++);
                    initValList.add(aInitVal());
                }
            }
            if (sym.getType().equals("RBRACE")) {
                NextSym();//sym = wordList.get(index++);
            }
        } else {
            expNode = aExp();
        }
        //if (singleVal != null) {
        //    valList = null;
        //}
        System.out.println("<InitVal>");
        return new InitValNode(initValList,expNode);
    }
    //FuncDef → FuncType Ident '(' [FuncFParams] ')' Block
    public FuncDefNode aFuncDef(){
        FuncFParamsNode funcFParamsNode = null;
        BlockNode blockNode=null;
        String retype="";
        Token ident = null;
        if (sym.getType().equals("VOIDTK") || sym.getType().equals("INTTK")){
            retype=sym.getStr();//if(sym.getType().equals("VOIDTK")) retype=1;
           // else if(sym.getType().equals("INTTK")) retype=0;
            NextSym();//sym=wordList.get(index++);
            System.out.println("<FuncType>");
            if(sym.getType().equals("IDENFR")){
                ident=new Token(sym.getType(),sym.getStr(),sym.getLineNum());//symbolTable.addSymbol(new Symbol(sym.getStr(), -1, 0));
                //if(SymbolTable.findSymbol())
                //  Error.addError(new Error(wordList.get(index - 2).getLineNum(), 'b'));
                NextSym();//sym=wordList.get(index++);

                if(sym.getType().equals("LPARENT")){
                    NextSym();//sym=wordList.get(index++);
                    if (!sym.getType().equals("RPARENT")&&!wordList.get(index-1).getType().equals("LBRACE")) {
                        paramnum++;
                        funcFParamsNode=aFuncFParams();
                    }
                    //FuncDef.addFunc(new FuncDef(,retype,paramnum));
                    paramnum=0;
                    if (sym.getType().equals("RPARENT")) {
                        NextSym();//sym=wordList.get(index++);

                    } else {
                        ErrorHandler.addError(new ErrorHandler(wordList.get(index-2).getLineNum(), 'j'));
                        // errorCheckJ(wordList.get(index - 2).getLineNum());
                    }blockNode=aBlock();
                    //int lineNum;
                }
            }
        }
        //SymbolManager.findSymbol();
        System.out.println("<FuncDef>");
        return new FuncDefNode(funcFParamsNode,blockNode,ident,retype);
    }
    //MainFuncDef → 'int' 'main' '(' ')' Block
    public MainFuncDefNode aMainFuncDef(){
        BlockNode blockNode=null;
        if (sym.getType().equals("INTTK")){
            NextSym();//sym=wordList.get(index++);
            if (sym.getType().equals("MAINTK")){
                //symbolTable.addSymbol(new Symbol(sym.getStr(), -1, 0));
                //if(SymbolTable.findSymbol())
                //  Error.addError(new Error(wordList.get(index - 2).getLineNum(), 'b'));
                //FuncDef.addFunc(new FuncDef(,0,0));
                NextSym();//sym=wordList.get(index++);
                if (sym.getType().equals("LPARENT")){
                    NextSym();//sym=wordList.get(index++);
                    if (sym.getType().equals("RPARENT")){
                        NextSym();//sym=wordList.get(index++);

                        blockNode=aBlock();
                    }
                }
            }
        }
        System.out.println("<MainFuncDef>");
        return new MainFuncDefNode(blockNode);
    }

    public String aFuncType(){return "";    }
    //FuncFParams → FuncFParam { ',' FuncFParam }
    public FuncFParamsNode aFuncFParams(){
        ArrayList<FuncFParamNode> funcFParamList = new ArrayList<>();
        funcFParamList.add(aFuncFParam());
        while (sym.getType().equals("COMMA")) {
            paramnum++;
            NextSym();//sym=wordList.get(index++);
            funcFParamList.add(aFuncFParam());
        }
        System.out.println("<FuncFParams>");
        return new FuncFParamsNode(funcFParamList);
    }
    //FuncFParam → BType Ident ['[' ']' { '[' ConstExp ']' }]
    public FuncFParamNode aFuncFParam(){
        ArrayList<ConstExpNode> constExpList = new ArrayList<>();
        Token token=null;int type=0;
        String not0wei = "";
        VarSymbol symbol=new VarSymbol();
        if(sym.getType().equals("INTTK")){
            NextSym();//sym=wordList.get(index++);
            if(sym.getType().equals("IDENFR")){
                token=new Token(sym.getType(),sym.getStr(),sym.getLineNum());
                NextSym();//sym=wordList.get(index++);
                if (sym.getType().equals("LBRACK")){
                    type++;
                    NextSym();//sym=wordList.get(index++);
                    if(sym.getType().equals("RBRACK")){
                        NextSym();//sym=wordList.get(index++);
                    } else {
                        ErrorHandler.addError(new ErrorHandler(wordList.get(index-2).getLineNum(), 'k'));
                        symbol.settype(true);
                    }
                    while(sym.getType().equals("LBRACK")){
                        type++;
                        NextSym();//sym=wordList.get(index++);
                        constExpList.add(aConstExp());
                        if(sym.getType().equals("RBRACK")){
                            NextSym();//sym=wordList.get(index++);
                        }
                        else {
                            ErrorHandler.addError(new ErrorHandler(wordList.get(index-2).getLineNum(), 'k'));
                            symbol.settype(true);
                        }
                    }
                }
            }
        }
        //symbolTable.addSymbol(new Symbol(token, type, 0));
        //if(SymbolTable.findSymbol())
        //    Error.addError(new Error(wordList.get(index - 2).getLineNum(), 'b'));
        System.out.println("<FuncFParam>");
        return new FuncFParamNode(constExpList,token,type,symbol);
    }
    //Block → '{' { BlockItem-> Decl | Stmt } '}'
    public BlockNode aBlock() {
        ArrayList<BlockItemNode> blockList = new ArrayList<>();
        //ArrayList<StmtNode> stmtList = new ArrayList<>();
        String left = null;
        Token right = null;
        if (sym.getType().equals("LBRACE")) {
            left = "{";
            NextSym();//sym=wordList.get(index++);
            while (!sym.getType().equals("RBRACE")) {
                //NextSym();sym=wordList.get(index++);
                blockList.add(aBlockItem());
                //} else {
                //    blockList.add(aStmt());+wordList.get(index)
            }
            if (sym.getType().equals("RBRACE")) {
                right=sym;//right = "}";
                NextSym();//sym=wordList.get(index++);
            }
        }
        System.out.println("<Block>");
        return new BlockNode(blockList, right);
    }
    private BlockItemNode aBlockItem() {
        // BlockItem -> Decl | Stmt
        DeclNode declNode = null;
        StmtNode stmtNode = null;
        if (sym.getType().equals("CONSTTK") || sym.getType().equals("INTTK")) {
            declNode = aDecl();
        } else {
            stmtNode = aStmt();
        }
        return new BlockItemNode(declNode, stmtNode);
    }
    public int HasAssign(){
        int assign = index;//System.out.println(wordList.get(index));wordList.get(index-1).getType().equals("SEMICN")
        for (int i = index-1; !wordList.get(i).getType().equals("SEMICN") && wordList.get(i).getLineNum() == sym.getLineNum(); i++) {
            if (wordList.get(i).getType().equals("ASSIGN")) {
                assign = index+1;
            }
        }
        return assign;
    }
    public StmtNode aStmt(){
        if (sym.getType().equals("LBRACE")) {// Block

            BlockNode blockNode = aBlock();
            System.out.println("<Stmt>");
            return new StmtNode(StmtNode.StmtType.Block,blockNode);
        } else if (sym.getType().equals("IFTK")) {
            // 'if' '(' Cond ')' Stmt [ 'else' Stmt ]
            NextSym();//sym = wordList.get(index++);
            ArrayList<StmtNode> stmtList = null;
            CondNode condNode=null;
            if (sym.getType().equals("LPARENT")) {
                NextSym();//sym = wordList.get(index++);
                condNode = aCond();
                if (sym.getType().equals("RPARENT")) {
                    NextSym();//sym = wordList.get(index++);
                }
                else ErrorHandler.addError(new ErrorHandler(wordList.get(index-2).getLineNum(), 'j'));
                stmtList = new ArrayList<>();
                stmtList.add(aStmt());
                if (sym.getType().equals("ELSETK")) {
                    NextSym();//sym = wordList.get(index++);
                    stmtList.add(aStmt());
                }
            }
            System.out.println("<Stmt>");
            return new StmtNode(StmtNode.StmtType.If,condNode, stmtList);
        } else if (sym.getType().equals("FORTK")) {
            // 'for' '(' [ForStmt] ';' [Cond] ';' [ForStmt] ')' Stmt
            NextSym();//sym = wordList.get(index++);
            ForStmtNode forStmtNode1=null,forStmtNode2=null;
            CondNode condNode = null;
            StmtNode stmtNode=null;
            if (sym.getType().equals("LPARENT")) {
                NextSym();//sym = wordList.get(index++);
                //System.out.println(sym);
                if(HasAssign()>index&&!sym.getType().equals("SEMICN")){
                    forStmtNode1=aForStmt();
                }
                if(sym.getType().equals("SEMICN")) NextSym();//sym=wordList.get(index++);
                else ErrorHandler.addError(new ErrorHandler(wordList.get(index-2).getLineNum(), 'i'));//System.out.println(wordList.get(index)+""+HasAssign());
                if(HasAssign()==index&&!sym.getType().equals("SEMICN")){
                    condNode = aCond();
                }
                if(sym.getType().equals("SEMICN")) NextSym();//sym=wordList.get(index++);
                else ErrorHandler.addError(new ErrorHandler(wordList.get(index-2).getLineNum(), 'i'));
                if(HasAssign()>index&&!sym.getType().equals("RPARENT")){
                    forStmtNode2=aForStmt();
                }
                if(sym.getType().equals("RPARENT")){
                    NextSym();//sym=wordList.get(index++);

                } else ErrorHandler.addError(new ErrorHandler(wordList.get(index-2).getLineNum(), 'j'));
                stmtNode=aStmt();
            }
            System.out.println("<Stmt>");
            return new StmtNode(StmtNode.StmtType.For,forStmtNode1,forStmtNode2,condNode, stmtNode);
        } else if (sym.getType().equals("BREAKTK") || sym.getType().equals("CONTINUETK")) {
            // 'break' ';'| 'continue' ';'
            boolean bORc=false;Token ident=null;
            if(sym.getType().equals("CONTINUETK")) bORc=true;
            ident=sym;
            NextSym();//sym=wordList.get(index++);
            if (sym.getType().equals("SEMICN")){
                NextSym();//sym=wordList.get(index++);
            }else ErrorHandler.addError(new ErrorHandler(wordList.get(index-2).getLineNum(), 'i'));
            System.out.println("<Stmt>");
            if (bORc) return new StmtNode(StmtNode.StmtType.Continue,ident);
            else return  new StmtNode(StmtNode.StmtType.Break,ident);
        } else if (sym.getType().equals("RETURNTK")) {
            // 'return' [Exp] ';'
            Token ret=new Token(sym.getType(),sym.getStr(),sym.getLineNum());
            NextSym();//sym=wordList.get(index++);
            ExpNode expNode = null;
            if (sym.getType().equals("IDENFR") || sym.getType().equals("PLUS") ||
                    sym.getType().equals("MINU") || sym.getType().equals("NOT") ||
                    sym.getType().equals("LPARENT") || sym.getType().equals("INTCON")) {
                expNode = aExp();
            }
            if(sym.getType().equals("SEMICN")){
                NextSym();//sym=wordList.get(index++);
            }else ErrorHandler.addError(new ErrorHandler(wordList.get(index-2).getLineNum(), 'i'));
            System.out.println("<Stmt>");
            return new StmtNode(StmtNode.StmtType.Return,expNode,ret);
        } else if (sym.getType().equals("PRINTFTK")) {
            // 'printf' '(' FormatString { ',' Exp } ')' ';'
            NextSym();//sym=wordList.get(index++);
            ArrayList<ExpNode> expList = new ArrayList<>();
            String string = "";
            if(sym.getType().equals("LPARENT")){
                NextSym();//sym=wordList.get(index++);
                if(sym.getType().equals("STRCON")){
                    string=sym.getStr();
                    NextSym();//sym=wordList.get(index++);
                    int count=0;
                    while (sym.getType().equals("COMMA")) {
                        NextSym();//sym=wordList.get(index++);
                        count++;
                        expList.add(aExp());
                    }
                    if(count!=Lexer.forstmtcount.get(num)){
                        ErrorHandler.addError(new ErrorHandler(sym.getLineNum(),'l'));
                        noteq=true;
                    }//Lexer.forstmtcount=0;
                    if(sym.getType().equals("RPARENT")){
                        NextSym();//sym=wordList.get(index++);
                        if(sym.getType().equals("SEMICN")) NextSym();//sym=wordList.get(index++);
                        else ErrorHandler.addError(new ErrorHandler(wordList.get(index-2).getLineNum(), 'i'));
                    }
                    else ErrorHandler.addError(new ErrorHandler(wordList.get(index-2).getLineNum(), 'j'));
                    num++;
                }
            }
            System.out.println("<Stmt>");
            return new StmtNode(StmtNode.StmtType.Printf,expList,string);
        }/*else if(sym.getType().equals("repeat")){NextSym();aStmt();
            if(sym.equals("until")){NextSym();
                if(sym.equals("(")){NextSym();aCond();
                    if(sym.equals(")")){NextSym();}
                }
            }
        }//+reserveword,+parser,+FIRST:||sym.equals("repeat")*/
        else {

            if (HasAssign() > index) {
                // LVal '=' Exp ';'
                // LVal '=' 'getint' '(' ')' ';'System.out.println("<VarDef>"+sym);
                LValNode lValNode = aLVal();
                if(sym.getType().equals("ASSIGN")){
                    NextSym();//sym=wordList.get(index++);
                    changevalue=true;
                    if (sym.getType().equals("GETINTTK")) {
                        NextSym();NextSym();
                        if(sym.getType().equals("RPARENT"))
                            NextSym();
                        else ErrorHandler.addError(new ErrorHandler(wordList.get(index-2).getLineNum(), 'j'));
                        if(sym.getType().equals("SEMICN")){
                            NextSym();//Token.Token getintToken = match("GETINTTK");
                        }
                        else ErrorHandler.addError(new ErrorHandler(wordList.get(index-2).getLineNum(), 'i'));
                        //Token.Token leftParentToken = match("LPARENT");
                        System.out.println("<Stmt>");
                        return new StmtNode(StmtNode.StmtType.LValGetint,lValNode);
                    } else {

                        ExpNode expNode = aExp();
                        if(sym.getType().equals("SEMICN")){
                            NextSym();//sym=wordList.get(index++);

                        }
                        else ErrorHandler.addError(new ErrorHandler(wordList.get(index-2).getLineNum(), 'i'));
                        System.out.println("<Stmt>");
                        return new StmtNode(StmtNode.StmtType.LValExp,lValNode,expNode);
                    }
                }

            } else {
                // [Exp] ';'
                ExpNode expNode = null;
                if (sym.getType().equals("IDENFR") || sym.getType().equals("PLUS") ||
                        sym.getType().equals("MINU") || sym.getType().equals("NOT") ||
                        sym.getType().equals("LPARENT") || sym.getType().equals("INTCON")) {
                    expNode = aExp();
                }
                if(sym.getType().equals("SEMICN")){
                    NextSym();// sym=wordList.get(index++);

                }
                else ErrorHandler.addError(new ErrorHandler(wordList.get(index-2).getLineNum(), 'i'));

                System.out.println("<Stmt>");
                return new StmtNode(StmtNode.StmtType.Exp,expNode);
            }
        }

        return null;
    }
//ForStmt → LVal '=' Exp
    public ForStmtNode aForStmt(){
        LValNode lvalNode=aLVal();
        ExpNode expNode=null;
        if(sym.getType().equals("ASSIGN")){
            NextSym();//sym=wordList.get(index++);
            expNode=aExp();
        }
        System.out.println("<ForStmt>");
        return new ForStmtNode(lvalNode,expNode);
    }
    //Exp → AddExp
    public ExpNode aExp(){
        changevalue=false;
        AddExpNode addExpNode=aAddExp();
        System.out.println("<Exp>");
        return new ExpNode(addExpNode);
    }

    //Cond → LOrExp
    public CondNode aCond(){
        LOrExpNode lOrExpNode=aLOrExp();
        System.out.println("<Cond>");
        return new CondNode(lOrExpNode);
    }
    //LVal → Ident {'[' Exp ']'}
    public LValNode aLVal(){
        ArrayList<ExpNode> expList = new ArrayList<>();
        Token ident = null;
        if (sym.getType().equals("IDENFR")){
            ident=new Token(sym.getType(),sym.getStr(),sym.getLineNum());
            NextSym();//sym=wordList.get(index++);
            while (sym.getType().equals("LBRACK")) {
                NextSym();
                expList.add(aExp());
                if (sym.getType()=="RBRACK") NextSym();
                else ErrorHandler.addError(new ErrorHandler(wordList.get(index-2).getLineNum(), 'k'));
            }

        }
        System.out.println("<LVal>");
        return new LValNode(expList,ident,changevalue);
    }
    //PrimaryExp → '(' Exp ')' | LVal | Number
    public PrimaryExpNode aPrimaryExp(){
        LValNode lvalNode=null;
        ExpNode expNode=null;
        String numberNode=null;

        if (sym.getType().equals("LPARENT")) {
            NextSym();//sym=wordList.get(index++);
            expNode = aExp();
            if (sym.getType().equals("RPARENT")) {
                NextSym();//sym=wordList.get(index++);
            } else {//errorCheckJ(wordList.get(index - 2).getLineNum());
            }
        }// LVal
        else if ((sym.getType().equals("IDENFR")) || (sym.getType().equals("MAINTK"))) {
            lvalNode = aLVal();
        }// Number
        else if (sym.getType().equals("INTCON")) {
            numberNode=aNumber();
        }
        System.out.println("<PrimaryExp>");
        return new PrimaryExpNode(expNode,lvalNode,numberNode);
    }
    //Number → IntConst
    public String aNumber() {
        String num = null;
        if (sym.getType().equals("INTCON")) {
            num = sym.getStr();
            NextSym();//sym=wordList.get(index++);
            System.out.println("<Number>");
        }
        return num;
    }
    // UnaryExp := PrimaryExp | Ident '(' [FuncRParams] ')' | UnaryOp UnaryExp
    public UnaryExpNode aUnaryExp(){
        PrimaryExpNode primaryExpNode=null;
        ArrayList<ExpNode> funcRParamsNode = new ArrayList<>();
        String unaryOpNode =null;
        UnaryExpNode unaryExpNode=null;
        Token ident = null;
        if (sym.getType().equals("IDENFR") && wordList.get(index).getType().equals("LPARENT")) {
            ident=new Token(sym.getType(),sym.getStr(),sym.getLineNum());
            NextSym();//sym=wordList.get(index++);
            if (sym.getType().equals("LPARENT")) {
                NextSym();//sym=wordList.get(index++);
                while(!sym.getType().equals("RPARENT")&&!wordList.get(index-1).getType().equals("SEMICN")){
                    funcRParamsNode=aFuncRParams() ;
                }
            }
            if(sym.getType().equals("RPARENT")){
                NextSym();//sym=wordList.get(index++);
               // return new UnaryExpNode(ident, leftParentToken, funcRParamsNode, rightParentToken);
            }
            else ErrorHandler.addError(new ErrorHandler(wordList.get(index-2).getLineNum(), 'j'));
        } else if (sym.getType().equals("PLUS") || sym.getType().equals("MINU") || sym.getType().equals("NOT")) {
            unaryOpNode = aUnaryOp();
            unaryExpNode = aUnaryExp();

            //return new UnaryExpNode(unaryOpNode, unaryExpNode);
        } else {
            primaryExpNode = aPrimaryExp();

        }
        System.out.println("<UnaryExp>");
        return new UnaryExpNode(primaryExpNode,funcRParamsNode,unaryExpNode,unaryOpNode,ident);
    }
    //UnaryOp → '+' | '−' | '!'
    public String aUnaryOp() {
        String op = null;
        if (sym.getType().equals("PLUS") || sym.getType().equals("MINU") || sym.getType().equals("NOT")) {
            op = sym.getStr();
            NextSym();
            System.out.println("<UnaryOp>");
        }
        return op;
    }
    //FuncRParams → Exp { ',' Exp }
    public ArrayList<ExpNode> aFuncRParams(){
        ArrayList<ExpNode> funcRParams = new ArrayList<>();
        funcRParams.add(aExp());
        while (sym.getType().equals("COMMA")) {
            NextSym();//sym=wordList.get(index++);
            funcRParams.add(aExp());
        }
        System.out.println("<FuncRParams>");
        return funcRParams;
    }
    //MulExp → UnaryExp | MulExp ('*' | '/' | '%') UnaryExp
    public MulExpNode aMulExp(){
        UnaryExpNode unaryExpNode = aUnaryExp();
        //MulExpNode mulExpNode = null;
        ArrayList<UnaryExpNode> mulExpList=new ArrayList<>();//
        ArrayList<String> ops = new ArrayList<>();
        while (sym.getType().equals("MULT") || sym.getType().equals("DIV") || sym.getType().equals("MOD")) {
            System.out.println("<MulExp>");
            ops.add(sym.getStr());
            NextSym();//
            mulExpList.add(aUnaryExp());//aMulExp();
            //System.out.println("<MulExp>");
        }
        System.out.println("<MulExp>");
        return new MulExpNode(unaryExpNode,mulExpList,ops);
    }
    //AddExp → MulExp | AddExp ('+' | '−') MulExp   MulExp ('+' | '−') AddExp | MulExp
    public AddExpNode aAddExp() {
        MulExpNode mulExpNode = aMulExp();
        ArrayList<MulExpNode> addExpNodes = new ArrayList<>();
        ArrayList<String> ops = new ArrayList<>();

        while (sym.getType().equals("PLUS") || sym.getType().equals("MINU")) {
            System.out.println("<AddExp>");
            ops.add(sym.getStr());
            NextSym();
            addExpNodes.add(aMulExp());// aAddExp();//System.out.println(sym);
        }  System.out.println("<AddExp>");
        return new AddExpNode(addExpNodes, mulExpNode, ops);
    }
    //RelExp → AddExp | RelExp ('<' | '>' | '<=' | '>=') AddExp
    public RelExpNode aRelExp(){

        AddExpNode addExpNode = aAddExp();
        ArrayList<AddExpNode> relExpNodes = new ArrayList<>();
        ArrayList<String> ops = new ArrayList<>();
        while(sym.getType().equals("LSS") || sym.getType().equals("LEQ") || sym.getType().equals("GRE") || sym.getType().equals("GEQ")){
            System.out.println("<RelExp>");
            ops.add(sym.getStr());
            NextSym();
            relExpNodes.add(aAddExp());
        }
        System.out.println("<RelExp>");
        return new RelExpNode(addExpNode,relExpNodes,ops);
    }
    //EqExp → RelExp | EqExp ('==' | '!=') RelExp
    public EqExpNode aEqExp(){
        RelExpNode relExpNode= aRelExp();
        ArrayList<RelExpNode> eqExpNodes = new ArrayList<>();
        ArrayList<String> ops = new ArrayList<>();
        while(sym.getType().equals("EQL") || sym.getType().equals("NEQ")){
            System.out.println("<EqExp>");
            ops.add(sym.getStr());
            NextSym();
            eqExpNodes.add(aRelExp());
        }
        System.out.println("<EqExp>");
        return new EqExpNode(eqExpNodes,relExpNode,ops);
    }
    //LAndExp → EqExp | LAndExp '&&' EqExp
    public LAndExpNode aLAndExp(){
        EqExpNode eqExpNode = aEqExp();
        LAndExpNode landExpNode = null;
        if(sym.getType().equals("AND")) {
            System.out.println("<LAndExp>");
            NextSym();
            landExpNode = aLAndExp();
        }
        else System.out.println("<LAndExp>");
        return new LAndExpNode(eqExpNode,landExpNode);
    }
    //LOrExp → LAndExp | LOrExp '||' LAndExp
    public LOrExpNode aLOrExp(){
        LOrExpNode lorExpNode = null;
        LAndExpNode landExpNode = aLAndExp();
        if(sym.getType().equals("OR")){
            System.out.println("<LOrExp>");
            NextSym();
            lorExpNode=aLOrExp();
        }
        else System.out.println("<LOrExp>");
        return new LOrExpNode(lorExpNode,landExpNode);
    }
    //ConstExp → AddExp
    public ConstExpNode aConstExp(){
        AddExpNode addExpNode=aAddExp();
        System.out.println("<ConstExp>");
        return new ConstExpNode(addExpNode);
    }

}
