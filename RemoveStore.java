package ryan.android.shopping;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.ArrayList;

public class RemoveStore extends Fragment {

    private Shopping shopping;
    private StoreData storeData;
    private DBStoreHelper dbStoreHelper;
    private Spinner storeSpinner;

    public RemoveStore() {}

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.remove_store, container, false);

        shopping = (Shopping) getActivity();
        storeData = shopping.getStoreData();
        dbStoreHelper = new DBStoreHelper(getActivity());

        Button removeStoreButton = view.findViewById(R.id.removeStoreButton);
        Button cancelButton = view.findViewById(R.id.cancelButton);

        ArrayList<String> spinnerData = storeData.getStoreListWithBlank();
        storeSpinner = view.findViewById(R.id.storeSpinner);
        ArrayAdapter adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, spinnerData);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        storeSpinner.setAdapter(adapter);

        removeStoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String storeName = storeSpinner.getSelectedItem().toString();

                if (storeName.isEmpty()) {
                    Toast.makeText(getActivity(), "Choose a store to remove.", Toast.LENGTH_SHORT).show();
                    return;
                }

                int orderNum = storeData.getStoreList().indexOf(storeName);
                dbStoreHelper.deleteStore(storeName);
                for (int i = orderNum + 1; i < storeData.getStoreList().size(); i++) {
                    dbStoreHelper.moveOrderDownOne(i);
                }
                shopping.updateStoreData();

                Toast.makeText(getActivity(), "Store has been removed.", Toast.LENGTH_SHORT).show();

                shopping.loadFragment(new FullInventory());
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopping.loadFragment(new FullInventory());
            }
        });

        return view;
    }
}