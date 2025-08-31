package ryan.android.shopping;

public class Status {

    private String itemName;
    private Boolean isInStock;
    private Boolean isNeeded;
    private Boolean isPaused;
    private Boolean isClickedInInventory;
    private Boolean isClickedInShopByStore;
    private Boolean isSelectedInInventory;
    private Boolean isSelectedInShopByStore;
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
        isClickedInInventory = false;
        isClickedInShopByStore = false;
        isSelectedInInventory = false;
        isSelectedInShopByStore = false;
        isChecked = false;
    }

    public void setAsClickedInInventory() {
        this.isClickedInInventory = true;
    }

    public void setAsUnclickedInInventory() {
        this.isClickedInInventory = false;
    }

    public Boolean isClickedInInventory() {
        return isClickedInInventory;
    }

    public void setAsClickedInShopByStore() {
        this.isClickedInShopByStore = true;
    }

    public void setAsUnclickedInShopByStore() {
        this.isClickedInShopByStore = false;
    }

    public Boolean isClickedInShopByStore() {
        return isClickedInShopByStore;
    }

    public void setAsSelectedInInventory() {
        this.isSelectedInInventory = true;
    }

    public void setAsUnselectedInInventory() {
        this.isSelectedInInventory = false;
    }

    public Boolean isSelectedInInventory() {
        return isSelectedInInventory;
    }

    public void setAsSelectedInShopByStore() {
        this.isSelectedInShopByStore = true;
    }

    public void setAsUnselectedInShopByStore() {
        this.isSelectedInShopByStore = false;
    }

    public Boolean isSelectedInShopByStore() {
        return isSelectedInShopByStore;
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