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

public class EditCategory extends Fragment {

    private View view;
    private Shopping shopping;
    private CategoryData categoryData;
    private DBItemHelper dbItemHelper;
    private DBCategoryHelper dbCategoryHelper;

    private Spinner categorySpinner;
    private EditText categoryInput;
    private Button editCategoryButton;
    private Button cancelButton;

    public EditCategory() {}

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.edit_category, container, false);

        shopping = (Shopping) getActivity();
        dbItemHelper = new DBItemHelper(getActivity());
        dbCategoryHelper = new DBCategoryHelper(getActivity());
        categoryData = shopping.getCategoryData();

        categoryInput = view.findViewById(R.id.categoryInput);
        editCategoryButton = view.findViewById(R.id.editCategoryButton);
        cancelButton = view.findViewById(R.id.cancelButton);

        ArrayList<String> spinnerData = categoryData.getCategoryList();
        categorySpinner = view.findViewById(R.id.categorySpinner);
        ArrayAdapter adapter = new ArrayAdapter(this.getActivity(), android.R.layout.simple_spinner_item, spinnerData);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        categoryInput.setText("");

        editCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldCategory = categorySpinner.getSelectedItem().toString();
                String newCategory = categoryInput.getText().toString();

                if (newCategory.isEmpty() || oldCategory.equals(newCategory)) {
                    Toast.makeText(getActivity(), "Change category name to edit.", Toast.LENGTH_SHORT).show();
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
            @Override
            public void onClick(View view) {
                shopping.hideKeyboard();
                shopping.loadFragment(new FullInventory());
            }
        });

        return view;
    }

}