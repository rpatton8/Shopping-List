package ryan.android.shopping;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

//@SuppressWarnings("ALL")
public class AddStore extends Fragment {

    private View view;
    private Shopping shopping;
    private StoreData storeData;
    private DBStoreHelper dbStoreHelper;

    private EditText storeInput;
    private Button addStoreButton;
    private Button cancelButton;

    public AddStore() {}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.add_store, container, false);

        shopping = (Shopping) getActivity();
        storeData = shopping.getStoreData();
        dbStoreHelper = new DBStoreHelper(getActivity());

        storeInput = view.findViewById(R.id.storeInput);
        addStoreButton = view.findViewById(R.id.addStoreButton);
        cancelButton = view.findViewById(R.id.cancelButton);

        storeInput.setText("");

        addStoreButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String storeName = storeInput.getText().toString();

                if (storeName.isEmpty()) {
                    shopping.showAlertDialog("Add Store", "Please enter a store to add.");
                    return;
                }

                int numStores = storeData.getStoreList().size();
                dbStoreHelper.addNewStore(storeName, numStores);
                shopping.updateStoreData();

                shopping.hideKeyboard();
                shopping.loadFragment(new FullInventory());
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                shopping.hideKeyboard();
                shopping.loadFragment(new FullInventory());
            }
        });

        return view;
    }
}