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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.ArrayList;

public class AddItem extends Fragment {

    private View view;
    private Shopping shopping;
    private ItemData itemData;
    private CategoryData categoryData;
    private StoreData storeData;
    private OptionalData optionalData;
    private DBItemHelper dbItemHelper;
    private DBStatusHelper dbStatusHelper;
    private DBCategoryHelper dbCategoryHelper;
    private DBStoreHelper dbStoreHelper;
    private DBOptionalDataHelper dbOptionalDataHelper;

    private EditText itemNameInput;
    private EditText itemBrandTypeInput;
    private EditText itemCategoryInput;
    private EditText itemStoreInput;
    private TextView inStockLabel;
    private TextView neededLabel;
    private TextView pausedLabel;
    private boolean isInStock;
    private boolean isNeeded;
    private boolean isPaused;
    private TextView optionalDataTitle;
    private boolean optionalDataVisible;
    private boolean optionalDataExpanded;
    private TextView pictureDataTitle;
    private boolean pictureDataVisible;
    private boolean pictureDataExpanded;
    private TextView pictureLabel1;
    private EditText pictureInput1;
    private ImageView pictureFrame;
    private TextView quantityLabel;
    private EditText quantityInput;
    private TextView priceLabel;
    private EditText priceInput;
    private TextView locationLabel;
    private EditText locationInput;
    private TextView noteLabel;
    private EditText noteInput;
    private Button addItemButton;
    private Button cancelButton;

    private Spinner categorySpinner;
    private ArrayList<String> categorySpinnerData;
    private ArrayAdapter<String> categorySpinnerAdapter;
    private Spinner storeSpinner;
    private ArrayList<String> storeSpinnerData;
    private ArrayAdapter<String> storeSpinnerAdapter;

    public AddItem() {}

    private AddItem getThis() {
        return this;
    }

    public View getView() {
        return getThis().view;
    }

    private void setView(View view) {
        getThis().view = view;
    }

    private Shopping getShopping() {
        return getThis().shopping;
    }

    private void setShopping(Shopping shopping) {
        getThis().shopping = shopping;
    }

    private ItemData getItemData() {
        return getThis().itemData;
    }

    private void setItemData(ItemData itemData) {
        getThis().itemData = itemData;
    }

    private CategoryData getCategoryData() {
        return getThis().categoryData;
    }

    private void setCategoryData(CategoryData categoryData) {
        getThis().categoryData = categoryData;
    }

    private StoreData getStoreData() {
        return getThis().storeData;
    }

    private void setStoreData(StoreData storeData) {
        getThis().storeData = storeData;
    }

    private OptionalData getOptionalData() {
        return getThis().optionalData;
    }

    private void setOptionalData(OptionalData optionalData) {
        getThis().optionalData = optionalData;
    }

    private DBItemHelper getDbItemHelper() {
        return getThis().dbItemHelper;
    }

    private void setDbItemHelper(DBItemHelper dbItemHelper) {
        getThis().dbItemHelper = dbItemHelper;
    }

    private DBStatusHelper getDbStatusHelper() {
        return getThis().dbStatusHelper;
    }

    private void setDbStatusHelper(DBStatusHelper dbStatusHelper) {
        getThis().dbStatusHelper = dbStatusHelper;
    }

    private DBCategoryHelper getDbCategoryHelper() {
        return getThis().dbCategoryHelper;
    }

    private void setDbCategoryHelper(DBCategoryHelper dbCategoryHelper) {
        getThis().dbCategoryHelper = dbCategoryHelper;
    }

    private DBStoreHelper getDbStoreHelper() {
        return getThis().dbStoreHelper;
    }

    private void setDbStoreHelper(DBStoreHelper dbStoreHelper) {
        getThis().dbStoreHelper = dbStoreHelper;
    }

    private DBOptionalDataHelper getDbOptionalDataHelper() {
        return getThis().dbOptionalDataHelper;
    }

    private void setDbOptionalDataHelper(DBOptionalDataHelper dbOptionalDataHelper) {
        getThis().dbOptionalDataHelper = dbOptionalDataHelper;
    }

    private EditText getItemNameInput() {
        return getThis().itemNameInput;
    }

    private void setItemNameInput(EditText itemNameInput) {
        getThis().itemNameInput = itemNameInput;
    }

    private EditText getItemBrandTypeInput() {
        return getThis().itemBrandTypeInput;
    }

    private void setItemBrandTypeInput(EditText itemBrandTypeInput) {
        getThis().itemBrandTypeInput = itemBrandTypeInput;
    }

    private Spinner getCategorySpinner() {
        return getThis().categorySpinner;
    }

    private void setCategorySpinner(Spinner categorySpinner) {
        getThis().categorySpinner = categorySpinner;
    }

