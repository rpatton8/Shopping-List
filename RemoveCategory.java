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
public class RemoveCategory extends Fragment {

    private View view;
    private Shopping shopping;
    private CategoryData categoryData;
    private DBCategoryHelper dbCategoryHelper;

    private Spinner categorySpinner;
    private ArrayList<String> spinnerData;
    private ArrayAdapter adapter;
    private Button removeCategoryButton;
    private Button cancelButton;

    public RemoveCategory() {}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.remove_category, container, false);

        shopping = (Shopping) getActivity();
        categoryData = shopping.getCategoryData();
        dbCategoryHelper = new DBCategoryHelper(getActivity());

        removeCategoryButton = view.findViewById(R.id.removeCategoryButton);
        cancelButton = view.findViewById(R.id.cancelButton);

        spinnerData = categoryData.getCategoryListWithBlank();
        categorySpinner = view.findViewById(R.id.categorySpinner);
        adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, spinnerData);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        removeCategoryButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String categoryName = categorySpinner.getSelectedItem().toString();

                if (categoryName.isEmpty()) {
                    shopping.showAlertDialog("Remove Category", "Choose a category to remove.");
                    return;
                }

                int orderNum = categoryData.getCategoryList().indexOf(categoryName);
                dbCategoryHelper.deleteCategory(categoryName);
                for (int i = orderNum + 1; i < categoryData.getCategoryList().size(); i++) {
                    dbCategoryHelper.moveOrderDownOne(i);
                }

                shopping.updateCategoryData();
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