package ryan.android.shopping;

class Picture {

    private String pictureName;
    private String pictureURL;

    Picture(String pictureName, String pictureURL) {
        getThis().pictureName = pictureName;
        getThis().pictureURL = pictureURL;
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

    public String getURL() {
        return pictureURL;
    }

    public void setURL(String pictureURL) {
        getThis().pictureURL = pictureURL;
    }

}