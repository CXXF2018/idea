package weibo.server;

import weibo.constant.Names;
import weibo.dao.WeiBoDao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WeiboService {

    private WeiBoDao dao=new WeiBoDao();

    public void init() throws IOException {
        //创建命名空间
        //dao.createNameSpace(Names.NAMESPACE_WEIBO);

        //创建微博内容表
        dao.createTable(Names.TABLE_WEIBO,Names.WEIBO_FAMILY_DATA);

        //创建用户关系表
        dao.createTable(Names.TABLE_RELATION,Names.RELATION_FAMILY_DATA);

        //创建收件箱
        dao.createTable(Names.TABLE_INBOX,Names.INBOX_DATA_VERSIONS,Names.INBOX_FAMILY_DATA);

    }

    //发微博
    public void publish(String star,String content) throws IOException {
        //向微博内容表中插入一条数据
        String rowKey=star+"_"+System.currentTimeMillis();
        dao.putCell(Names.TABLE_WEIBO,rowKey,Names.WEIBO_FAMILY_DATA,Names.WEIBO_COLUMN_CONTENT,content);

        //获取粉丝ID
        String prefix=star+":followedby:";
        List<String> rowKeys = dao.getRowKeysByPrefix(Names.TABLE_RELATION, prefix);

        //向粉丝的收件箱发送本人的微博内容ID
        if (rowKeys.size()<=0){
            return ;
        }

        ArrayList<String> fansId = new ArrayList<String>();

        for (String key : rowKeys) {
            String[] split = key.split(":");
            fansId.add(split[2]);
        }

        dao.putCells(Names.TABLE_INBOX,fansId,Names.INBOX_FAMILY_DATA,star,rowKey);

    }

    //关注某人
    public void follow(String star,String fan) throws IOException {

        String rowKey1=fan+":follow:"+star;
        String rowKey2=star+":followed:"+fan;
        String time=System.currentTimeMillis()+"";

        //向关系表中插入相应的关系
        dao.putCell(Names.TABLE_RELATION,rowKey1,Names.RELATION_FAMILY_DATA,Names
                .RELATION_COLUMN_TIME,time);
        dao.putCell(Names.TABLE_RELATION,rowKey2,Names.RELATION_FAMILY_DATA,Names
                .RELATION_COLUMN_TIME,time);

        //从微博表中获取star近期的微博的rowkey
        String startRow=star;
        String endRow=star+"|";
        List<String> keys = dao.getRowKeysByRange(Names.TABLE_WEIBO, startRow, endRow);
        if (keys.size()<=0){
            return;
        }

        //获取近期的微博要么获取versions的个数，达不到version的个数，就获取现有个数
        int fromIndex=keys.size()>Names.INBOX_DATA_VERSIONS ? keys.size()-Names
                .INBOX_DATA_VERSIONS : 0;
        List<String> recordWeiBoIds = keys.subList(fromIndex, keys.size());

        //向粉丝的信箱中发送star近期的微博Id
        for (String recordWeiBoId : recordWeiBoIds) {
            dao.putCell(Names.TABLE_INBOX,fan,Names.INBOX_FAMILY_DATA,star,recordWeiBoId);
        }

    }

    //取消关注
    public void unfollow(String star,String fan) throws IOException {

        String rowKey1=fan+":follow:"+star;
        String rowKey2=star+":followed:"+fan;

        //删除关系表中的关系
        dao.deleteRow(Names.TABLE_RELATION,rowKey1);
        dao.deleteRow(Names.TABLE_RELATION,rowKey2);

        //删除fan信箱中的star最新信息
        dao.deleteCells(Names.TABLE_INBOX,fan,Names.INBOX_FAMILY_DATA,star);
    }

    //查看某位star的全部微博
    public List<String> getAllWeiBoByUserId(String star) throws IOException {
        return dao.getCellsByPrefix(Names.TABLE_WEIBO,star,Names.WEIBO_FAMILY_DATA,Names
                .WEIBO_COLUMN_CONTENT);
    }

    //查看微博中近期的内容
    public List<String> getAllRecentWeiBo(String fan) throws IOException {
        //从收件箱中获取所有star近期的微博rowkey
        List<String> WeiBoIds = dao.getFamilyByRowKey(Names.TABLE_INBOX, fan, Names
                .INBOX_FAMILY_DATA);

        //根据查到的rowkey去微博内容表中获取相关的微博内容
        return dao.getCellsByRowKey(Names.TABLE_WEIBO, WeiBoIds, Names.WEIBO_FAMILY_DATA, Names
                    .WEIBO_COLUMN_CONTENT);

    }
}