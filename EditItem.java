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

    private View view;
    private Shopping shopping;
    private CategoryData categoryData;
    private StoreData storeData;
    private DBHelper dbHelper;
    private DBStatusHelper dbStatusHelper;
    private DBCategoryHelper dbCategoryHelper;
    private DBStoreHelper dbStoreHelper;

    private EditText itemNameInput;
    private EditText itemTypeInput;
    private Spinner categorySpinner;
    private Spinner storeSpinner;
    private EditText itemCategoryInput;
    private EditText itemStoreInput;
    private Button editItemButton;
    private Button cancelButton;

    public EditItem() {}

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.edit_item, container, false);

        shopping = (Shopping) getActivity();
        dbHelper = new DBHelper(getActivity());
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

        itemNameInput.setText(shopping.selectedItem.getName());
        itemTypeInput.setText(shopping.selectedItem.getBrand());
        itemCategoryInput.setText("");
        itemStoreInput.setText("");

        ArrayList<String> categorySpinnerData = categoryData.getCategoryListWithAddNew();
        categorySpinner = view.findViewById(R.id.categorySpinner);
        ArrayAdapter adapter1 = new ArrayAdapter(this.getActivity(), android.R.layout.simple_spinner_item, categorySpinnerData);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter1);
        int categorySpinnerPosition = adapter1.getPosition(shopping.selectedItem.getCategory(0).toString());
        categorySpinner.setSelection(categorySpinnerPosition);

        ArrayList<String> storeSpinnerData = storeData.getStoreListWithAddNew();
        storeSpinner = view.findViewById(R.id.storeSpinner);
        ArrayAdapter adapter2 = new ArrayAdapter(this.getActivity(), android.R.layout.simple_spinner_item, storeSpinnerData);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        storeSpinner.setAdapter(adapter2);
        int storeSpinnerPosition = adapter2.getPosition(shopping.selectedItem.getStore(0).toString());
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

                String oldItemName = shopping.selectedItem.getName();
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

                String isInStock = "false";
                String isNeeded = "false";
                String isPaused = "false";
                if (shopping.selectedItem.getStatus().isInStock()) isInStock = "true";
                else if (shopping.selectedItem.getStatus().isNeeded()) isNeeded = "true";
                else if (shopping.selectedItem.getStatus().isNeeded()) isPaused = "true";

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

                dbHelper.updateItem(oldItemName, newItemName, itemType, itemCategory, itemStore);
                dbStatusHelper.changeStatusName(oldItemName, newItemName, isInStock, isNeeded, isPaused);
                shopping.updateItemData();
                shopping.updateStatusData();
                shopping.selectedItem = shopping.getItemData().getItemMap().get(newItemName);

                Toast.makeText(getActivity(), "Item has been edited.", Toast.LENGTH_SHORT).show();

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