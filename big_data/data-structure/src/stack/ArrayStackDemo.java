package stack;

import java.util.Scanner;

public class ArrayStackDemo {
    public static void main(String[] args) {

        ArrayStack arrayStack = new ArrayStack(10);
        String key="";
        Scanner scanner = new Scanner(System.in);
        boolean loop=true;
        while(loop){
            System. out. println("show:表示显示栈");
            System.out.println("exit:退出程序");
            System.out. println("push:表示添加数据到栈(入栈)");
            System.out. println("pop:表示从栈取出数据(出栈)");
            System.out.println("请输入你的选择");
            key=scanner.next();
            switch (key){
                case "show":
                    arrayStack.list();
                    break;
                case "push":
                    System.out.println("请输入- - 个数");
                    int value = scanner.nextInt();
                    arrayStack.push(value);
                    break;
                case "pop":
                    try {
                        int res = arrayStack.pop();
                        System.out. printf("出栈的数据是%d\n", res);
                    } catch (Exception e) {
// TODO: handle exception
                        System.out.println(e.getMessage());
                        break;
                    }
                case "exit":
                    scanner. close();
                    loop = false;
                    break;
                default:
                    break;
            }
        }
        System.out.println("程序退出~~");
    }
}

class ArrayStack{
    private int maxSize;
    private int[] stack;
    private int top=-1;

    public ArrayStack(int maxSize) {
        this.maxSize = maxSize;
        stack=new int[maxSize];
    }

    public boolean isFull(){
        return top==maxSize-1;
    }

    public boolean isEmpty(){
        return top==-1;
    }

    public void push(int value){
        if (isFull()){
            System.out.println("isFull");
            return;
        }
        top++;
        stack[top]=value;
    }

    public int pop(){
        if (isEmpty()){
            throw new RuntimeException("isEmpty");
        }
        int value=stack[top];
        top--;
        return value;
    }

    public void list(){
        if (isEmpty()){
            System.out.println("isEmpty");
            return;
        }

        for (int i = top; i >=0 ; i--){
            System.out.printf("stack[%d]=%d\n",i,stack[i]);
        }
    }
}


