package linkedListDemo;

import java.util.Stack;

public class SingleLinkedListDemo {
    public static void main(String[] args) {

        HeroNode herol = new HeroNode(1, "宋江", "及时雨");
        HeroNode hero5 = new HeroNode(2, "卢义", "玉麒麟");
        HeroNode hero3 = new HeroNode(3, "吴用", "智多星");
        HeroNode hero4 = new HeroNode(4, "林冲", "豹子头");

        SingleLinkedList singleLinkedList = new SingleLinkedList();
        singleLinkedList.add(herol);
        singleLinkedList.add(hero5);
        singleLinkedList.add(hero3);
        singleLinkedList.add(hero4);

        HeroNode hero2 = new HeroNode(2, "卢俊义", "玉麒麟");
        singleLinkedList.update(hero2);

        singleLinkedList.delete(4);

        singleLinkedList.list();

        int length = singleLinkedList.getLength(singleLinkedList.getHead());
        System.out.println(length);

        HeroNode heroNode = singleLinkedList.finLastINdexNode(singleLinkedList.getHead(), 2);
        System.out.println(heroNode);


        singleLinkedList.reverseList(singleLinkedList.getHead());
        singleLinkedList.list();

        singleLinkedList.reversePrint(singleLinkedList.getHead());
    }
}

class HeroNode {
    public int no;
    public String name;
    public String nickName;
    public HeroNode next;

    @Override
    public String toString() {
        return "HeroNode{" +
                "no=" + no +
                ", name='" + name + '\'' +
                ", nickName='" + nickName + '\'' +
                '}';
    }

    public HeroNode(int no, String name, String nickName) {
        this.name = name;
        this.nickName = nickName;
        this.no = no;


    }
}
    //定义list
    class SingleLinkedList{

        //头节点，不存放具体数据
        private HeroNode head=new HeroNode(0,"","");

        public HeroNode getHead() {
            return head;
        }

        //添加节点到单向链表
        public void add(HeroNode node){

            HeroNode tmp=head;
            while (true){
                if(tmp.next!=null){
                    tmp=tmp.next;
                }else{
                    break;
                }
            }

            tmp.next=node;
        }

        public void list(){
            if(head.next==null){
                return;
            }
            HeroNode tmp=head;
            while(true){
                if(tmp==null){
                    break;
                }
                System.out.println(tmp);
                tmp=tmp.next;
            }
        }

        public void addByOrder(HeroNode heroNode){
            HeroNode tmp=head;
            boolean flag=false;
            while (true){
                if (tmp.next==null){
                    break;
                }
                if(tmp.next.no>heroNode.no){
                    break;
                }
                else if(tmp.next.no==heroNode.no){
                    flag=true;
                }
                tmp=tmp.next;
            }
            if (flag){
                System.out.println("该节点已经存在");
            }else {
                heroNode.next=tmp.next;
                tmp.next=heroNode;
            }
        }

        public void update(HeroNode heroNode){
            if(head.next==null){
                return;
            }
            HeroNode tmp=head;
            boolean flag=false;
            while(true){
                if(tmp==null){
                    break;
                }
                if(tmp.no==heroNode.no){
                    flag=true;
                    break;
                }

                tmp=tmp.next;

            }
            if(flag){
                tmp.name=heroNode.name;
            }
            else{
                System.out.println("不存在该节点");
            }
        }

        public void delete(int no){
            HeroNode tmp=head;
            boolean flag=false;
            while (true){
                if(tmp.next==null){
                    return;
                }
                if(tmp.next.no==no){
                    flag=true;
                    break;
                }
                tmp=tmp.next;
            }
            if (flag){
                tmp.next=tmp.next.next;
            }else {
                System.out.println("该节点不存在");
            }
        }

        public int getLength(HeroNode head){
            if (head.next==null){
                return 0;
            }

            int length=0;
            HeroNode cur=head.next;
            while(cur!=null){
                cur=cur.next;
                length++;
            }

            return length;
        }

        //返回倒数第k个节点

        public HeroNode finLastINdexNode(HeroNode head,int index){
            if (head.next == null) {
                return null;
            }

            int size=getLength(head);
            if(index<0||index>size){
                return null;
            }

            HeroNode cur=head.next;
            for (int i = 0; i < size-index; i++) {
                cur=cur.next;
            }

            return cur;
        }

        public void reverseList(HeroNode head){
            if (head.next==null||head.next.next==null){
                return;
            }

            HeroNode cur=head.next;
            HeroNode curNext=null;

            HeroNode reverseHead = new HeroNode(0, "", "");

            while(cur!=null){
                curNext=cur.next;
                cur.next=reverseHead.next;
                reverseHead.next=cur;
                cur=curNext;
            }

            head.next=reverseHead.next;
        }

        public void reversePrint(HeroNode head){
            if(head.next==null){
                return;
            }
            Stack<HeroNode> nodeStack = new Stack<>();
            HeroNode cur =head.next;
            while(cur!=null){
                nodeStack.push(cur);
                cur=cur.next;
            }
            while(nodeStack.size()>0){
                System.out.println(nodeStack.pop());
            }
        }

        class Combine{
            public void combineTwoList(HeroNode head1,HeroNode head2){
                if(head1.next==null||head2.next==null){
                    return;
                }
                HeroNode cur1=head1.next;
                HeroNode cur2=head2.next;

                HeroNode newHead = new HeroNode(0, "", "");
                HeroNode cur=newHead;
                while(cur1!=null&&cur2!=null){
                    if (cur1.no<=cur2.no){
                        cur.next=cur1;
                        cur=cur.next;
                        cur1=cur1.next;
                    }else if(cur1.no>cur2.no){
                        cur.next=cur2;
                        cur=cur.next;
                        cur2=cur2.next;
                    }
                }
                if(cur1!=null){
                    cur.next=cur1;
                }else if(cur2!=null){
                    cur.next=cur2;
                }

                head=newHead;
            }
        }
    }
