package ryan.android.shopping;

import android.app.Application;

public class ShoppingApp extends Application {

    private static ShoppingApp instance;

    public static String getStringRes(int resID) {
        return instance.getString(resID);
    }

    public void onCreate() {
        super.onCreate();
        instance = this;
    }

}