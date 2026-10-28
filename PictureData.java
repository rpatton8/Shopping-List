package ryan.android.shopping;

import android.content.Context;
import java.util.HashMap;

class PictureData {

    private Context context;
    private HashMap<String, Picture> pictureMap;

    PictureData(Context context) {
        setContext(context);
        setPictureMap(new HashMap<>());
    }

    private PictureData getThis() {
        return this;
    }

    private Context getContext() {
        return getThis().context;
    }

    private void setContext(Context context) {
        getThis().context = context;
    }

    HashMap<String, Picture> getPictureMap() {
        return getThis().pictureMap;
    }

    private void setPictureMap(HashMap<String, Picture> pictureMap) {
        getThis().pictureMap = pictureMap;
    }

    void readPictureData(String itemName, String pictureURI) {
        Picture picture = new Picture(itemName, pictureURI);
        getPictureMap().put(itemName, picture);
    }

}