package ryan.android.shopping;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

class DBPictureHelper extends SQLiteOpenHelper {

    private Context context;

    private static final String DB_NAME = ShoppingApp.getStringRes(R.string.dbPicturesCap);
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = ShoppingApp.getStringRes(R.string.dbPictures);
    private static final String ID = ShoppingApp.getStringRes(R.string.dbID);
    private static final String PICTURE_NAME = ShoppingApp.getStringRes(R.string.dbPictureName);
    private static final String PICTURE_URI = ShoppingApp.getStringRes(R.string.dbPictureURI);

    DBPictureHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        setContext(context);
    }

    private DBPictureHelper getThis() {
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
                + PICTURE_NAME + getContext().getString(R.string.dbTextWithComma)
                + PICTURE_URI + getContext().getString(R.string.dbTextWithParenthesis);
        db.execSQL(query);
    }

    public void onUpgrade(SQLiteDatabase db, int i, int j) {
        db.execSQL(getContext().getString(R.string.dbDropTable) + TABLE_NAME);
        onCreate(db);
    }

    void readPictureData(PictureData pictureData) {

        SQLiteDatabase db = getThis().getReadableDatabase();
        Cursor cursor = db.rawQuery(getContext().getString(R.string.dbSelectFrom) + TABLE_NAME
                + getContext().getString(R.string.dbOrderBy) + PICTURE_NAME, null);

        if (cursor.moveToFirst()) {
            do { pictureData.readPictureData(cursor.getString(1), cursor.getString(2));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
    }

    void addNewPictureData(String pictureName, String pictuerURI) {

        SQLiteDatabase db = getThis().getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(PICTURE_NAME, pictureName);
        values.put(PICTURE_URI, pictuerURI);

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    void changePicture(String pictureName, String newPictureURI) {

        SQLiteDatabase db = getThis().getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(PICTURE_URI, newPictureURI);

        db.update(TABLE_NAME, values, getContext().getString(R.string.dbPictureNameQuestion), new String[]{pictureName});
        db.close();
    }

    void deletePicture(String pictureName) {

        SQLiteDatabase db = getThis().getWritableDatabase();
        db.delete(TABLE_NAME, getContext().getString(R.string.dbPictureNameQuestion), new String[]{pictureName});
        db.close();
    }

    void deleteDatabase() {

        getContext().deleteDatabase(DB_NAME);

    }
}