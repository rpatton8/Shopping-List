package ryan.android.shopping;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBCategoryHelper extends SQLiteOpenHelper {

    Context context;

    private static final String DB_NAME = "Categories";
    private static final int DB_VERSION = 4;
    private static final String TABLE_NAME = "categories";
    private static final String ID_COL = "id";
    private static final String CATEGORY_NAME = "categoryName";
    private static final String CATEGORY_ORDER = "categoryOrder";

    public DBCategoryHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CATEGORY_NAME + " TEXT,"
                + CATEGORY_ORDER + " INT)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int j) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public CategoryData readCategoryData() {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " + CATEGORY_ORDER, null);
        CategoryData categoryData = new CategoryData();

        if (cursor.moveToFirst()) {
            do { categoryData.readCategory(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        cursor.close();
            db.close();
        return categoryData;
    }

    public void addNewCategory(String categoryName, int categoryOrder) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(CATEGORY_NAME, categoryName);
        values.put(CATEGORY_ORDER, categoryOrder);

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public void changeCategoryName(String originalCategoryName, String newCategoryName) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(CATEGORY_NAME, newCategoryName);

        db.update(TABLE_NAME, values, "categoryName=?", new String[]{originalCategoryName});
        db.close();
    }

    public void swapOrder(int category1, int category2) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values1 = new ContentValues();
        values1.put(CATEGORY_ORDER, -1);
        db.update(TABLE_NAME, values1, "categoryOrder=?", new String[]{Integer.toString(category1)});


        ContentValues values2 = new ContentValues();
        values2.put(CATEGORY_ORDER, category1);
        db.update(TABLE_NAME, values2, "categoryOrder=?", new String[]{Integer.toString(category2)});

        ContentValues values3 = new ContentValues();
        values3.put(CATEGORY_ORDER, category2);
        db.update(TABLE_NAME, values3, "categoryOrder=?", new String[]{Integer.toString(-1)});

        db.close();
    }

    public void deleteCategory(String categoryName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "categoryName=?", new String[]{categoryName});
        db.close();
    }

    public void moveOrderDownOne(int orderNum) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CATEGORY_ORDER, orderNum - 1);
        db.update(TABLE_NAME, values, "categoryOrder=?", new String[]{Integer.toString(orderNum)});

        db.close();
    }

    public void deleteDatabase() {

        context.deleteDatabase(DB_NAME);

    }

}