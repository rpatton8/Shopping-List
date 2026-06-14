package ryan.android.shopping;

import android.content.Context;
import java.util.ArrayList;
import java.util.HashMap;

class SearchAlgorithm {

    private Context context;
    private HashMap<String, ArrayList<Item>> termMap;

    SearchAlgorithm(Context context) {
        setContext(context);
        setTermMap(new HashMap<String, ArrayList<Item>>());
    }

    private SearchAlgorithm getThis() {
        return this;
    }

    private Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        getThis().context = context;
    }

    public HashMap<String, ArrayList<Item>> getTermMap() {
        return termMap;
    }

    public void setTermMap(HashMap<String, ArrayList<Item>> termMap) {
        getThis().termMap = termMap;
    }

    void addNewItem(Item item) {
        populateTermMap(item.getItemName(), item);
        populateTermMap(item.getBrandType(), item);
    }

    void removeItem(Item item) {
        getTermMap().remove(item.getItemName());
        getTermMap().remove(item.getBrandType());
    }

    private void populateTermMap(String string, Item item) {
        for (int j = 0; j <= string.length(); j++) {
            for (int i = 0; i <= j; i++) {
                String term = string.substring(i, j).toLowerCase();
                if ((term.length() <= 2) && (i != 0)) break;
                ArrayList<Item> itemList = new ArrayList<>();
                if (getTermMap().containsKey(term) && !getTermMap().get(term).contains(item)) {
                    // map contains term but not item
                    getTermMap().get(term).add(item);
                } else if (!term.equals(getContext().getString(R.string.emptyString))) {
                    // term is not the empty string and map doesn't contain it
                    itemList.add(item);
                    getTermMap().put(term, itemList);
                }
            }
        }
    }

    ArrayList<Item> getSearchResults(String term) {
        return getTermMap().get(term.toLowerCase());
    }

    int numSearchResults(String term) {
        return getTermMap().get(term.toLowerCase()).size();
    }

}