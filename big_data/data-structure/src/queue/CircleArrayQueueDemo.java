package queue;

import java.util.Scanner;

public class CircleArrayQueueDemo {

        public static void main(String[] args) {

            queue.ArrayQueue arrayQueue = new queue.ArrayQueue(3);

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
    class CircleArrayQueue{

        private int maxSize;
        private int front;
        private int rear;
        private int[] array;

        public CircleArrayQueue(int arrMaxSize) {
            maxSize=arrMaxSize;
            array=new int[maxSize];
            front=-1;//只想队列头部的前一个位置
            rear=-1;//指向队列尾的具体数据
        }

        public boolean isFull(){
            return (rear+1)%maxSize==front;
        }

        public boolean isEmpty(){
            return rear==front;
        }

        public void addQueue(int n){
            if (isFull()){
                System.out.println("full");
                return;
            }
            rear=(rear+1)%maxSize;
            array[rear]=n;
        }

        public int getQueue(){
            if(isEmpty()){
                throw new RuntimeException("empty");
            }
            int value=array[front];
            front=(front+1)%maxSize;
            return value;
        }

        public void showQueue(){
            if (isEmpty()) {
                return;
            }
            for (int i = front; i < front+(rear-front+maxSize)%maxSize; i++) {
                System.out.printf("arr[%d]=%d\n",i%maxSize,array[i%maxSize]);
            }
        }

        //显示队列的头数据
        public int headQueue(){
            if (isEmpty()){
                throw new RuntimeException("empty");
            }
            int value=array[front];
            front=(front+1)%maxSize;
            return value;
        }
    }