    private Spinner getStoreSpinner() {
        return getThis().storeSpinner;
    }

    private void setStoreSpinner(Spinner storeSpinner) {
        getThis().storeSpinner = storeSpinner;
    }

    private EditText getItemCategoryInput() {
        return getThis().itemCategoryInput;
    }

    private void setItemCategoryInput(EditText itemCategoryInput) {
        getThis().itemCategoryInput = itemCategoryInput;
    }

    private EditText getItemStoreInput() {
        return getThis().itemStoreInput;
    }

    private void setItemStoreInput(EditText itemStoreInput) {
        getThis().itemStoreInput = itemStoreInput;
    }

    private TextView getInStockLabel() {
        return getThis().inStockLabel;
    }

    private void setInStockLabel(TextView inStockLabel) {
        getThis().inStockLabel = inStockLabel;
    }

    private TextView getNeededLabel() {
        return getThis().neededLabel;
    }

    private void setNeededLabel(TextView neededLabel) {
        getThis().neededLabel = neededLabel;
    }

    private TextView getPausedLabel() {
        return getThis().pausedLabel;
    }

    private void setPausedLabel(TextView pausedLabel) {
        getThis().pausedLabel = pausedLabel;
    }

    private boolean isInStock() {
        return getThis().isInStock;
    }

    private boolean isNeeded() {
        return getThis().isNeeded;
    }

    private boolean isPaused() {
        return getThis().isPaused;
    }

    private void setAsInStock() {
        getThis().isInStock = true;
        getThis().isNeeded = false;
        getThis().isPaused = false;
    }

    private void setAsNeeded() {
        getThis().isInStock = false;
        getThis().isNeeded = true;
        getThis().isPaused = false;
    }

    private void setAsPaused() {
        getThis().isInStock = false;
        getThis().isNeeded = false;
        getThis().isPaused = true;
    }

    private TextView getOptionalDataTitle() {
        return getThis().optionalDataTitle;
    }

    private void setOptionalDataTitle(TextView optionalDataTitle) {
        getThis().optionalDataTitle = optionalDataTitle;
    }

    private boolean optionalDataVisible() {
        return getThis().optionalDataVisible;
    }

    private void setOptionalDataVisible(boolean optionalDataVisible) {
        getThis().optionalDataVisible = optionalDataVisible;
    }

    private boolean optionalDataExpanded() {
        return getThis().optionalDataExpanded;
    }

    private void setOptionalDataExpanded(boolean optionalDataExpanded) {
        getThis().optionalDataExpanded = optionalDataExpanded;
    }

    private TextView getPictureDataTitle() {
        return getThis().pictureDataTitle;
    }

    private void setPictureDataTitle(TextView pictureDataTitle) {
        getThis().pictureDataTitle = pictureDataTitle;
    }

    private boolean pictureDataVisible() {
        return getThis().pictureDataVisible;
    }

    private void setPictureDataVisible(boolean pictureDataVisible) {
        getThis().pictureDataVisible = pictureDataVisible;
    }

    private boolean pictureDataExpanded() {
        return getThis().pictureDataExpanded;
    }

    private void setPictureDataExpanded(boolean pictureDataExpanded) {
        getThis().pictureDataExpanded = pictureDataExpanded;
    }

    private TextView getPictureLabel1() {
        return getThis().pictureLabel1;
    }

    private void setPictureLabel1(TextView pictureLabel1) {
        getThis().pictureLabel1 = pictureLabel1;
    }

    private EditText getPictureInput1() {
        return getThis().pictureInput1;
    }

    private void setPictureInput1(EditText pictureInput1) {
        getThis().pictureInput1 = pictureInput1;
    }

    private ImageView getPictureFrame() {
        return getThis().pictureFrame;
    }

    private void setPictureFrame(ImageView pictureFrame) {
        getThis().pictureFrame = pictureFrame;
    }

    private TextView getQuantityLabel() {
        return getThis().quantityLabel;
    }

    private void setQuantityLabel(TextView quantityLabel) {
        getThis().quantityLabel = quantityLabel;
    }

    private EditText getQuantityInput() {
        return getThis().quantityInput;
    }

    private void setQuantityInput(EditText quantityInput) {
        getThis().quantityInput = quantityInput;
    }

    private TextView getPriceLabel() {
        return getThis().priceLabel;
    }

    private void setPriceLabel(TextView priceLabel) {
        getThis().priceLabel = priceLabel;
    }

    private EditText getPriceInput() {
        return getThis().priceInput;
    }

    private void setPriceInput(EditText priceInput) {
        getThis().priceInput = priceInput;
    }

    private TextView getLocationLabel() {
        return getThis().locationLabel;
    }

    private void setLocationLabel(TextView locationLabel) {
        getThis().locationLabel = locationLabel;
    }

