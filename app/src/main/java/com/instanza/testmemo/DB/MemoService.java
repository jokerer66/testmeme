package com.instanza.testmemo.DB;

import android.content.Context;

import com.instanza.testmemo.Bean.MemoListItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by apple on 2017/9/30.
 */

public class MemoService {

    private static MemoService memoService;
    private MemoDao memoDao;
    private Context context;

    public static MemoService getInstance(Context context){
        if(memoService == null){
            synchronized (MemoService.class){
                memoService = new MemoService(context);
            }
        }
        return memoService;
    }

    public MemoService(Context context) {
        this.context = context;
        memoDao = new MemoDao(context);
    }

    public void addMemo(Map<String,String> map){
        if(memoDao == null){
            new MemoDao(context);
            return;
        }
        synchronized(MemoService.class) {
            memoDao.execMemoSQL(getAddSql(map));
        }
    }

    public void updateMemo(Map<String,String> map){
        if(memoDao == null){
            new MemoDao(context);
            return;
        }
        synchronized(MemoService.class) {
            memoDao.execMemoSQL(getUpdateSql(map));
        }
    }


    public List<MemoListItem> getAllMemo(){
        if(memoDao == null){
            new MemoDao(context);
            return null;
        }
        List<MemoListItem> memolist = new ArrayList<>();
        memolist = memoDao.selectMemoBySql("");
        return memolist;
    }


    public List<MemoListItem> selectMemoByParam(Map<String,String> sqlmap){

        if(memoDao == null){
            new MemoDao(context);
            return null;
        }
        List<MemoListItem> memolist = new ArrayList<>();

        memolist = memoDao.selectMemoBySql(getSelectSql(sqlmap));
        return memolist;

    }

    public void deleteMemo(int id){
        if(memoDao == null){
            new MemoDao(context);
            return;
        }
        memoDao.execMemoSQL(getdeleteSql(id));
    }

    public String getAddSql(Map<String,String> map){
        String tempcontent = map.get("content");
        String createtime = map.get("createtime");
        String type = map.get("type");
        String tag = map.get("tag");
        return "INSERT INTO memo VALUES(null,'"+tempcontent+"','"+ createtime +"' ,'"+type+"','"+tag +"') ;";
    }

    public String getUpdateSql(Map<String,String> map){
        String tempcontent = map.get("content");
        String createtime = map.get("createtime");
        String memoid = map.get("memoid");
        String type = map.get("type");
        String tag = map.get("tag");
        return "UPDATE memo SET content ='"+tempcontent+"' ,createtime = '"+createtime+"' , type = '"+type+"' , tag = '"+tag+"' where id = "+memoid+"  ;";

    }

    public String getSelectSql(Map<String,String> sqlmap){
        Boolean firstone = true;
        String sql = "";
        if(sqlmap.containsKey("tag") ){
            if(firstone){
                sql = sql + " where ";
                firstone = false;
            }else{
                sql = sql + " and ";
            }
            sql = sql + " tag = '"+sqlmap.get("tag")+"'";
        }
        if(sqlmap.containsKey("type") ){
            if(firstone){
                sql = sql + " where ";
                firstone = false;
            }else{
                sql = sql + " and ";
            }
            sql = sql + " type = '"+sqlmap.get("type")+"'";
        }
        if(sqlmap.containsKey("content") ){
            if(!sqlmap.get("content").equals("")){
                if(firstone){
                    sql = sql + " where ";
                    firstone = false;
                }else{
                    sql = sql + " and ";
                }
                sql = sql + " content like '%"+sqlmap.get("content")+"%'";
            }


        }
        return sql;
    }

    public String getdeleteSql(int id){
        return "delete from memo where id = "+id;
    }
}
