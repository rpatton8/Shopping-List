package ryan.android.shopping;

import android.content.Context;

class Status {

    private Context context;

    private Boolean isInStock;
    private Boolean isNeeded;
    private Boolean isPaused;
    private Boolean isChecked;
    private Boolean isExpandedInInventory;
    private Boolean isContractedInInventory;
    private Boolean isExpandedInSearchResults;
    private Boolean isContractedInSearchResults;
    private Boolean isExpandedInShoppingList;
    private Boolean isContractedInShoppingList;
    private Boolean isSelectedInInventory;
    private Boolean isSelectedInSearchResults;
    private Boolean isSelectedInShoppingList;
    
    Status(String itemName, String status, String checked, Context context) {

        setContext(context);

        if (status.equals(getContext().getString(R.string.instock))) {
            setAsInStock();
        } else if (status.equals(getContext().getString(R.string.needed))) {
            setAsNeeded();
        } else if (status.equals(getContext().getString(R.string.paused))) {
            setAsPaused();
        }

        if (checked.equals(getContext().getString(R.string.checked))) {
            setAsChecked();
        } else if (checked.equals(getContext().getString(R.string.unchecked))) {
            setAsUnchecked();
        }

        setAsContractedInInventory();
        setAsContractedInSearchResults();
        setAsContractedInShoppingList();
        setAsUnselectedInInventory();
        setAsUnselectedInSearchResults();
        setAsUnselectedInShoppingList();
    }

    private Status getThis() {
        return this;
    }

    private Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        getThis().context = context;
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

    Boolean isChecked() {
        return isChecked;
    }

    void setAsChecked() {
        isChecked = true;
    }

    Boolean isUnchecked() {
        return !isChecked;
    }

    void setAsUnchecked() {
        isChecked = false;
    }

    Boolean isExpandedInInventory() {
        return isExpandedInInventory;
    }

    void setAsExpandedInInventory() {
        isExpandedInInventory = true;
        isContractedInInventory = false;
    }

    Boolean isContractedInInventory() {
        return isContractedInInventory;
    }

    void setAsContractedInInventory() {
        isContractedInInventory = true;
        isExpandedInInventory = false;
    }

    Boolean isExpandedInSearchResults() {
        return isExpandedInSearchResults;
    }

    void setAsExpandedInSearchResults() {
        isExpandedInSearchResults = true;
        isContractedInSearchResults = false;
    }

    Boolean isContractedInSearchResults() {
        return isContractedInSearchResults;
    }

    void setAsContractedInSearchResults() {
        isContractedInSearchResults = true;
        isExpandedInSearchResults = false;
    }

    Boolean isExpandedInShoppingList() {
        return isExpandedInShoppingList;
    }

    void setAsExpandedInShoppingList() {
        isExpandedInShoppingList = true;
        isContractedInShoppingList = false;
    }

    Boolean isContractedInShoppingList() {
        return isContractedInShoppingList;
    }

    void setAsContractedInShoppingList() {
        isContractedInShoppingList = true;
        isExpandedInShoppingList = false;
    }

    Boolean isSelectedInInventory() {
        return isSelectedInInventory;
    }

    void setAsSelectedInInventory() {
        isSelectedInInventory = true;
    }

    void setAsUnselectedInInventory() {
        isSelectedInInventory = false;
    }

    Boolean isSelectedInSearchResults() {
        return isSelectedInSearchResults;
    }

    void setAsSelectedInSearchResults() {
        isSelectedInSearchResults = true;
    }

    void setAsUnselectedInSearchResults() {
        isSelectedInSearchResults = false;
    }

    Boolean isSelectedInShoppingList() {
        return isSelectedInShoppingList;
    }

    void setAsSelectedInShoppingList() {
        isSelectedInShoppingList = true;
    }

    void setAsUnselectedInShoppingList() {
        isSelectedInShoppingList = false;
    }
    
    public String toString() {
        if (isInStock()) return getContext().getString(R.string.inStockCap);
        if (isNeeded()) return getContext().getString(R.string.neededCap);
        if (isPaused()) return getContext().getString(R.string.pausedCap);
        return null;
    }

}