    private EditText getLocationInput() {
        return getThis().locationInput;
    }

    private void setLocationInput(EditText locationInput) {
        getThis().locationInput = locationInput;
    }

    private TextView getNoteLabel() {
        return getThis().noteLabel;
    }

    private void setNoteLabel(TextView noteLabel) {
        getThis().noteLabel = noteLabel;
    }

    private EditText getNoteInput() {
        return getThis().noteInput;
    }

    private void setNoteInput(EditText noteInput) {
        getThis().noteInput = noteInput;
    }

    private Button getAddItemButton() {
        return getThis().addItemButton;
    }

    private void setAddItemButton(Button addItemButton) {
        getThis().addItemButton = addItemButton;
    }

    private Button getCancelButton() {
        return getThis().cancelButton;
    }

    private void setCancelButton(Button cancelButton) {
        getThis().cancelButton = cancelButton;
    }

    private ArrayList<String> getCategorySpinnerData() {
        return getThis().categorySpinnerData;
    }

    private void setCategorySpinnerData(ArrayList<String> categorySpinnerData) {
        getThis().categorySpinnerData = categorySpinnerData;
    }

    private ArrayAdapter<String> getCategorySpinnerAdapter() {
        return getThis().categorySpinnerAdapter;
    }

    private void setCategorySpinnerAdapter(ArrayAdapter<String> categorySpinnerAdapter) {
        getThis().categorySpinnerAdapter = categorySpinnerAdapter;
    }

    private ArrayList<String> getStoreSpinnerData() {
        return getThis().storeSpinnerData;
    }

    private void setStoreSpinnerData(ArrayList<String> storeSpinnerData) {
        getThis().storeSpinnerData = storeSpinnerData;
    }

    private ArrayAdapter<String> getStoreSpinnerAdapter() {
        return getThis().storeSpinnerAdapter;
    }

