import Token.Token;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintStream;

import java.util.ArrayList;

public class Lexer {

    public char ch;//int ch;
    public int curPos;
    public int number;//
    //保留字状态码
    public String lexType;//解析单词类型,
    public int lineNum;//当前行号,
    public static ArrayList<Integer> forstmtcount=new ArrayList<>();
    int num;
    //public String line;
    public String source=new String();
    public String getToken = "";//StringBuffer getToken = new StringBuffer();存放构成单词符号的字符串
    public String[] retainWord = {"int","if","else","return","main","void","break","continue","const","for","getint","printf"};//保留字表,
    //保留字
    public String[] operator = {"\"","!","&&","||","+", "-", "*", "/", "%",">", ">=", "<", "<=", "==","!=","(", ")", "=",";", "," , "[","]","{","}"};

    //public static void main(String[] args) {
    //    Compiler wordAnalysis = new Compiler();
    //    wordAnalysis.next();
    //}
    /*public ArrayList<Token.Token> analyze() {//throws Exception
        ArrayList<Token.Token> list = new ArrayList<>();

            for (Integer i = 0; ; i++) {
        String line =  br.readLine();
        lineNum = i+1;
        list.addAll(next()); //添加行数
    }
        return list;
    }*/
    private final ArrayList<Token> tokens = new ArrayList<>();
    public ArrayList<Token> getTokens() {
        return tokens;
    }
    //判断是否是字母
    public boolean isLetter(){
        return (ch >= 'a' && ch <= 'z') || ch == '_' || (ch >= 'A' && ch <= 'Z');
    }

    //判断是否是数字
    public boolean isDigit(){
        return ch >= '0' && ch <= '9';//Character.isDigit();
    }

       // return (isInt(str) || isHexadecimal(str));
    /*public static boolean isHexadecimal(String str){
    if(str.length() < 2) { return false; }
    if(str.charAt(0) == '0'){
        if(str.charAt(1) == 'x' || str.charAt(1) == 'X'){
            for(int i = 2; i < str.length(); i++){
                if(!((str.charAt(i) >= '0' && str.charAt(i) <= '9') || (str.charAt(i) >= 'a' && str.charAt(i) <= 'f') || (str.charAt(i) >= 'A' && str.charAt(i) <= 'F'))){
                    return false;
                }
            }
            return true;
        }
    }
    return false;
}*/

    //判断是否是分界符
    public boolean isOperator() {
        for(int i=0; i<operator.length; i++) {
            if(Character.toString(ch).equals(operator[i].substring(0,1))) {
                return true;
            }
        }
        return false;
    }
    public void error(){
        //if(!isLetter() && !isDigit()&& !isOperator()&&ch!=' ')
        System.out.println(lineNum+" a");
    }

    //判断返回值
    public int Reserve(){
        //判断是否为保留字，是的话返回3
        for(int i = 0; i < retainWord.length; i++){
            if(getToken.equals(retainWord[i])){

                return 3;
            }
        }
        //判断是否为数字，是的话返回2
        if(getToken.length() != 0){
            if(getToken.charAt(0)>='0' && getToken.charAt(0)<='9'){
                return 2;
            }
        }
        /*else if(isHexadecimal(sym)){
            pw.println("HEXCON "+sym);
            pw.flush();
        }*/
        //判断是否为字母(变量)，是的话返回1
        if(getToken.length() != 0) {
            if((getToken.charAt(0)>='a' && getToken.charAt(0)<='z')||getToken.charAt(0)=='_'|| (getToken.charAt(0) >= 'A' && getToken.charAt(0) <= 'Z')) {
                //System.out.println(getToken.charAt(0)+"?");//"letter"
                return 1;
            }
        }

        //存在错误信息，打印输出
        //if(isLetter()==false && isDigit()==false && notBC() && isOperator()==false) {
        //    System.out.println("存在错误信息!");
        //}
        return 0;
    }

