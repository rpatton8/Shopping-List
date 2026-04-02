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

        switch (status) {
            case "instock":
                isInStock = true;
                isNeeded = false;
                isPaused = false;
                break;
            case "needed":
                isInStock = false;
                isNeeded = true;
                isPaused = false;
                break;
            case "paused":
                isInStock = false;
                isNeeded = false;
                isPaused = true;
                break;
        }
        
        switch (checked) {
            case "checked":
                setAsChecked();
                break;
            case "unchecked":
                setAsUnchecked();
                break;
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

    public void setAsExpandedInInventory() {
        isExpandedInInventory = true;
        isContractedInInventory = false;
    }

    public void setAsContractedInInventory() {
        isContractedInInventory = true;
        isExpandedInInventory = false;
    }

    public Boolean isExpandedInInventory() {
        return isExpandedInInventory;
    }

    public Boolean isContractedInInventory() {
        return isContractedInInventory;
    }

    public void setAsExpandedInSearchResults() {
        isExpandedInSearchResults = true;
        isContractedInSearchResults = false;
    }

    public void setAsContractedInSearchResults() {
        isContractedInSearchResults = true;
        isExpandedInSearchResults = false;
    }

    public Boolean isExpandedInSearchResults() {
        return isExpandedInSearchResults;
    }

    public Boolean isContractedInSearchResults() {
        return isContractedInSearchResults;
    }

    public void setAsExpandedInShoppingList() {
        isExpandedInShoppingList = true;
        isContractedInShoppingList = false;
    }

    public void setAsContractedInShoppingList() {
        isContractedInShoppingList = true;
        isExpandedInShoppingList = false;
    }

    public Boolean isExpandedInShoppingList() {
        return isExpandedInShoppingList;
    }

    public Boolean isContractedInShoppingList() {
        return isContractedInShoppingList;
    }

    public void setAsSelectedInInventory() {
        isSelectedInInventory = true;
    }

    public void setAsUnselectedInInventory() {
        isSelectedInInventory = false;
    }

    public Boolean isSelectedInInventory() {
        return isSelectedInInventory;
    }

    public void setAsSelectedInSearchResults() {
        isSelectedInSearchResults = true;
    }

    public void setAsUnselectedInSearchResults() {
        isSelectedInSearchResults = false;
    }

    public Boolean isSelectedInSearchResults() {
        return isSelectedInSearchResults;
    }

    public void setAsSelectedInShoppingList() {
        isSelectedInShoppingList = true;
    }

    public void setAsUnselectedInShoppingList() {
        isSelectedInShoppingList = false;
    }

    public Boolean isSelectedInShoppingList() {
        return isSelectedInShoppingList;
    }

    public void setAsChecked() {
        isChecked = true;
    }

    public void setAsUnchecked() {
        isChecked = false;
    }

    public Boolean isChecked() {
        return isChecked;
    }

    public Boolean isUnchecked() {
        return !isChecked;
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

    public String toString() {
        if (isInStock) return "In Stock";
        if (isNeeded) return "Needed";
        if (isPaused) return "Paused";
        return null;
    }

}