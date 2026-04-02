package ryan.android.shopping;

import android.app.Fragment;
import android.os.Bundle;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

public class LoadScreen extends Fragment {

    private Shopping shopping;


    private boolean menuOptionsVisible;
    private Button clearAllData;
    private Button loadSampleData1;
    private Button loadSampleData2;


    private Button changeDefaultSortBy;
    private RadioButton sortAlphabetical;
    private RadioButton sortByCategory;
    private RadioButton sortByStore;


    private Button changeReorderingMethod;
    private RadioButton dragAndDrop;
    private RadioButton upAndDownArrows;
    private RadioButton withNumbers;


    private Button changeColorScheme;
    private RadioButton colorScheme1;
    private RadioButton colorScheme2;
    private RadioButton colorScheme3;


    private TextView shoppingOptionsBackground;

    public LoadScreen() {}

    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.load_screen, container, false);
        shopping = (Shopping) getActivity();

        menuOptionsVisible = false;

        TextView loadScreenEditButton = view.findViewById(R.id.loadScreenEditButton);
        shoppingOptionsBackground =  view.findViewById(R.id.shoppingOptionsBackground);
        clearAllData = view.findViewById(R.id.clearAllData);
        loadSampleData1 = view.findViewById(R.id.loadSampleData1);
        loadSampleData2 = view.findViewById(R.id.loadSampleData2);

        changeDefaultSortBy = view.findViewById(R.id.changeDefaultSortBy);
        sortAlphabetical = view.findViewById(R.id.sortAlphabetical);
        sortByCategory = view.findViewById(R.id.sortByCategory);
        sortByStore = view.findViewById(R.id.sortByStore);

        changeReorderingMethod = view.findViewById(R.id.changeReorderingMethod);
        dragAndDrop = view.findViewById(R.id.dragAndDrop);
        upAndDownArrows = view.findViewById(R.id.upAndDownArrows);
        withNumbers = view.findViewById(R.id.withNumbers);

        changeColorScheme = view.findViewById(R.id.changeColorScheme);
        colorScheme1 = view.findViewById(R.id.colorScheme1);
        colorScheme2 = view.findViewById(R.id.colorScheme2);
        colorScheme3 = view.findViewById(R.id.colorScheme3);

        Button instructions = view.findViewById(R.id.instructions);

        loadScreenEditButton.setOnClickListener(v -> {
            if (menuOptionsVisible) {
                clearAllData.setVisibility(View.GONE);
                loadSampleData1.setVisibility(View.GONE);
                loadSampleData2.setVisibility(View.GONE);

                changeDefaultSortBy.setVisibility(View.GONE);
                sortAlphabetical.setVisibility(View.GONE);
                sortByCategory.setVisibility(View.GONE);
                sortByStore.setVisibility(View.GONE);

                changeReorderingMethod.setVisibility(View.GONE);
                dragAndDrop.setVisibility(View.GONE);
                upAndDownArrows.setVisibility(View.GONE);
                withNumbers.setVisibility(View.GONE);

                changeColorScheme.setVisibility(View.GONE);
                colorScheme1.setVisibility(View.GONE);
                colorScheme2.setVisibility(View.GONE);
                colorScheme3.setVisibility(View.GONE);

                shoppingOptionsBackground.setVisibility(View.GONE);
                menuOptionsVisible = false;
            } else {
                switch (shopping.defaultSortBy) {
                    case Shopping.SORT_ALPHABETICAL:
                        sortAlphabetical.setChecked(true);
                        break;
                    case Shopping.SORT_BY_CATEGORY:
                        sortByCategory.setChecked(true);
                        break;
                    case Shopping.SORT_BY_STORE:
                        sortByStore.setChecked(true);
                        break;
                }
                switch (shopping.reorderingMethod) {
                    case Shopping.DRAG_AND_DROP:
                        dragAndDrop.setChecked(true);
                        break;
                    case Shopping.UP_AND_DOWN_ARROWS:
                        upAndDownArrows.setChecked(true);
                        break;
                    case Shopping.WITH_NUMBERS:
                        withNumbers.setChecked(true);
                        break;
                }
                switch (shopping.colorScheme) {
                    case Shopping.COLOR_SCHEME_1:
                        colorScheme1.setChecked(true);
                        break;
                    case Shopping.COLOR_SCHEME_2:
                        colorScheme2.setChecked(true);
                        break;
                    case Shopping.COLOR_SCHEME_3:
                        colorScheme3.setChecked(true);
                        break;
                }
                clearAllData.setVisibility(View.VISIBLE);
                loadSampleData1.setVisibility(View.VISIBLE);
                loadSampleData2.setVisibility(View.VISIBLE);

                changeDefaultSortBy.setVisibility(View.VISIBLE);
                sortAlphabetical.setVisibility(View.VISIBLE);
                sortByCategory.setVisibility(View.VISIBLE);
                sortByStore.setVisibility(View.VISIBLE);

                changeReorderingMethod.setVisibility(View.VISIBLE);
                dragAndDrop.setVisibility(View.VISIBLE);
                upAndDownArrows.setVisibility(View.VISIBLE);
                withNumbers.setVisibility(View.VISIBLE);

                changeColorScheme.setVisibility(View.VISIBLE);
                colorScheme1.setVisibility(View.VISIBLE);
                colorScheme2.setVisibility(View.VISIBLE);
                colorScheme3.setVisibility(View.VISIBLE);

                shoppingOptionsBackground.setVisibility(View.VISIBLE);
                menuOptionsVisible = true;
            }
        });

        clearAllData.setOnClickListener(v -> {
            shopping.clearAllData();
            shopping.initializeData();
        });

        loadSampleData1.setOnClickListener(v -> {
            shopping.loadStoresAndCategories();
            shopping.loadCategoryData1();
            shopping.loadStoreData1();
            shopping.initializeData();
        });

        loadSampleData2.setOnClickListener(v -> {
            shopping.loadStoresAndCategories();
            shopping.loadCategoryData2();
            shopping.loadStoreData2();
            shopping.initializeData();
        });

        sortAlphabetical.setOnClickListener(v -> {
            shopping.defaultSortBy = Shopping.SORT_ALPHABETICAL;
            shopping.inventorySortBy = Shopping.SORT_ALPHABETICAL;
            SharedPreferences sharedPref = getContext().getSharedPreferences("PreferencesFile", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("default_sort_by", "alphabetical");
            editor.apply();
        });

        sortByCategory.setOnClickListener(v -> {
            shopping.defaultSortBy = Shopping.SORT_BY_CATEGORY;
            shopping.inventorySortBy = Shopping.SORT_BY_CATEGORY;
            SharedPreferences sharedPref = getContext().getSharedPreferences("PreferencesFile", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("default_sort_by", "category");
            editor.apply();
        });

        sortByStore.setOnClickListener(v -> {
            shopping.defaultSortBy = Shopping.SORT_BY_STORE;
            shopping.inventorySortBy = Shopping.SORT_BY_STORE;
            SharedPreferences sharedPref = getContext().getSharedPreferences("PreferencesFile", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("default_sort_by", "store");
            editor.apply();
        });

        dragAndDrop.setOnClickListener(v -> {
            shopping.reorderingMethod  = Shopping.DRAG_AND_DROP;
            SharedPreferences sharedPref = getContext().getSharedPreferences("PreferencesFile", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("reorder_method", "drag and drop");
            editor.apply();
        });

        upAndDownArrows.setOnClickListener(v -> {
            shopping.reorderingMethod  = Shopping.UP_AND_DOWN_ARROWS;
            SharedPreferences sharedPref = getContext().getSharedPreferences("PreferencesFile", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("reorder_method", "up and down arrows");
            editor.apply();
        });

        withNumbers.setOnClickListener(v -> {
            shopping.reorderingMethod  = Shopping.WITH_NUMBERS;
            SharedPreferences sharedPref = getContext().getSharedPreferences("PreferencesFile", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("reorder_method", "with numbers");
            editor.apply();
        });

        colorScheme1.setOnClickListener(v -> {
            shopping.colorScheme = Shopping.COLOR_SCHEME_1;
            SharedPreferences sharedPref = getContext().getSharedPreferences("PreferencesFile", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("color_scheme", "color scheme 1");
            editor.apply();
        });

        colorScheme2.setOnClickListener(v -> {
            shopping.colorScheme = Shopping.COLOR_SCHEME_2;
            SharedPreferences sharedPref = getContext().getSharedPreferences("PreferencesFile", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("color_scheme", "color scheme 2");
            editor.apply();
        });

        colorScheme3.setOnClickListener(v -> {
            shopping.colorScheme = Shopping.COLOR_SCHEME_3;
            SharedPreferences sharedPref = getContext().getSharedPreferences("PreferencesFile", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("color_scheme", "color scheme 3");
            editor.apply();
        });

        instructions.setOnClickListener(v -> {
            // to do
        });

        return view;
    }
}