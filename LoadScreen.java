package ryan.android.shopping;

import android.app.Fragment;
import android.os.Bundle;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

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

    private Button pictures;
    private RadioButton picturesOn;
    private RadioButton picturesOff;

    private Button reorderEmojis;
    private TextView reorderCategoryEmojiLabel;
    private EditText reorderCategoryEmojiBox;
    private TextView reorderItemByCategoryEmojiLabel;
    private EditText reorderItemByCategoryEmojiBox;
    private TextView reorderItemByStoreEmojiLabel;
    private EditText reorderItemByStoreEmojiBox;
    private TextView reorderStoreEmojiLabel;
    private EditText reorderStoreEmojiBox;

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

        pictures = view.findViewById(R.id.pictures);
        picturesOn = view.findViewById(R.id.picturesOn);
        picturesOff = view.findViewById(R.id.picturesOff);

        reorderEmojis = view.findViewById(R.id.reorderEmojis);
        reorderCategoryEmojiLabel = view.findViewById(R.id.reorderCategoryEmojiLabel);
        reorderCategoryEmojiBox = view.findViewById(R.id.reorderCategoryEmojiBox);
        reorderItemByCategoryEmojiLabel = view.findViewById(R.id.reorderItemByCategoryEmojiLabel);
        reorderItemByCategoryEmojiBox = view.findViewById(R.id.reorderItemByCategoryEmojiBox);
        reorderItemByStoreEmojiLabel = view.findViewById(R.id.reorderItemByStoreEmojiLabel);
        reorderItemByStoreEmojiBox = view.findViewById(R.id.reorderItemByStoreEmojiBox);
        reorderStoreEmojiLabel = view.findViewById(R.id.reorderStoreEmojiLabel);
        reorderStoreEmojiBox = view.findViewById(R.id.reorderStoreEmojiBox);

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

                    pictures.setVisibility(View.GONE);
                    picturesOn.setVisibility(View.GONE);
                    picturesOff.setVisibility(View.GONE);

                    reorderEmojis.setVisibility(View.GONE);
                    reorderCategoryEmojiLabel.setVisibility(View.GONE);
                    reorderCategoryEmojiBox.setVisibility(View.GONE);
                    reorderItemByCategoryEmojiLabel.setVisibility(View.GONE);
                    reorderItemByCategoryEmojiBox.setVisibility(View.GONE);
                    reorderItemByStoreEmojiLabel.setVisibility(View.GONE);
                    reorderItemByStoreEmojiBox.setVisibility(View.GONE);
                    reorderStoreEmojiLabel.setVisibility(View.GONE);
                    reorderStoreEmojiBox.setVisibility(View.GONE);

                    shoppingOptionsBackground.setVisibility(View.GONE);
                    menuOptionsVisible = false;

                } else {

                    if (shopping.getDefaultSortBy().equals(Shopping.SORT_ALPHABETICAL)) {
                        sortAlphabetical.setChecked(true);
                    } else if (shopping.getDefaultSortBy().equals(Shopping.SORT_BY_CATEGORY)) {
                        sortByCategory.setChecked(true);
                    } else if (shopping.getDefaultSortBy().equals(Shopping.SORT_BY_STORE)) {
                        sortByStore.setChecked(true);
                    }

                    if (shopping.getReorderingMethod().equals(Shopping.DRAG_AND_DROP)) {
                        dragAndDrop.setChecked(true);
                    } else if (shopping.getReorderingMethod().equals(Shopping.UP_AND_DOWN_ARROWS)) {
                        upAndDownArrows.setChecked(true);
                    } else if (shopping.getReorderingMethod().equals(Shopping.WITH_NUMBERS)) {
                        withNumbers.setChecked(true);
                    }

                    if (shopping.getColorScheme().equals(Shopping.COLOR_SCHEME_1)) {
                        colorScheme1.setChecked(true);
                    } else if (shopping.getColorScheme().equals(Shopping.COLOR_SCHEME_2)) {
                        colorScheme2.setChecked(true);
                    } else if (shopping.getColorScheme().equals(Shopping.COLOR_SCHEME_3)) {
                        colorScheme3.setChecked(true);
                    }

                    if (shopping.getDefaultCategoryTitles().equals(Shopping.CATEGORY_TITLES_EXPANDED)) {
                        categoryTitlesExpanded.setChecked(true);
                    } else if (shopping.getDefaultCategoryTitles().equals(Shopping.CATEGORY_TITLES_CONTRACTED)) {
                        categoryTitlesContracted.setChecked(true);
                    }

                    if (shopping.getDefaultStoreTitles().equals(Shopping.STORE_TITLES_EXPANDED)) {
                        storeTitlesExpanded.setChecked(true);
                    } else if (shopping.getDefaultStoreTitles().equals(Shopping.STORE_TITLES_CONTRACTED)) {
                        storeTitlesContracted.setChecked(true);
                    }

                    if (shopping.getSwipingOption().equals(Shopping.SWIPING_ON)) {
                        swipingOn.setChecked(true);
                    } else if (shopping.getSwipingOption().equals(Shopping.SWIPING_OFF)) {
                        swipingOff.setChecked(true);
                    }

                    if (shopping.getPicturesOption().equals(Shopping.PICTURES_ON)) {
                        picturesOn.setChecked(true);
                    } else if (shopping.getPicturesOption().equals(Shopping.PICTURES_OFF)) {
                        picturesOff.setChecked(true);
                    }

                    if (shopping.getOptionalDataQuantity().equals(Shopping.OPTIONAL_DATA_ON)) {
                        optionalDataQuantity.setChecked(true);
                    } else if (shopping.getOptionalDataQuantity().equals(Shopping.OPTIONAL_DATA_OFF)) {
                        optionalDataQuantity.setChecked(false);
                    }

                    if (shopping.getOptionalDataPrice().equals(Shopping.OPTIONAL_DATA_ON)) {
                        optionalDataPrice.setChecked(true);
                    } else if (shopping.getOptionalDataPrice().equals(Shopping.OPTIONAL_DATA_OFF)) {
                        optionalDataPrice.setChecked(false);
                    }

                    if (shopping.getOptionalDataLocation().equals(Shopping.OPTIONAL_DATA_ON)) {
                        optionalDataLocation.setChecked(true);
                    } else if (shopping.getOptionalDataLocation().equals(Shopping.OPTIONAL_DATA_OFF)) {
                        optionalDataLocation.setChecked(false);
                    }

                    if (shopping.getOptionalDataNote().equals(Shopping.OPTIONAL_DATA_ON)) {
                        optionalDataNote.setChecked(true);
                    } else if (shopping.getOptionalDataNote().equals(Shopping.OPTIONAL_DATA_OFF)) {
                        optionalDataNote.setChecked(false);
                    }

                    reorderCategoryEmojiBox.setText(shopping.getReorderCategoryEmoji());
                    reorderItemByCategoryEmojiBox.setText(shopping.getReorderItemByCategoryEmoji());
                    reorderItemByStoreEmojiBox.setText(shopping.getReorderItemByStoreEmoji());
                    reorderStoreEmojiBox.setText(shopping.getReorderStoreEmoji());

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

                    pictures.setVisibility(View.VISIBLE);
                    picturesOn.setVisibility(View.VISIBLE);
                    picturesOff.setVisibility(View.VISIBLE);

                    reorderEmojis.setVisibility(View.VISIBLE);
                    reorderCategoryEmojiLabel.setVisibility(View.VISIBLE);
                    reorderCategoryEmojiBox.setVisibility(View.VISIBLE);
                    reorderItemByCategoryEmojiLabel.setVisibility(View.VISIBLE);
                    reorderItemByCategoryEmojiBox.setVisibility(View.VISIBLE);
                    reorderItemByStoreEmojiLabel.setVisibility(View.VISIBLE);
                    reorderItemByStoreEmojiBox.setVisibility(View.VISIBLE);
                    reorderStoreEmojiLabel.setVisibility(View.VISIBLE);
                    reorderStoreEmojiBox.setVisibility(View.VISIBLE);

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
                shopping.setDefaultSortBy(Shopping.SORT_ALPHABETICAL);
                shopping.setInventorySortBy(Shopping.SORT_ALPHABETICAL);
                SharedPreferences sharedPref = getContext().getSharedPreferences(getString(R.string.spPreferencesFile), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.sp_default_sort_by), getString(R.string.spSortAlphabetically));
                editor.apply();
            }
        });

        sortByCategory.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                shopping.setDefaultSortBy(Shopping.SORT_BY_CATEGORY);
                shopping.setInventorySortBy(Shopping.SORT_BY_CATEGORY);
                SharedPreferences sharedPref = getContext().getSharedPreferences(getString(R.string.spPreferencesFile), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.sp_default_sort_by), getString(R.string.spSortByCategory));
                editor.apply();
            }
        });

        sortByStore.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                shopping.setDefaultSortBy(Shopping.SORT_BY_STORE);
                shopping.setInventorySortBy(Shopping.SORT_BY_STORE);
                SharedPreferences sharedPref = getContext().getSharedPreferences(getString(R.string.spPreferencesFile), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.sp_default_sort_by), getString(R.string.spSortByStore));
                editor.apply();
            }
        });

        dragAndDrop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                shopping.setReorderingMethod(Shopping.DRAG_AND_DROP);
                SharedPreferences sharedPref = getContext().getSharedPreferences(getString(R.string.spPreferencesFile), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.sp_reorder_method), getString(R.string.spDragAndDrop));
                editor.apply();
            }
        });

        upAndDownArrows.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                shopping.setReorderingMethod(Shopping.UP_AND_DOWN_ARROWS);
                SharedPreferences sharedPref = getContext().getSharedPreferences(getString(R.string.spPreferencesFile), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.sp_reorder_method), getString(R.string.spUpAndDownArrows));
                editor.apply();
            }
        });

        withNumbers.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                shopping.setReorderingMethod(Shopping.WITH_NUMBERS);
                SharedPreferences sharedPref = getContext().getSharedPreferences(getString(R.string.spPreferencesFile), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.sp_reorder_method), getString(R.string.spWithNumbers));
                editor.apply();
            }
        });

        colorScheme1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                shopping.setColorScheme(Shopping.COLOR_SCHEME_1);
                SharedPreferences sharedPref = getContext().getSharedPreferences(getString(R.string.spPreferencesFile), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.sp_color_scheme), getString(R.string.spColorScheme1));
                editor.apply();
            }
        });

        colorScheme2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                shopping.setColorScheme(Shopping.COLOR_SCHEME_2);
                SharedPreferences sharedPref = getContext().getSharedPreferences(getString(R.string.spPreferencesFile), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.sp_color_scheme), getString(R.string.spColorScheme2));
                editor.apply();
            }
        });

        colorScheme3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                shopping.setColorScheme(Shopping.COLOR_SCHEME_3);
                SharedPreferences sharedPref = getContext().getSharedPreferences(getString(R.string.spPreferencesFile), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.sp_color_scheme), getString(R.string.spColorScheme3));
                editor.apply();
            }
        });

        categoryTitlesExpanded.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                shopping.setDefaultCategoryTitles(Shopping.CATEGORY_TITLES_EXPANDED);
                shopping.setCategoryTitles(Shopping.CATEGORY_TITLES_EXPANDED);
                SharedPreferences sharedPref = getContext().getSharedPreferences(getString(R.string.spPreferencesFile), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.sp_default_category_titles), getString(R.string.spCategoryTitlesExpanded));
                editor.apply();
            }
        });

        categoryTitlesContracted.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                shopping.setDefaultCategoryTitles(Shopping.CATEGORY_TITLES_CONTRACTED);
                shopping.setCategoryTitles(Shopping.CATEGORY_TITLES_CONTRACTED);
                SharedPreferences sharedPref = getContext().getSharedPreferences(getString(R.string.spPreferencesFile), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.sp_default_category_titles), getString(R.string.spCategoryTitlesContracted));
                editor.apply();
            }
        });

        storeTitlesExpanded.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                shopping.setDefaultStoreTitles(Shopping.STORE_TITLES_EXPANDED);
                shopping.setStoreTitles(Shopping.STORE_TITLES_EXPANDED);
                SharedPreferences sharedPref = getContext().getSharedPreferences(getString(R.string.spPreferencesFile), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.sp_default_store_titles), getString(R.string.spStoreTitlesExpanded));
                editor.apply();
            }
        });

        storeTitlesContracted.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                shopping.setDefaultStoreTitles(Shopping.STORE_TITLES_CONTRACTED);
                shopping.setStoreTitles(Shopping.STORE_TITLES_CONTRACTED);
                SharedPreferences sharedPref = getContext().getSharedPreferences(getString(R.string.spPreferencesFile), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.sp_default_store_titles), getString(R.string.spStoreTitlesContracted));
                editor.apply();
            }
        });

        swipingOn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                shopping.setSwipingOption(Shopping.SWIPING_ON);
                SharedPreferences sharedPref = getContext().getSharedPreferences(getString(R.string.spPreferencesFile), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.sp_swiping_option), getString(R.string.spSwipingOn));
                editor.apply();
            }
        });

        swipingOff.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                shopping.setSwipingOption(Shopping.SWIPING_OFF);
                SharedPreferences sharedPref = getContext().getSharedPreferences(getString(R.string.spPreferencesFile), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.sp_swiping_option), getString(R.string.spSwipingOff));
                editor.apply();
            }
        });

        picturesOn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                shopping.setSwipingOption(Shopping.PICTURES_ON);
                SharedPreferences sharedPref = getContext().getSharedPreferences(getString(R.string.spPreferencesFile), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.sp_pictures_option), getString(R.string.spPicturesOn));
                editor.apply();
            }
        });

        picturesOff.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                shopping.setSwipingOption(Shopping.PICTURES_OFF);
                SharedPreferences sharedPref = getContext().getSharedPreferences(getString(R.string.spPreferencesFile), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.sp_pictures_option), getString(R.string.spPicturesOff));
                editor.apply();
            }
        });

        optionalDataQuantity.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences sharedPref = getContext().getSharedPreferences(getString(R.string.spPreferencesFile), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                if (shopping.getOptionalDataQuantity().equals(Shopping.OPTIONAL_DATA_ON)) {
                    shopping.setOptionalDataQuantity(Shopping.OPTIONAL_DATA_OFF);
                    editor.putString(getString(R.string.sp_optional_data_quantity), getString(R.string.spOptionalDataOff));
                } else if (shopping.getOptionalDataQuantity().equals(Shopping.OPTIONAL_DATA_OFF)) {
                    shopping.setOptionalDataQuantity(Shopping.OPTIONAL_DATA_ON);
                    editor.putString(getString(R.string.sp_optional_data_quantity), getString(R.string.spOptionalDataOn));
                }
                editor.apply();
            }
        });

        optionalDataPrice.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences sharedPref = getContext().getSharedPreferences(getString(R.string.spPreferencesFile), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                if (shopping.getOptionalDataPrice().equals(Shopping.OPTIONAL_DATA_ON)) {
                    shopping.setOptionalDataPrice(Shopping.OPTIONAL_DATA_OFF);
                    editor.putString(getString(R.string.sp_optional_data_price), getString(R.string.spOptionalDataOff));
                } else if (shopping.getOptionalDataPrice().equals(Shopping.OPTIONAL_DATA_OFF)) {
                    shopping.setOptionalDataPrice(Shopping.OPTIONAL_DATA_ON);
                    editor.putString(getString(R.string.sp_optional_data_price), getString(R.string.spOptionalDataOn));
                }
                editor.apply();
            }
        });

        optionalDataLocation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences sharedPref = getContext().getSharedPreferences(getString(R.string.spPreferencesFile), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                if (shopping.getOptionalDataLocation().equals(Shopping.OPTIONAL_DATA_ON)) {
                    shopping.setOptionalDataLocation(Shopping.OPTIONAL_DATA_OFF);
                    editor.putString(getString(R.string.sp_optional_data_location), getString(R.string.spOptionalDataOff));
                } else if (shopping.getOptionalDataLocation().equals(Shopping.OPTIONAL_DATA_OFF)) {
                    shopping.setOptionalDataLocation(Shopping.OPTIONAL_DATA_ON);
                    editor.putString(getString(R.string.sp_optional_data_location), getString(R.string.spOptionalDataOn));
                }
                editor.apply();
            }
        });

        optionalDataNote.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences sharedPref = getContext().getSharedPreferences(getString(R.string.spPreferencesFile), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                if (shopping.getOptionalDataNote().equals(Shopping.OPTIONAL_DATA_ON)) {
                    shopping.setOptionalDataNote(Shopping.OPTIONAL_DATA_OFF);
                    editor.putString(getString(R.string.sp_optional_data_note), getString(R.string.spOptionalDataOff));
                } else if (shopping.getOptionalDataNote().equals(Shopping.OPTIONAL_DATA_OFF)) {
                    shopping.setOptionalDataNote(Shopping.OPTIONAL_DATA_ON);
                    editor.putString(getString(R.string.sp_optional_data_note), getString(R.string.spOptionalDataOn));
                }
                editor.apply();
            }
        });

        reorderCategoryEmojiBox.addTextChangedListener(new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void afterTextChanged(Editable s) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SharedPreferences sharedPref = getContext().getSharedPreferences(getString(R.string.spPreferencesFile), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.sp_reorder_category_emoji), reorderCategoryEmojiBox.getText().toString());
                editor.apply();
            }

        });

        reorderItemByCategoryEmojiBox.addTextChangedListener(new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void afterTextChanged(Editable s) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SharedPreferences sharedPref = getContext().getSharedPreferences(getString(R.string.spPreferencesFile), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.sp_reorder_item_by_category_emoji), reorderItemByCategoryEmojiBox.getText().toString());
                editor.apply();
            }

        });

        reorderItemByStoreEmojiBox.addTextChangedListener(new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void afterTextChanged(Editable s) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SharedPreferences sharedPref = getContext().getSharedPreferences(getString(R.string.spPreferencesFile), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.sp_reorder_item_by_store_emoji), reorderItemByStoreEmojiBox.getText().toString());
                editor.apply();
            }

        });

        reorderStoreEmojiBox.addTextChangedListener(new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void afterTextChanged(Editable s) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SharedPreferences sharedPref = getContext().getSharedPreferences(getString(R.string.spPreferencesFile), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.sp_reorder_store_emoji), reorderStoreEmojiBox.getText().toString());
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