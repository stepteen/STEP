package example.com.step.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by qinghua on 2017/6/10.
 */

public class MsgDBOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "msg.db";
    private static final int DATABASE_VERSION = 1;

    public MsgDBOpenHelper(Context context) {
        //CursorFactory设置为null,使用默认值
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //数据库第一次被创建时onCreate会被调用
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS message" +
                "(msgid integer primary key autoincrement, msgcontext TEXT NOT NULL)");
    }

    //如果DATABASE_VERSION值被改为2,系统发现现有数据库版本不同,即会调用onUpgrade
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("ALTER TABLE message ADD COLUMN other STRING");
    }

}
