package ryan.android.shopping;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class AddStore extends Fragment {

    private View view;
    private Shopping shopping;
    private StoreData storeData;
    private DBStoreHelper dbStoreHelper;
    private EditText storeInput;
    private Button addStoreButton;
    private Button cancelButton;

    public AddStore() {}

    private AddStore getThis() {
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

    private StoreData getStoreData() {
        return storeData;
    }

    private void setStoreData(StoreData storeData) {
        getThis().storeData = storeData;
    }

    private DBStoreHelper getDbStoreHelper() {
        return dbStoreHelper;
    }

    private void setDbStoreHelper(DBStoreHelper dbStoreHelper) {
        getThis().dbStoreHelper = dbStoreHelper;
    }

    private EditText getStoreInput() {
        return storeInput;
    }

    private void setStoreInput(EditText storeInput) {
        getThis().storeInput = storeInput;
    }

    private Button getAddStoreButton() {
        return addStoreButton;
    }

    private void setAddStoreButton(Button addStoreButton) {
        getThis().addStoreButton = addStoreButton;
    }

    private Button getCancelButton() {
        return cancelButton;
    }

    private void setCancelButton(Button cancelButton) {
        getThis().cancelButton = cancelButton;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setView(inflater.inflate(R.layout.add_store, container, false));

        setShopping((Shopping) getActivity());
        setStoreData(getShopping().getStoreData());
        setDbStoreHelper(new DBStoreHelper(getActivity()));

        setStoreInput((EditText) getView().findViewById(R.id.storeInput));
        setAddStoreButton((Button) getView().findViewById(R.id.addStoreButton));
        setCancelButton((Button) getView().findViewById(R.id.cancelButton));

        getStoreInput().setText(getString(R.string.emptyString));

        getAddStoreButton().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String storeName = getStoreInput().getText().toString();

                if (storeName.isEmpty()) {
                    getShopping().showAlertDialog(getString(R.string.addStore), getString(R.string.enterStoreToAdd), getString(R.string.ok));
                    return;
                }

                int numStores = getStoreData().getStoreList().size();
                getDbStoreHelper().addNewStore(storeName, numStores);
                getShopping().updateStoreData();

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