    //书写格式
    public void getLexType(){
        if (getToken.equals("int")) {
            lexType = "INTTK";//System.out.println(", "+getToken);
            //String get=getToken.toString().toLowerCase();
            //getToken.delete(0,getToken.length()); getToken.append("int");
        }
        else if (getToken.equals("if")) lexType = "IFTK";
        else if (getToken.equals("else")) lexType = "ELSETK";
        else if (getToken.equals("return")) lexType = "RETURNTK";
        else if (getToken.equals("main")) lexType = "MAINTK";
        else if (getToken.equals("void")) lexType = "VOIDTK";
        else if (getToken.equals("break")) lexType = "BREAKTK";
        else if (getToken.equals("continue")) lexType = "CONTINUETK";
        else if (getToken.equals("const")) lexType = "CONSTTK";
        else if (getToken.equals("for")) lexType = "FORTK";
        else if (getToken.equals("getint")) lexType = "GETINTTK";
        else if (getToken.equals("printf")) lexType = "PRINTFTK";

        else if (getToken.length() != 0&&getToken.charAt(0)=='"') lexType = "STRCON";//Reserve()==5
        else if (getToken.equals("!")) lexType = "NOT";
        else if (getToken.equals("+")) lexType = "PLUS";
        else if (getToken.equals("-")) lexType = "MINU";
        else if (getToken.equals("*")) lexType = "MULT";
        else if (getToken.equals("/")) lexType = "DIV";
        else if (getToken.equals("%")) lexType = "MOD";
        else if (getToken.equals("<")) lexType = "LSS";
        else if (getToken.equals(">")) lexType = "GRE";
        else if(getToken.equals("=")) lexType = "ASSIGN";
        else if (getToken.equals(";")) lexType = "SEMICN";
        else if (getToken.equals(",")) lexType = "COMMA";
        else if (getToken.equals("{")) lexType = "LBRACE";
        else if (getToken.equals("}")) lexType = "RBRACE";
        else if (getToken.equals("(")) lexType = "LPARENT";
        else if (getToken.equals(")")) lexType = "RPARENT";
        else if (getToken.equals("[")) lexType = "LBRACK";
        else if (getToken.equals("]")) lexType = "RBRACK";

        else if (getToken.equals("&&")) lexType = "AND";
        else if (getToken.equals("||")) lexType = "OR";
        else if (getToken.equals("<=")) lexType = "LEQ";
        else if (getToken.equals(">=")) lexType = "GEQ";
        else if (getToken.equals("==")) lexType = "EQL";
        else if (getToken.equals("!=")) lexType = "NEQ";

        else if(Reserve()==2){//getToken.charAt(0)>='0' && getToken.charAt(0)<='9'
            lexType = "INTCON";//System.out.println("INTCON "+getToken);
            number = Integer.parseInt(getToken.toString());
            //System.out.println(number);
        }else if(Reserve()==1){//(getToken.charAt(0)>='a' && getToken.charAt(0)<='z')||getToken.charAt(0)=='_'){//
            lexType = "IDENFR";//System.out.println("IDENFR "+getToken);
            //Symbol.Symbol.addSymbol(new SymbolTableItem(retA, getToken, type));
        }
        if (getToken.length()!=0&&!getToken.equals(" ")){
            ////System.out.println(lexType+" "+getToken);
            tokens.add(new Token(lexType, getToken,lineNum ));

        }
        //System.out.println(getToken);lexType.length()!=0&&
        getToken="";//delete(0, getToken.length());
    }

