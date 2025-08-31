package ryan.android.shopping;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBStatusHelper extends SQLiteOpenHelper {

    Context context;

    private static final String DB_NAME = "ItemStatus";
    private static final int DB_VERSION = 5;
    private static final String TABLE_NAME = "itemStatus";
    private static final String ID_COL = "id";
    private static final String ITEMNAME_COL = "itemName";
    private static final String IN_STOCK = "inStock";
    private static final String NEEDED = "needed";
    private static final String PAUSED = "paused";

    public DBStatusHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ITEMNAME_COL + " TEXT,"
                + IN_STOCK + " TEXT,"
                + NEEDED + " TEXT,"
                + PAUSED + " TEXT)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int j) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public StatusData readStatusData() {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        StatusData statusData = new StatusData();

        if (cursor.moveToFirst()) {
            do { statusData.readItemStatus(cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return statusData;
    }

    public void addNewStatus(String itemName, String isInStock, String isNeeded, String isPaused) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ITEMNAME_COL, itemName);
        values.put(IN_STOCK, isInStock);
        values.put(NEEDED, isNeeded);
        values.put(PAUSED, isPaused);

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public void updateStatus(String itemName, String isInStock,
                             String isNeeded, String isPaused) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ITEMNAME_COL, itemName);
        values.put(IN_STOCK, isInStock);
        values.put(NEEDED, isNeeded);
        values.put(PAUSED, isPaused);

        db.update(TABLE_NAME, values, "itemName=?", new String[]{itemName});
        db.close();
    }

    public void changeStatusName(String originalItemName, String itemName, String isInStock,
                             String isNeeded, String isPaused) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ITEMNAME_COL, itemName);
        values.put(IN_STOCK, isInStock);
        values.put(NEEDED, isNeeded);
        values.put(PAUSED, isPaused);

        db.update(TABLE_NAME, values, "itemName=?", new String[]{originalItemName});
        db.close();
    }

    public void deleteStatus(String itemName) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME, "itemName=?", new String[]{itemName});
        db.close();
    }

    public void deleteDatabase() {

        context.deleteDatabase(DB_NAME);

    }

}