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
    private boolean isExpandedInIndividualCategories;
    private boolean isContractedInIndividualCategories;
    private boolean isExpandedInIndividualStores;
    private boolean isContractedInIndividualStores;
    private boolean isSelectedInInventory;
    private boolean isSelectedInSearchResults;
    private boolean isSelectedInShoppingList;
    private boolean isSelectedInIndividualCategories;
    private boolean isSelectedInIndividualStores;

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
        setAsContractedInIndividualCategories();
        setAsContractedInIndividualStores();
        setAsUnselectedInInventory();
        setAsUnselectedInSearchResults();
        setAsUnselectedInShoppingList();
        setAsUnselectedInIndividualCategories();
        setAsUnselectedInIndividualStores();

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

    public boolean isExpandedInIndividualCategories() {
        return isExpandedInIndividualCategories;
    }

    public void setAsExpandedInIndividualCategories() {
        isExpandedInIndividualCategories = true;
        isContractedInIndividualCategories = false;
    }

    public boolean isContractedInIndividualCategories() {
        return isContractedInIndividualCategories;
    }

    public void setAsContractedInIndividualCategories() {
        isContractedInIndividualCategories = true;
        isExpandedInIndividualCategories = false;
    }

    public boolean isExpandedInIndividualStores() {
        return isExpandedInIndividualStores;
    }

    public void setAsExpandedInIndividualStores() {
        isExpandedInIndividualStores = true;
        isContractedInIndividualStores = false;
    }

    public boolean isContractedInIndividualStores() {
        return isContractedInIndividualStores;
    }

    public void setAsContractedInIndividualStores() {
        isContractedInIndividualStores = true;
        isExpandedInIndividualStores = false;
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

    boolean isSelectedInIndividualCategories() {
        return isSelectedInIndividualCategories;
    }

    void setAsSelectedInIndividualCategories() {
        isSelectedInIndividualCategories = true;
    }

    void setAsUnselectedInIndividualCategories() {
        isSelectedInIndividualCategories = false;
    }

    boolean isSelectedInIndividualStores() {
        return isSelectedInIndividualStores;
    }

    void setAsSelectedInIndividualStores() {
        isSelectedInIndividualStores = true;
    }

    void setAsUnselectedInIndividualStores() {
        isSelectedInIndividualStores = false;
    }

    public String toString() {
        if (isInStock()) return getContext().getString(R.string.inStockCap);
        if (isNeeded()) return getContext().getString(R.string.neededCap);
        if (isPaused()) return getContext().getString(R.string.pausedCap);
        return null;
    }

}