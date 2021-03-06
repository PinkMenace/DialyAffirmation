package ashley.dewald.com.dailyaffirmations.SQL;

import android.content.Context;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/*
 * Holds the reference to our database,
 *
 * @version 1.0
 * @author Ashley Dewald
 *
 */

public class SQL_Engine {
    public static final String TAG = SQL_Engine.class.getCanonicalName();

    // Constant, automatic table generated by android, needs to be ignored when generating a list
    // of tables the user can manipulate/drop
    private String ANDROID_METADATA = "android_metadata";

    SQLiteDatabase database;

    public SQL_Engine(Context context) {
        database = new SQL_Connection(context).getWritableDatabase();
    }

    /*
     * Retrieves all available user defined table names in database.
     * @returns String[] returns a string array of all table names.
     */
    public List<String> getAllTableNames() {
        try {

            List<String> tableList = new ArrayList<>();
            Cursor c = database.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

            c.moveToFirst();

            while(c.moveToNext()) {
                String name = c.getString(0);

                if (!name.equals(ANDROID_METADATA))
                    tableList.add(name);
            }

            return tableList;
        } catch (Exception e) {
            return null;
        }
    }

    /*
     * Drops all user defined tables. Returns a integer of how many tables were dropped.
     * @return int Indicates how many tables were dropped.
     */
    public int dropAllTables(){
        int numTableDropped = 0;
        List<String> tableNames = getAllTableNames();

        for(int x=0;x < tableNames.size();x++){
            if(dropTable(tableNames.get(x)))
                numTableDropped++;
        }

        return numTableDropped;
    }

    /*
     * Allows the user to drop specified table based on the table name. If the operation successful,
     * a boolean is returned.
     * @return boolean Returns whether the operation was successful or not.
     */
    public boolean dropTable(String tableName){
        try{
            database.execSQL("DROP TABLE [" + tableName + "]");
            return true;
        }catch (SQLException e){
            return false;
        }
    }

    public void close(){
        database.close();
    }
}