    //读取文件并进行处理输出
    public void next(){
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader("./testfile.txt"));//D:\IdeaProjects\SysY-compiler
            PrintStream ps = new PrintStream("./output.txt");//可能会出现异常，直接throws就行了
            System.setOut(ps);
            //line=br.readLine();String line = null;
            //while ((line = br.readLine()) != null) {
                // buffer.append(line);
              //  source += line;
            //}
            //int count=0;
            lineNum=1;
            int read=0;
            while((read=br.read()) != -1){
                ch=(char)read;//System.out.println((char)ch+"!?"+getToken);
                if (ch=='\n') {//||(ch=='\r'&&br.read()=='\n')
                    lineNum++;//||||
                }
                else if(isLetter()){
                    if((Reserve() == 1)||Reserve()==3){//while(isDigit()||isLetter()){
                        //getToken.delete(0, getToken.length());
                        getToken+=ch;
                        //ch= br.read();(getToken.length() == 0)||
                    }else {
                        getLexType();
                        getToken+=(char)ch;//.append((char)ch);
                        //System.out.println((char)ch+"?!?!"+getToken);
                    }

                }else if(isDigit()){

                    if(Reserve()==2||Reserve()==1||Reserve()==3) {//!isDigit()||Reserve()==3
                        getToken+=ch;//.append((char)ch);
                        //System.out.println((char)ch+"!"+getToken+"?");//}
                    }else {
                        getLexType();
                        getToken+=ch;//.append((char)ch);
                        number = Integer.parseInt(getToken.toString());
                    }//valueOf

                }else if(isOperator()){
                    if((ch=='"')){//!isOperator()&& Reserve()!=4
                        getLexType();
                        getToken+=ch;//.append((char)ch);
                        ch=(char)br.read();
                        forstmtcount.add(0);//error();
                        while (ch!='"'){
                            //for (int i = 1; i < str.length()-1; i++) {
                                //char c = str.charAt(i);
                                if (getToken.charAt(getToken.length() - 1) == '%') {
                                    //if ((i + 1) >= str.length()) {
                                    //   Error.addError(new Error(lineNum, 'a'));
                                    if (ch != 'd') {
                                        ErrorHandler.addError(new ErrorHandler(lineNum, 'a'));

                                    }else forstmtcount.set(num,forstmtcount.get(num)+1);
                                }
                            else if (getToken.charAt(getToken.length() - 1) == '\\') {
                                //if ((i + 1) >= str.length()) {
                                //   Error.addError(new Error(lineNum, 'a'));
                                if (ch != 'n') {
                                    ErrorHandler.addError(new ErrorHandler(lineNum, 'a'));
                                }
                            }/*else if (getToken.charAt(str.length() - 1) == '/') {
                                    if ((i + 1) >= str.length()) {
                                        Error.addError(new Error(lineNum, 'a'));
                                    } else if (str.charAt(i + 1) != 'n') {
                                        Error.addError(new Error(lineNum, 'a'));
                                    }
                                } */
                                else if ((ch < 32) || ((ch > 33) && (ch < 37))||((ch > 37) && (ch < 40)) || (ch > 126)) {
                                    ErrorHandler.addError(new ErrorHandler(lineNum, 'a'));
                                }
                            //}
                            getToken+=ch;//append((char)ch);
                            ch=(char)br.read();
                            //error();
                        }if(getToken.charAt(getToken.length() - 1) == '\\') ErrorHandler.addError(new ErrorHandler(lineNum, 'a'));
                        getToken+=ch;//append((char)ch);
                        num++;
                    } else if (ch == '/') {
                        //System.out.println((char)ch+"!"+getToken+"?");
                        //getToken.append((char) ch);
                        getLexType();
                        getToken="";//getToken.delete(0, getToken.length());
                        getToken+=ch;//append((char) ch);
                        //System.out.println((char)ch+"!!"+getToken);
                        ch=(char)br.read();// 1/ /*//***/ /*200
                        if (ch=='/'){
                            getToken="";//getToken.delete(0, getToken.length());
                            //System.out.println((char)ch+"!"+getToken);//getToken.append((char)ch);
                            ch=(char)br.read();
                            while (ch!='\n'){//ch!='\r'
                                //getToken.append((char)ch);
                                ch=(char)br.read();
                            }
                            lineNum++;
                            //getToken.append((char)ch);
                        } else if(ch=='*'){
                            getToken="";//getToken.delete(0, getToken.length());
                            ch=(char) br.read();//;
                            while((ch)!=-1){//int cha=br.read();?????????
                                while (ch!='*'){/*999*,**///*/
                                    if (ch=='\n') lineNum++;//br.read()!='/'||
                                    //System.out.println((char)ch+"!"+getToken+"~"+(char)cha);
                                    ch=(char)br.read();
                                    getToken="";//getToken.delete(0, getToken.length());
                                }/* 88**8/ **/      /* * /  */        /* **/ /* *//* */
                                while (ch=='*') {//&&ch!='*'
                                    ch=(char)br.read();
                                    getToken="";//getToken.delete(0, getToken.length());
                                }
                                //System.out.println((char)ch+"!?"+getToken);
                                if (ch=='/'){
                                    //ch=br.read();
                                    getLexType();
                                    break;//return next();//getToken.append((char) ch);
                                }//ch=br.read();
                            }
                            //ch = br.read();
                            /***1*/  /**/ /*/*/
                        }else {// chufa
                            getLexType();
                            getToken+=ch;//.append((char) ch);
                        }

                    } else  if (ch=='&') {
                        getLexType();
                        getToken+=ch;//.append((char) ch);
                        ch=(char)br.read();
                        //getToken.delete(0, getToken.length());//
                        getToken+=ch;//.append((char) ch);
                        if (getToken.equals("&&")) {
                            //System.out.println((char)ch+"!?!"+getToken);
                            //getToken.append((char) ch);
                            getLexType();
                            //System.out.println((char)ch+"!?"+getToken);
                        }
                    } else if (ch == '|') {
                        getLexType();
                        getToken+=ch;//.append((char) ch);
                        ch=(char)br.read();
                        getToken+=ch;//.append((char) ch);
                        if (getToken.equals("||")) {
                            getLexType();
                        }
                    } else if (ch == '='||ch=='<'||ch=='>'||ch=='!') {
                        getLexType();
                        getToken+=ch;//.append((char) ch);
                        ch = (char)br.read();
                        if ((ch) == '=') {
                            getToken+=ch;//.append((char) ch);
                            getLexType();
                        }else if(ch=='\r'||ch=='\n'){
                            ch=(char) br.read();
                            getLexType();
                           lineNum++;
                            // while(ch=='\r'||ch=='\n'){
                             //   ch=(char)br.read();
                            //}
                        }
                        else {
                            //System.out.println((char)ch+"!?"+getToken);
                            getLexType();
                            getToken+=ch;//.append((char) ch);
                        }
                    }else{
                        //System.out.println((char)ch+"!!"+getToken);
                        getLexType();
                        getToken+=ch;//.append((char) ch);
                    }
                }else if (ch!='\r'&&ch!=' '&&ch!='\t') System.out.println(lineNum+" a");//"?");//ch=br.read();
                else {
                    getLexType();//System.out.println(lineNum+"?"+(char)ch);
                }
                //System.out.println((char)ch+"!"+getToken);//}else if(getToken.toString().equals("}")){
            }
            getLexType();
            getToken+=ch;//.append((char)ch);
        } catch (Exception e) {//IO
            e.printStackTrace();
        }
    }
}

