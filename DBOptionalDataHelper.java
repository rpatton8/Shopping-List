package ryan.android.shopping;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DBOptionalDataHelper extends SQLiteOpenHelper {

    private Context context;

    private static final String DB_NAME = ShoppingApp.getStringRes(R.string.dbOptionalDataCap);
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = ShoppingApp.getStringRes(R.string.dbOptionalData);
    private static final String ID = ShoppingApp.getStringRes(R.string.dbID);
    private static final String ITEM_NAME = ShoppingApp.getStringRes(R.string.dbItemName);
    private static final String QUANTITY = ShoppingApp.getStringRes(R.string.dbQuantity);
    private static final String PRICE = ShoppingApp.getStringRes(R.string.dbPrice);
    private static final String LOCATION = ShoppingApp.getStringRes(R.string.dbLocation);
    private static final String NOTE = ShoppingApp.getStringRes(R.string.dbNote);

    DBOptionalDataHelper(Context context) {
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
                + QUANTITY + getContext().getString(R.string.dbTextWithComma)
                + PRICE + getContext().getString(R.string.dbTextWithComma)
                + LOCATION + getContext().getString(R.string.dbTextWithComma)
                + NOTE + getContext().getString(R.string.dbTextWithParenthesis);
        db.execSQL(query);
    }

    public void onUpgrade(SQLiteDatabase db, int i, int j) {
        db.execSQL(getContext().getString(R.string.dbDropTable) + TABLE_NAME);
        onCreate(db);
    }

    void readOptionalData(OptionalData optionalData) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(getContext().getString(R.string.dbSelectFrom) + TABLE_NAME
                + getContext().getString(R.string.dbOrderBy) + ITEM_NAME, null);

        if (cursor.moveToFirst()) {
            do { optionalData.readOptionalData(cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
    }

    void addNewOptionalData(String itemName, String quantity, String price, String location, int note) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ITEM_NAME, itemName);
        values.put(QUANTITY, quantity);
        values.put(PRICE, price);
        values.put(LOCATION, location);
        values.put(NOTE, note);

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    void changeQuantity(String itemName, String newQuantity) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(QUANTITY, newQuantity);

        db.update(TABLE_NAME, values, getContext().getString(R.string.dbQuantityQuestion), new String[]{itemName});
        db.close();
    }

    void changePrice(String itemName, String newPrice) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(PRICE, newPrice);

        db.update(TABLE_NAME, values, getContext().getString(R.string.dbPriceQuestion), new String[]{itemName});
        db.close();
    }

    void changeLocation(String itemName, String newLocation) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(LOCATION, newLocation);

        db.update(TABLE_NAME, values, getContext().getString(R.string.dbLocationQuestion), new String[]{itemName});
        db.close();
    }

    void changeNote(String itemName, String newNote) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(NOTE, newNote);

        db.update(TABLE_NAME, values, getContext().getString(R.string.dbNoteQuestion), new String[]{itemName});
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