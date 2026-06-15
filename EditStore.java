package ryan.android.shopping;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import java.util.ArrayList;

public class EditStore extends Fragment {

    private View view;
    private Shopping shopping;
    private StoreData storeData;
    private DBItemHelper dbItemHelper;
    private DBStoreHelper dbStoreHelper;

    private EditText storeInput;
    private Spinner storeSpinner;
    private ArrayList<String> storeSpinnerData;
    private ArrayAdapter storeSpinnerAdapter;
    private Button editStoreButton;
    private Button cancelButton;

    public EditStore() {}

    private EditStore getThis() {
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

    private DBItemHelper getDbItemHelper() {
        return dbItemHelper;
    }

    private void setDbItemHelper(DBItemHelper dbItemHelper) {
        getThis().dbItemHelper = dbItemHelper;
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

    private Spinner getStoreSpinner() {
        return storeSpinner;
    }

    private void setStoreSpinner(Spinner storeSpinner) {
        getThis().storeSpinner = storeSpinner;
    }

    private ArrayList<String> getStoreSpinnerData() {
        return storeSpinnerData;
    }

    private void setStoreSpinnerData(ArrayList<String> storeSpinnerData) {
        getThis().storeSpinnerData = storeSpinnerData;
    }

    private ArrayAdapter getStoreSpinnerAdapter() {
        return storeSpinnerAdapter;
    }

    private void setStoreSpinnerAdapter(ArrayAdapter storeSpinnerAdapter) {
        getThis().storeSpinnerAdapter = storeSpinnerAdapter;
    }

    private Button getEditStoreButton() {
        return editStoreButton;
    }

    private void setEditStoreButton(Button editStoreButton) {
        getThis().editStoreButton = editStoreButton;
    }

    private Button getCancelButton() {
        return cancelButton;
    }

    private void setCancelButton(Button cancelButton) {
        getThis().cancelButton = cancelButton;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setView(inflater.inflate(R.layout.edit_store, container, false));

        setShopping((Shopping) getActivity());
        setStoreData(getShopping().getStoreData());
        setDbItemHelper(new DBItemHelper(getActivity()));
        setDbStoreHelper(new DBStoreHelper(getActivity()));

        setStoreInput((EditText) getView().findViewById(R.id.storeInput));
        setEditStoreButton((Button) getView().findViewById(R.id.editStoreButton));
        setCancelButton((Button) getView().findViewById(R.id.cancelButton));

        setStoreSpinnerData(getStoreData().getStoreListWithBlank());
        setStoreSpinner((Spinner) getView().findViewById(R.id.storeSpinner));
        setStoreSpinnerAdapter(new ArrayAdapter<>(getThis().getActivity(), android.R.layout.simple_spinner_item, getStoreSpinnerData()));
        getStoreSpinnerAdapter().setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        getStoreSpinner().setAdapter(getStoreSpinnerAdapter());

        getStoreInput().setText(getString(R.string.emptyString));

        getEditStoreButton().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String oldStore = getStoreSpinner().getSelectedItem().toString();
                String newStore = getStoreInput().getText().toString();

                if (newStore.isEmpty() || oldStore.equals(newStore)) {
                    getShopping().showAlertDialog(getString(R.string.editStore), getString(R.string.changeStoreName), getString(R.string.ok));
                    return;
                }

                getDbItemHelper().changeStore(oldStore, newStore);
                getShopping().updateItemData();

                getDbStoreHelper().changeStoreName(oldStore, newStore);
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