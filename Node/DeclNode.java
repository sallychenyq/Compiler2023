package Node;

public class DeclNode {
    private final ConstDeclNode constDecl;
    private final VarDeclNode varDecl;
    private int decl;

    public DeclNode(ConstDeclNode constDecl, VarDeclNode varDecl,int decl) {
        this.constDecl = constDecl;
        this.varDecl = varDecl;
        this.decl=decl;
    }

    public ConstDeclNode getConstDecl() {
        return constDecl;
    }
    public VarDeclNode getVarDecl() {
        return varDecl;
    }

    public void print() {
        if (constDecl != null) {
            constDecl.print();
        } else {
            varDecl.print();
        }
        //System.out.println(sym+"+");
    }
    public int constORvar(){
        return decl;
    }
}
