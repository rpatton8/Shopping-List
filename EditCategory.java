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
public class EditCategory extends Fragment {

    private View view;
    private Shopping shopping;
    private CategoryData categoryData;
    private DBItemHelper dbItemHelper;
    private DBCategoryHelper dbCategoryHelper;

    private EditText categoryInput;
    private Spinner categorySpinner;
    private ArrayList<String> spinnerData;
    private ArrayAdapter adapter;
    private Button editCategoryButton;
    private Button cancelButton;

    public EditCategory() {}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.edit_category, container, false);

        shopping = (Shopping) getActivity();
        dbItemHelper = new DBItemHelper(getActivity());
        dbCategoryHelper = new DBCategoryHelper(getActivity());
        categoryData = shopping.getCategoryData();

        categoryInput = view.findViewById(R.id.categoryInput);
        editCategoryButton = view.findViewById(R.id.editCategoryButton);
        cancelButton = view.findViewById(R.id.cancelButton);

        spinnerData = categoryData.getCategoryListWithBlank();
        categorySpinner = view.findViewById(R.id.categorySpinner);
        adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, spinnerData);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        categoryInput.setText("");

        editCategoryButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String oldCategory = categorySpinner.getSelectedItem().toString();
                String newCategory = categoryInput.getText().toString();

                if (newCategory.isEmpty() || oldCategory.equals(newCategory)) {
                    shopping.showAlertDialog("Edit Category", "Change category name to edit.");
                    return;
                }

                dbItemHelper.changeCategory(oldCategory, newCategory);
                shopping.updateItemData();

                dbCategoryHelper.changeCategoryName(oldCategory, newCategory);
                shopping.updateCategoryData();

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