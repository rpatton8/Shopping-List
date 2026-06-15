package ryan.android.shopping;

import android.app.Application;

public class ShoppingApp extends Application {

    private static ShoppingApp instance;

    static String getStringRes(int resID) {
        return getInstance().getString(resID);
    }

    public void onCreate() {
        super.onCreate();
        setInstance(getThis());
    }

    private static ShoppingApp getInstance() {
        return instance;
    }

    private static void setInstance(ShoppingApp instance) {
        ShoppingApp.instance = instance;
    }

    private ShoppingApp getThis() {
        return this;
    }

}