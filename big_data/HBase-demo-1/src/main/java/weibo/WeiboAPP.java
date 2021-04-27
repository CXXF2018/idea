package weibo;

import weibo.controller.WeiboController;

import java.io.IOException;
import java.util.List;

public class WeiboAPP {

    public static WeiboController controller=new WeiboController();

    public static void main(String[] args) throws IOException {

        //初始化微博
        //controller.init();

        //发微博
        controller.publish("buwenbuhuo","Happy 1");
        controller.publish("buwenbuhuo","Happy 2");
        controller.publish("buwenbuhuo","Happy 3");
        controller.publish("buwenbuhuo","Happy 4");
        controller.publish("buwenbuhuo","Happy 5");

       // 关注微博
        controller.follow("1002","buwenbuhuo");
        controller.follow("1003","buwenbuhuo");


         //获取微博内容
         //最新的消息(获取)
       List<String> allRecentWeibos = controller.getAllRecentWeibos("1002");

         //查看数据
        for (String allRecentWeibo : allRecentWeibos) {
            System.out.println(allRecentWeibo);
        }


         // 取关微博
        controller.unFollow("1002","buwenbuhuo");

        List<String> Weibos = controller.getAllRecentWeibos("1002");
         //查看数据
        for (String allRecentWeibo : Weibos) {
            System.out.println(allRecentWeibo);
        }

         // 获取某一个人的所有微博
        List<String> allWeibosByUserID = controller.getAllWeibosByUserID("buwenbuhuo");

        for (String s : allWeibosByUserID) {
            System.out.println(s);
        }
    }
}
