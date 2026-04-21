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

//@SuppressWarnings("ALL")
public class RemoveStore extends Fragment {

    private View view;
    private Shopping shopping;
    private StoreData storeData;
    private DBStoreHelper dbStoreHelper;

    private Spinner storeSpinner;
    private ArrayList<String> spinnerData;
    private ArrayAdapter adapter;
    private Button removeStoreButton;
    private Button cancelButton;

    public RemoveStore() {}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.remove_store, container, false);

        shopping = (Shopping) getActivity();
        storeData = shopping.getStoreData();
        dbStoreHelper = new DBStoreHelper(getActivity());

        removeStoreButton = view.findViewById(R.id.removeStoreButton);
        cancelButton = view.findViewById(R.id.cancelButton);

        spinnerData = storeData.getStoreListWithBlank();
        storeSpinner = view.findViewById(R.id.storeSpinner);
        adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, spinnerData);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        storeSpinner.setAdapter(adapter);

        removeStoreButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String storeName = storeSpinner.getSelectedItem().toString();

                if (storeName.isEmpty()) {
                    shopping.showAlertDialog("Remove Store", "Choose a store to remove.");
                    return;
                }

                int orderNum = storeData.getStoreList().indexOf(storeName);
                dbStoreHelper.deleteStore(storeName);
                for (int i = orderNum + 1; i < storeData.getStoreList().size(); i++) {
                    dbStoreHelper.moveOrderDownOne(i);
                }

                shopping.updateStoreData();
                shopping.loadFragment(new FullInventory());
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                shopping.loadFragment(new FullInventory());
            }
        });

        return view;
    }
}