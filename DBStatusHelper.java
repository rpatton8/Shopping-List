package ryan.android.shopping;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DBStatusHelper extends SQLiteOpenHelper {

    private Context context;

    private static final String DB_NAME = ShoppingApp.getStringRes(R.string.dbItemStatusCap);
    private static final int DB_VERSION = 24;
    private static final String TABLE_NAME = ShoppingApp.getStringRes(R.string.dbItemStatus);
    private static final String ID = ShoppingApp.getStringRes(R.string.dbID);
    private static final String ITEM_NAME = ShoppingApp.getStringRes(R.string.dbItemName);
    private static final String STATUS = ShoppingApp.getStringRes(R.string.dbStatus);
    private static final String CHECKED = ShoppingApp.getStringRes(R.string.dbChecked);

    DBStatusHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    public void onCreate(SQLiteDatabase db) {
        String query = context.getString(R.string.dbCreateTable) + TABLE_NAME
                + context.getString(R.string.dbLeftParenthesis)
                + ID + context.getString(R.string.dbIntegerAutoincrement)
                + ITEM_NAME + context.getString(R.string.dbTextWithComma)
                + STATUS + context.getString(R.string.dbTextWithComma)
                + CHECKED + context.getString(R.string.dbTextWithParenthesis);
        db.execSQL(query);
    }

    public void onUpgrade(SQLiteDatabase db, int i, int j) {
        db.execSQL(context.getString(R.string.dbDropTable) + TABLE_NAME);
        onCreate(db);
    }

    StatusData readStatusData() {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(context.getString(R.string.dbSelectFrom) + TABLE_NAME, null);
        StatusData statusData = new StatusData(context);

        if (cursor.moveToFirst()) {
            do { statusData.readStatus(cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return statusData;
    }

    void addNewStatus(String itemName, String status, String checked) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ITEM_NAME, itemName);
        values.put(STATUS, status);
        values.put(CHECKED, checked);

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    void updateStatus(String itemName, String status, String checked) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ITEM_NAME, itemName);
        values.put(STATUS, status);
        values.put(CHECKED, checked);

        db.update(TABLE_NAME, values, context.getString(R.string.dbItemNameQuestion), new String[]{itemName});
        db.close();
    }

    void changeStatusName(String originalItemName, String newItemName) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ITEM_NAME, newItemName);

        db.update(TABLE_NAME, values, context.getString(R.string.dbItemNameQuestion), new String[]{originalItemName});
        db.close();
    }

    void deleteStatus(String itemName) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME, context.getString(R.string.dbItemNameQuestion), new String[]{itemName});
        db.close();
    }

    void deleteDatabase() {

        context.deleteDatabase(DB_NAME);

    }
}