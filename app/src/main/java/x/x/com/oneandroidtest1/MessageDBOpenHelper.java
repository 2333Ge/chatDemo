package x.x.com.oneandroidtest1;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MessageDBOpenHelper extends SQLiteOpenHelper {


    private final String tableContent = "id integer primary key autoincrement,"
            +"name text,"
            +"time integer,"
            +"messageType text,"
            +"fromOthers integer,"//boolean类型
            +"content text,"
            +"headImgUrl text,"
            +"isSendSuccessful integer";
    private String newTableName;
    private String createTable;

    /**
     *
     * @param context
     * @param name
     * @param factory 查询时返回一个自定义的cursor
     * @param version
     */
    public MessageDBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public MessageDBOpenHelper(Context context, String name,String tableName, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        newTableName = tableName;
        createTable = "create table " + newTableName + "(" + tableContent + ")";
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        if(createTable != null && !isTableExits(sqLiteDatabase,newTableName)){
           sqLiteDatabase.execSQL(createTable);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if(createTable != null && !isTableExits(sqLiteDatabase,newTableName)){
            sqLiteDatabase.execSQL(createTable);
        }
    }

    public boolean isTableExits(SQLiteDatabase db,String tableName){
        Cursor cursor;
        boolean result = false;
        String sql = "select count(*) as c from Sqlite_master  where type ='table' and name ='" + tableName.trim() + "' ";
        cursor = db.rawQuery(sql, null);
        if (cursor.moveToNext()) {
            int count = cursor.getInt(0);
            if (count > 0) {
                result = true;
            }
        }
        Log.d("isTableExits()",tableName + "is Exit?" + result);
        return  result;
    }



}
