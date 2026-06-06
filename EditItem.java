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
import java.util.ArrayList;

public class EditItem extends Fragment {

    private Shopping shopping;
    private CategoryData categoryData;
    private StoreData storeData;
    private DBItemHelper dbItemHelper;
    private DBStatusHelper dbStatusHelper;
    private DBCategoryHelper dbCategoryHelper;
    private DBStoreHelper dbStoreHelper;

    private EditText itemNameInput;
    private EditText itemTypeInput;
    private EditText itemCategoryInput;
    private EditText itemStoreInput;
    private Button editItemButton;
    private Button cancelButton;

    private Spinner categorySpinner;
    private Spinner storeSpinner;
    private ArrayList<String> categorySpinnerData;
    private ArrayAdapter<String> categoryAdapter;
    private ArrayList<String> storeSpinnerData;
    private ArrayAdapter<String> storeAdapter;

    public EditItem() {}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.edit_item, container, false);

        shopping = (Shopping) getActivity();
        dbItemHelper = new DBItemHelper(getActivity());
        dbStatusHelper = new DBStatusHelper(getActivity());
        dbCategoryHelper = new DBCategoryHelper(getActivity());
        dbStoreHelper = new DBStoreHelper(getActivity());
        categoryData = shopping.getCategoryData();
        storeData = shopping.getStoreData();

        itemNameInput = view.findViewById(R.id.itemNameInput);
        itemTypeInput = view.findViewById(R.id.itemTypeInput);
        itemCategoryInput = view.findViewById(R.id.itemCategoryInput);
        itemStoreInput = view.findViewById(R.id.itemStoreInput);
        editItemButton = view.findViewById(R.id.editItemButton);
        cancelButton = view.findViewById(R.id.cancelButton);

        if(shopping.editItemInInventory()) {
            System.out.println("Edit item in inventory");
            itemNameInput.setText(shopping.getSelectedItemInInventory().getName());
            itemTypeInput.setText(shopping.getSelectedItemInInventory().getBrandType());
        } else if (shopping.editItemInSearchResults()) {
            System.out.println("Edit item in search results");
            itemNameInput.setText(shopping.getSelectedItemInSearchResults().getName());
            itemTypeInput.setText(shopping.getSelectedItemInSearchResults().getBrandType());
        } else if (shopping.editItemInShoppingList()) {
            System.out.println("Edit item in shopping list");
            itemNameInput.setText(shopping.getSelectedItemInShoppingList().getName());
            itemTypeInput.setText(shopping.getSelectedItemInShoppingList().getBrandType());
        }
        itemCategoryInput.setText(getString(R.string.emptyString));
        itemStoreInput.setText(getString(R.string.emptyString));

        categorySpinnerData = categoryData.getCategoryListWithAddNew();
        categorySpinner = view.findViewById(R.id.categorySpinner);
        categoryAdapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, categorySpinnerData);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        int categorySpinnerPosition = 0;
        if(shopping.editItemInInventory()) {
            categorySpinnerPosition = categoryAdapter.getPosition(shopping.getSelectedItemInInventory().getCategory().toString());
        } else if (shopping.editItemInSearchResults()) {
            categorySpinnerPosition = categoryAdapter.getPosition(shopping.getSelectedItemInSearchResults().getCategory().toString());
        } else if (shopping.editItemInShoppingList()) {
            categorySpinnerPosition = categoryAdapter.getPosition(shopping.getSelectedItemInShoppingList().getCategory().toString());
        }
        categorySpinner.setSelection(categorySpinnerPosition);

        storeSpinnerData = storeData.getStoreListWithAddNew();
        storeSpinner = view.findViewById(R.id.storeSpinner);
        storeAdapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, storeSpinnerData);
        storeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        storeSpinner.setAdapter(storeAdapter);

        int storeSpinnerPosition = 0;
        if(shopping.editItemInInventory()) {
            storeSpinnerPosition = storeAdapter.getPosition(shopping.getSelectedItemInInventory().getStore().toString());
        } else if (shopping.editItemInSearchResults()) {
            storeSpinnerPosition = storeAdapter.getPosition(shopping.getSelectedItemInSearchResults().getStore().toString());
        } else if (shopping.editItemInShoppingList()) {
            storeSpinnerPosition = storeAdapter.getPosition(shopping.getSelectedItemInShoppingList().getStore().toString());
        }
        storeSpinner.setSelection(storeSpinnerPosition);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapter, View view, int i, long l) {
                String selectedItem =  adapter.getItemAtPosition(i).toString();
                if (selectedItem.equals(getString(R.string.addNewCategory))) {
                    itemCategoryInput.setVisibility(View.VISIBLE);
                } else {
                    itemCategoryInput.setVisibility(View.GONE);
                }
            }
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        storeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapter, View view, int i, long l) {
                String selectedItem =  adapter.getItemAtPosition(i).toString();
                if (selectedItem.equals(getString(R.string.addNewStore))) {
                    itemStoreInput.setVisibility(View.VISIBLE);
                } else {
                    itemStoreInput.setVisibility(View.GONE);
                }
            }
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        editItemButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String oldItemName = getString(R.string.emptyString);
                if (shopping.editItemInInventory()) {
                    oldItemName = shopping.getSelectedItemInInventory().getName();
                } else if (shopping.editItemInSearchResults()) {
                    oldItemName = shopping.getSelectedItemInSearchResults().getName();
                } else if (shopping.editItemInShoppingList()) {
                    oldItemName = shopping.getSelectedItemInShoppingList().getName();
                }
                String newItemName = itemNameInput.getText().toString();
                String itemType = itemTypeInput.getText().toString();
                String itemCategory = itemCategoryInput.getText().toString();
                String itemStore = itemStoreInput.getText().toString();

                if (newItemName.isEmpty() || itemType.isEmpty()) {
                    shopping.showAlertDialog(getString(R.string.editItem), getString(R.string.enterAllData), getString(R.string.ok));
                    return;
                } else if (categorySpinner.getSelectedItem().toString().equals(getString(R.string.emptyString)) || storeSpinner.getSelectedItem().toString().equals(getString(R.string.emptyString))) {
                    shopping.showAlertDialog(getString(R.string.editItem), getString(R.string.enterAllData), getString(R.string.ok));
                    return;
                } else if (categorySpinner.getSelectedItem().toString().equals(getString(R.string.addNewCategory)) && itemCategory.isEmpty()) {
                    shopping.showAlertDialog(getString(R.string.editItem), getString(R.string.enterAllData), getString(R.string.ok));
                    return;
                } else if (storeSpinner.getSelectedItem().toString().equals(getString(R.string.addNewStore)) && itemStore.isEmpty()) {
                    shopping.showAlertDialog(getString(R.string.editItem), getString(R.string.enterAllData), getString(R.string.ok));
                    return;
                }

                if (categorySpinner.getSelectedItem().toString().equals(getString(R.string.addNewCategory))) {
                    int numCategories = categoryData.getCategoryList().size();
                    dbCategoryHelper.addNewCategory(itemCategory, numCategories);
                    shopping.updateCategoryData();
                }

                if (storeSpinner.getSelectedItem().toString().equals(getString(R.string.addNewStore))) {
                    int numStores = storeData.getStoreList().size();
                    dbStoreHelper.addNewStore(itemStore, numStores);
                    shopping.updateStoreData();
                }

                if (!categorySpinner.getSelectedItem().toString().equals(getString(R.string.addNewCategory))) {
                    itemCategory = categorySpinner.getSelectedItem().toString();
                }

                if (!storeSpinner.getSelectedItem().toString().equals(getString(R.string.addNewStore))) {
                    itemStore = storeSpinner.getSelectedItem().toString();
                }

                dbItemHelper.updateItem(oldItemName, newItemName, itemType, itemCategory, itemStore);
                dbStatusHelper.changeStatusName(oldItemName, newItemName);
                shopping.updateItemData();
                shopping.updateStatusData();

                if (shopping.editItemInInventory()) {
                    shopping.setSelectedItemInInventory(shopping.getItemData().getItemMap().get(newItemName));
                } else if (shopping.editItemInSearchResults()) {
                    shopping.setSelectedItemInSearchResults(shopping.getItemData().getItemMap().get(newItemName));
                } else if (shopping.editItemInShoppingList()) {
                    shopping.setSelectedItemInShoppingList(shopping.getItemData().getItemMap().get(newItemName));
                }

                shopping.hideKeyboard();
                if (shopping.editItemInInventory()) {
                    shopping.loadFragment(new FullInventory());
                } else if (shopping.editItemInSearchResults()) {
                    shopping.loadFragment(new FullInventory());
                } else if (shopping.editItemInShoppingList()) {
                    shopping.loadFragment(new ShoppingList());
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                shopping.hideKeyboard();
                if (shopping.editItemInInventory()) {
                    shopping.loadFragment(new FullInventory());
                } else if (shopping.editItemInSearchResults()) {
                    shopping.loadFragment(new FullInventory());
                } else if (shopping.editItemInShoppingList()) {
                    shopping.loadFragment(new ShoppingList());
                }
            }
        });

        return view;
    }
}