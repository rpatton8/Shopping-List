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
        return getThis().context;
    }

    private void setContext(Context context) {
        getThis().context = context;
    }

    boolean isInStock() {
        return getThis().isInStock;
    }

    boolean isNeeded() {
        return getThis().isNeeded;
    }

    boolean isPaused() {
        return getThis().isPaused;
    }

    void setAsInStock() {
        getThis().isInStock = true;
        getThis().isNeeded = false;
        getThis().isPaused = false;
    }

    void setAsNeeded() {
        getThis().isInStock = false;
        getThis().isNeeded = true;
        getThis().isPaused = false;
    }

    void setAsPaused() {
        getThis().isInStock = false;
        getThis().isNeeded = false;
        getThis().isPaused = true;
    }

    boolean isChecked() {
        return getThis().isChecked;
    }

    void setAsChecked() {
        getThis().isChecked = true;
    }

    boolean isUnchecked() {
        return !getThis().isChecked;
    }

    void setAsUnchecked() {
        getThis().isChecked = false;
    }

    boolean isExpandedInInventory() {
        return getThis().isExpandedInInventory;
    }

    void setAsExpandedInInventory() {
        getThis().isExpandedInInventory = true;
        getThis().isContractedInInventory = false;
    }

    boolean isContractedInInventory() {
        return getThis().isContractedInInventory;
    }

    void setAsContractedInInventory() {
        getThis().isContractedInInventory = true;
        getThis().isExpandedInInventory = false;
    }

    boolean isExpandedInSearchResults() {
        return getThis().isExpandedInSearchResults;
    }

    void setAsExpandedInSearchResults() {
        getThis().isExpandedInSearchResults = true;
        getThis().isContractedInSearchResults = false;
    }

    boolean isContractedInSearchResults() {
        return getThis().isContractedInSearchResults;
    }

    void setAsContractedInSearchResults() {
        getThis().isContractedInSearchResults = true;
        getThis().isExpandedInSearchResults = false;
    }

    boolean isExpandedInShoppingList() {
        return getThis().isExpandedInShoppingList;
    }

    void setAsExpandedInShoppingList() {
        getThis().isExpandedInShoppingList = true;
        getThis().isContractedInShoppingList = false;
    }

    boolean isContractedInShoppingList() {
        return getThis().isContractedInShoppingList;
    }

    void setAsContractedInShoppingList() {
        getThis().isContractedInShoppingList = true;
        getThis().isExpandedInShoppingList = false;
    }

    public boolean isExpandedInIndividualCategories() {
        return getThis().isExpandedInIndividualCategories;
    }

    public void setAsExpandedInIndividualCategories() {
        getThis().isExpandedInIndividualCategories = true;
        getThis().isContractedInIndividualCategories = false;
    }

    public boolean isContractedInIndividualCategories() {
        return getThis().isContractedInIndividualCategories;
    }

    public void setAsContractedInIndividualCategories() {
        getThis().isContractedInIndividualCategories = true;
        getThis().isExpandedInIndividualCategories = false;
    }

    public boolean isExpandedInIndividualStores() {
        return getThis().isExpandedInIndividualStores;
    }

    public void setAsExpandedInIndividualStores() {
        getThis().isExpandedInIndividualStores = true;
        getThis().isContractedInIndividualStores = false;
    }

    public boolean isContractedInIndividualStores() {
        return getThis().isContractedInIndividualStores;
    }

    public void setAsContractedInIndividualStores() {
        getThis().isContractedInIndividualStores = true;
        getThis().isExpandedInIndividualStores = false;
    }

    boolean isSelectedInInventory() {
        return getThis().isSelectedInInventory;
    }

    void setAsSelectedInInventory() {
        getThis().isSelectedInInventory = true;
    }

    void setAsUnselectedInInventory() {
        getThis().isSelectedInInventory = false;
    }

    boolean isSelectedInSearchResults() {
        return getThis().isSelectedInSearchResults;
    }

    void setAsSelectedInSearchResults() {
        getThis().isSelectedInSearchResults = true;
    }

    void setAsUnselectedInSearchResults() {
        getThis().isSelectedInSearchResults = false;
    }

    boolean isSelectedInShoppingList() {
        return getThis().isSelectedInShoppingList;
    }

    void setAsSelectedInShoppingList() {
        getThis().isSelectedInShoppingList = true;
    }

    void setAsUnselectedInShoppingList() {
        getThis().isSelectedInShoppingList = false;
    }

    boolean isSelectedInIndividualCategories() {
        return getThis().isSelectedInIndividualCategories;
    }

    void setAsSelectedInIndividualCategories() {
        getThis().isSelectedInIndividualCategories = true;
    }

    void setAsUnselectedInIndividualCategories() {
        getThis().isSelectedInIndividualCategories = false;
    }

    boolean isSelectedInIndividualStores() {
        return getThis().isSelectedInIndividualStores;
    }

    void setAsSelectedInIndividualStores() {
        getThis().isSelectedInIndividualStores = true;
    }

    void setAsUnselectedInIndividualStores() {
        getThis().isSelectedInIndividualStores = false;
    }

    public String toString() {
        if (isInStock()) return getContext().getString(R.string.instock);
        if (isNeeded()) return getContext().getString(R.string.needed);
        if (isPaused()) return getContext().getString(R.string.paused);
        return null;
    }

}