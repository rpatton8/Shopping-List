package ryan.android.shopping;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBItemHelper extends SQLiteOpenHelper {

    private Context context;

    private static final String DB_NAME = "Shopping";
    private static final int DB_VERSION = 9;
    private static final String TABLE_NAME = "shopping";
    private static final String ID = "id";
    private static final String ITEMNAME = "itemName";
    private static final String BRANDTYPE = "brandType";
    private static final String CATEGORY = "category";
    private static final String STORE = "store";
    private static final String ORDER = "itemOrder";

    DBItemHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ITEMNAME + " TEXT,"
                + BRANDTYPE + " TEXT,"
                + CATEGORY + " TEXT,"
                + STORE + " TEXT,"
                + ORDER + " INT)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int j) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public ItemData readItemData() {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " + CATEGORY + ", " + ORDER, null);
        ItemData itemData = new ItemData();

        if (cursor.moveToFirst()) {
            do { itemData.readLineOfData(cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return itemData;
    }

    public void addNewItem(String itemName, String brandType, String category, String store, int itemOrder) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ITEMNAME, itemName);
        values.put(BRANDTYPE, brandType);
        values.put(CATEGORY, category);
        values.put(STORE, store);
        values.put(ORDER, itemOrder);

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public void updateItem(String originalItemName, String itemName, String brandType,
                           String category, String store) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ITEMNAME, itemName);
        values.put(BRANDTYPE, brandType);
        values.put(CATEGORY, category);
        values.put(STORE, store);

        db.update(TABLE_NAME, values, "itemName=?", new String[]{originalItemName});
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

    public void swapOrder(String category, int order1, int order2) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values1 = new ContentValues();
        values1.put(ORDER, -1);
        db.update(TABLE_NAME, values1, "category=? AND itemOrder=?", new String[]{category, Integer.toString(order1)});

        ContentValues values2 = new ContentValues();
        values2.put(ORDER, order1);
        db.update(TABLE_NAME, values2, "category=? AND itemOrder=?", new String[]{category, Integer.toString(order2)});

        ContentValues values3 = new ContentValues();
        values3.put(ORDER, order2);
        db.update(TABLE_NAME, values3, "category=? AND itemOrder=?", new String[]{category, Integer.toString(-1)});

        db.close();
    }

    public void moveOrderDownOne(String category, int orderNum) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ORDER, orderNum - 1);
        db.update(TABLE_NAME, values, "category=? AND itemOrder=?", new String[]{category, Integer.toString(orderNum)});

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