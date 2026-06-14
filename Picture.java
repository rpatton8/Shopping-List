package ryan.android.shopping;

class Picture {

    private String pictureName;
    private String pictureURL;

    Picture(String pictureName, String pictureURL) {
        this.pictureName = pictureName;
        this.pictureURL = pictureURL;
    }

    public String getPictureName() {
        return pictureName;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }

    public String getURL() {
        return pictureURL;
    }

    public void setURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }

}