package ryan.android.shopping;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBStoreHelper extends SQLiteOpenHelper {

    private Context context;

    private static final String DB_NAME = "Stores";
    private static final int DB_VERSION = 16;
    private static final String TABLE_NAME = "stores";
    private static final String ID = "id";
    private static final String STORE_NAME = "storeName";
    private static final String STORE_ORDER = "storeOrder";
    private static final String STORE_VIEW_ALL = "storeViewAll";
    private static final String STORE_IN_STOCK = "storeInStock";
    private static final String STORE_NEEDED = "storeNeeded";
    private static final String STORE_PAUSED = "storePaused";


    public DBStoreHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + STORE_NAME + " TEXT,"
                + STORE_ORDER  + " INT,"
                + STORE_VIEW_ALL  + " INT,"
                + STORE_IN_STOCK  + " INT,"
                + STORE_NEEDED  + " INT,"
                + STORE_PAUSED + " INT)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int j) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public StoreData readStoreData() {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " + STORE_ORDER, null);
        StoreData storeData = new StoreData();

        if (cursor.moveToFirst()) {
            do { storeData.readStore(cursor.getString(1), cursor.getInt(3),
                    cursor.getInt(4), cursor.getInt(5), cursor.getInt(6));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return storeData;
    }

    public void addNewStore(String storeName, int storeOrder) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(STORE_NAME, storeName);
        values.put(STORE_ORDER, storeOrder);
        values.put(STORE_VIEW_ALL, 0);
        values.put(STORE_IN_STOCK, 0);
        values.put(STORE_NEEDED, 0);
        values.put(STORE_PAUSED, 0);

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public void changeStoreName(String originalStoreName, String newStoreName) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(STORE_NAME, newStoreName);

        db.update(TABLE_NAME, values, "storeName=?", new String[]{originalStoreName});
        db.close();
    }

    public void setStoreViews(String storeName, int storeViewAll, int storeInStock,
                                 int storeNeeded, int storePaused) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(STORE_VIEW_ALL, storeViewAll);
        values.put(STORE_IN_STOCK, storeInStock);
        values.put(STORE_NEEDED, storeNeeded);
        values.put(STORE_PAUSED, storePaused);

        db.update(TABLE_NAME, values, "storeName=?", new String[]{storeName});
        db.close();
    }

    public void swapOrder(int store1, int store2) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values1 = new ContentValues();
        values1.put(STORE_ORDER, -1);
        db.update(TABLE_NAME, values1, "storeOrder=?", new String[]{Integer.toString(store1)});


        ContentValues values2 = new ContentValues();
        values2.put(STORE_ORDER, store1);
        db.update(TABLE_NAME, values2, "storeOrder=?", new String[]{Integer.toString(store2)});

        ContentValues values3 = new ContentValues();
        values3.put(STORE_ORDER, store2);
        db.update(TABLE_NAME, values3, "storeOrder=?", new String[]{Integer.toString(-1)});

        db.close();
    }

    public void deleteStore(String storeName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "storeName=?", new String[]{storeName});
        db.close();
    }

    public void moveOrderDownOne(int orderNum) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(STORE_ORDER, orderNum - 1);
        db.update(TABLE_NAME, values, "storeOrder=?", new String[]{Integer.toString(orderNum)});

        db.close();
    }

    public void deleteDatabase() {

        context.deleteDatabase(DB_NAME);

    }

}