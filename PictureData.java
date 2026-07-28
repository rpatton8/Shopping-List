package ryan.android.shopping;

import android.content.Context;
import java.util.HashMap;

class PictureData {

    private Context context;
    private HashMap<String, Picture> pictureMap;

    PictureData() {
        setContext(context);
        setPictureMap(new HashMap<>());
    }

    private PictureData getThis() {
        return this;
    }

    private Context getContext() {
        return context;
    }

    private void setContext(Context context) {
        getThis().context = context;
    }

    HashMap<String, Picture> getPictureMap() {
        return pictureMap;
    }

    private void setPictureMap(HashMap<String, Picture> pictureMap) {
        getThis().pictureMap = pictureMap;
    }

    void readPictureData(String itemName, String pictureURI) {

    }

}