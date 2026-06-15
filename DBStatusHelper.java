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
        setContext(context);
    }

    private DBStatusHelper getThis() {
        return this;
    }

    private Context getContext() {
        return context;
    }

    private void setContext(Context context) {
        getThis().context = context;
    }

    public void onCreate(SQLiteDatabase db) {
        String query = getContext().getString(R.string.dbCreateTable) + TABLE_NAME
                + getContext().getString(R.string.dbLeftParenthesis)
                + ID + getContext().getString(R.string.dbIntegerAutoincrement)
                + ITEM_NAME + getContext().getString(R.string.dbTextWithComma)
                + STATUS + getContext().getString(R.string.dbTextWithComma)
                + CHECKED + getContext().getString(R.string.dbTextWithParenthesis);
        db.execSQL(query);
    }

    public void onUpgrade(SQLiteDatabase db, int i, int j) {
        db.execSQL(getContext().getString(R.string.dbDropTable) + TABLE_NAME);
        onCreate(db);
    }

    StatusData readStatusData() {

        SQLiteDatabase db = getThis().getReadableDatabase();
        Cursor cursor = db.rawQuery(getContext().getString(R.string.dbSelectFrom) + TABLE_NAME, null);
        StatusData statusData = new StatusData(getContext());

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

        SQLiteDatabase db = getThis().getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ITEM_NAME, itemName);
        values.put(STATUS, status);
        values.put(CHECKED, checked);

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    void updateStatus(String itemName, String status, String checked) {

        SQLiteDatabase db = getThis().getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ITEM_NAME, itemName);
        values.put(STATUS, status);
        values.put(CHECKED, checked);

        db.update(TABLE_NAME, values, getContext().getString(R.string.dbItemNameQuestion), new String[]{itemName});
        db.close();
    }

    void changeStatusName(String originalItemName, String newItemName) {

        SQLiteDatabase db = getThis().getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ITEM_NAME, newItemName);

        db.update(TABLE_NAME, values, getContext().getString(R.string.dbItemNameQuestion), new String[]{originalItemName});
        db.close();
    }

    void deleteStatus(String itemName) {

        SQLiteDatabase db = getThis().getWritableDatabase();

        db.delete(TABLE_NAME, getContext().getString(R.string.dbItemNameQuestion), new String[]{itemName});
        db.close();
    }

    void deleteDatabase() {

        getContext().deleteDatabase(DB_NAME);

    }
}