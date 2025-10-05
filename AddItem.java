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

public class AddItem extends Fragment {

    private Shopping shopping;
    private ItemData itemData;
    private CategoryData categoryData;
    private StoreData storeData;
    private DBItemHelper dbItemHelper;
    private DBStatusHelper dbStatusHelper;
    private DBCategoryHelper dbCategoryHelper;
    private DBStoreHelper dbStoreHelper;

    private EditText itemNameInput;
    private EditText itemTypeInput;
    private Spinner categorySpinner;
    private Spinner storeSpinner;
    private EditText itemCategoryInput;
    private EditText itemStoreInput;

    public AddItem() {}

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.add_item, container, false);

        shopping = (Shopping) getActivity();
        dbItemHelper = new DBItemHelper(getActivity());
        dbStatusHelper = new DBStatusHelper(getActivity());
        dbCategoryHelper = new DBCategoryHelper(getActivity());
        dbStoreHelper = new DBStoreHelper(getActivity());
        itemData = shopping.getItemData();
        categoryData = shopping.getCategoryData();
        storeData = shopping.getStoreData();

        itemNameInput = view.findViewById(R.id.itemNameInput);
        itemTypeInput = view.findViewById(R.id.itemTypeInput);
        itemCategoryInput = view.findViewById(R.id.itemCategoryInput);
        itemStoreInput = view.findViewById(R.id.itemStoreInput);
        Button addItemButton = view.findViewById(R.id.addItemButton);
        Button cancelButton = view.findViewById(R.id.cancelButton);

        itemNameInput.setText("");
        itemTypeInput.setText("");
        itemCategoryInput.setText("");
        itemStoreInput.setText("");

        ArrayList<String> categorySpinnerData = categoryData.getCategoryListWithAddNew();
        categorySpinner = view.findViewById(R.id.categorySpinner);
        ArrayAdapter adapter1 = new ArrayAdapter(this.getActivity(), android.R.layout.simple_spinner_item, categorySpinnerData);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter1);

        ArrayList<String> storeSpinnerData = storeData.getStoreListWithAddNew();
        storeSpinner = view.findViewById(R.id.storeSpinner);
        ArrayAdapter adapter2 = new ArrayAdapter(this.getActivity(), android.R.layout.simple_spinner_item, storeSpinnerData);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        storeSpinner.setAdapter(adapter2);

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

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String itemName = itemNameInput.getText().toString();
                String itemType = itemTypeInput.getText().toString();
                String itemCategory = itemCategoryInput.getText().toString();
                String itemStore = itemStoreInput.getText().toString();

                if (itemName.isEmpty() || itemType.isEmpty()) {
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

                int itemsInCategory;
                if (itemData.getCategoryMap().get(itemCategory) == null) itemsInCategory = 0;
                else itemsInCategory = itemData.getCategoryMap().get(itemCategory).getItemList().size();

                dbItemHelper.addNewItem(itemName, itemType, itemCategory, itemStore, itemsInCategory);
                dbStatusHelper.addNewStatus(itemName, "true", "false", "false");
                shopping.updateItemData();
                shopping.updateStatusData();
                shopping.itemIsClickedInInventory.add(false);
                shopping.itemIsClickedInShoppingList.add(false);
                shopping.itemIsChecked.add(false);

                Toast.makeText(getActivity(), "Item has been added.", Toast.LENGTH_SHORT).show();

                shopping.hideKeyboard();
                shopping.loadFragment(new FullInventory());
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopping.hideKeyboard();
                shopping.loadFragment(new FullInventory());
            }
        });

        return view;
    }
}