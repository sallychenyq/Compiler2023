package Symbol;

import java.util.ArrayList;
import java.util.HashMap;

public class SymbolTable {

        private static SymbolTable instance;
        private SymbolTable pre;
        private int curNext;
        public HashMap<String, Symbol> table;

        private ArrayList<SymbolTable> nexts;
        private int depth;
        public HashMap<String, SymbolTable> directory = new HashMap<>();
        //public static ArrayList<SymbolManager> Symbols = new ArrayList<>();
        //public int id;
        //public int fatherId; 	// 当前符号表的父id。
        //pre指针
        public int listid;        // 当前单词对应的poi。
        //ident,return,const值,"&num",for,break,continue,
        //public int tableId; 	// 当前单词所在的符号表编号。
    /*采用类似链表的数据结构维护符号表，结构示意如下图。创建一个新的类SubProTable，维护一个列表用于栈式子程序一层的符号，
    维护一个指向上一层子程序符号表的引用，维护一个列表用于存储其子程序符号表的引用。创建类SymbolTable用于维护符号表，
    在其中维护一个指向全局符号表的引用和一个指向当前子程序符号表的引用，每当进入新的子程序，则创建新的SubProTable对象并加入到链表中，
    子程序解析结束则回退到父程序。这样各层的符号表都得以保存。*/

        // 数组的维数：a[dim1][dim2]   dim1 dim2
        // 变量的值：val，寄存器：reg
        // func：
        // 	retype		// 0 -> int, 1 -> void
        //	paramNum	// 参数数量
        // 	paramTypeList	// 参数类型
        //private static SymbolManager symbols = new SymbolManager();//= new ArrayList<>();
        //public static SymbolManager Symbols=symbols;

        public SymbolTable(int depth) {
                this.table = new HashMap<>();
                this.nexts = new ArrayList<>();
                this.depth = depth;
                //this.curNext = 0;

        }

        public HashMap<String,Symbol> getTable() {
                //if(instance == null) instance = ;
                return table;
        }
        public void setTable(HashMap table){
                this.table=table;
        }public int getCurNext() {
                return curNext;
        }

        public void addSymbol(Symbol item) {
                /*for (String key: Symbols.directory.keySet()) {//int i = 0; i < Symbols.directory.size(); i++
                        if (key.equals(item.token))//&&Symbols.directory.get(i).tableId==item.tableId)
                                return true;//Error.addError(new Error(wordList.get(index - 2).getLineNum(), 'b'));

                }
                Symbols.directory.put(item.token,item);//new SymbolTableItem(func, name, type);
                num++;
                System.out.println(Symbols.id);*/
                this.table.put(item.getIdent().getStr(), item);
        }

        public void setPre(SymbolTable pre) {
                this.pre = pre;
        }

        public void addNext(SymbolTable next) {
                this.nexts.add(next);
        }

        public SymbolTable getPre() {
                return pre;
        }

        public Symbol findSymbol(String name) {
                if (this.table.get(name)!=null) return table.get(name);
                return null;
        }

        public void print() {
                for (Symbol value : this.table.values()) {//int i = 0; i < Symbols.directory.size(); i++) {
                        //System.out.println(Symbols.directory.values().toString().replace("[", "").replaceAll("]", ""));
                        System.out.println(value.token + " " + value.level);
                }
        }
        @Override
        public String toString() {
                StringBuilder sb = new StringBuilder();
                for (Symbol value : table.values()) {
//                        HashMap<String, Value> layer = table.get(i);
//                        for (Map.Entry<String, Value> entry : layer.entrySet())
//                        {
//                                sb.append(entry.getKey()).append(": ").append(entry.getValue()).append(", ");
//                        }
                        sb.append(value.token).append(" ").append(value.level);
                }
                return sb.toString();
        }
}
