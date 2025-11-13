package ryan.android.shopping;

public class Status {

    private Boolean isInStock;
    private Boolean isNeeded;
    private Boolean isPaused;
    private Boolean isClickedInInventory;
    private Boolean isClickedInShoppingList;
    private Boolean isSelectedInInventory;
    private Boolean isSelectedInShoppingList;
    private Boolean isChecked;

    Status(String itemName, String status, String checked) {

        switch(status) {
            case "instock":
                this.isInStock = true;
                this.isNeeded = false;
                this.isPaused = false;
            case "needed":
                this.isInStock = false;
                this.isNeeded = true;
                this.isPaused = false;
            case "paused":
                this.isInStock = false;
                this.isNeeded = false;
                this.isPaused = true;
        }

        isClickedInInventory = false;
        isClickedInShoppingList = false;
        isSelectedInInventory = false;
        isSelectedInShoppingList = false;

        switch(checked) {
            case "checked":
                isChecked = true;
            case "unchecked":
                isChecked = false;
        }
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

    public void setAsClickedInShoppingList() {
        this.isClickedInShoppingList = true;
    }

    public void setAsUnclickedInShoppingList() {
        this.isClickedInShoppingList = false;
    }

    public Boolean isClickedInShoppingList() {
        return isClickedInShoppingList;
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

    public void setAsSelectedInShoppingList() {
        this.isSelectedInShoppingList = true;
    }

    public void setAsUnselectedInShoppingList() {
        this.isSelectedInShoppingList = false;
    }

    public Boolean isSelectedInShoppingList() {
        return isSelectedInShoppingList;
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