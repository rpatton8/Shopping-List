package ryan.android.shopping;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DBCategoryHelper extends SQLiteOpenHelper {

    private Context context;

    private static final String DB_NAME = ShoppingApp.getStringRes(R.string.dbCategoriesCap);
    private static final int DB_VERSION = 24;
    private static final String TABLE_NAME = ShoppingApp.getStringRes(R.string.dbCategories);
    private static final String ID = ShoppingApp.getStringRes(R.string.dbID);
    private static final String CATEGORY_NAME = ShoppingApp.getStringRes(R.string.dbCategoryName);
    private static final String CATEGORY_ORDER = ShoppingApp.getStringRes(R.string.dbCategoryOrder);
    private static final String CATEGORY_VIEW_ALL = ShoppingApp.getStringRes(R.string.dbCategoryViewAll);
    private static final String CATEGORY_IN_STOCK = ShoppingApp.getStringRes(R.string.dbCategoryInStock);
    private static final String CATEGORY_NEEDED = ShoppingApp.getStringRes(R.string.dbCategoryNeeded);
    private static final String CATEGORY_PAUSED = ShoppingApp.getStringRes(R.string.dbCategoryPaused);

    DBCategoryHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    private Context getContext() {
        return context;
    }

    public void onCreate(SQLiteDatabase db) {
        String query = getContext().getString(R.string.dbCreateTable) + TABLE_NAME
                + getContext().getString(R.string.dbLeftParenthesis)
                + ID + getContext().getString(R.string.dbIntegerAutoincrement)
                + CATEGORY_NAME + getContext().getString(R.string.dbTextWithComma)
                + CATEGORY_ORDER  + getContext().getString(R.string.dbIntWithComma)
                + CATEGORY_VIEW_ALL  + getContext().getString(R.string.dbIntWithComma)
                + CATEGORY_IN_STOCK  + getContext().getString(R.string.dbIntWithComma)
                + CATEGORY_NEEDED  + getContext().getString(R.string.dbIntWithComma)
                + CATEGORY_PAUSED + getContext().getString(R.string.dbIntWithParenthesis);
        db.execSQL(query);
    }

    public void onUpgrade(SQLiteDatabase db, int i, int j) {
        db.execSQL(getContext().getString(R.string.dbDropTable) + TABLE_NAME);
        onCreate(db);
    }

    CategoryData readCategoryData() {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(getContext().getString(R.string.dbSelectFrom) + TABLE_NAME
                + getContext().getString(R.string.dbOrderBy) + CATEGORY_ORDER, null);
        CategoryData categoryData = new CategoryData(getContext());

        if (cursor.moveToFirst()) {
            do { categoryData.readCategory(cursor.getString(1),
                    cursor.getInt(3),
                    cursor.getInt(4),
                    cursor.getInt(5),
                    cursor.getInt(6));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return categoryData;
    }

    void addNewCategory(String categoryName, int categoryOrder) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(CATEGORY_NAME, categoryName);
        values.put(CATEGORY_ORDER, categoryOrder);
        values.put(CATEGORY_VIEW_ALL, 0);
        values.put(CATEGORY_IN_STOCK, 0);
        values.put(CATEGORY_NEEDED, 0);
        values.put(CATEGORY_PAUSED, 0);

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    void changeCategoryName(String originalCategoryName, String newCategoryName) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(CATEGORY_NAME, newCategoryName);

        db.update(TABLE_NAME, values, getContext().getString(R.string.dbCategoryNameQuestion), new String[]{originalCategoryName});
        db.close();
    }

    void setCategoryViews(String categoryName, int categoryViewAll, int categoryInStock, int categoryNeeded, int categoryPaused) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(CATEGORY_VIEW_ALL, categoryViewAll);
        values.put(CATEGORY_IN_STOCK, categoryInStock);
        values.put(CATEGORY_NEEDED, categoryNeeded);
        values.put(CATEGORY_PAUSED, categoryPaused);

        db.update(TABLE_NAME, values, getContext().getString(R.string.dbCategoryNameQuestion), new String[]{categoryName});
        db.close();
    }

    void swapOrder(int category1, int category2) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values1 = new ContentValues();

        values1.put(CATEGORY_ORDER, -1);
        db.update(TABLE_NAME, values1, getContext().getString(R.string.dbCategoryOrderQuestion), new String[]{Integer.toString(category1)});

        ContentValues values2 = new ContentValues();
        values2.put(CATEGORY_ORDER, category1);
        db.update(TABLE_NAME, values2, getContext().getString(R.string.dbCategoryOrderQuestion), new String[]{Integer.toString(category2)});

        ContentValues values3 = new ContentValues();
        values3.put(CATEGORY_ORDER, category2);
        db.update(TABLE_NAME, values3, getContext().getString(R.string.dbCategoryOrderQuestion), new String[]{Integer.toString(-1)});

        db.close();
    }

    void moveOrderDownOne(int orderNum) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CATEGORY_ORDER, orderNum - 1);
        db.update(TABLE_NAME, values, getContext().getString(R.string.dbCategoryOrderQuestion), new String[]{Integer.toString(orderNum)});

        db.close();
    }

    void deleteCategory(String categoryName) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, getContext().getString(R.string.dbCategoryNameQuestion), new String[]{categoryName});
        db.close();
    }

    void deleteDatabase() {

        getContext().deleteDatabase(DB_NAME);

    }
}