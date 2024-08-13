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
import android.widget.Toast;
import java.util.ArrayList;

public class EditStore extends Fragment {

    private View view;
    private Shopping shopping;
    private StoreData storeData;
    private DBHelper dbHelper;
    private DBStoreHelper dbStoreHelper;

    private Spinner storeSpinner;
    private EditText storeInput;
    private Button editStoreButton;
    private Button cancelButton;

    public EditStore() {}

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.edit_store, container, false);

        shopping = (Shopping) getActivity();
        dbHelper = new DBHelper(getActivity());
        dbStoreHelper = new DBStoreHelper(getActivity());
        storeData = shopping.getStoreData();

        storeInput = view.findViewById(R.id.storeInput);
        editStoreButton = view.findViewById(R.id.editStoreButton);
        cancelButton = view.findViewById(R.id.cancelButton);

        ArrayList<String> spinnerData = storeData.getStoreList();
        storeSpinner = view.findViewById(R.id.storeSpinner);
        ArrayAdapter adapter = new ArrayAdapter(this.getActivity(), android.R.layout.simple_spinner_item, spinnerData);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        storeSpinner.setAdapter(adapter);

        storeInput.setText("");

        editStoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldStore = storeSpinner.getSelectedItem().toString();
                String newStore = storeInput.getText().toString();

                if (newStore.isEmpty() || oldStore.equals(newStore)) {
                    Toast.makeText(getActivity(), "Change store name to edit.", Toast.LENGTH_SHORT).show();
                    return;
                }

                dbHelper.changeStore(oldStore, newStore);
                shopping.updateItemData();

                dbStoreHelper.changeStoreName(oldStore, newStore);
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