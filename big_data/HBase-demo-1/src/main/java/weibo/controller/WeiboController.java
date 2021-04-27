package weibo.controller;

import weibo.server.WeiboService;

import java.io.IOException;
import java.util.List;

public class WeiboController {

    WeiboService services=new WeiboService();

    //初始化微博
    public void init() throws IOException {
        services.init();
    }

    //发布微博
    public void publish(String star,String content) throws IOException {
        services.publish(star,content);
    }

    //关注用户
    public void follow(String fan,String star) throws IOException {
        services.follow(star,fan);
    }

    //取消关注某人
    public void unFollow(String fan,String star) throws IOException {
        services.unfollow(star,fan);
    }

    //获取某人的微博信息
    public List<String> getAllWeibosByUserID(String star) throws IOException {
        return services.getAllWeiBoByUserId(star);
    }

    //获取最近的微博信息
    public List<String> getAllRecentWeibos(String fan) throws IOException {
        return services.getAllRecentWeiBo(fan);
    }
}
