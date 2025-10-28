package ryan.android.shopping;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DBItemHelper extends SQLiteOpenHelper {

    private final Context context;

    private static final String DB_NAME = "Shopping";
    private static final int DB_VERSION = 12;
    private static final String TABLE_NAME = "shopping";
    private static final String ID = "id";
    private static final String ITEM_NAME = "itemName";
    private static final String BRAND_TYPE = "brandType";
    private static final String CATEGORY = "category";
    private static final String STORE = "store";
    private static final String CATEGORY_ORDER = "categoryOrder";
    private static final String STORE_ORDER = "storeOrder";

    DBItemHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ITEM_NAME + " TEXT,"
                + BRAND_TYPE + " TEXT,"
                + CATEGORY + " TEXT,"
                + STORE + " TEXT,"
                + CATEGORY_ORDER + " INT,"
                + STORE_ORDER + " INT)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int j) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public ItemData readItemDataByCategory() {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " + CATEGORY + ", " + CATEGORY_ORDER, null);
        ItemData itemData = new ItemData();

        if (cursor.moveToFirst()) {
            do { itemData.readLineOfDataByCategory(cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getInt(5));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return itemData;
    }

    public ItemData readItemDataByStore() {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " + STORE + ", " + STORE_ORDER, null);
        ItemData itemData = new ItemData();

        if (cursor.moveToFirst()) {
            do { itemData.readLineOfDataByStore(cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getInt(6));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return itemData;
    }

    public void addNewItemByCategory(String itemName, String brandType, String category, String store, int itemOrder) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ITEM_NAME, itemName);
        values.put(BRAND_TYPE, brandType);
        values.put(CATEGORY, category);
        values.put(STORE, store);
        values.put(CATEGORY_ORDER, itemOrder);

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public void addNewItemByStore(String itemName, String brandType, String category, String store, int itemOrder) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ITEM_NAME, itemName);
        values.put(BRAND_TYPE, brandType);
        values.put(CATEGORY, category);
        values.put(STORE, store);
        values.put(STORE_ORDER, itemOrder);

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public void updateItem(String oldItemName, String newItemName, String brandType,
                           String category, String store) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ITEM_NAME, newItemName);
        values.put(BRAND_TYPE, brandType);
        values.put(CATEGORY, category);
        values.put(STORE, store);

        db.update(TABLE_NAME, values, "itemName=?", new String[]{oldItemName});
        db.close();
    }

    public void changeCategory(String oldCategoryName, String newCategoryName) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(CATEGORY, newCategoryName);

        db.update(TABLE_NAME, values, "category=?", new String[]{oldCategoryName});
        db.close();
    }

    public void changeStore(String oldStoreName, String newStoreName) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(STORE, newStoreName);

        db.update(TABLE_NAME, values, "store=?", new String[]{oldStoreName});
        db.close();
    }

    public void swapOrderByCategory(String category, int order1, int order2) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values1 = new ContentValues();
        values1.put(CATEGORY_ORDER, -1);
        db.update(TABLE_NAME, values1, "category=? AND itemOrder=?", new String[]{category, Integer.toString(order1)});

        ContentValues values2 = new ContentValues();
        values2.put(CATEGORY_ORDER, order1);
        db.update(TABLE_NAME, values2, "category=? AND itemOrder=?", new String[]{category, Integer.toString(order2)});

        ContentValues values3 = new ContentValues();
        values3.put(CATEGORY_ORDER, order2);
        db.update(TABLE_NAME, values3, "category=? AND itemOrder=?", new String[]{category, Integer.toString(-1)});

        db.close();
    }

    public void swapOrderByStore(String store, int order1, int order2) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values1 = new ContentValues();
        values1.put(STORE_ORDER, -1);
        db.update(TABLE_NAME, values1, "store=? AND itemOrder=?", new String[]{store, Integer.toString(order1)});

        ContentValues values2 = new ContentValues();
        values2.put(STORE_ORDER, order1);
        db.update(TABLE_NAME, values2, "store=? AND itemOrder=?", new String[]{store, Integer.toString(order2)});

        ContentValues values3 = new ContentValues();
        values3.put(STORE_ORDER, order2);
        db.update(TABLE_NAME, values3, "store=? AND itemOrder=?", new String[]{store, Integer.toString(-1)});

        db.close();
    }

    public void moveOrderDownOneByCategory(String category, int orderNum) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CATEGORY_ORDER, orderNum - 1);
        db.update(TABLE_NAME, values, "category=? AND itemOrder=?", new String[]{category, Integer.toString(orderNum)});

        db.close();
    }

    public void moveOrderDownOneByStore(String store, int orderNum) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(STORE_ORDER, orderNum - 1);
        db.update(TABLE_NAME, values, "store=? AND itemOrder=?", new String[]{store, Integer.toString(orderNum)});

        db.close();
    }

    public void deleteItem(String itemName) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME, "itemName=?", new String[]{itemName});
        db.close();
    }

    public void deleteDatabase() {

        context.deleteDatabase(DB_NAME);

    }
}