package ryan.android.shopping;

import android.app.Fragment;
import android.os.Bundle;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

//@SuppressWarnings("ALL")
public class LoadScreen extends Fragment {

    private View view;
    private Shopping shopping;
    private TextView loadScreenDeveloperOptionsButton;
    private TextView loadScreenEditButton;
    private TextView shoppingOptionsBackground;
    private Button instructions;

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

    private Button changeDefaultCategoryTitles;
    private RadioButton categoryTitlesExpanded;
    private RadioButton categoryTitlesContracted;

    private Button changeDefaultStoreTitles;
    private RadioButton storeTitlesExpanded;
    private RadioButton storeTitlesContracted;

    private Button optionalData;
    private CheckBox optionalDataQuantity;
    private CheckBox optionalDataPrice;
    private CheckBox optionalDataLocation;
    private CheckBox optionalDataNote;

    private Button swiping;
    private RadioButton swipingOn;
    private RadioButton swipingOff;

    public LoadScreen() {}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.load_screen, container, false);
        shopping = (Shopping) getActivity();

        menuOptionsVisible = false;

        loadScreenDeveloperOptionsButton = view.findViewById(R.id.loadScreenDeveloperOptions);
        loadScreenEditButton = view.findViewById(R.id.loadScreenEditButton);
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

        changeDefaultCategoryTitles = view.findViewById(R.id.changeDefaultCategoryTitles);
        categoryTitlesExpanded = view.findViewById(R.id.categoryTitlesExpanded);
        categoryTitlesContracted = view.findViewById(R.id.categoryTitlesContracted);

        changeDefaultStoreTitles = view.findViewById(R.id.changeDefaultStoreTitles);
        storeTitlesExpanded = view.findViewById(R.id.storeTitlesExpanded);
        storeTitlesContracted = view.findViewById(R.id.storeTitlesContracted);

        optionalData = view.findViewById(R.id.optionalData);
        optionalDataQuantity = view.findViewById(R.id.optionalDataQuantity);
        optionalDataPrice = view.findViewById(R.id.optionalDataPrice);
        optionalDataLocation = view.findViewById(R.id.optionalDataLocation);
        optionalDataNote = view.findViewById(R.id.optionalDataNote);

        swiping = view.findViewById(R.id.swiping);
        swipingOn = view.findViewById(R.id.swipingOn);
        swipingOff = view.findViewById(R.id.swipingOff);

        instructions = view.findViewById(R.id.instructions);

        loadScreenDeveloperOptionsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (menuOptionsVisible) {
                    clearAllData.setVisibility(View.GONE);
                    loadSampleData1.setVisibility(View.GONE);
                    loadSampleData2.setVisibility(View.GONE);
                    menuOptionsVisible = false;
                } else {
                    clearAllData.setVisibility(View.VISIBLE);
                    loadSampleData1.setVisibility(View.VISIBLE);
                    loadSampleData2.setVisibility(View.VISIBLE);
                    menuOptionsVisible = true;
                }
            }
        });

        loadScreenEditButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (menuOptionsVisible) {

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

                    changeDefaultCategoryTitles.setVisibility(View.GONE);
                    categoryTitlesExpanded.setVisibility(View.GONE);
                    categoryTitlesContracted.setVisibility(View.GONE);

                    changeDefaultStoreTitles.setVisibility(View.GONE);
                    storeTitlesExpanded.setVisibility(View.GONE);
                    storeTitlesContracted.setVisibility(View.GONE);

                    optionalData.setVisibility(View.GONE);
                    optionalDataQuantity.setVisibility(View.GONE);
                    optionalDataPrice.setVisibility(View.GONE);
                    optionalDataLocation.setVisibility(View.GONE);
                    optionalDataNote.setVisibility(View.GONE);

                    swiping.setVisibility(View.GONE);
                    swipingOn.setVisibility(View.GONE);
                    swipingOff.setVisibility(View.GONE);

                    shoppingOptionsBackground.setVisibility(View.GONE);
                    menuOptionsVisible = false;

                } else {

                    if (shopping.defaultSortBy.equals(Shopping.SORT_ALPHABETICAL)) {
                        sortAlphabetical.setChecked(true);
                    } else if (shopping.defaultSortBy.equals(Shopping.SORT_BY_CATEGORY)) {
                        sortByCategory.setChecked(true);
                    } else if (shopping.defaultSortBy.equals(Shopping.SORT_BY_STORE)) {
                        sortByStore.setChecked(true);
                    }

                    if (shopping.reorderingMethod.equals(Shopping.DRAG_AND_DROP)) {
                        dragAndDrop.setChecked(true);
                    } else if (shopping.reorderingMethod.equals(Shopping.UP_AND_DOWN_ARROWS)) {
                        upAndDownArrows.setChecked(true);
                    } else if (shopping.reorderingMethod.equals(Shopping.WITH_NUMBERS)) {
                        withNumbers.setChecked(true);
                    }

                    if (shopping.colorScheme.equals(Shopping.COLOR_SCHEME_1)) {
                        colorScheme1.setChecked(true);
                    } else if (shopping.colorScheme.equals(Shopping.COLOR_SCHEME_2)) {
                        colorScheme2.setChecked(true);
                    } else if (shopping.colorScheme.equals(Shopping.COLOR_SCHEME_3)) {
                        colorScheme3.setChecked(true);
                    }

                    if (shopping.defaultCategoryTitles.equals(Shopping.CATEGORY_TITLES_EXPANDED)) {
                        categoryTitlesExpanded.setChecked(true);
                    } else if (shopping.defaultCategoryTitles.equals(Shopping.CATEGORY_TITLES_CONTRACTED)) {
                        categoryTitlesContracted.setChecked(true);
                    }

                    if (shopping.defaultStoreTitles.equals(Shopping.STORE_TITLES_EXPANDED)) {
                        storeTitlesExpanded.setChecked(true);
                    } else if (shopping.defaultStoreTitles.equals(Shopping.STORE_TITLES_CONTRACTED)) {
                        storeTitlesContracted.setChecked(true);
                    }

                    if (shopping.swipingOption.equals(Shopping.SWIPING_ON)) {
                        swipingOn.setChecked(true);
                    } else if (shopping.swipingOption.equals(Shopping.SWIPING_OFF)) {
                        swipingOff.setChecked(true);
                    }

                    if (shopping.optionalDataQuantity.equals(Shopping.OPTIONAL_DATA_ON)) {
                        optionalDataQuantity.setChecked(true);
                    } else if (shopping.optionalDataQuantity.equals(Shopping.OPTIONAL_DATA_OFF)) {
                        optionalDataQuantity.setChecked(false);
                    }

                    if (shopping.optionalDataPrice.equals(Shopping.OPTIONAL_DATA_ON)) {
                        optionalDataPrice.setChecked(true);
                    } else if (shopping.optionalDataPrice.equals(Shopping.OPTIONAL_DATA_OFF)) {
                        optionalDataPrice.setChecked(false);
                    }

                    if (shopping.optionalDataLocation.equals(Shopping.OPTIONAL_DATA_ON)) {
                        optionalDataLocation.setChecked(true);
                    } else if (shopping.optionalDataLocation.equals(Shopping.OPTIONAL_DATA_OFF)) {
                        optionalDataLocation.setChecked(false);
                    }

                    if (shopping.optionalDataNote.equals(Shopping.OPTIONAL_DATA_ON)) {
                        optionalDataNote.setChecked(true);
                    } else if (shopping.optionalDataNote.equals(Shopping.OPTIONAL_DATA_OFF)) {
                        optionalDataNote.setChecked(false);
                    }

                    changeDefaultSortBy.setVisibility(View.VISIBLE);
                    sortAlphabetical.setVisibility(View.VISIBLE);
                    sortByCategory.setVisibility(View.VISIBLE);
                    sortByStore.setVisibility(View.VISIBLE);

                    changeDefaultCategoryTitles.setVisibility(View.VISIBLE);
                    categoryTitlesExpanded.setVisibility(View.VISIBLE);
                    categoryTitlesContracted.setVisibility(View.VISIBLE);

                    changeDefaultStoreTitles.setVisibility(View.VISIBLE);
                    storeTitlesExpanded.setVisibility(View.VISIBLE);
                    storeTitlesContracted.setVisibility(View.VISIBLE);

                    changeReorderingMethod.setVisibility(View.VISIBLE);
                    dragAndDrop.setVisibility(View.VISIBLE);
                    upAndDownArrows.setVisibility(View.VISIBLE);
                    withNumbers.setVisibility(View.VISIBLE);

                    changeColorScheme.setVisibility(View.VISIBLE);
                    colorScheme1.setVisibility(View.VISIBLE);
                    colorScheme2.setVisibility(View.VISIBLE);
                    colorScheme3.setVisibility(View.VISIBLE);

                    optionalData.setVisibility(View.VISIBLE);
                    optionalDataQuantity.setVisibility(View.VISIBLE);
                    optionalDataPrice.setVisibility(View.VISIBLE);
                    optionalDataLocation.setVisibility(View.VISIBLE);
                    optionalDataNote.setVisibility(View.VISIBLE);

                    swiping.setVisibility(View.VISIBLE);
                    swipingOn.setVisibility(View.VISIBLE);
                    swipingOff.setVisibility(View.VISIBLE);

                    shoppingOptionsBackground.setVisibility(View.VISIBLE);
                    menuOptionsVisible = true;
                }
            }
        });

        clearAllData.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                shopping.clearAllData();
                shopping.initializeData();
            }
        });

        loadSampleData1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                shopping.loadStoresAndCategories();
                shopping.loadCategoryData1();
                shopping.loadStoreData1();
                shopping.initializeData();
            }
        });

        loadSampleData2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                shopping.loadStoresAndCategories();
                shopping.loadCategoryData2();
                shopping.loadStoreData2();
                shopping.initializeData();
            }
        });

        sortAlphabetical.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                shopping.defaultSortBy = Shopping.SORT_ALPHABETICAL;
                shopping.inventorySortBy = Shopping.SORT_ALPHABETICAL;
                SharedPreferences sharedPref = getContext().getSharedPreferences("PreferencesFile", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("default_sort_by", "alphabetical");
                editor.apply();
            }
        });

        sortByCategory.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                shopping.defaultSortBy = Shopping.SORT_BY_CATEGORY;
                shopping.inventorySortBy = Shopping.SORT_BY_CATEGORY;
                SharedPreferences sharedPref = getContext().getSharedPreferences("PreferencesFile", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("default_sort_by", "category");
                editor.apply();
            }
        });

        sortByStore.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                shopping.defaultSortBy = Shopping.SORT_BY_STORE;
                shopping.inventorySortBy = Shopping.SORT_BY_STORE;
                SharedPreferences sharedPref = getContext().getSharedPreferences("PreferencesFile", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("default_sort_by", "store");
                editor.apply();
            }
        });

        dragAndDrop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                shopping.reorderingMethod = Shopping.DRAG_AND_DROP;
                SharedPreferences sharedPref = getContext().getSharedPreferences("PreferencesFile", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("reorder_method", "drag and drop");
                editor.apply();
            }
        });

        upAndDownArrows.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                shopping.reorderingMethod = Shopping.UP_AND_DOWN_ARROWS;
                SharedPreferences sharedPref = getContext().getSharedPreferences("PreferencesFile", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("reorder_method", "up and down arrows");
                editor.apply();
            }
        });

        withNumbers.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                shopping.reorderingMethod = Shopping.WITH_NUMBERS;
                SharedPreferences sharedPref = getContext().getSharedPreferences("PreferencesFile", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("reorder_method", "with numbers");
                editor.apply();
            }
        });

        colorScheme1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                shopping.colorScheme = Shopping.COLOR_SCHEME_1;
                SharedPreferences sharedPref = getContext().getSharedPreferences("PreferencesFile", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("color_scheme", "color scheme 1");
                editor.apply();
            }
        });

        colorScheme2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                shopping.colorScheme = Shopping.COLOR_SCHEME_2;
                SharedPreferences sharedPref = getContext().getSharedPreferences("PreferencesFile", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("color_scheme", "color scheme 2");
                editor.apply();
            }
        });

        colorScheme3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                shopping.colorScheme = Shopping.COLOR_SCHEME_3;
                SharedPreferences sharedPref = getContext().getSharedPreferences("PreferencesFile", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("color_scheme", "color scheme 3");
                editor.apply();
            }
        });

        categoryTitlesExpanded.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                shopping.defaultCategoryTitles = Shopping.CATEGORY_TITLES_EXPANDED;
                shopping.categoryTitles = Shopping.CATEGORY_TITLES_EXPANDED;
                SharedPreferences sharedPref = getContext().getSharedPreferences("PreferencesFile", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("default_category_titles", "category titles expanded");
                editor.apply();
            }
        });

        categoryTitlesContracted.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                shopping.defaultCategoryTitles = Shopping.CATEGORY_TITLES_CONTRACTED;
                shopping.categoryTitles = Shopping.CATEGORY_TITLES_CONTRACTED;
                SharedPreferences sharedPref = getContext().getSharedPreferences("PreferencesFile", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("default_category_titles", "category titles contracted");
                editor.apply();
            }
        });

        storeTitlesExpanded.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                shopping.defaultStoreTitles = Shopping.STORE_TITLES_EXPANDED;
                shopping.storeTitles = Shopping.STORE_TITLES_EXPANDED;
                SharedPreferences sharedPref = getContext().getSharedPreferences("PreferencesFile", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("default_store_titles", "store titles expanded");
                editor.apply();
            }
        });

        storeTitlesContracted.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                shopping.defaultStoreTitles = Shopping.STORE_TITLES_CONTRACTED;
                shopping.storeTitles = Shopping.STORE_TITLES_CONTRACTED;
                SharedPreferences sharedPref = getContext().getSharedPreferences("PreferencesFile", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("default_store_titles", "store titles contracted");
                editor.apply();
            }
        });


        swipingOn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                shopping.swipingOption = Shopping.SWIPING_ON;
                SharedPreferences sharedPref = getContext().getSharedPreferences("PreferencesFile", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("swiping_option", "swiping on");
                editor.apply();
            }
        });

        swipingOff.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                shopping.swipingOption = Shopping.SWIPING_OFF;
                SharedPreferences sharedPref = getContext().getSharedPreferences("PreferencesFile", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("swiping_option", "swiping off");
                editor.apply();
            }
        });

        optionalDataQuantity.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                SharedPreferences sharedPref = getContext().getSharedPreferences("PreferencesFile", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();

                if (shopping.optionalDataQuantity.equals(Shopping.OPTIONAL_DATA_ON)) {

                    shopping.optionalDataQuantity = Shopping.OPTIONAL_DATA_OFF;
                    editor.putString("optional_data_quantity", "optional data off");

                } else if (shopping.optionalDataQuantity.equals(Shopping.OPTIONAL_DATA_OFF)) {

                    shopping.optionalDataQuantity = Shopping.OPTIONAL_DATA_ON;
                    editor.putString("optional_data_quantity", "optional data on");

                }

                editor.apply();
            }
        });

        optionalDataPrice.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                SharedPreferences sharedPref = getContext().getSharedPreferences("PreferencesFile", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();

                if (shopping.optionalDataPrice.equals(Shopping.OPTIONAL_DATA_ON)) {

                    shopping.optionalDataPrice = Shopping.OPTIONAL_DATA_OFF;
                    editor.putString("optional_data_price", "optional data off");

                } else if (shopping.optionalDataPrice.equals(Shopping.OPTIONAL_DATA_OFF)) {

                    shopping.optionalDataPrice = Shopping.OPTIONAL_DATA_ON;
                    editor.putString("optional_data_price", "optional data on");

                }

                editor.apply();
            }
        });

        optionalDataLocation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                SharedPreferences sharedPref = getContext().getSharedPreferences("PreferencesFile", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();

                if (shopping.optionalDataLocation.equals(Shopping.OPTIONAL_DATA_ON)) {

                    shopping.optionalDataLocation = Shopping.OPTIONAL_DATA_OFF;
                    editor.putString("optional_data_location", "optional data off");

                } else if (shopping.optionalDataLocation.equals(Shopping.OPTIONAL_DATA_OFF)) {

                    shopping.optionalDataLocation = Shopping.OPTIONAL_DATA_ON;
                    editor.putString("optional_data_location", "optional data on");

                }

                editor.apply();
            }
        });

        optionalDataNote.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                SharedPreferences sharedPref = getContext().getSharedPreferences("PreferencesFile", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();

                if (shopping.optionalDataNote.equals(Shopping.OPTIONAL_DATA_ON)) {

                    shopping.optionalDataNote = Shopping.OPTIONAL_DATA_OFF;
                    editor.putString("optional_data_note", "optional data off");

                } else if (shopping.optionalDataNote.equals(Shopping.OPTIONAL_DATA_OFF)) {

                    shopping.optionalDataNote = Shopping.OPTIONAL_DATA_ON;
                    editor.putString("optional_data_note", "optional data on");

                }

                editor.apply();
            }
        });

        instructions.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // to do
            }
        });

        return view;
    }
}