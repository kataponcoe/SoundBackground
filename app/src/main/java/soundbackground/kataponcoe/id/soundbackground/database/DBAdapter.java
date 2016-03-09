package soundbackground.kataponcoe.id.soundbackground.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.sql.SQLException;

public class DBAdapter
{
    private static final String DB_NAME = "databases_db";
    private static final String TABLE_USER = "user";

    private Context ctx;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBAdapter(Context ctx)
    {
        this.ctx = ctx;
        this.DBHelper = new DatabaseHelper(ctx);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper
    {

        private final String CREATE_TABLE_CATEGORY = "CREATE TABLE user (user text, password text)";
        private final String DROP_TABLE_IF_EXISTS = "DROP TABLE IF EXISTS ";

        public DatabaseHelper(Context context)
        {
            super(context, DB_NAME, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            db.execSQL(CREATE_TABLE_CATEGORY);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            db.execSQL(DROP_TABLE_IF_EXISTS + TABLE_USER);
            onCreate(db);
        }
    }

    public DBAdapter open() throws SQLException
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    public void close()
    {
        if (DBHelper != null)
            DBHelper.close();
    }

    public void insertCategory(String username, String password)
    {
        String sql = "INSERT INTO " + TABLE_USER + " VALUES (?);";
        SQLiteStatement statement = db.compileStatement(sql);
        db.beginTransaction();

        statement.clearBindings();

        statement.bindString(1, username);
        statement.bindString(2, password);

        statement.execute();

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public Cursor retriveUser(String user, String password)
    {
        Cursor cursor = db.rawQuery("SELECT user WHERE user='" + user + "' AND password='" + password + "' FROM " + TABLE_USER, null);
        return cursor;
    }

    private String getUser(String user, String password)
    {
        Cursor mdl = retriveUser(user, password);

        String userName = null;

        for (mdl.moveToFirst(); !mdl.isAfterLast(); mdl.moveToNext())
        {
            userName = mdl.getString(0);
        }

        return userName != null ? userName : "";
    }

}
