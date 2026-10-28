package ryan.android.shopping;

import android.net.Uri;

class Picture {

    private String pictureName;
    private String pictureURI;

    Picture(String pictureName, String pictureURI) {
        getThis().pictureName = pictureName;
        getThis().pictureURI = pictureURI;
    }
    
    private Picture getThis() {
        return this;
    }

    public String getPictureName() {
        return getThis().pictureName;
    }

    public void setPictureName(String pictureName) {
        getThis().pictureName = pictureName;
    }

    public String getURI() {
        return getThis().pictureURI;
    }

    public void setURI(String pictureURI) {
        getThis().pictureURI = pictureURI;
    }

}