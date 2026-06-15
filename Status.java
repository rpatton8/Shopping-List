package ryan.android.shopping;

import android.content.Context;

class Status {

    private Context context;

    private boolean isInStock;
    private boolean isNeeded;
    private boolean isPaused;
    private boolean isChecked;
    private boolean isExpandedInInventory;
    private boolean isContractedInInventory;
    private boolean isExpandedInSearchResults;
    private boolean isContractedInSearchResults;
    private boolean isExpandedInShoppingList;
    private boolean isContractedInShoppingList;
    private boolean isSelectedInInventory;
    private boolean isSelectedInSearchResults;
    private boolean isSelectedInShoppingList;
    
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

    private void setContext(Context context) {
        getThis().context = context;
    }

    boolean isInStock() {
        return isInStock;
    }

    boolean isNeeded() {
        return isNeeded;
    }

    boolean isPaused() {
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

    boolean isChecked() {
        return isChecked;
    }

    void setAsChecked() {
        isChecked = true;
    }

    boolean isUnchecked() {
        return !isChecked;
    }

    void setAsUnchecked() {
        isChecked = false;
    }

    boolean isExpandedInInventory() {
        return isExpandedInInventory;
    }

    void setAsExpandedInInventory() {
        isExpandedInInventory = true;
        isContractedInInventory = false;
    }

    boolean isContractedInInventory() {
        return isContractedInInventory;
    }

    void setAsContractedInInventory() {
        isContractedInInventory = true;
        isExpandedInInventory = false;
    }

    boolean isExpandedInSearchResults() {
        return isExpandedInSearchResults;
    }

    void setAsExpandedInSearchResults() {
        isExpandedInSearchResults = true;
        isContractedInSearchResults = false;
    }

    boolean isContractedInSearchResults() {
        return isContractedInSearchResults;
    }

    void setAsContractedInSearchResults() {
        isContractedInSearchResults = true;
        isExpandedInSearchResults = false;
    }

    boolean isExpandedInShoppingList() {
        return isExpandedInShoppingList;
    }

    void setAsExpandedInShoppingList() {
        isExpandedInShoppingList = true;
        isContractedInShoppingList = false;
    }

    boolean isContractedInShoppingList() {
        return isContractedInShoppingList;
    }

    void setAsContractedInShoppingList() {
        isContractedInShoppingList = true;
        isExpandedInShoppingList = false;
    }

    boolean isSelectedInInventory() {
        return isSelectedInInventory;
    }

    void setAsSelectedInInventory() {
        isSelectedInInventory = true;
    }

    void setAsUnselectedInInventory() {
        isSelectedInInventory = false;
    }

    boolean isSelectedInSearchResults() {
        return isSelectedInSearchResults;
    }

    void setAsSelectedInSearchResults() {
        isSelectedInSearchResults = true;
    }

    void setAsUnselectedInSearchResults() {
        isSelectedInSearchResults = false;
    }

    boolean isSelectedInShoppingList() {
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