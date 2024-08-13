package ryan.android.shopping;

public class Status {

    private String itemName;
    private Boolean isInStock;
    private Boolean isNeeded;
    private Boolean isPaused;
    private Boolean isClicked;
    private Boolean isSelected;
    private Boolean isChecked;

    public Status(String itemName, String isInStock, String isNeeded, String isPaused) {
        this.itemName = itemName;
        if (isInStock.equals("true")) {
            this.isInStock = true;
            this.isNeeded = false;
            this.isPaused = false;
        } else if (isNeeded.equals("true")) {
            this.isInStock = false;
            this.isNeeded = true;
            this.isPaused = false;
        } else if (isPaused.equals("true")) {
            this.isInStock = false;
            this.isNeeded = false;
            this.isPaused = true;
        }
        isClicked = false;
        isSelected = false;
        isChecked = false;
    }

    public void setAsClicked() {
        this.isClicked = true;
    }

    public void setAsUnclicked() {
        this.isClicked = false;
    }

    public Boolean isClicked() {
        return isClicked;
    }

    public void setAsSelected() {
        this.isSelected = true;
    }

    public void setAsUnselected() {
        this.isSelected = false;
    }

    public Boolean isSelected() {
        return isSelected;
    }

    public void setAsChecked() {
        this.isChecked = true;
    }

    public void setAsUnchecked() {
        this.isChecked = false;
    }

    public Boolean isChecked() {
        return isChecked;
    }

    public Boolean isInStock() {
        return isInStock;
    }

    public Boolean isNeeded() {
        return isNeeded;
    }

    public Boolean isPaused() {
        return isPaused;
    }

    public void setAsInStock() {
        isInStock = true;
        isNeeded = false;
        isPaused = false;
    }

    public void setAsNeeded() {
        isInStock = false;
        isNeeded = true;
        isPaused = false;
    }

    public void setAsPaused() {
        isInStock = false;
        isNeeded = false;
        isPaused = true;
    }

}