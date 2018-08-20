package x.x.com.oneandroidtest1;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * 注意外要存好版本号
 */
public class MessageDBUtils {
    private Context context;
    private MessageDBOpenHelper mDbHelper;
    private SQLiteDatabase sqLiteDatabase;
    private int versionNow;
//    private SharedPreferences sp;
    public static final String MESSAGEDB_NAME = "message_db";
    public static final String SP_NAME = "sp_message";
    public static final String KEY_SP_VERSION = "version";

    //KEY值,数据库列名，以免插入数据库出错

    private String KEY_FROM_OTHERS = "fromOthers";//信息的来源，true发过来，false发过去
    private String KEY_NAME = "name";
    private String KEY_TIME = "time";
    private String KEY_CONTENT = "content";
    private String KEY_HEAD_IMGURL = "headImgUrl";
    private String KEY_IS_SEND_SUCCESSFUL = "isSendSuccessful";//boolean类型
    private String KEY_MESSAGE_TYPE = "messageType";

    /**
     * 创建带新表的数据库，version 要大于等于原来，否者新表就创建不了,注意小心输入的版本号比原本的小的情况
     * @param context
     * @param tableName
     */
    public MessageDBUtils(Context context,String tableName){
        this.context = context;
        int version = getSpVersion(context);
        mDbHelper = new MessageDBOpenHelper(context,MESSAGEDB_NAME,null,version);
        sqLiteDatabase = mDbHelper.getWritableDatabase();
        if( !mDbHelper.isTableExits(sqLiteDatabase,tableName)){//这个表若不存在,版本号+1，创建新表
            createNewTable(tableName);
        }

    }

    /**
     *创建可写的数据库，此时的version必须和原来一致
     * @param context
     */
    public MessageDBUtils(Context context){
        this.context = context;
        int version = getSpVersion(context);
        mDbHelper = new MessageDBOpenHelper(context,MESSAGEDB_NAME,null,version);
        sqLiteDatabase = mDbHelper.getWritableDatabase();
    }


    /**
     *创建新表，version自动加一
     * @param tableName
     */
    public void createNewTable(String tableName){
        int versionNow = sqLiteDatabase.getVersion();
        setSpVersion(context,versionNow+1);
        mDbHelper = new MessageDBOpenHelper(context,MESSAGEDB_NAME,tableName,null,versionNow + 1);
        sqLiteDatabase = mDbHelper.getWritableDatabase();
    }

    /**
     * 插入一条数据到指定的表里
     * @param messageBean
     * @param tableName
     */
    public void insertAMessage(MessageBean messageBean,String tableName){
        ContentValues values = new ContentValues();
        values.put(KEY_CONTENT,messageBean.getContent());
        values.put(KEY_FROM_OTHERS, messageBean.getFromOthers()? 1:0 );//数据库不能传Boolean类型，要转化
        values.put(KEY_HEAD_IMGURL,messageBean.getHeadImgUrl());
        values.put(KEY_IS_SEND_SUCCESSFUL,messageBean.isSendSuccessful()?1:0);
        values.put(KEY_MESSAGE_TYPE,messageBean.getMessageType());
        values.put(KEY_NAME,messageBean.getName());
        values.put(KEY_TIME,messageBean.getTime());
        sqLiteDatabase.insert(tableName,null,values);
    }

    /**
     * 查询所有数据，返回一个List&ltMessageBean&gt,注意返回的是一个引用，直接付值给另一个List&ltMessage&gt将使原引用改变
     * @param tableName
     * @return List&ltMessage&gt
     */
    public List<MessageBean> selectAllFromTable(String tableName){
        List<MessageBean> listMessage = new ArrayList<MessageBean>();
        String name,type,content,headImgUrl;
        boolean isSendSuccessful ,fromOthers;
        long time;
        Cursor cursor = sqLiteDatabase.query(tableName,null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                name = cursor.getString(cursor.getColumnIndex(KEY_NAME));
                content = cursor.getString(cursor.getColumnIndex(KEY_CONTENT));
                headImgUrl = cursor.getString(cursor.getColumnIndex(KEY_HEAD_IMGURL));
                type = cursor.getString(cursor.getColumnIndex(KEY_MESSAGE_TYPE));

                isSendSuccessful = cursor.getInt(cursor.getColumnIndex(KEY_IS_SEND_SUCCESSFUL)) == 1;
                fromOthers = cursor.getInt(cursor.getColumnIndex(KEY_FROM_OTHERS)) == 1;

                time = cursor.getLong(cursor.getColumnIndex(KEY_TIME));

                MessageBean messageBean = new MessageBean();
                messageBean.setContent(content);
                messageBean.setFromOthers(fromOthers);
                messageBean.setTime(time);
                messageBean.setMessageType(type);
                messageBean.setSendSuccessful(isSendSuccessful);
                messageBean.setName(name);
                messageBean.setHeadImgUrl(headImgUrl);

                listMessage.add(messageBean);

            }while (cursor.moveToNext());
        }
        return listMessage;
    }

    /**
     * 根据时间来更新是否发送成功
     * @param time
     * @param table
     * @param isSendSuccessful
     */
    public void updateData(long time,String table,boolean isSendSuccessful){
        ContentValues values = new ContentValues();
        values.put(KEY_IS_SEND_SUCCESSFUL,isSendSuccessful?1:0);
        sqLiteDatabase.update(table,values,KEY_TIME + " = ?",new String[]{time+""});
    }

    /**
     * 根据时间删除相应行
     * @param time
     * @param table
     */
    public void delete(long time,String table){
        sqLiteDatabase.delete(table,KEY_TIME + " = ?",new String[]{time+""});
    }

    /**
     * 返回数据库版本号
     * @return int 版本号
     */
    public int getVersion(){
        return sqLiteDatabase.getVersion();
    }

    /**
     * 从SharedPreferences中获得数据库版本
     * @param context
     * @return
     */
    public static int getSpVersion(Context context){
        SharedPreferences sp = context.getSharedPreferences(SP_NAME,Context.MODE_PRIVATE);
        return sp.getInt(KEY_SP_VERSION,1);

    }
    /**
     * 存储数据库版本于SharedPreferences
     * @param context
     * @return
     */
    public static void setSpVersion(Context context,int versionNow){
        SharedPreferences sp = context.getSharedPreferences(SP_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(KEY_SP_VERSION,versionNow);
        editor.commit();

    }

    /**
     * 关闭数据库等操作
     */
    public void dispose(){
        sqLiteDatabase.close();
    }
}
