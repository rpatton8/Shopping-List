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

public class RemoveCategory extends Fragment {

    private Shopping shopping;
    private CategoryData categoryData;
    private DBCategoryHelper dbCategoryHelper;
    private Spinner categorySpinner;

    public RemoveCategory() {}

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.remove_category, container, false);

        shopping = (Shopping) getActivity();
        categoryData = shopping.getCategoryData();
        dbCategoryHelper = new DBCategoryHelper(getActivity());

        Button removeCategoryButton = view.findViewById(R.id.removeCategoryButton);
        Button cancelButton = view.findViewById(R.id.cancelButton);

        ArrayList<String> spinnerData = categoryData.getCategoryListWithBlank();
        categorySpinner = view.findViewById(R.id.categorySpinner);
        ArrayAdapter adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, spinnerData);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        removeCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String categoryName = categorySpinner.getSelectedItem().toString();

                if (categoryName.isEmpty()) {
                    Toast.makeText(getActivity(), "Choose a category to remove.", Toast.LENGTH_SHORT).show();
                    return;
                }

                int orderNum = categoryData.getCategoryList().indexOf(categoryName);
                dbCategoryHelper.deleteCategory(categoryName);
                for (int i = orderNum + 1; i < categoryData.getCategoryList().size(); i++) {
                    dbCategoryHelper.moveOrderDownOne(i);
                }
                shopping.updateCategoryData();

                Toast.makeText(getActivity(), "Category has been removed.", Toast.LENGTH_SHORT).show();

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