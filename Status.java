package ryan.android.shopping;

class Status {

    private Boolean isInStock;
    private Boolean isNeeded;
    private Boolean isPaused;
    private Boolean isExpandedInInventory;
    private Boolean isContractedInInventory;
    private Boolean isExpandedInSearchResults;
    private Boolean isContractedInSearchResults;
    private Boolean isExpandedInShoppingList;
    private Boolean isContractedInShoppingList;
    private Boolean isSelectedInInventory;
    private Boolean isSelectedInSearchResults;
    private Boolean isSelectedInShoppingList;
    private Boolean isChecked;

    Status(String itemName, String status, String checked) {

        if (status.equals("instock")) {
            isInStock = true;
            isNeeded = false;
            isPaused = false;
        } else if (status.equals("needed")) {
            isInStock = false;
            isNeeded = true;
            isPaused = false;
        } else if (status.equals("paused")) {
            isInStock = false;
            isNeeded = false;
            isPaused = true;
        }

        if (checked.equals("checked")) {
            setAsChecked();
        } else if (checked.equals("unchecked")) {
            setAsUnchecked();
        }

        isExpandedInInventory = false;
        isContractedInInventory = true;
        isExpandedInSearchResults = false;
        isContractedInSearchResults = true;
        isExpandedInShoppingList = false;
        isContractedInShoppingList = true;
        isSelectedInInventory = false;
        isSelectedInSearchResults = false;
        isSelectedInShoppingList = false;
        isChecked = false;
    }

    void setAsExpandedInInventory() {
        isExpandedInInventory = true;
        isContractedInInventory = false;
    }

    void setAsContractedInInventory() {
        isContractedInInventory = true;
        isExpandedInInventory = false;
    }

    Boolean isExpandedInInventory() {
        return isExpandedInInventory;
    }

    Boolean isContractedInInventory() {
        return isContractedInInventory;
    }

    void setAsExpandedInSearchResults() {
        isExpandedInSearchResults = true;
        isContractedInSearchResults = false;
    }

    void setAsContractedInSearchResults() {
        isContractedInSearchResults = true;
        isExpandedInSearchResults = false;
    }

    Boolean isExpandedInSearchResults() {
        return isExpandedInSearchResults;
    }

    Boolean isContractedInSearchResults() {
        return isContractedInSearchResults;
    }

    void setAsExpandedInShoppingList() {
        isExpandedInShoppingList = true;
        isContractedInShoppingList = false;
    }

    void setAsContractedInShoppingList() {
        isContractedInShoppingList = true;
        isExpandedInShoppingList = false;
    }

    Boolean isExpandedInShoppingList() {
        return isExpandedInShoppingList;
    }

    Boolean isContractedInShoppingList() {
        return isContractedInShoppingList;
    }

    void setAsSelectedInInventory() {
        isSelectedInInventory = true;
    }

    void setAsUnselectedInInventory() {
        isSelectedInInventory = false;
    }

    Boolean isSelectedInInventory() {
        return isSelectedInInventory;
    }

    void setAsSelectedInSearchResults() {
        isSelectedInSearchResults = true;
    }

    void setAsUnselectedInSearchResults() {
        isSelectedInSearchResults = false;
    }

    Boolean isSelectedInSearchResults() {
        return isSelectedInSearchResults;
    }

    void setAsSelectedInShoppingList() {
        isSelectedInShoppingList = true;
    }

    void setAsUnselectedInShoppingList() {
        isSelectedInShoppingList = false;
    }

    Boolean isSelectedInShoppingList() {
        return isSelectedInShoppingList;
    }

    void setAsChecked() {
        isChecked = true;
    }

    void setAsUnchecked() {
        isChecked = false;
    }

    Boolean isChecked() {
        return isChecked;
    }

    Boolean isUnchecked() {
        return !isChecked;
    }

    Boolean isInStock() {
        return isInStock;
    }

    Boolean isNeeded() {
        return isNeeded;
    }

    Boolean isPaused() {
        return isPaused;
    }

    void setAsInStock() {
        isInStock = true;
        isNeeded = false;
        isPaused = false;
    }

    void setAsNeeded() {
        isInStock = false;
        isNeeded = true;
        isPaused = false;
    }

    void setAsPaused() {
        isInStock = false;
        isNeeded = false;
        isPaused = true;
    }

    public String toString() {
        if (isInStock) return "In Stock";
        if (isNeeded) return "Needed";
        if (isPaused) return "Paused";
        return null;
    }

}