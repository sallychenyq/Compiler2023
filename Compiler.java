//import Lexer;
import Node.CompUnitNode;
import Token.Token;

import java.util.ArrayList;
public class Compiler {
    //public void func(){
    //}

    public static void main(String[] args) {
        Lexer a = new Lexer();//Compiler test = new Compiler();
        a.next();
        ArrayList<Token> lexical_list =a.getTokens();
        Parser b = new Parser(lexical_list);
        b.analyze();
        //test.func();Lexer.next();
        //Lexer.getInstance();//Lexer.getInstance().next();//
       //System.out.println(lexical_list);
        CompUnitNode ast=b.getAst();
        IRMidCodeGen c = new IRMidCodeGen(ast);
        c.visitCompUnit();
        ErrorHandler.printe();
    }

}

