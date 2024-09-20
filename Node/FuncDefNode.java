package Node;

import Symbol.FuncSymbol;
import Token.Token;
import com.sun.security.auth.NTNumericCredential;

import java.net.ContentHandler;
import java.util.ArrayList;

public class FuncDefNode {
    private FuncFParamsNode funcFParamsNode;
    private BlockNode blockNode;
    private FuncSymbol funcSymbol;
    private Token ident;
    private String functype;

    public FuncDefNode(FuncFParamsNode funcFParamsNode, BlockNode blockNode, Token ident, String functype){
        this.blockNode=blockNode;
        this.funcFParamsNode=funcFParamsNode;
        this.ident=ident;
        this.functype=functype;
    }
    public void print(){
        if (funcFParamsNode != null) {
            funcFParamsNode.print();
        }
        blockNode.print();
       // System.out.println("<FuncDef>");
    }

    public Token getIdent() {
        return ident;
    }

    public String getFuncType() {
        return functype;
    }

    public FuncFParamsNode getFuncFParamsNode() {
        return funcFParamsNode;
    }

    public BlockNode getBlockNode() {
        return blockNode;
    }
    public FuncSymbol getFuncInfo(){
        return funcSymbol;
    }

    public void setFuncInfo(FuncSymbol funcSymbol){
        this.funcSymbol=funcSymbol;
    }
}
