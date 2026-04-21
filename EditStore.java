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

//@SuppressWarnings("ALL")
public class EditStore extends Fragment {

    private View view;
    private Shopping shopping;
    private StoreData storeData;
    private DBItemHelper dbItemHelper;
    private DBStoreHelper dbStoreHelper;

    private EditText storeInput;
    private Spinner storeSpinner;
    private ArrayList<String> spinnerData;
    private ArrayAdapter adapter;
    private Button editStoreButton;
    private Button cancelButton;

    public EditStore() {}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.edit_store, container, false);

        shopping = (Shopping) getActivity();
        storeData = shopping.getStoreData();
        dbItemHelper = new DBItemHelper(getActivity());
        dbStoreHelper = new DBStoreHelper(getActivity());

        storeInput = view.findViewById(R.id.storeInput);
        editStoreButton = view.findViewById(R.id.editStoreButton);
        cancelButton = view.findViewById(R.id.cancelButton);

        spinnerData = storeData.getStoreListWithBlank();
        storeSpinner = view.findViewById(R.id.storeSpinner);
        adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, spinnerData);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        storeSpinner.setAdapter(adapter);

        storeInput.setText("");

        editStoreButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String oldStore = storeSpinner.getSelectedItem().toString();
                String newStore = storeInput.getText().toString();

                if (newStore.isEmpty() || oldStore.equals(newStore)) {
                    shopping.showAlertDialog("Edit Store", "Change store name to edit.");
                    return;
                }

                dbItemHelper.changeStore(oldStore, newStore);
                shopping.updateItemData();

                dbStoreHelper.changeStoreName(oldStore, newStore);
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