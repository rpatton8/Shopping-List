package ryan.android.shopping;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DBItemHelper extends SQLiteOpenHelper {

    private Context context;

    private static final String DB_NAME = ShoppingApp.getStringRes(R.string.dbShoppingCap);
    private static final int DB_VERSION = 24;
    private static final String TABLE_NAME = ShoppingApp.getStringRes(R.string.dbShopping);
    private static final String ID = ShoppingApp.getStringRes(R.string.dbID);
    private static final String ITEM_NAME = ShoppingApp.getStringRes(R.string.dbItemName);
    private static final String BRAND_TYPE = ShoppingApp.getStringRes(R.string.dbBrandType);
    private static final String CATEGORY = ShoppingApp.getStringRes(R.string.dbCategory);
    private static final String STORE = ShoppingApp.getStringRes(R.string.dbStore);
    private static final String CATEGORY_ORDER = ShoppingApp.getStringRes(R.string.dbItemCategoryOrder);
    private static final String STORE_ORDER = ShoppingApp.getStringRes(R.string.dbItemStoreOrder);


    DBItemHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    Context getContext() {
        return context;
    }

    public void onCreate(SQLiteDatabase db) {
        String query = getContext().getString(R.string.dbCreateTable) + TABLE_NAME
                + getContext().getString(R.string.dbLeftParenthesis)
                + ID + getContext().getString(R.string.dbIntegerAutoincrement)
                + ITEM_NAME + getContext().getString(R.string.dbTextWithComma)
                + BRAND_TYPE + getContext().getString(R.string.dbTextWithComma)
                + CATEGORY + getContext().getString(R.string.dbTextWithComma)
                + STORE + getContext().getString(R.string.dbTextWithComma)
                + CATEGORY_ORDER + getContext().getString(R.string.dbIntWithComma)
                + STORE_ORDER + getContext().getString(R.string.dbIntWithParenthesis);
        db.execSQL(query);
    }

    public void onUpgrade(SQLiteDatabase db, int i, int j) {
        db.execSQL(getContext().getString(R.string.dbDropTable) + TABLE_NAME);
        onCreate(db);
    }

    void readItemDataByCategory(ItemData itemData) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(getContext().getString(R.string.dbSelectFrom) + TABLE_NAME
                + getContext().getString(R.string.dbOrderBy) + CATEGORY + getContext().getString(R.string.dbComma) + CATEGORY_ORDER, null);

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
    }

    void readItemDataByStore(ItemData itemData) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(getContext().getString(R.string.dbSelectFrom) + TABLE_NAME
                + getContext().getString(R.string.dbOrderBy) + STORE + getContext().getString(R.string.dbComma) + STORE_ORDER, null);

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
    }

    void addNewItemByCategory(String itemName, String brandType, String category, String store, int itemCategoryOrder) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ITEM_NAME, itemName);
        values.put(BRAND_TYPE, brandType);
        values.put(CATEGORY, category);
        values.put(STORE, store);
        values.put(CATEGORY_ORDER, itemCategoryOrder);

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    void addNewItemByStore(String itemName, String brandType, String category, String store, int itemStoreOrder) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ITEM_NAME, itemName);
        values.put(BRAND_TYPE, brandType);
        values.put(CATEGORY, category);
        values.put(STORE, store);
        values.put(STORE_ORDER, itemStoreOrder);

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    void updateItem(String originalItemName, String newItemName, String brandType,
                           String category, String store) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ITEM_NAME, newItemName);
        values.put(BRAND_TYPE, brandType);
        values.put(CATEGORY, category);
        values.put(STORE, store);

        db.update(TABLE_NAME, values, getContext().getString(R.string.dbItemNameQuestion), new String[]{originalItemName});
        db.close();
    }

    void changeCategory(String oldCategoryName, String newCategoryName) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(CATEGORY, newCategoryName);

        db.update(TABLE_NAME, values, getContext().getString(R.string.dbCategoryQuestion), new String[]{oldCategoryName});
        db.close();
    }

    void changeStore(String oldStoreName, String newStoreName) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(STORE, newStoreName);

        db.update(TABLE_NAME, values, getContext().getString(R.string.dbStoreQuestion), new String[]{oldStoreName});
        db.close();
    }

    void swapOrderByCategory(String category, int order1, int order2) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values1 = new ContentValues();
        values1.put(CATEGORY_ORDER, -1);
        db.update(TABLE_NAME, values1, getContext().getString(R.string.dbItemCategoryOrderQuestion), new String[]{category, Integer.toString(order1)});

        ContentValues values2 = new ContentValues();
        values2.put(CATEGORY_ORDER, order1);
        db.update(TABLE_NAME, values2, getContext().getString(R.string.dbItemCategoryOrderQuestion), new String[]{category, Integer.toString(order2)});

        ContentValues values3 = new ContentValues();
        values3.put(CATEGORY_ORDER, order2);
        db.update(TABLE_NAME, values3, getContext().getString(R.string.dbItemCategoryOrderQuestion), new String[]{category, Integer.toString(-1)});

        db.close();
    }

    void swapOrderByStore(String store, int order1, int order2) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values1 = new ContentValues();
        values1.put(STORE_ORDER, -1);
        db.update(TABLE_NAME, values1, getContext().getString(R.string.dbItemStoreOrderQuestion), new String[]{store, Integer.toString(order1)});

        ContentValues values2 = new ContentValues();
        values2.put(STORE_ORDER, order1);
        db.update(TABLE_NAME, values2, getContext().getString(R.string.dbItemStoreOrderQuestion), new String[]{store, Integer.toString(order2)});

        ContentValues values3 = new ContentValues();
        values3.put(STORE_ORDER, order2);
        db.update(TABLE_NAME, values3, getContext().getString(R.string.dbItemStoreOrderQuestion), new String[]{store, Integer.toString(-1)});

        db.close();
    }

    void moveOrderDownOneByCategory(String category, int orderNum) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CATEGORY_ORDER, orderNum - 1);
        db.update(TABLE_NAME, values, getContext().getString(R.string.dbItemCategoryOrderQuestion), new String[]{category, Integer.toString(orderNum)});

        db.close();
    }

    void moveOrderDownOneByStore(String store, int orderNum) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(STORE_ORDER, orderNum - 1);
        db.update(TABLE_NAME, values, getContext().getString(R.string.dbItemStoreOrderQuestion), new String[]{store, Integer.toString(orderNum)});

        db.close();
    }

    void deleteItem(String itemName) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, getContext().getString(R.string.dbItemNameQuestion), new String[]{itemName});
        db.close();
    }

    void deleteDatabase() {

        getContext().deleteDatabase(DB_NAME);

    }
}