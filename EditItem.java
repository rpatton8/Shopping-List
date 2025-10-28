package ryan.android.shopping;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.ArrayList;

public class EditItem extends Fragment {

    private Shopping shopping;
    private CategoryData categoryData;
    private StoreData storeData;
    private DBItemHelper dbItemHelper;
    private DBCategoryHelper dbCategoryHelper;
    private DBStoreHelper dbStoreHelper;

    private EditText itemNameInput;
    private EditText itemTypeInput;
    private Spinner categorySpinner;
    private Spinner storeSpinner;
    private EditText itemCategoryInput;
    private EditText itemStoreInput;

    public EditItem() {}

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.edit_item, container, false);

        shopping = (Shopping) getActivity();
        dbItemHelper = new DBItemHelper(getActivity());
        dbCategoryHelper = new DBCategoryHelper(getActivity());
        dbStoreHelper = new DBStoreHelper(getActivity());
        categoryData = shopping.getCategoryData();
        storeData = shopping.getStoreData();

        itemNameInput = view.findViewById(R.id.itemNameInput);
        itemTypeInput = view.findViewById(R.id.itemTypeInput);
        itemCategoryInput = view.findViewById(R.id.itemCategoryInput);
        itemStoreInput = view.findViewById(R.id.itemStoreInput);
        Button editItemButton = view.findViewById(R.id.editItemButton);
        Button cancelButton = view.findViewById(R.id.cancelButton);

        if(shopping.editItemInInventory) {
            itemNameInput.setText(shopping.selectedItemInInventory.getName());
            itemTypeInput.setText(shopping.selectedItemInInventory.getBrand());
        } else if (shopping.editItemInShoppingList) {
            itemNameInput.setText(shopping.selectedItemInShoppingList.getName());
            itemTypeInput.setText(shopping.selectedItemInShoppingList.getBrand());
        }
        itemCategoryInput.setText("");
        itemStoreInput.setText("");

        ArrayList<String> categorySpinnerData = categoryData.getCategoryListWithAddNew();
        categorySpinner = view.findViewById(R.id.categorySpinner);
        ArrayAdapter adapter1 = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, categorySpinnerData);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter1);

        int categorySpinnerPosition = 0;
        if(shopping.editItemInInventory) {
            categorySpinnerPosition = adapter1.getPosition(shopping.selectedItemInInventory.getCategory().toString());
        } else if (shopping.editItemInShoppingList) {
            categorySpinnerPosition = adapter1.getPosition(shopping.selectedItemInShoppingList.getCategory().toString());
        }
        categorySpinner.setSelection(categorySpinnerPosition);

        ArrayList<String> storeSpinnerData = storeData.getStoreListWithAddNew();
        storeSpinner = view.findViewById(R.id.storeSpinner);
        ArrayAdapter adapter2 = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, storeSpinnerData);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        storeSpinner.setAdapter(adapter2);

        int storeSpinnerPosition = 0;
        if(shopping.editItemInInventory) {
            storeSpinnerPosition = adapter2.getPosition(shopping.selectedItemInInventory.getStore().toString());
        } else if (shopping.editItemInShoppingList) {
            storeSpinnerPosition = adapter2.getPosition(shopping.selectedItemInShoppingList.getStore().toString());
        }
        storeSpinner.setSelection(storeSpinnerPosition);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView adapter, View view, int i, long l) {
                String selectedItem =  adapter.getItemAtPosition(i).toString();
                if (selectedItem.equals("(add new category)")) {
                    itemCategoryInput.setVisibility(View.VISIBLE);
                } else {
                    itemCategoryInput.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        storeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView adapter, View view, int i, long l) {
                String selectedItem =  adapter.getItemAtPosition(i).toString();
                if (selectedItem.equals("(add new store)")) {
                    itemStoreInput.setVisibility(View.VISIBLE);
                } else {
                    itemStoreInput.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        editItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldItemName = "";
                if(shopping.editItemInInventory) {
                    oldItemName = shopping.selectedItemInInventory.getName();
                } else if (shopping.editItemInShoppingList) {
                    oldItemName = shopping.selectedItemInShoppingList.getName();
                }
                String newItemName = itemNameInput.getText().toString();
                String itemType = itemTypeInput.getText().toString();
                String itemCategory = itemCategoryInput.getText().toString();
                String itemStore = itemStoreInput.getText().toString();

                if (newItemName.isEmpty() || itemType.isEmpty()) {
                    Toast.makeText(getActivity(), "Please enter all the data.", Toast.LENGTH_SHORT).show();
                    return;
                } else if (categorySpinner.getSelectedItem().toString().equals("") || storeSpinner.getSelectedItem().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please enter all the data.", Toast.LENGTH_SHORT).show();
                    return;
                } else if (categorySpinner.getSelectedItem().toString().equals("(add new category)") && itemCategory.isEmpty()) {
                    Toast.makeText(getActivity(), "Please enter all the data.", Toast.LENGTH_SHORT).show();
                    return;
                } else if (storeSpinner.getSelectedItem().toString().equals("(add new store)") && itemStore.isEmpty()) {
                    Toast.makeText(getActivity(), "Please enter all the data.", Toast.LENGTH_SHORT).show();
                    return;
                }

                /*String isInStock = "false";
                String isNeeded = "false";
                String isPaused = "false";
                if(shopping.editItemInInventory) {
                    if (shopping.selectedItemInInventory.getStatus().isInStock()) isInStock = "true";
                    else if (shopping.selectedItemInInventory.getStatus().isNeeded()) isNeeded = "true";
                    else if (shopping.selectedItemInInventory.getStatus().isNeeded()) isPaused = "true";
                } else if (shopping.editItemInShoppingList) {
                    if (shopping.selectedItemInShoppingList.getStatus().isInStock()) isInStock = "true";
                    else if (shopping.selectedItemInShoppingList.getStatus().isNeeded()) isNeeded = "true";
                    else if (shopping.selectedItemInShoppingList.getStatus().isNeeded()) isPaused = "true";
                }*/

                if (categorySpinner.getSelectedItem().toString().equals("(add new category)")) {
                    int numCategories = categoryData.getCategoryList().size();
                    dbCategoryHelper.addNewCategory(itemCategory, numCategories);
                    shopping.updateCategoryData();
                }

                if (storeSpinner.getSelectedItem().toString().equals("(add new store)")) {
                    int numStores = storeData.getStoreList().size();
                    dbStoreHelper.addNewStore(itemStore, numStores);
                    shopping.updateStoreData();
                }

                if (!categorySpinner.getSelectedItem().toString().equals("(add new category)")) {
                    itemCategory = categorySpinner.getSelectedItem().toString();
                }

                if (!storeSpinner.getSelectedItem().toString().equals("(add new store)")) {
                    itemStore = storeSpinner.getSelectedItem().toString();
                }

                dbItemHelper.updateItem(oldItemName, newItemName, itemType, itemCategory, itemStore);
                //dbStatusHelper.changeStatusName(oldItemName, newItemName);
                shopping.updateItemData();
                shopping.updateStatusData();
                if(shopping.editItemInInventory) {
                    shopping.selectedItemInInventory = shopping.getItemData().getItemMap().get(newItemName);
                } else if (shopping.editItemInShoppingList) {
                    shopping.selectedItemInShoppingList = shopping.getItemData().getItemMap().get(newItemName);
                }

                Toast.makeText(getActivity(), "Item has been edited.", Toast.LENGTH_SHORT).show();

                shopping.hideKeyboard();
                if(shopping.editItemInInventory) {
                    shopping.loadFragment(new FullInventory());
                } else if (shopping.editItemInShoppingList) {
                    shopping.loadFragment(new ShoppingList());
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopping.hideKeyboard();
                if(shopping.editItemInInventory) {
                    shopping.loadFragment(new FullInventory());
                } else if (shopping.editItemInShoppingList) {
                    shopping.loadFragment(new ShoppingList());
                }
            }
        });

        return view;
    }
}