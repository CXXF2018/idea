package queue;

import java.util.Scanner;

public class ArrayQueueDemo {
    public static void main(String[] args) {

        ArrayQueue arrayQueue = new ArrayQueue(3);

        char key=' ';//接受用户输入
        Scanner scanner = new Scanner(System.in);
        boolean loop=true;
        while (loop){
            key=scanner.next().charAt(0);
            switch (key){
                case 's':
                    arrayQueue.showQueue();
                    break;
                case 'a':
                    arrayQueue.addQueue(scanner.nextInt());
                    break;
                case 'f':
                    scanner.close();
                    loop=false;
                    default:
                        break;
            }
        }



    }
}
    class ArrayQueue{

        private int maxSize;
        private int front;
        private int rear;
        private int[] array;

        public ArrayQueue(int arrMaxSize) {
            maxSize=arrMaxSize;
            array=new int[maxSize];
            front=-1;//只想队列头部的前一个位置
            rear=-1;//指向队列尾的具体数据
        }

        public boolean isFull(){
                return rear==maxSize-1;
        }

        public boolean isEmpty(){
                return rear==front;
        }

        public void addQueue(int n){
            if (isFull()){
                System.out.println("full");
                return;
            }
            rear++;
            array[rear]=n;
        }

        public int getQueue(){
            if(isEmpty()){
                throw new RuntimeException("empty");
            }
            return array[++front];
        }

        public void showQueue(){
            if (isEmpty()) {
                return;
            }
            for (int i = 0; i < array.length; i++) {
                System.out.println(array[i]);

            }
        }

        //显示队列的头数据
        public int headQueue(){
            if (isEmpty()){
                throw new RuntimeException("empty");
            }
            return array[++front];
        }
    }

