package ryan.android.shopping;

import java.util.ArrayList;
import java.util.HashMap;

class SearchInventory {

    private final HashMap<String, ArrayList<Item>> termMap;

    SearchInventory() {
        termMap = new HashMap<>();
    }

    public void addNewItem(Item item) {
        String itemName = item.getName();
        String brandType = item.getBrandType();
        populateTermMap(itemName, item);
        populateTermMap(brandType, item);
    }

    private void populateTermMap(String string, Item item) {
        for (int j = 0; j <= string.length(); j++) {
            for (int i = 0; i <= j; i++) {
                String term = string.substring(i, j).toLowerCase();
                ArrayList<Item> itemList = new ArrayList<>();
                if (termMap.containsKey(term) && !termMap.get(term).contains(item)) {
                    // map contains term but not item
                    termMap.get(term).add(item);
                } else if (!term.equals("")) {
                    // term is not the empty string and map doesn't contain it already
                    itemList.add(item);
                    termMap.put(term, itemList);
                }
            }
        }
    }
    
    public ArrayList<Item> getSearchResults(String term) {
        return termMap.get(term.toLowerCase());
    }

    public int numSearchResults(String term) {

        if (termMap.get(term.toLowerCase()) != null)  {
            return termMap.get(term.toLowerCase()).size();
        }  else return 0;
    }
}