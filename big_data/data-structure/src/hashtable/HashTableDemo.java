package hashtable;

import java.util.Scanner;

public class HashTableDemo {

    public static void main(String[] args) {

        HashTable hashTable = new HashTable(7);

        //写一个简单的菜单
        String key = "";
        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.println("add:添加雇 员");
            System.out. println("list:显示雇员");
            System.out.println("find:查找雇员");
            System.out.println("exit:退出系统");

            key = scanner.next();
            switch (key){
                case "add":
                    System.out.println("输入ID");
                    int id = scanner.nextInt();
                    System.out.println("输入名字");
                    String name=scanner.next();

                    Emp emp = new Emp(id, name);
                    hashTable.add(emp);
                    break;
                case "list":
                    hashTable.list();
                    break;
                case "find":
                    System.out.println("请输入你要查找的id");
                    id=scanner.nextInt();
                    hashTable.findEmpById(id);
                    break;
                case "exit":
                    System.out.println("退出系统");
                    scanner.close();
                    System.exit(0);
                    default:
                        break;
            }
        }
    }
}

class Emp{
    public int id;
    public String name;
    public Emp next;

    public Emp(int id, String name) {
        this.id = id;
        this.name = name;
    }
}

class HashTable{
    private EmpLinkedList[] empLinkedListArray;
    private int size;//链表条数

    public HashTable(int size) {
        this.size = size;
        empLinkedListArray=new EmpLinkedList[size];
        for (int i = 0; i < size; i++) {
            empLinkedListArray[i]=new EmpLinkedList();
        }
    }

    public void add(Emp emp) {
        int empLinkedListNo = hashFun(emp.id);
        empLinkedListArray[empLinkedListNo].add(emp);
    }

    public void list() {
        for (int i = 0; i < size; i++) {
            empLinkedListArray[i].list(i);
        }
    }

    public void findEmpById(int id) {
        int empLinkedListNo = hashFun(id);
        Emp emp = empLinkedListArray[empLinkedListNo].findEmpById(id);
        if (emp==null){
            System.out.println("不存在该员工");
        }else {
            System.out.println("在第" + empLinkedListNo + "条链表中的雇员" + id);
        }
    }

    public int hashFun(int id){
        return id%size;
    }
}

class EmpLinkedList{

    private Emp head;

    public void add(Emp emp) {

        if (head==null){
            head=emp;
            return;
        }

        Emp curEmp=head;
        while (curEmp.next!=null){
            curEmp=curEmp.next;
        }
        curEmp.next=emp;
    }

    public void list(int no) {
        if (head==null){
            System.out.println("第" + no + 1 + "条链表为空");
            return;
        }
        System.out.println("第" + no + 1 + "条链表的信息为：");
        Emp curEmp=head;
        while (curEmp!=null){
            System.out.printf("=>id=%d name=%s\t",curEmp.id,curEmp.name);
            curEmp=curEmp.next;
        }
        System.out.println();
    }

    public Emp findEmpById(int id) {
        if (head==null){
            return null;
        }
        Emp curEmp=head;
        while (curEmp!=null){
            if (curEmp.id==id){
                break;
            }
            curEmp=curEmp.next;
        }

        return curEmp;
    }
}

