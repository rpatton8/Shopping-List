package ryan.android.shopping;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class AddCategory extends Fragment {

    private View view;
    private Shopping shopping;
    private CategoryData categoryData;
    private DBCategoryHelper dbCategoryHelper;

    private EditText categoryInput;
    private Button addCategoryButton;
    private Button cancelButton;

    public AddCategory() {}

    private AddCategory getThis() {
        return this;
    }

    public View getView() {
        return view;
    }

    private void setView(View view) {
        getThis().view = view;
    }

    private Shopping getShopping() {
        return shopping;
    }

    private void setShopping(Shopping shopping) {
        getThis().shopping = shopping;
    }

    private CategoryData getCategoryData() {
        return categoryData;
    }

    private void setCategoryData(CategoryData categoryData) {
        getThis().categoryData = categoryData;
    }

    private DBCategoryHelper getDbCategoryHelper() {
        return dbCategoryHelper;
    }

    private void setDbCategoryHelper(DBCategoryHelper dbCategoryHelper) {
        getThis().dbCategoryHelper = dbCategoryHelper;
    }

    private EditText getCategoryInput() {
        return categoryInput;
    }

    private void setCategoryInput(EditText categoryInput) {
        getThis().categoryInput = categoryInput;
    }

    private Button getAddCategoryButton() {
        return addCategoryButton;
    }

    private void setAddCategoryButton(Button addCategoryButton) {
        getThis().addCategoryButton = addCategoryButton;
    }

    private Button getCancelButton() {
        return cancelButton;
    }

    private void setCancelButton(Button cancelButton) {
        getThis().cancelButton = cancelButton;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setView(inflater.inflate(R.layout.add_category, container, false));

        setShopping((Shopping) getActivity());
        setCategoryData(getShopping().getCategoryData());
        setDbCategoryHelper(new DBCategoryHelper(getActivity()));

        setCategoryInput((EditText) getView().findViewById(R.id.categoryInput));
        setAddCategoryButton((Button) getView().findViewById(R.id.addCategoryButton));
        setCancelButton((Button) getView().findViewById(R.id.cancelButton));

        getCategoryInput().setText(getString(R.string.emptyString));

        getAddCategoryButton().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String categoryName = getCategoryInput().getText().toString();

                if (categoryName.isEmpty()) {
                    getShopping().showAlertDialog(getString(R.string.addCategory), getString(R.string.enterCategoryToAdd), getString(R.string.ok));
                    return;
                }

                int numCategories = getCategoryData().getCategoryList().size();
                getDbCategoryHelper().addNewCategory(categoryName, numCategories);
                getShopping().updateCategoryData();

                getShopping().hideKeyboard();
                getShopping().loadFragment(new FullInventory());
            }
        });

        getCancelButton().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getShopping().hideKeyboard();
                getShopping().loadFragment(new FullInventory());
            }
        });

        return getView();
    }
}