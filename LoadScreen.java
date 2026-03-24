package ryan.android.shopping;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class LoadScreen extends Fragment {

    private Shopping shopping;
    private boolean menuOptionsVisible;
    private Button clearAllData;
    private Button loadSampleData;
    private Button changeDefaultSortBy;
    private boolean defaultSortByOptionsVisible;
    private Button sortAlphabetical;
    private Button sortByCategory;
    private Button sortByStore;

    public LoadScreen() {}

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.load_screen, container, false);
        shopping = (Shopping) getActivity();
        menuOptionsVisible = false;
        defaultSortByOptionsVisible = false;

        TextView loadScreenEditButton = view.findViewById(R.id.loadScreenEditButton);
        clearAllData = view.findViewById(R.id.clearAllData);
        loadSampleData = view.findViewById(R.id.loadSampleData);
        changeDefaultSortBy = view.findViewById(R.id.changeDefaultSortBy);
        sortAlphabetical = view.findViewById(R.id.sortAlphabetical);
        sortByCategory = view.findViewById(R.id.sortByCategory);
        sortByStore = view.findViewById(R.id.sortByStore);
        Button instructions = view.findViewById(R.id.instructions);

        loadScreenEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (menuOptionsVisible) {
                    clearAllData.setVisibility(View.GONE);
                    loadSampleData.setVisibility(View.GONE);
                    changeDefaultSortBy.setVisibility(View.GONE);
                    menuOptionsVisible = false;
                } else {
                    clearAllData.setVisibility(View.VISIBLE);
                    loadSampleData.setVisibility(View.VISIBLE);
                    changeDefaultSortBy.setVisibility(View.VISIBLE);
                    menuOptionsVisible = true;
                }
            }
        });

        clearAllData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopping.clearAllData();
                shopping.initializeData();
            }
        });

        loadSampleData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopping.loadStoresAndCategories();
                shopping.loadCategoryData();
                shopping.loadStoreData();
                shopping.initializeData();
            }
        });

        changeDefaultSortBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (defaultSortByOptionsVisible) {
                    sortAlphabetical.setVisibility(View.GONE);
                    sortByCategory.setVisibility(View.GONE);
                    sortByStore.setVisibility(View.GONE);
                    defaultSortByOptionsVisible = false;
                } else {
                    sortAlphabetical.setVisibility(View.VISIBLE);
                    sortByCategory.setVisibility(View.VISIBLE);
                    sortByStore.setVisibility(View.VISIBLE);
                    defaultSortByOptionsVisible = true;
                }
            }
        });

        sortAlphabetical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopping.defaultSortBy = Shopping.SORT_ALPHABETICAL;
                SharedPreferences sharedPref = getContext().getSharedPreferences("PreferencesFile", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("default_sort_by", "alphabetical");
                editor.apply();
            }
        });

        sortByCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopping.defaultSortBy = Shopping.SORT_BY_CATEGORY;
                SharedPreferences sharedPref = getContext().getSharedPreferences("PreferencesFile", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("default_sort_by", "category");
                editor.apply();
            }
        });

        sortByStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopping.defaultSortBy = Shopping.SORT_BY_STORE;
                SharedPreferences sharedPref = getContext().getSharedPreferences("PreferencesFile", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("default_sort_by", "store");
                editor.apply();
            }
        });

        instructions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // to do
            }
        });

        return view;
    }
}