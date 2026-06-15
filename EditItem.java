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

    private View view;
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

    private EditItem getThis() {
        return this;
    }

    public View getView() {
        return view;
    }

    private void setView(View view) {
        getThis().view = view;
    }

    private Shopping getShopping() {
        return shopping;
    }

    private void setShopping(Shopping shopping) {
        getThis().shopping = shopping;
    }

    private CategoryData getCategoryData() {
        return categoryData;
    }

    private void setCategoryData(CategoryData categoryData) {
        getThis().categoryData = categoryData;
    }

    private StoreData getStoreData() {
        return storeData;
    }

    private void setStoreData(StoreData storeData) {
        getThis().storeData = storeData;
    }

    private DBItemHelper getDbItemHelper() {
        return dbItemHelper;
    }

    private void setDbItemHelper(DBItemHelper dbItemHelper) {
        getThis().dbItemHelper = dbItemHelper;
    }

    private DBStatusHelper getDbStatusHelper() {
        return dbStatusHelper;
    }

    private void setDbStatusHelper(DBStatusHelper dbStatusHelper) {
        getThis().dbStatusHelper = dbStatusHelper;
    }

    private DBCategoryHelper getDbCategoryHelper() {
        return dbCategoryHelper;
    }

    private void setDbCategoryHelper(DBCategoryHelper dbCategoryHelper) {
        getThis().dbCategoryHelper = dbCategoryHelper;
    }

    private DBStoreHelper getDbStoreHelper() {
        return dbStoreHelper;
    }

    private void setDbStoreHelper(DBStoreHelper dbStoreHelper) {
        getThis().dbStoreHelper = dbStoreHelper;
    }

    private EditText getItemNameInput() {
        return itemNameInput;
    }

    private void setItemNameInput(EditText itemNameInput) {
        getThis().itemNameInput = itemNameInput;
    }

    private EditText getItemTypeInput() {
        return itemTypeInput;
    }

    private void setItemTypeInput(EditText itemTypeInput) {
        getThis().itemTypeInput = itemTypeInput;
    }

    private EditText getItemCategoryInput() {
        return itemCategoryInput;
    }

    private void setItemCategoryInput(EditText itemCategoryInput) {
        getThis().itemCategoryInput = itemCategoryInput;
    }

    private EditText getItemStoreInput() {
        return itemStoreInput;
    }

    private void setItemStoreInput(EditText itemStoreInput) {
        getThis().itemStoreInput = itemStoreInput;
    }

    private Button getEditItemButton() {
        return editItemButton;
    }

    private void setEditItemButton(Button editItemButton) {
        getThis().editItemButton = editItemButton;
    }

    private Button getCancelButton() {
        return cancelButton;
    }

    private void setCancelButton(Button cancelButton) {
        getThis().cancelButton = cancelButton;
    }

    private Spinner getCategorySpinner() {
        return categorySpinner;
    }

    private void setCategorySpinner(Spinner categorySpinner) {
        getThis().categorySpinner = categorySpinner;
    }

    private Spinner getStoreSpinner() {
        return storeSpinner;
    }

    private void setStoreSpinner(Spinner storeSpinner) {
        getThis().storeSpinner = storeSpinner;
    }

    private ArrayList<String> getCategorySpinnerData() {
        return categorySpinnerData;
    }

    private void setCategorySpinnerData(ArrayList<String> categorySpinnerData) {
        getThis().categorySpinnerData = categorySpinnerData;
    }

    private ArrayAdapter<String> getCategoryAdapter() {
        return categoryAdapter;
    }

    private void setCategoryAdapter(ArrayAdapter<String> categoryAdapter) {
        getThis().categoryAdapter = categoryAdapter;
    }

    private ArrayList<String> getStoreSpinnerData() {
        return storeSpinnerData;
    }

    private void setStoreSpinnerData(ArrayList<String> storeSpinnerData) {
        getThis().storeSpinnerData = storeSpinnerData;
    }

    private ArrayAdapter<String> getStoreAdapter() {
        return storeAdapter;
    }

    private void setStoreAdapter(ArrayAdapter<String> storeAdapter) {
        getThis().storeAdapter = storeAdapter;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setView(inflater.inflate(R.layout.edit_item, container, false));

        setShopping((Shopping) getActivity());
        setDbItemHelper(new DBItemHelper(getActivity()));
        setDbStatusHelper(new DBStatusHelper(getActivity()));
        setDbCategoryHelper(new DBCategoryHelper(getActivity()));
        setDbStoreHelper(new DBStoreHelper(getActivity()));
        setCategoryData(getShopping().getCategoryData());
        setStoreData(getShopping().getStoreData());

        setItemNameInput((EditText) getView().findViewById(R.id.itemNameInput));
        setItemTypeInput((EditText) getView().findViewById(R.id.itemTypeInput));
        setItemCategoryInput((EditText) getView().findViewById(R.id.itemCategoryInput));
        setItemStoreInput((EditText) getView().findViewById(R.id.itemStoreInput));
        setEditItemButton((Button) getView().findViewById(R.id.editItemButton));
        setCancelButton((Button) getView().findViewById(R.id.cancelButton));

        if(getShopping().editItemInInventory()) {
            getItemNameInput().setText(getShopping().getSelectedItemInInventory().getItemName());
            getItemTypeInput().setText(getShopping().getSelectedItemInInventory().getBrandType());
        } else if (getShopping().editItemInSearchResults()) {
            getItemNameInput().setText(getShopping().getSelectedItemInSearchResults().getItemName());
            getItemTypeInput().setText(getShopping().getSelectedItemInSearchResults().getBrandType());
        } else if (getShopping().editItemInShoppingList()) {
            getItemNameInput().setText(getShopping().getSelectedItemInShoppingList().getItemName());
            getItemTypeInput().setText(getShopping().getSelectedItemInShoppingList().getBrandType());
        } else if (getShopping().editItemInPictureDialog()) {
            getItemNameInput().setText(getShopping().getItemInPictureDialog().getItemName());
            getItemTypeInput().setText(getShopping().getItemInPictureDialog().getBrandType());
        }
        getItemCategoryInput().setText(getString(R.string.emptyString));
        getItemStoreInput().setText(getString(R.string.emptyString));

        setCategorySpinnerData(getCategoryData().getCategoryListWithAddNew());
        setCategorySpinner((Spinner) getView().findViewById(R.id.categorySpinner));
        setCategoryAdapter(new ArrayAdapter<>(getThis().getActivity(), android.R.layout.simple_spinner_item, getCategorySpinnerData()));
        getCategoryAdapter().setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        getCategorySpinner().setAdapter(getCategoryAdapter());

        int categorySpinnerPosition = 0;
        if(getShopping().editItemInInventory()) {
            categorySpinnerPosition = getCategoryAdapter().getPosition(getShopping().getSelectedItemInInventory().getCategory().toString());
        } else if (getShopping().editItemInSearchResults()) {
            categorySpinnerPosition = getCategoryAdapter().getPosition(getShopping().getSelectedItemInSearchResults().getCategory().toString());
        } else if (getShopping().editItemInShoppingList()) {
            categorySpinnerPosition = getCategoryAdapter().getPosition(getShopping().getSelectedItemInShoppingList().getCategory().toString());
        } else if (getShopping().editItemInPictureDialog()) {
            categorySpinnerPosition = getCategoryAdapter().getPosition(getShopping().getItemInPictureDialog().getCategory().toString());
        }
        getCategorySpinner().setSelection(categorySpinnerPosition);

        setStoreSpinnerData(getStoreData().getStoreListWithAddNew());
        setStoreSpinner((Spinner) getView().findViewById(R.id.storeSpinner));
        setStoreAdapter(new ArrayAdapter<>(getThis().getActivity(), android.R.layout.simple_spinner_item, getStoreSpinnerData()));
        getStoreAdapter().setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        getStoreSpinner().setAdapter(getStoreAdapter());

        int storeSpinnerPosition = 0;
        if(getShopping().editItemInInventory()) {
            storeSpinnerPosition = getStoreAdapter().getPosition(getShopping().getSelectedItemInInventory().getStore().toString());
        } else if (getShopping().editItemInSearchResults()) {
            storeSpinnerPosition = getStoreAdapter().getPosition(getShopping().getSelectedItemInSearchResults().getStore().toString());
        } else if (getShopping().editItemInShoppingList()) {
            storeSpinnerPosition = getStoreAdapter().getPosition(getShopping().getSelectedItemInShoppingList().getStore().toString());
        } else if (getShopping().editItemInPictureDialog()) {
            storeSpinnerPosition = getStoreAdapter().getPosition(getShopping().getItemInPictureDialog().getStore().toString());
        }
        getStoreSpinner().setSelection(storeSpinnerPosition);

        getCategorySpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapter, View view, int i, long l) {
                String selectedItem =  adapter.getItemAtPosition(i).toString();
                if (selectedItem.equals(getString(R.string.addNewCategory))) {
                    getItemCategoryInput().setVisibility(View.VISIBLE);
                } else {
                    getItemCategoryInput().setVisibility(View.GONE);
                }
            }
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        getStoreSpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapter, View view, int i, long l) {
                String selectedItem =  adapter.getItemAtPosition(i).toString();
                if (selectedItem.equals(getString(R.string.addNewStore))) {
                    getItemStoreInput().setVisibility(View.VISIBLE);
                } else {
                    getItemStoreInput().setVisibility(View.GONE);
                }
            }
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        getEditItemButton().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String oldItemName = getString(R.string.emptyString);
                if (getShopping().editItemInInventory()) {
                    oldItemName = getShopping().getSelectedItemInInventory().getItemName();
                } else if (getShopping().editItemInSearchResults()) {
                    oldItemName = getShopping().getSelectedItemInSearchResults().getItemName();
                } else if (getShopping().editItemInShoppingList()) {
                    oldItemName = getShopping().getSelectedItemInShoppingList().getItemName();
                } else if (getShopping().editItemInPictureDialog()) {
                    oldItemName = getShopping().getItemInPictureDialog().getItemName();
                }
                String newItemName = getItemNameInput().getText().toString();
                String itemType = getItemTypeInput().getText().toString();
                String itemCategory = getItemCategoryInput().getText().toString();
                String itemStore = getItemStoreInput().getText().toString();

                if (newItemName.isEmpty() || itemType.isEmpty()) {
                    getShopping().showAlertDialog(getString(R.string.editItem), getString(R.string.enterAllData), getString(R.string.ok));
                    return;
                } else if (getCategorySpinner().getSelectedItem().toString().equals(getString(R.string.emptyString)) || getStoreSpinner().getSelectedItem().toString().equals(getString(R.string.emptyString))) {
                    getShopping().showAlertDialog(getString(R.string.editItem), getString(R.string.enterAllData), getString(R.string.ok));
                    return;
                } else if (getCategorySpinner().getSelectedItem().toString().equals(getString(R.string.addNewCategory)) && itemCategory.isEmpty()) {
                    getShopping().showAlertDialog(getString(R.string.editItem), getString(R.string.enterAllData), getString(R.string.ok));
                    return;
                } else if (getStoreSpinner().getSelectedItem().toString().equals(getString(R.string.addNewStore)) && itemStore.isEmpty()) {
                    getShopping().showAlertDialog(getString(R.string.editItem), getString(R.string.enterAllData), getString(R.string.ok));
                    return;
                }

                if (getCategorySpinner().getSelectedItem().toString().equals(getString(R.string.addNewCategory))) {
                    int numCategories = getCategoryData().getCategoryList().size();
                    getDbCategoryHelper().addNewCategory(itemCategory, numCategories);
                    getShopping().updateCategoryData();
                }

                if (getStoreSpinner().getSelectedItem().toString().equals(getString(R.string.addNewStore))) {
                    int numStores = getStoreData().getStoreList().size();
                    getDbStoreHelper().addNewStore(itemStore, numStores);
                    getShopping().updateStoreData();
                }

                if (!getCategorySpinner().getSelectedItem().toString().equals(getString(R.string.addNewCategory))) {
                    itemCategory = getCategorySpinner().getSelectedItem().toString();
                }

                if (!getStoreSpinner().getSelectedItem().toString().equals(getString(R.string.addNewStore))) {
                    itemStore = getStoreSpinner().getSelectedItem().toString();
                }

                getDbItemHelper().updateItem(oldItemName, newItemName, itemType, itemCategory, itemStore);
                getDbStatusHelper().changeStatusName(oldItemName, newItemName);
                getShopping().updateItemData();
                getShopping().updateStatusData();

                if (getShopping().editItemInInventory()) {
                    getShopping().setSelectedItemInInventory(getShopping().getItemData().getItemMap().get(newItemName));
                } else if (getShopping().editItemInSearchResults()) {
                    getShopping().setSelectedItemInSearchResults(getShopping().getItemData().getItemMap().get(newItemName));
                } else if (getShopping().editItemInShoppingList()) {
                    getShopping().setSelectedItemInShoppingList(getShopping().getItemData().getItemMap().get(newItemName));
                } else if (getShopping().editItemInPictureDialog()) {
                    getShopping().setItemInPictureDialog(getShopping().getItemData().getItemMap().get(newItemName));
                }

                closeEditItem();
            }
        });

        getCancelButton().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                closeEditItem();
            }
        });
        return getView();
    }

    private void closeEditItem() {
        if (getShopping().editItemInInventory()) {
            getShopping().loadFragment(new FullInventory());
        } else if (getShopping().editItemInSearchResults()) {
            getShopping().loadFragment(new FullInventory());
        } else if (getShopping().editItemInShoppingList()) {
            getShopping().loadFragment(new ShoppingList());
        } else if (getShopping().editItemInPictureDialog()) {
            if (getShopping().pictureDialogInInventory()) {
                getShopping().loadFragment(new FullInventory());
            } else if (getShopping().pictureDialogInSearchResults()) {
                getShopping().loadFragment(new FullInventory());
            } else if  (getShopping().pictureDialogInShoppingList()) {
                getShopping().loadFragment(new ShoppingList());
            }
        }
    }
}