package ryan.android.shopping;

import java.util.ArrayList;
import java.util.HashMap;

class SearchInventory {

    private HashMap<String, ArrayList<Item>> termMap;

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
        for (int i = 0; i < string.length(); i++) {
            for (int j = 0; j < string.length(); j++) {
                String term = string.substring(i, j);
                ArrayList<Item> itemList = new ArrayList<>();
                if (termMap.containsKey(term)) {
                    termMap.get(term).add(item);
                } else {
                    itemList.add(item);
                    termMap.put(term, itemList);
                }
            }
        }
    }
    
    public ArrayList<Item> getItemListWithTerm(String term) {
        return termMap.get(term);
    }

    public void printTermMap() {

    }

}