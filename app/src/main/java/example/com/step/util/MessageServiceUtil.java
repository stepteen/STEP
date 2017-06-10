package example.com.step.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.alibaba.fastjson.JSON;
import java.util.ArrayList;
import java.util.List;
import example.com.step.bean.MessageBean;


/**
 * Created by qinghua on 2017/6/10.
 * 操作Messge表
 */

public class MessageServiceUtil {
    private MsgDBOpenHelper msgDBOpenHelper;
    public MessageServiceUtil(Context context){
        msgDBOpenHelper=new MsgDBOpenHelper(context);
    }

    /**
     * 添加记录
     */
    public void save(MessageBean msg){;
        SQLiteDatabase db = msgDBOpenHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("msgcontext",JSON.toJSONString(msg) );
        long id=db.insert("message", null, cv);
        db.close();
    }

    /**
     * 删除记录
     * @param id 记录ID
     */
    public void delete(Integer id){
        SQLiteDatabase db = msgDBOpenHelper.getWritableDatabase();
        db.execSQL("delete from message where msgid=?", new Object[]{id});
    }

    /**
     * 更新记录
     */
    public void update(MessageBean messageBean){

        SQLiteDatabase db = msgDBOpenHelper.getWritableDatabase();
        db.execSQL("update message set msgcontext=?  where msgid=?",
                new Object[]{JSON.toJSONString(messageBean),messageBean.getId()});
    }

    /**
     * 查询记录
     */
    public List<MessageBean>  find(){
        SQLiteDatabase db = msgDBOpenHelper.getReadableDatabase();
        //Cursor cursor = db.query("message",null,null,null,null,null,null);
        Cursor cursor = db.rawQuery("select * from message", null);
        List<MessageBean> mData=new ArrayList<MessageBean>();

        while (cursor.moveToNext()){
            int msgid = cursor.getInt(cursor.getColumnIndex("msgid"));
            String name = cursor.getString(cursor.getColumnIndex("msgcontext"));
            MessageBean messageBean= JSON.parseObject(name,MessageBean.class);
            messageBean.setId(msgid);
            mData.add(0,messageBean);
        }
        cursor.close();
        db.close();
        return mData;
    }
}
