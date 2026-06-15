package ryan.android.shopping;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import java.util.ArrayList;

public class RemoveStore extends Fragment {

    private View view;
    private Shopping shopping;
    private StoreData storeData;
    private DBStoreHelper dbStoreHelper;
    private Spinner removeStoreSpinner;
    private ArrayList<String> removeStoreSpinnerData;
    private ArrayAdapter removeStoreAdapter;
    private Button removeStoreButton;
    private Button cancelButton;

    public RemoveStore() {}

    private RemoveStore getThis() {
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

    private Spinner getRemoveStoreSpinner() {
        return removeStoreSpinner;
    }

    private void setRemoveStoreSpinner(Spinner removeStoreSpinner) {
        getThis().removeStoreSpinner = removeStoreSpinner;
    }

    private ArrayList<String> getRemoveStoreSpinnerData() {
        return removeStoreSpinnerData;
    }

    private void setRemoveStoreSpinnerData(ArrayList<String> removeStoreSpinnerData) {
        getThis().removeStoreSpinnerData = removeStoreSpinnerData;
    }

    private ArrayAdapter getRemoveStoreAdapter() {
        return removeStoreAdapter;
    }

    private void setRemoveStoreAdapter(ArrayAdapter removeStoreAdapter) {
        getThis().removeStoreAdapter = removeStoreAdapter;
    }

    private Button getRemoveStoreButton() {
        return removeStoreButton;
    }

    private void setRemoveStoreButton(Button removeStoreButton) {
        getThis().removeStoreButton = removeStoreButton;
    }

    private Button getCancelButton() {
        return cancelButton;
    }

    private void setCancelButton(Button cancelButton) {
        getThis().cancelButton = cancelButton;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setView(inflater.inflate(R.layout.remove_store, container, false));

        setShopping((Shopping) getActivity());
        setStoreData(getShopping().getStoreData());
        setDbStoreHelper(new DBStoreHelper(getActivity()));

        setRemoveStoreButton((Button) getView().findViewById(R.id.removeStoreButton));
        setCancelButton((Button) getView().findViewById(R.id.cancelButton));

        setRemoveStoreSpinnerData(getStoreData().getStoreListWithBlank());
        setRemoveStoreSpinner((Spinner) getView().findViewById(R.id.storeSpinner));
        setRemoveStoreAdapter(new ArrayAdapter<>(getThis().getActivity(), android.R.layout.simple_spinner_item, getRemoveStoreSpinnerData()));
        getRemoveStoreAdapter().setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        getRemoveStoreSpinner().setAdapter(getRemoveStoreAdapter());

        getRemoveStoreButton().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String storeName = getRemoveStoreSpinner().getSelectedItem().toString();

                if (storeName.isEmpty()) {
                    getShopping().showAlertDialog(getString(R.string.removeStore), getString(R.string.chooseStoreToRemove), getString(R.string.ok));
                    return;
                }

                int orderNum = getStoreData().getStoreList().indexOf(storeName);
                getDbStoreHelper().deleteStore(storeName);
                for (int i = orderNum + 1; i < storeData.getStoreList().size(); i++) {
                    getDbStoreHelper().moveOrderDownOne(i);
                }

                getShopping().updateStoreData();
                getShopping().loadFragment(new FullInventory());
            }
        });

        getCancelButton().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getShopping().loadFragment(new FullInventory());
            }
        });

        return getView();
    }
}