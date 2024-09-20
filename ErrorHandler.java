import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;

public class ErrorHandler {
    private  int lineNum;
    private  char exceptionType;
    static PrintWriter pw;
    private static ArrayList<ErrorHandler> errors = new ArrayList<>();

    public ErrorHandler(int lineNum, char exceptionType) {
        this.lineNum = lineNum;
        this.exceptionType = exceptionType;
    }
    public static void addError(ErrorHandler e) {
        errors.add(e);

    }

    public String toString() {
        return lineNum + " " + exceptionType;
    }
    public int getLineNum(){
        return lineNum;
    }
    public static void printe() {
        Comparator<ErrorHandler> nameComparator = Comparator.comparing(ErrorHandler::getLineNum);

        // 使用Collections工具类对List进行排序
        errors.sort(nameComparator);

        try {
            //fw=new FileWriter("./llvm_ir.txt",true);
            pw = new PrintWriter("./error.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < errors.size(); i++) {
            pw.println(errors.get(i).toString().replace("[", "").replaceAll("]", ""));pw.flush();
        }
    }
}
