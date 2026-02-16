package ryan.android.shopping;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddCategory extends Fragment {

    private Shopping shopping;
    private CategoryData categoryData;
    private DBCategoryHelper dbCategoryHelper;

    private EditText categoryInput;

    public AddCategory() {}

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.add_category, container, false);

        shopping = (Shopping) getActivity();
        categoryData = shopping.getCategoryData();
        dbCategoryHelper = new DBCategoryHelper(getActivity());

        categoryInput = view.findViewById(R.id.categoryInput);
        Button addCategoryButton = view.findViewById(R.id.addCategoryButton);
        Button cancelButton = view.findViewById(R.id.cancelButton);

        categoryInput.setText("");

        addCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String categoryName = categoryInput.getText().toString();

                if (categoryName.isEmpty()) {
                    Toast.makeText(getActivity(), "Please enter a category to add.", Toast.LENGTH_SHORT).show();
                    return;
                }

                int numCategories = categoryData.getCategoryList().size();
                dbCategoryHelper.addNewCategory(categoryName, numCategories);
                shopping.updateCategoryData();

                Toast.makeText(getActivity(), "Category #" + numCategories + " has been added.", Toast.LENGTH_SHORT).show();

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