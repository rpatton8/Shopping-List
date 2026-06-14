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

public class RemoveCategory extends Fragment {

    private View view;
    private Shopping shopping;
    private CategoryData categoryData;
    private DBCategoryHelper dbCategoryHelper;
    private Spinner removeCategorySpinner;
    private ArrayList<String> removeCategorySpinnerData;
    private ArrayAdapter removeCategoryAdapter;
    private Button removeCategoryButton;
    private Button cancelButton;

    public RemoveCategory() {}

    private RemoveCategory getThis() {
        return this;
    }
    
    public View getView() {
        return view;
    }

    public void setView(View view) {
        getThis().view = view;
    }

    public Shopping getShopping() {
        return shopping;
    }

    public void setShopping(Shopping shopping) {
        getThis().shopping = shopping;
    }

    public CategoryData getCategoryData() {
        return categoryData;
    }

    public void setCategoryData(CategoryData categoryData) {
        getThis().categoryData = categoryData;
    }

    public DBCategoryHelper getDbCategoryHelper() {
        return dbCategoryHelper;
    }

    public void setDbCategoryHelper(DBCategoryHelper dbCategoryHelper) {
        getThis().dbCategoryHelper = dbCategoryHelper;
    }

    public Spinner getRemoveCategorySpinner() {
        return removeCategorySpinner;
    }

    public void setRemoveCategorySpinner(Spinner removeCategorySpinner) {
        getThis().removeCategorySpinner = removeCategorySpinner;
    }

    public ArrayList<String> getRemoveCategorySpinnerData() {
        return removeCategorySpinnerData;
    }

    public void setRemoveCategorySpinnerData(ArrayList<String> removeCategorySpinnerData) {
        getThis().removeCategorySpinnerData = removeCategorySpinnerData;
    }

    public ArrayAdapter getRemoveCategoryAdapter() {
        return removeCategoryAdapter;
    }

    public void setRemoveCategoryAdapter(ArrayAdapter removeCategoryAdapter) {
        getThis().removeCategoryAdapter = removeCategoryAdapter;
    }

    public Button getRemoveCategoryButton() {
        return removeCategoryButton;
    }

    public void setRemoveCategoryButton(Button removeCategoryButton) {
        getThis().removeCategoryButton = removeCategoryButton;
    }

    public Button getCancelButton() {
        return cancelButton;
    }

    public void setCancelButton(Button cancelButton) {
        getThis().cancelButton = cancelButton;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setView(inflater.inflate(R.layout.remove_category, container, false));

        setShopping((Shopping) getActivity());
        setCategoryData(getShopping().getCategoryData());
        setDbCategoryHelper(new DBCategoryHelper(getActivity()));

        setRemoveCategoryButton((Button) getView().findViewById(R.id.removeCategoryButton));
        setCancelButton((Button) getView().findViewById(R.id.cancelButton));

        setRemoveCategorySpinnerData(getCategoryData().getCategoryListWithBlank());
        setRemoveCategorySpinner((Spinner) getView().findViewById(R.id.categorySpinner));
        setRemoveCategoryAdapter(new ArrayAdapter<>(getThis().getActivity(), android.R.layout.simple_spinner_item, getRemoveCategorySpinnerData()));
        getRemoveCategoryAdapter().setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        getRemoveCategorySpinner().setAdapter(getRemoveCategoryAdapter());

        getRemoveCategoryButton().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String categoryName = getRemoveCategorySpinner().getSelectedItem().toString();

                if (categoryName.isEmpty()) {
                    getShopping().showAlertDialog(getString(R.string.removeCategory), getString(R.string.chooseCategoryToRemove), getString(R.string.ok));
                    return;
                }

                int orderNum = getCategoryData().getCategoryList().indexOf(categoryName);
                getDbCategoryHelper().deleteCategory(categoryName);
                for (int i = orderNum + 1; i < getCategoryData().getCategoryList().size(); i++) {
                    getDbCategoryHelper().moveOrderDownOne(i);
                }

                getShopping().updateCategoryData();
                getShopping().loadFragment(new FullInventory());
            }
        });

        getCancelButton().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getShopping().loadFragment(new FullInventory());
            }
        });

        return view;
    }
}