    private void setStoreSpinnerAdapter(ArrayAdapter<String> storeSpinnerAdapter) {
        getThis().storeSpinnerAdapter = storeSpinnerAdapter;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setView(inflater.inflate(R.layout.add_item, container, false));

        setShopping((Shopping) getActivity());
        setDbItemHelper(new DBItemHelper(getActivity()));
        setDbStatusHelper(new DBStatusHelper(getActivity()));
        setDbCategoryHelper(new DBCategoryHelper(getActivity()));
        setDbStoreHelper(new DBStoreHelper(getActivity()));
        setDbOptionalDataHelper(new DBOptionalDataHelper(getActivity()));
        setItemData(getShopping().getItemData());
        setCategoryData(getShopping().getCategoryData());
        setStoreData(getShopping().getStoreData());

        setItemNameInput(getView().findViewById(R.id.itemNameInput));
        setItemBrandTypeInput(getView().findViewById(R.id.itemBrandTypeInput));
        setItemCategoryInput(getView().findViewById(R.id.itemCategoryInput));
        setItemStoreInput(getView().findViewById(R.id.itemStoreInput));

        getItemNameInput().setText(getString(R.string.emptyString));
        getItemBrandTypeInput().setText(getString(R.string.emptyString));
        getItemCategoryInput().setText(getString(R.string.emptyString));
        getItemStoreInput().setText(getString(R.string.emptyString));

        setInStockLabel(getView().findViewById(R.id.inStockLabel));
        setNeededLabel(getView().findViewById(R.id.neededLabel));
        setPausedLabel(getView().findViewById(R.id.pausedLabel));

        setOptionalDataTitle(getView().findViewById(R.id.optionalDataTitle));
        setOptionalDataVisible(true);
        setOptionalDataExpanded(false);
        setPictureDataTitle(getView().findViewById(R.id.pictureDataTitle));
        setPictureDataVisible(true);
        setPictureDataExpanded(false);

        setPictureLabel1(getView().findViewById(R.id.pictureLabel1));  // xxx
        setPictureInput1(getView().findViewById(R.id.pictureInput1));  // xxx
        setPictureFrame(getView().findViewById(R.id.pictureFrame));

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

        setAddItemButton(getView().findViewById(R.id.addItemButton));
        setCancelButton(getView().findViewById(R.id.cancelButton));

        setCategorySpinnerData(getCategoryData().getCategoryListWithAddNew());
        setCategorySpinner(getView().findViewById(R.id.categorySpinner));
        setCategorySpinnerAdapter(new ArrayAdapter<>(getThis().getActivity(), R.layout.spinner_outer_item_centered, getCategorySpinnerData()));
        getCategorySpinnerAdapter().setDropDownViewResource(R.layout.spinner_inner_items_1);
        getCategorySpinner().setAdapter(getCategorySpinnerAdapter());

        setStoreSpinnerData(getStoreData().getStoreListWithAddNew());
        setStoreSpinner(getView().findViewById(R.id.storeSpinner));
        setStoreSpinnerAdapter(new ArrayAdapter<>(getThis().getActivity(), R.layout.spinner_outer_item_centered, getStoreSpinnerData()));
        getStoreSpinnerAdapter().setDropDownViewResource(R.layout.spinner_inner_items_1);
        getStoreSpinner().setAdapter(getStoreSpinnerAdapter());

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
                if (getInStockLabel().getVisibility() == View.VISIBLE) {
                    setAsNeeded();
                    getNeededLabel().setVisibility(View.VISIBLE);
                    getInStockLabel().setVisibility(View.GONE);
                    getPausedLabel().setVisibility(View.GONE);
                }
            }
        });

        getNeededLabel().setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (getNeededLabel().getVisibility() == View.VISIBLE) {
                    setAsPaused();
                    getPausedLabel().setVisibility(View.VISIBLE);
                    getInStockLabel().setVisibility(View.GONE);
                    getNeededLabel().setVisibility(View.GONE);
                }
            }
        });

        getPausedLabel().setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (getPausedLabel().getVisibility() == View.VISIBLE) {
                    setAsInStock();
                    getInStockLabel().setVisibility(View.VISIBLE);
                    getPausedLabel().setVisibility(View.GONE);
                    getNeededLabel().setVisibility(View.GONE);
                }
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

        getPictureDataTitle().setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (pictureDataExpanded()) {
                    setPictureDataExpanded(false);
                    getPictureLabel1().setVisibility(View.GONE);
                    getPictureInput1().setVisibility(View.GONE);
                    getPictureFrame().setVisibility(View.GONE);
                } else {
                    setPictureDataExpanded(true);
                    getPictureLabel1().setVisibility(View.VISIBLE);
                    getPictureInput1().setVisibility(View.VISIBLE);
                    getPictureFrame().setVisibility(View.VISIBLE);
                }
            }
        });

        getAddItemButton().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String itemName = getItemNameInput().getText().toString();
                String itemType = getItemBrandTypeInput().getText().toString();
                String itemCategory = getItemCategoryInput().getText().toString();
                String itemStore = getItemStoreInput().getText().toString();

                if (itemName.isEmpty() || itemType.isEmpty()) {
                    getShopping().showAlertDialog(getString(R.string.addItem), getString(R.string.enterAllData), getString(R.string.ok));
                    return;
                } else if (getCategorySpinner().getSelectedItem().toString().equals(getString(R.string.emptyString)) || getStoreSpinner().getSelectedItem().toString().equals(getString(R.string.emptyString))) {
                    getShopping().showAlertDialog(getString(R.string.addItem), getString(R.string.enterAllData), getString(R.string.ok));
                    return;
                } else if (getCategorySpinner().getSelectedItem().toString().equals(getString(R.string.addNewCategory)) && itemCategory.isEmpty()) {
                    getShopping().showAlertDialog(getString(R.string.addItem), getString(R.string.enterAllData), getString(R.string.ok));
                    return;
                } else if (getStoreSpinner().getSelectedItem().toString().equals(getString(R.string.addNewStore)) && itemStore.isEmpty()) {
                    getShopping().showAlertDialog(getString(R.string.addItem), getString(R.string.enterAllData), getString(R.string.ok));
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

                int itemsInCategory;
                if (getItemData().getCategoryMap().get(itemCategory) == null) itemsInCategory = 0;
                else itemsInCategory = getItemData().getCategoryMap().get(itemCategory).getCategoryItemsList().size();

                int itemsInStore;
                if (getItemData().getStoreMap().get(itemStore) == null) itemsInStore = 0;
                else itemsInStore = getItemData().getStoreMap().get(itemStore).getStoreItemsList().size();

                String quantity = getQuantityInput().getText().toString();
                String price = getPriceInput().getText().toString();
                String location = getLocationInput().getText().toString();
                String note = getNoteInput().getText().toString();

                getDbItemHelper().addNewItemByCategory(itemName, itemType, itemCategory, itemStore, itemsInCategory);
                getDbItemHelper().addNewItemByStore(itemName, itemType, itemCategory, itemStore, itemsInStore);
                getDbStatusHelper().addNewStatus(itemName, getString(R.string.paused), getString(R.string.unchecked));
                getDbOptionalDataHelper().addNewOptionalData(itemName, quantity, price, location, note);

                getShopping().updateItemData();
                getShopping().updateStatusData();
                getShopping().updateOptionalData();

                getShopping().hideKeyboard();
                getShopping().loadFragment(new FullInventory());
            }
        });

        getCancelButton().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getShopping().hideKeyboard();
                getShopping().loadFragment(new FullInventory());
            }
        });

        return getView();
    }
}