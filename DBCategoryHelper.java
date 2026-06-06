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

    public void onCreate(SQLiteDatabase db) {
        String query = context.getString(R.string.dbCreateTable) + TABLE_NAME
                + context.getString(R.string.dbLeftParenthesis)
                + ID + context.getString(R.string.dbIntegerAutoincrement)
                + CATEGORY_NAME + context.getString(R.string.dbTextWithComma)
                + CATEGORY_ORDER  + context.getString(R.string.dbIntWithComma)
                + CATEGORY_VIEW_ALL  + context.getString(R.string.dbIntWithComma)
                + CATEGORY_IN_STOCK  + context.getString(R.string.dbIntWithComma)
                + CATEGORY_NEEDED  + context.getString(R.string.dbIntWithComma)
                + CATEGORY_PAUSED + context.getString(R.string.dbIntWithParenthesis);
        db.execSQL(query);
    }

    public void onUpgrade(SQLiteDatabase db, int i, int j) {
        db.execSQL(context.getString(R.string.dbDropTable) + TABLE_NAME);
        onCreate(db);
    }

    CategoryData readCategoryData() {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(context.getString(R.string.dbSelectFrom) + TABLE_NAME
                + context.getString(R.string.dbOrderBy) + CATEGORY_ORDER, null);
        CategoryData categoryData = new CategoryData(context);

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

        db.update(TABLE_NAME, values, context.getString(R.string.dbCategoryNameQuestion), new String[]{originalCategoryName});
        db.close();
    }

    void setCategoryViews(String categoryName, int categoryViewAll, int categoryInStock, int categoryNeeded, int categoryPaused) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(CATEGORY_VIEW_ALL, categoryViewAll);
        values.put(CATEGORY_IN_STOCK, categoryInStock);
        values.put(CATEGORY_NEEDED, categoryNeeded);
        values.put(CATEGORY_PAUSED, categoryPaused);

        db.update(TABLE_NAME, values, context.getString(R.string.dbCategoryNameQuestion), new String[]{categoryName});
        db.close();
    }

    void swapOrder(int category1, int category2) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values1 = new ContentValues();

        values1.put(CATEGORY_ORDER, -1);
        db.update(TABLE_NAME, values1, context.getString(R.string.dbCategoryOrderQuestion), new String[]{Integer.toString(category1)});

        ContentValues values2 = new ContentValues();
        values2.put(CATEGORY_ORDER, category1);
        db.update(TABLE_NAME, values2, context.getString(R.string.dbCategoryOrderQuestion), new String[]{Integer.toString(category2)});

        ContentValues values3 = new ContentValues();
        values3.put(CATEGORY_ORDER, category2);
        db.update(TABLE_NAME, values3, context.getString(R.string.dbCategoryOrderQuestion), new String[]{Integer.toString(-1)});

        db.close();
    }

    void moveOrderDownOne(int orderNum) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CATEGORY_ORDER, orderNum - 1);
        db.update(TABLE_NAME, values, context.getString(R.string.dbCategoryOrderQuestion), new String[]{Integer.toString(orderNum)});

        db.close();
    }

    void deleteCategory(String categoryName) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, context.getString(R.string.dbCategoryNameQuestion), new String[]{categoryName});
        db.close();
    }

    void deleteDatabase() {

        context.deleteDatabase(DB_NAME);

    }
}