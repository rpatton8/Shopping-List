package ryan.android.shopping;

import java.util.ArrayList;
import java.util.HashMap;

class SearchAlgorithm {

    private HashMap<String, ArrayList<Item>> termMap;

    SearchAlgorithm() {
        termMap = new HashMap<>();
    }

    void addNewItem(Item item) {
        populateTermMap(item.getName(), item);
        populateTermMap(item.getBrandType(), item);
    }

    void removeItem(Item item) {
        // to do
    }

    private void populateTermMap(String string, Item item) {
        for (int j = 0; j <= string.length(); j++) {
            for (int i = 0; i <= j; i++) {
                String term = string.substring(i, j).toLowerCase();
                if ((term.length() <= 2) && (i != 0)) break;
                ArrayList<Item> itemList = new ArrayList<>();
                if (termMap.containsKey(term) && !termMap.get(term).contains(item)) {
                    // map contains term but not item
                    termMap.get(term).add(item);
                } else if (!term.equals("")) {
                    // term is not the empty string and map doesn't contain it
                    itemList.add(item);
                    termMap.put(term, itemList);
                }
            }
        }
    }

    ArrayList<Item> getSearchResults(String term) {
        return termMap.get(term.toLowerCase());
    }

    int numSearchResults(String term) {
        if (termMap.get(term.toLowerCase()) != null)  {
            return termMap.get(term.toLowerCase()).size();
        }  else return 0;
    }
}