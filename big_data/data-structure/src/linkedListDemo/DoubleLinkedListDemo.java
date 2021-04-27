package linkedListDemo;

public class DoubleLinkedListDemo {

    public static void main(String[] args) {
        HeroNode2 herol = new HeroNode2(1, "宋江", "及时雨");
        HeroNode2 hero5 = new HeroNode2(2, "卢义", "玉麒麟");
        HeroNode2 hero3 = new HeroNode2(3, "吴用", "智多星");
        HeroNode2 hero4 = new HeroNode2(4, "林冲", "豹子头");

        DoubleLinkedList doubleLinkedList = new DoubleLinkedList();

        doubleLinkedList.add(herol);
        doubleLinkedList.add(hero3);
        doubleLinkedList.add(hero4);
        doubleLinkedList.add(hero5);

        doubleLinkedList.list();

        HeroNode2 hero2 = new HeroNode2(2, "卢jun义", "玉麒麟");
        doubleLinkedList.update(hero2);

        doubleLinkedList.list();

        doubleLinkedList.delete(2);

        doubleLinkedList.list();
    }
}
    class HeroNode2 {
        public int no;
        public String name;
        public String nickName;
        public HeroNode2 next;
        public HeroNode2 pre;

        @Override
        public String toString() {
            return "HeroNode{" +
                    "no=" + no +
                    ", name='" + name + '\'' +
                    ", nickName='" + nickName + '\'' +
                    '}';
        }

        public HeroNode2(int no, String name, String nickName) {
            this.name = name;
            this.nickName = nickName;
            this.no = no;


        }
    }

    class DoubleLinkedList{

        private HeroNode2 head=new HeroNode2(0,"","");

        public HeroNode2 getHead() {
            return head;
        }

        public void list(){
            if(head.next==null){
                return;
            }
            HeroNode2 tmp=head;
            while(true){
                if(tmp==null){
                    break;
                }
                System.out.println(tmp);
                tmp=tmp.next;
            }
        }

        public void add(HeroNode2 node){

            HeroNode2 tmp=head;
            while (true){
                if(tmp.next!=null){
                    tmp=tmp.next;
                }else{
                    break;
                }
            }

            tmp.next=node;
            node.pre=tmp;
        }

        public void addByOrder(HeroNode2 heroNode){
            HeroNode2 tmp=head;
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
                heroNode.pre=tmp;
            }
        }

        public void update(HeroNode2 heroNode){
            if(head.next==null){
                return;
            }
            HeroNode2 tmp=head;
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
            HeroNode2 tmp=head.next;

            if(tmp==null){
                return;
            }

            boolean flag=false;
            while (true){

                if(tmp.no==no){
                    flag=true;
                    break;
                }
                tmp=tmp.next;
            }
            if (flag){
                tmp.pre.next=tmp.next;
                if (tmp.next!=null) {
                    tmp.next.pre=tmp.pre;
                }
            }else {
                System.out.println("该节点不存在");
            }
        }
    }


