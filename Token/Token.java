package Token;

public class Token {

    private String str;
    private String type;
    private int lineNum;
    public Token(String type, String str, int lineNum) {
        this.type = type;
        this.str = str;
        this.lineNum = lineNum;
    }
    @Override
    public String toString() {
        return this.type + " " + this.str+" "+this.lineNum;
    }

    public String getType() {
        return type;
    }

    public int getLineNum() {
        return lineNum;
    }

    public String getStr() {
        return str;
    }

}
