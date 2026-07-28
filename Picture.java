package ryan.android.shopping;

import android.net.Uri;

class Picture {

    private String pictureName;
    private Uri pictureURI;

    Picture(String pictureName, Uri pictureURI) {
        getThis().pictureName = pictureName;
        getThis().pictureURI = pictureURI;
    }
    
    private Picture getThis() {
        return this;
    }

    public String getPictureName() {
        return pictureName;
    }

    public void setPictureName(String pictureName) {
        getThis().pictureName = pictureName;
    }

    public Uri getURI() {
        return pictureURI;
    }

    public void setURI(Uri pictureURI) {
        getThis().pictureURI = pictureURI;
    }

}