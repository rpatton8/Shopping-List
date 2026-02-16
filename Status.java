package ryan.android.shopping;

public class Status {

    private String itemName;
    private Boolean isInStock;
    private Boolean isNeeded;
    private Boolean isPaused;
    private Boolean isExpandedInInventory;
    private Boolean isExpandedInShoppingList;
    private Boolean isSelectedInInventory;
    private Boolean isSelectedInShoppingList;
    private Boolean isChecked;

    Status(String itemName, String status, String checked) {
        this.itemName = itemName;
        if (status.equals("instock")) {
            this.isInStock = true;
            this.isNeeded = false;
            this.isPaused = false;
        } else if (status.equals("needed")) {
            this.isInStock = false;
            this.isNeeded = true;
            this.isPaused = false;
        } else if (status.equals("paused")) {
            this.isInStock = false;
            this.isNeeded = false;
            this.isPaused = true;
        }
        isExpandedInInventory = false;
        isExpandedInShoppingList = false;
        isSelectedInInventory = false;
        isSelectedInShoppingList = false;
        isChecked = false;
    }

    public void setAsExpandedInInventory() {
        this.isExpandedInInventory = true;
    }

    public void setAsContractedInInventory() {
        this.isExpandedInInventory = false;
    }

    public Boolean isExpandedInInventory() {
        return isExpandedInInventory;
    }

    public void setAsExpandedInShoppingList() {
        this.isExpandedInShoppingList = true;
    }

    public void setAsContractedInShoppingList() {
        this.isExpandedInShoppingList = false;
    }

    public Boolean isExpandedInShoppingList() {
        return isExpandedInShoppingList;
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