package ryan.android.shopping;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

//@SuppressWarnings("ALL")
public class AddCategory extends Fragment {

    private View view;
    private Shopping shopping;
    private CategoryData categoryData;
    private DBCategoryHelper dbCategoryHelper;

    private EditText categoryInput;
    private Button addCategoryButton;
    private Button cancelButton;

    public AddCategory() {}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.add_category, container, false);

        shopping = (Shopping) getActivity();
        categoryData = shopping.getCategoryData();
        dbCategoryHelper = new DBCategoryHelper(getActivity());

        categoryInput = view.findViewById(R.id.categoryInput);
        addCategoryButton = view.findViewById(R.id.addCategoryButton);
        cancelButton = view.findViewById(R.id.cancelButton);

        categoryInput.setText("");

        addCategoryButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String categoryName = categoryInput.getText().toString();

                if (categoryName.isEmpty()) {
                    shopping.showAlertDialog("Add Category", "Please enter a category to add.");
                    return;
                }

                int numCategories = categoryData.getCategoryList().size();
                dbCategoryHelper.addNewCategory(categoryName, numCategories);
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