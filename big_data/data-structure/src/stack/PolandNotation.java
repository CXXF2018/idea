package stack;


import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

//逆波兰表法式完整版
public class PolandNotation {
/**
 * 匹配+、-、*、/、(、)运算符
 */
    public static void main(String[] args) {

        String expression="1+((2+3)*4)-5";

        List<String> infixExpressionList=toInfixExpressionList(expression);

        List<String> suffixExpression=parseSuffixExpressionList(infixExpressionList);

        int res = calculate(suffixExpression);
        System.out.println(res);
    }

    public static List<String> parseSuffixExpressionList(List<String> infixExpressionList) {
        Stack<String> s1 = new Stack<String>();
        List<String> s2 = new ArrayList<String>();
        for (String s : infixExpressionList) {
            if (s.matches("\\d+")){
                s2.add(s);
            }else if(s.equals("(")){
                s1.push("(");
            }else if (s.equals(")")){
                while(s1.peek()!="("){
                    s2.add(s1.pop());
                }
                s1.pop();
            }else {
                if(s1.size()>0&&s1.peek()!="("){
                    while (s1.size()>0&& Operation.getValue(s1.peek())>=Operation.getValue(s)){
                        s2.add(s1.pop());
                    }
                }

                s1.push(s);
            }
        }

        while (s1.size()!=0){
            s2.add(s1.pop());
        }
        System.out.println(s2);
        return s2;
    }

    public static List<String> toInfixExpressionList(String expression) {
        ArrayList<String> strings = new ArrayList<>();

        String str="";
        char c;

        int i=0;
        do {
            if ((c=expression.charAt(i))<48||(c=expression.charAt(i))>57){
                strings.add(c+"");
                i++;
            }else{
                while (i<expression.length()&&((c=expression.charAt(i))>=48&&(c=expression.charAt
                        (i))<=57)){
                    str=str+c;
                    i++;
                }
                strings.add(str);
                str="";
            }
        }while (i<expression.length());
        System.out.println(strings);
        return strings;
    }

    public static List<String> getListString(String suffixExpression){
        String[] split = suffixExpression.split(" ");
        ArrayList<String> list = new ArrayList<>();
        for (String s : split) {
            list.add(s);
        }

        return list;
    }

    public static int calculate(List<String> list){
        Stack<String> stack = new Stack<>();

        for (String s : list) {
            if (s.matches("\\d+")){
                stack.push(s);
            }else {
                int num2=Integer.parseInt(stack.pop());
                int num1=Integer.parseInt(stack.pop());

                int res=0;
                if (s.equals("+")){
                    res=num1+num2;
                }else if (s.equals("-")){
                    res=num1-num2;
                }else if (s.equals("*")){
                    res=num1*num2;
                }else if (s.equals("/")){
                    res=num1/num2;
                }else {
                    throw new RuntimeException("运算符有误");
                }

                stack.push(""+res);
            }
        }

        return Integer.parseInt(stack.pop());
    }
}

class Operation{
    private static int ADD=1;
    private static int SUB=1;
    private static int MUL=2;
    private static int DIV=2;

    public static int getValue(String operation){
        int result=0;
        switch (operation){
            case "+":
                result=ADD;
                break;
            case "-":
                result=SUB;
                break;
            case "*":
                result=MUL;
                break;
            case "/":
                result=DIV;
                break;
            default:
                System.out.println("不存在该运算符");
                break;
        }

        return result;
    }
}
