package Symbol;

import Token.Token;

public class SymbolManager {
    private static SymbolManager instance;//fatherId
    private SymbolTable curSymbolTable;
    private int depth;// 当前符号表的维数

    /*public SymbolManager(){
        this.id= SymbolTable.table;
        //this.fatherId = Symbol.table-1;
        HashMap<String, SymbolTable> directory = new HashMap<>();
    }
    public SymbolManager(SymbolManager fatherId){
        this.id= SymbolTable.table;
        this.fatherId = fatherId;
        HashMap<String, SymbolTable> directory = new HashMap<>();
    }*/
    public SymbolManager() {
        this.depth = 0;
        this.curSymbolTable = new SymbolTable(depth);
    }

    public static SymbolManager getInstance() {
        if (instance == null) instance = new SymbolManager();
        return instance;
    }
    public int getDepth(){
        return depth;
    }
    public SymbolTable getCurSymbolTable(){
        return curSymbolTable;
    }

    public void init() {
        this.depth = 0;
        this.curSymbolTable = new SymbolTable(depth);
    }

    public void addTable() {
        // .add(directory);SymbolManager Table
        //Symbols.add(Table);System.out.println(Symbols.get(0).directory);
        this.depth++;
        SymbolTable symbolTable = new SymbolTable(depth);
        curSymbolTable.addNext(symbolTable);
        symbolTable.setPre(curSymbolTable);
        curSymbolTable = symbolTable;

    }//public static void buildSubPro() {        Symbols = new SymbolManager(Symbols);}

    public void traceBackFather() {//返回上级符号表
        //Symbols = Symbols.getFatherPro();
        if(this.depth>0) this.depth--;
        if (curSymbolTable.getPre()!=null) curSymbolTable = curSymbolTable.getPre();

    }
    //public static void popTable(){        Symbols.remove(Symbols.size()-1);    }

    public Symbol findSymbol(String name, boolean recurse) {
        //String name = ident.toString();
        if (!recurse) return curSymbolTable.findSymbol(name);
        SymbolTable symbolTable = curSymbolTable;
        Symbol symbol = symbolTable.findSymbol(name);
        while (symbol == null) {
            // 递归查找
            symbolTable = symbolTable.getPre();
            if (symbolTable == null) return null;
            symbol = symbolTable.findSymbol(name);
        }
        return symbol;
    }

    public int addVarDef(Symbol def,VarSymbol varSymbol) {
        if(findSymbol(def.getIdent().getStr(), false) != null) {
            // 重定义报错
            return -1;
        }
        //symbol=new Symbol(def,type);
        //VarSymbol varSymbol = new VarSymbol();
        varSymbol.setLevel(depth);
        //
        def.setVarInfo(varSymbol);
        curSymbolTable.addSymbol(def);
        return 0;
    }

    public int addFuncDef(Symbol funcDef,FuncSymbol funcSymbol) {
        // 在上一层的符号表加入函数定义
        SymbolTable newSymbolTable = curSymbolTable;
        traceBackFather();
        if(findSymbol(funcDef.getIdent().getStr(), false) != null) {
            // 重定义报错
            return -1;
            //RedefinedException exception = new RedefinedException(funcDef.getIdent().getLine());
            //exception.write();
        }
        else {
            //FuncSymbol funcSymbol = new FuncSymbol();
            funcSymbol.setDepth(depth);
            //Symbol symbol=new Symbol(funcDef,-1);
            curSymbolTable.addSymbol(funcDef);
        }
        curSymbolTable = newSymbolTable;
        depth++;
        return 0;
    }


}
