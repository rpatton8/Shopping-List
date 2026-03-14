package ryan.android.shopping;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class AddStore extends Fragment {

    private Shopping shopping;
    private StoreData storeData;
    private DBStoreHelper dbStoreHelper;

    private EditText storeInput;

    public AddStore() {}

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.add_store, container, false);

        shopping = (Shopping) getActivity();
        dbStoreHelper = new DBStoreHelper(getActivity());
        storeData = shopping.getStoreData();

        storeInput = view.findViewById(R.id.storeInput);
        Button addStoreButton = view.findViewById(R.id.addStoreButton);
        Button cancelButton = view.findViewById(R.id.cancelButton);

        storeInput.setText("");

        addStoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
            @Override
            public void onClick(View view) {
                shopping.hideKeyboard();
                shopping.loadFragment(new FullInventory());
            }
        });

        return view;
    }
}