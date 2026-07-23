package ryan.android.shopping;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

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
    private EditText itemBrandTypeInput;
    private EditText itemCategoryInput;
    private EditText itemStoreInput;
    private TextView inStockLabel;
    private TextView neededLabel;
    private TextView pausedLabel;
    private TextView optionalDataTitle;
    private boolean optionalDataVisible;
    private boolean optionalDataExpanded;
    private TextView quantityLabel;
    private EditText quantityInput;
    private TextView priceLabel;
    private EditText priceInput;
    private TextView locationLabel;
    private EditText locationInput;
    private TextView noteLabel;
    private EditText noteInput;
    private Button editItemButton;
    private Button cancelButton;

    private Spinner categorySpinner;
    private ArrayList<String> categorySpinnerData;
    private ArrayAdapter<String> categorySpinnerAdapter;
    private Spinner storeSpinner;
    private ArrayList<String> storeSpinnerData;
    private ArrayAdapter<String> storeSpinnerAdapter;

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

    private EditText getItemBrandTypeInput() {
        return itemBrandTypeInput;
    }

    private void setItemBrandTypeInput(EditText itemBrandTypeInput) {
        getThis().itemBrandTypeInput = itemBrandTypeInput;
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

    public TextView getInStockLabel() {
        return inStockLabel;
    }

    public void setInStockLabel(TextView inStockLabel) {
        this.inStockLabel = inStockLabel;
    }

    public TextView getNeededLabel() {
        return neededLabel;
    }

    public void setNeededLabel(TextView neededLabel) {
        this.neededLabel = neededLabel;
    }

    public TextView getPausedLabel() {
        return pausedLabel;
    }

    public void setPausedLabel(TextView pausedLabel) {
        this.pausedLabel = pausedLabel;
    }

    public TextView getOptionalDataTitle() {
        return optionalDataTitle;
    }

    public void setOptionalDataTitle(TextView optionalDataTitle) {
        this.optionalDataTitle = optionalDataTitle;
    }

    public boolean optionalDataVisible() {
        return optionalDataVisible;
    }

    public void setOptionalDataVisible(boolean optionalDataVisible) {
        this.optionalDataVisible = optionalDataVisible;
    }

    public boolean optionalDataExpanded() {
        return optionalDataExpanded;
    }

    public void setOptionalDataExpanded(boolean optionalDataExpanded) {
        this.optionalDataExpanded = optionalDataExpanded;
    }

    public TextView getQuantityLabel() {
        return quantityLabel;
    }

    public void setQuantityLabel(TextView quantityLabel) {
        this.quantityLabel = quantityLabel;
    }

    public EditText getQuantityInput() {
        return quantityInput;
    }

    public void setQuantityInput(EditText quantityInput) {
        this.quantityInput = quantityInput;
    }

    public TextView getPriceLabel() {
        return priceLabel;
    }

    public void setPriceLabel(TextView priceLabel) {
        this.priceLabel = priceLabel;
    }

    public EditText getPriceInput() {
        return priceInput;
    }

    public void setPriceInput(EditText priceInput) {
        this.priceInput = priceInput;
    }

    public TextView getLocationLabel() {
        return locationLabel;
    }

    public void setLocationLabel(TextView locationLabel) {
        this.locationLabel = locationLabel;
    }

    public EditText getLocationInput() {
        return locationInput;
    }

    public void setLocationInput(EditText locationInput) {
        this.locationInput = locationInput;
    }

    public TextView getNoteLabel() {
        return noteLabel;
    }

    public void setNoteLabel(TextView noteLabel) {
        this.noteLabel = noteLabel;
    }

    public EditText getNoteInput() {
        return noteInput;
    }

    public void setNoteInput(EditText noteInput) {
        this.noteInput = noteInput;
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

    private ArrayAdapter<String> getCategorySpinnerAdapter() {
        return categorySpinnerAdapter;
    }

    private void setCategorySpinnerAdapter(ArrayAdapter<String> categorySpinnerAdapter) {
        getThis().categorySpinnerAdapter = categorySpinnerAdapter;
    }

    private ArrayList<String> getStoreSpinnerData() {
        return storeSpinnerData;
    }

    private void setStoreSpinnerData(ArrayList<String> storeSpinnerData) {
        getThis().storeSpinnerData = storeSpinnerData;
    }

    private ArrayAdapter<String> getStoreSpinnerAdapter() {
        return storeSpinnerAdapter;
    }

    private void setStoreSpinnerAdapter(ArrayAdapter<String> storeSpinnerAdapter) {
        getThis().storeSpinnerAdapter = storeSpinnerAdapter;
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

        setItemNameInput(getView().findViewById(R.id.itemNameInput));
        setItemBrandTypeInput(getView().findViewById(R.id.itemBrandTypeInput));
        setItemCategoryInput(getView().findViewById(R.id.itemCategoryInput));
        setItemStoreInput(getView().findViewById(R.id.itemStoreInput));

        if(getShopping().editItemInInventory()) {
            getItemNameInput().setText(getShopping().getSelectedItemInInventory().getItemName());
            getItemBrandTypeInput().setText(getShopping().getSelectedItemInInventory().getBrandType());
        } else if (getShopping().editItemInSearchResults()) {
            getItemNameInput().setText(getShopping().getSelectedItemInSearchResults().getItemName());
            getItemBrandTypeInput().setText(getShopping().getSelectedItemInSearchResults().getBrandType());
        } else if (getShopping().editItemInShoppingList()) {
            getItemNameInput().setText(getShopping().getSelectedItemInShoppingList().getItemName());
            getItemBrandTypeInput().setText(getShopping().getSelectedItemInShoppingList().getBrandType());
        } else if (getShopping().editItemInPictureDialog()) {
            getItemNameInput().setText(getShopping().getItemInPictureDialog().getItemName());
            getItemBrandTypeInput().setText(getShopping().getItemInPictureDialog().getBrandType());
        }
        getItemCategoryInput().setText(getString(R.string.emptyString));
        getItemStoreInput().setText(getString(R.string.emptyString));

        setInStockLabel(getView().findViewById(R.id.inStockLabel));
        setNeededLabel(getView().findViewById(R.id.neededLabel));
        setPausedLabel(getView().findViewById(R.id.pausedLabel));

        setOptionalDataTitle(getView().findViewById(R.id.optionalDataTitle));
        setOptionalDataVisible(true);
        setOptionalDataExpanded(false);

        setQuantityLabel(getView().findViewById(R.id.quantityLabel));
        setQuantityInput(getView().findViewById(R.id.quantityInput));
        setPriceLabel(getView().findViewById(R.id.priceLabel));
        setPriceInput(getView().findViewById(R.id.priceInput));
        setLocationLabel(getView().findViewById(R.id.locationLabel));
        setLocationInput(getView().findViewById(R.id.locationInput));
        setNoteLabel(getView().findViewById(R.id.noteLabel));
        setNoteInput(getView().findViewById(R.id.noteInput));

        getQuantityInput().setText(getString(R.string.emptyString));
        getPriceInput().setText(getString(R.string.emptyString));
        getLocationInput().setText(getString(R.string.emptyString));
        getNoteInput().setText(getString(R.string.emptyString));

        setEditItemButton(getView().findViewById(R.id.editItemButton));
        setCancelButton(getView().findViewById(R.id.cancelButton));

        setCategorySpinnerData(getCategoryData().getCategoryListWithAddNew());
        setCategorySpinner(getView().findViewById(R.id.categorySpinner));
        setCategorySpinnerAdapter(new ArrayAdapter<>(getThis().getActivity(), R.layout.spinner_outer_item_centered, getCategorySpinnerData()));
        getCategorySpinnerAdapter().setDropDownViewResource(R.layout.spinner_inner_items_1);
        getCategorySpinner().setAdapter(getCategorySpinnerAdapter());

        int categorySpinnerPosition = 0;
        if(getShopping().editItemInInventory()) {
            categorySpinnerPosition = getCategorySpinnerAdapter().getPosition(getShopping().getSelectedItemInInventory().getCategory().toString());
        } else if (getShopping().editItemInSearchResults()) {
            categorySpinnerPosition = getCategorySpinnerAdapter().getPosition(getShopping().getSelectedItemInSearchResults().getCategory().toString());
        } else if (getShopping().editItemInShoppingList()) {
            categorySpinnerPosition = getCategorySpinnerAdapter().getPosition(getShopping().getSelectedItemInShoppingList().getCategory().toString());
        } else if (getShopping().editItemInPictureDialog()) {
            categorySpinnerPosition = getCategorySpinnerAdapter().getPosition(getShopping().getItemInPictureDialog().getCategory().toString());
        }
        getCategorySpinner().setSelection(categorySpinnerPosition);

        setStoreSpinnerData(getStoreData().getStoreListWithAddNew());
        setStoreSpinner(getView().findViewById(R.id.storeSpinner));
        setStoreSpinnerAdapter(new ArrayAdapter<>(getThis().getActivity(), R.layout.spinner_outer_item_centered, getStoreSpinnerData()));
        getStoreSpinnerAdapter().setDropDownViewResource(R.layout.spinner_inner_items_1);
        getStoreSpinner().setAdapter(getStoreSpinnerAdapter());

        int storeSpinnerPosition = 0;
        if(getShopping().editItemInInventory()) {
            storeSpinnerPosition = getStoreSpinnerAdapter().getPosition(getShopping().getSelectedItemInInventory().getStore().toString());
        } else if (getShopping().editItemInSearchResults()) {
            storeSpinnerPosition = getStoreSpinnerAdapter().getPosition(getShopping().getSelectedItemInSearchResults().getStore().toString());
        } else if (getShopping().editItemInShoppingList()) {
            storeSpinnerPosition = getStoreSpinnerAdapter().getPosition(getShopping().getSelectedItemInShoppingList().getStore().toString());
        } else if (getShopping().editItemInPictureDialog()) {
            storeSpinnerPosition = getStoreSpinnerAdapter().getPosition(getShopping().getItemInPictureDialog().getStore().toString());
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

        getInStockLabel().setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // to do
            }
        });

        getNeededLabel().setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // to do
            }
        });

        getPausedLabel().setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // to do
            }
        });

        getOptionalDataTitle().setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (optionalDataExpanded()) {
                    setOptionalDataExpanded(false);
                    getQuantityLabel().setVisibility(View.GONE);
                    getQuantityInput().setVisibility(View.GONE);
                    getPriceLabel().setVisibility(View.GONE);
                    getPriceInput().setVisibility(View.GONE);
                    getLocationLabel().setVisibility(View.GONE);
                    getLocationInput().setVisibility(View.GONE);
                    getNoteLabel().setVisibility(View.GONE);
                    getNoteInput().setVisibility(View.GONE);
                } else {
                    setOptionalDataExpanded(true);
                    getQuantityLabel().setVisibility(View.VISIBLE);
                    getQuantityInput().setVisibility(View.VISIBLE);
                    getPriceLabel().setVisibility(View.VISIBLE);
                    getPriceInput().setVisibility(View.VISIBLE);
                    getLocationLabel().setVisibility(View.VISIBLE);
                    getLocationInput().setVisibility(View.VISIBLE);
                    getNoteLabel().setVisibility(View.VISIBLE);
                    getNoteInput().setVisibility(View.VISIBLE);
                }
            }
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
                String itemType = getItemBrandTypeInput().getText().toString();
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