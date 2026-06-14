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

public class EditCategory extends Fragment {

    private View view;
    private Shopping shopping;
    private CategoryData categoryData;
    private DBItemHelper dbItemHelper;
    private DBCategoryHelper dbCategoryHelper;
    private EditText categoryInput;
    private Spinner categorySpinner;
    private ArrayList<String> categorySpinnerData;
    private ArrayAdapter categorySpinnerAdapter;
    private Button editCategoryButton;
    private Button cancelButton;

    public EditCategory() {}

    private EditCategory getThis() {
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

    public DBItemHelper getDbItemHelper() {
        return dbItemHelper;
    }

    public void setDbItemHelper(DBItemHelper dbItemHelper) {
        getThis().dbItemHelper = dbItemHelper;
    }

    public DBCategoryHelper getDbCategoryHelper() {
        return dbCategoryHelper;
    }

    public void setDbCategoryHelper(DBCategoryHelper dbCategoryHelper) {
        getThis().dbCategoryHelper = dbCategoryHelper;
    }

    public EditText getCategoryInput() {
        return categoryInput;
    }

    public void setCategoryInput(EditText categoryInput) {
        getThis().categoryInput = categoryInput;
    }

    public Spinner getCategorySpinner() {
        return categorySpinner;
    }

    public void setCategorySpinner(Spinner categorySpinner) {
        getThis().categorySpinner = categorySpinner;
    }

    public ArrayList<String> getCategorySpinnerData() {
        return categorySpinnerData;
    }

    public void setCategorySpinnerData(ArrayList<String> categorySpinnerData) {
        getThis().categorySpinnerData = categorySpinnerData;
    }

    public ArrayAdapter getCategorySpinnerAdapter() {
        return categorySpinnerAdapter;
    }

    public void setCategorySpinnerAdapter(ArrayAdapter categorySpinnerAdapter) {
        getThis().categorySpinnerAdapter = categorySpinnerAdapter;
    }

    public Button getEditCategoryButton() {
        return editCategoryButton;
    }

    public void setEditCategoryButton(Button editCategoryButton) {
        getThis().editCategoryButton = editCategoryButton;
    }

    public Button getCancelButton() {
        return cancelButton;
    }

    public void setCancelButton(Button cancelButton) {
        getThis().cancelButton = cancelButton;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setView(inflater.inflate(R.layout.edit_category, container, false));

        setShopping((Shopping) getActivity());
        setDbItemHelper(new DBItemHelper(getActivity()));
        setDbCategoryHelper(new DBCategoryHelper(getActivity()));
        setCategoryData(getShopping().getCategoryData());

        setCategoryInput((EditText) getView().findViewById(R.id.categoryInput));
        setEditCategoryButton((Button) getView().findViewById(R.id.editCategoryButton));
        setCancelButton((Button) getView().findViewById(R.id.cancelButton));

        setCategorySpinnerData(getCategoryData().getCategoryListWithBlank());
        setCategorySpinner((Spinner) getView().findViewById(R.id.categorySpinner));
        setCategorySpinnerAdapter(new ArrayAdapter<>(getThis().getActivity(), android.R.layout.simple_spinner_item, getCategorySpinnerData()));
        getCategorySpinnerAdapter().setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        getCategorySpinner().setAdapter(getCategorySpinnerAdapter());

        getCategoryInput().setText(getString(R.string.emptyString));

        getEditCategoryButton().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String oldCategory = getCategorySpinner().getSelectedItem().toString();
                String newCategory = getCategoryInput().getText().toString();

                if (newCategory.isEmpty() || oldCategory.equals(newCategory)) {
                    getShopping().showAlertDialog(getString(R.string.editCategory), getString(R.string.changeCategoryName), getString(R.string.ok));
                    return;
                }

                getDbItemHelper().changeCategory(oldCategory, newCategory);
                getShopping().updateItemData();

                getDbCategoryHelper().changeCategoryName(oldCategory, newCategory);
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

        return view;
    }
}