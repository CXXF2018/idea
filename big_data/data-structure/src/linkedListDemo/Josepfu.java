package linkedListDemo;

public class Josepfu {
    public static void main(String[] args) {

        CircleSingleLinkedList circleSingleLinkedList = new CircleSingleLinkedList();
        circleSingleLinkedList.addBoy(25);
        circleSingleLinkedList.showBoy();

        circleSingleLinkedList.countBoy(4,7,25);
    }
}

class Boy{
    private int no;
    private Boy next;

    public Boy(int no) {
        this.no = no;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public Boy getNext() {
        return next;
    }

    public void setNext(Boy next) {
        this.next = next;
    }
}

class CircleSingleLinkedList{
    private Boy first=new Boy(-1);

    public void addBoy(int nums){
        if(nums<1){
            return;
        }

        Boy curBoy=null;

        for (int i = 1; i <nums ; i++) {
            Boy boy = new Boy(i);
            if (i==1){
                first=boy;
                first.setNext(first);
                curBoy=first;
                }else{
                curBoy.setNext(boy);
                boy.setNext(first);
                curBoy=boy;
            }
        }
    }

    public void showBoy(){
        if (first==null){
            System.out.println("没有小孩");
            return;
        }
        Boy curBoy=first;
        while(true){
            System.out.printf("小孩的编号%d\n",curBoy.getNo());
            if (curBoy.getNext()==first){
                break;
            }
            curBoy=curBoy.getNext();
        }
    }

    public void countBoy(int startNo,int countNum,int num){
        if (startNo<1||startNo>num||first==null){
            return;
        }

        Boy helper=first;
        //指向环形链表的最后一个节点，也是头结点的前一个节点
        while (true){
            if (helper.getNext()==first){
                break;
            }
            helper=helper.getNext();
        }

        //到达技术起点
        for (int i = 0; i <startNo-1 ; i++) {
            first=first.getNext();
            helper=helper.getNext();
        }

        int con=0;
        while(true){
            if (first==helper){
                break;
            }

            for (int i = 0; i < countNum-1; i++) {
                first=first.getNext();
                helper=helper.getNext();
            }
            System.out.printf("%d小朋友出圈\n",first.getNo());
            first=first.getNext();
            helper.setNext(first);
            con++;
        }

        System.out.printf("最后还剩下%d号小朋友",first.getNo());
        System.out.println(con);
    }
}
