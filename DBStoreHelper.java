package ryan.android.shopping;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DBStoreHelper extends SQLiteOpenHelper {

    private Context context;

    private static final String DB_NAME = ShoppingApp.getStringRes(R.string.dbStoresCap);
    private static final int DB_VERSION = 24;
    private static final String TABLE_NAME = ShoppingApp.getStringRes(R.string.dbStores);
    private static final String ID = ShoppingApp.getStringRes(R.string.dbID);
    private static final String STORE_NAME = ShoppingApp.getStringRes(R.string.dbStoreName);
    private static final String STORE_ORDER = ShoppingApp.getStringRes(R.string.dbStoreOrder);
    private static final String STORE_VIEW_ALL = ShoppingApp.getStringRes(R.string.dbStoreViewAll);
    private static final String STORE_IN_STOCK = ShoppingApp.getStringRes(R.string.dbStoreInStock);
    private static final String STORE_NEEDED = ShoppingApp.getStringRes(R.string.dbStoreNeeded);
    private static final String STORE_PAUSED = ShoppingApp.getStringRes(R.string.dbStorePaused);

    DBStoreHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        setContext(context);
    }

    private DBStoreHelper getThis() {
        return this;
    }

    private Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        getThis().context = context;
    }

    public void onCreate(SQLiteDatabase db) {
        String query = getContext().getString(R.string.dbCreateTable) + TABLE_NAME
                + getContext().getString(R.string.dbLeftParenthesis)
                + ID + getContext().getString(R.string.dbIntegerAutoincrement)
                + STORE_NAME + getContext().getString(R.string.dbTextWithComma)
                + STORE_ORDER  + getContext().getString(R.string.dbIntWithComma)
                + STORE_VIEW_ALL  + getContext().getString(R.string.dbIntWithComma)
                + STORE_IN_STOCK  + getContext().getString(R.string.dbIntWithComma)
                + STORE_NEEDED  + getContext().getString(R.string.dbIntWithComma)
                + STORE_PAUSED + getContext().getString(R.string.dbIntWithParenthesis);
        db.execSQL(query);
    }

    public void onUpgrade(SQLiteDatabase db, int i, int j) {
        db.execSQL(getContext().getString(R.string.dbDropTable) + TABLE_NAME);
        onCreate(db);
    }

    StoreData readStoreData() {

        SQLiteDatabase db = getThis().getReadableDatabase();
        Cursor cursor = db.rawQuery(getContext().getString(R.string.dbSelectFrom) + TABLE_NAME
                + getContext().getString(R.string.dbOrderBy) + STORE_ORDER, null);
        StoreData storeData = new StoreData(getContext());

        if (cursor.moveToFirst()) {
            do { storeData.readStore(cursor.getString(1),
                    cursor.getInt(3),
                    cursor.getInt(4),
                    cursor.getInt(5),
                    cursor.getInt(6));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return storeData;
    }

    void addNewStore(String storeName, int storeOrder) {

        SQLiteDatabase db = getThis().getWritableDatabase();
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

    void changeStoreName(String originalStoreName, String newStoreName) {

        SQLiteDatabase db = getThis().getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(STORE_NAME, newStoreName);

        db.update(TABLE_NAME, values, getContext().getString(R.string.dbStoreNameQuestion), new String[]{originalStoreName});
        db.close();
    }

    void setStoreViews(String storeName, int storeViewAll, int storeInStock, int storeNeeded, int storePaused) {

        SQLiteDatabase db = getThis().getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(STORE_VIEW_ALL, storeViewAll);
        values.put(STORE_IN_STOCK, storeInStock);
        values.put(STORE_NEEDED, storeNeeded);
        values.put(STORE_PAUSED, storePaused);

        db.update(TABLE_NAME, values, getContext().getString(R.string.dbStoreNameQuestion), new String[]{storeName});
        db.close();
    }

    void swapOrder(int store1, int store2) {

        SQLiteDatabase db = getThis().getWritableDatabase();
        ContentValues values1 = new ContentValues();

        values1.put(STORE_ORDER, -1);
        db.update(TABLE_NAME, values1, getContext().getString(R.string.dbStoreOrderQuestion), new String[]{Integer.toString(store1)});

        ContentValues values2 = new ContentValues();
        values2.put(STORE_ORDER, store1);
        db.update(TABLE_NAME, values2, getContext().getString(R.string.dbStoreOrderQuestion), new String[]{Integer.toString(store2)});

        ContentValues values3 = new ContentValues();
        values3.put(STORE_ORDER, store2);
        db.update(TABLE_NAME, values3, getContext().getString(R.string.dbStoreOrderQuestion), new String[]{Integer.toString(-1)});

        db.close();
    }

    void moveOrderDownOne(int orderNum) {

        SQLiteDatabase db = getThis().getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(STORE_ORDER, orderNum - 1);
        db.update(TABLE_NAME, values, getContext().getString(R.string.dbStoreOrderQuestion), new String[]{Integer.toString(orderNum)});

        db.close();
    }

    void deleteStore(String storeName) {

        SQLiteDatabase db = getThis().getWritableDatabase();
        db.delete(TABLE_NAME, getContext().getString(R.string.dbStoreNameQuestion), new String[]{storeName});
        db.close();
    }

    void deleteDatabase() {

        getContext().deleteDatabase(DB_NAME);

    }
}