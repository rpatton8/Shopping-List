package ryan.android.shopping;

import androidx.fragment.app.Fragment;
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

public class HomeScreen extends Fragment {

    private View view;
    private Shopping shopping;
    private TextView homeScreenDeveloperOptionsButton;
    private TextView homeScreenEditButton;
    private TextView homeScreenTitle;
    private String lastMainTitle;
    private TextView shoppingSettingsBackground;
    private Button instructions;
    private boolean menuOptionsVisible;
    private boolean developerOptionsVisible;
    private boolean instructionsVisible;
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

    public HomeScreen() {}

    private HomeScreen getThis() {
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

    private TextView getHomeScreenDeveloperOptionsButton() {
        return homeScreenDeveloperOptionsButton;
    }

    private void setHomeScreenDeveloperOptionsButton(TextView homeScreenDeveloperOptionsButton) {
        getThis().homeScreenDeveloperOptionsButton = homeScreenDeveloperOptionsButton;
    }

    private TextView getHomeScreenEditButton() {
        return homeScreenEditButton;
    }

    private void setHomeScreenEditButton(TextView homeScreenEditButton) {
        getThis().homeScreenEditButton = homeScreenEditButton;
    }

    public TextView getHomeScreenTitle() {
        return homeScreenTitle;
    }

    public void setHomeScreenTitle(TextView homeScreenTitle) {
        getThis().homeScreenTitle = homeScreenTitle;
    }

    public String getLastMainTitle() {
        return lastMainTitle;
    }

    public void setLastMainTitle(String lastMainTitle) {
        getThis().lastMainTitle = lastMainTitle;
    }

    private TextView getShoppingSettingsBackground() {
        return shoppingSettingsBackground;
    }

    private void setShoppingSettingsBackground(TextView shoppingSettingsBackground) {
        getThis().shoppingSettingsBackground = shoppingSettingsBackground;
    }

    private Button getInstructions() {
        return instructions;
    }

    private void setInstructions(Button instructions) {
        getThis().instructions = instructions;
    }

    private boolean menuOptionsVisible() {
        return menuOptionsVisible;
    }

    private void setMenuOptionsVisible(boolean menuOptionsVisible) {
        getThis().menuOptionsVisible = menuOptionsVisible;
    }

    private boolean developerOptionsVisible() {
        return developerOptionsVisible;
    }

    private void setDeveloperOptionsVisible(boolean developerOptionsVisible) {
        getThis().developerOptionsVisible = developerOptionsVisible;
    }

    public boolean instructionsVisible() {
        return instructionsVisible;
    }

    public void setInstructionsVisible(boolean instructionsVisible) {
        getThis().instructionsVisible = instructionsVisible;
    }

    private Button getClearAllData() {
        return clearAllData;
    }

    private void setClearAllData(Button clearAllData) {
        getThis().clearAllData = clearAllData;
    }

    private Button getLoadSampleData1() {
        return loadSampleData1;
    }

    private void setLoadSampleData1(Button loadSampleData1) {
        getThis().loadSampleData1 = loadSampleData1;
    }

    private Button getLoadSampleData2() {
        return loadSampleData2;
    }

    private void setLoadSampleData2(Button loadSampleData2) {
        getThis().loadSampleData2 = loadSampleData2;
    }

    private Button getChangeDefaultSortBy() {
        return changeDefaultSortBy;
    }

    private void setChangeDefaultSortBy(Button changeDefaultSortBy) {
        getThis().changeDefaultSortBy = changeDefaultSortBy;
    }

    private RadioButton getSortAlphabetical() {
        return sortAlphabetical;
    }

    private void setSortAlphabetical(RadioButton sortAlphabetical) {
        getThis().sortAlphabetical = sortAlphabetical;
    }

    private RadioButton getSortByCategory() {
        return sortByCategory;
    }

    private void setSortByCategory(RadioButton sortByCategory) {
        getThis().sortByCategory = sortByCategory;
    }

    private RadioButton getSortByStore() {
        return sortByStore;
    }

    private void setSortByStore(RadioButton sortByStore) {
        getThis().sortByStore = sortByStore;
    }

    private Button getChangeReorderingMethod() {
        return changeReorderingMethod;
    }

    private void setChangeReorderingMethod(Button changeReorderingMethod) {
        getThis().changeReorderingMethod = changeReorderingMethod;
    }

    private RadioButton getDragAndDrop() {
        return dragAndDrop;
    }

    private void setDragAndDrop(RadioButton dragAndDrop) {
        getThis().dragAndDrop = dragAndDrop;
    }

    private RadioButton getUpAndDownArrows() {
        return upAndDownArrows;
    }

    private void setUpAndDownArrows(RadioButton upAndDownArrows) {
        getThis().upAndDownArrows = upAndDownArrows;
    }

    private RadioButton getWithNumbers() {
        return withNumbers;
    }

    private void setWithNumbers(RadioButton withNumbers) {
        getThis().withNumbers = withNumbers;
    }

    private Button getChangeColorScheme() {
        return changeColorScheme;
    }

    private void setChangeColorScheme(Button changeColorScheme) {
        getThis().changeColorScheme = changeColorScheme;
    }

    private RadioButton getColorScheme1() {
        return colorScheme1;
    }

    private void setColorScheme1(RadioButton colorScheme1) {
        getThis().colorScheme1 = colorScheme1;
    }

    private RadioButton getColorScheme2() {
        return colorScheme2;
    }

    private void setColorScheme2(RadioButton colorScheme2) {
        getThis().colorScheme2 = colorScheme2;
    }

    private RadioButton getColorScheme3() {
        return colorScheme3;
    }

    private void setColorScheme3(RadioButton colorScheme3) {
        getThis().colorScheme3 = colorScheme3;
    }

    private Button getChangeDefaultCategoryTitles() {
        return changeDefaultCategoryTitles;
    }

    private void setChangeDefaultCategoryTitles(Button changeDefaultCategoryTitles) {
        getThis().changeDefaultCategoryTitles = changeDefaultCategoryTitles;
    }

    private RadioButton getCategoryTitlesExpanded() {
        return categoryTitlesExpanded;
    }

    private void setCategoryTitlesExpanded(RadioButton categoryTitlesExpanded) {
        getThis().categoryTitlesExpanded = categoryTitlesExpanded;
    }

    private RadioButton getCategoryTitlesContracted() {
        return categoryTitlesContracted;
    }

    private void setCategoryTitlesContracted(RadioButton categoryTitlesContracted) {
        getThis().categoryTitlesContracted = categoryTitlesContracted;
    }

    private Button getChangeDefaultStoreTitles() {
        return changeDefaultStoreTitles;
    }

    private void setChangeDefaultStoreTitles(Button changeDefaultStoreTitles) {
        getThis().changeDefaultStoreTitles = changeDefaultStoreTitles;
    }

    private RadioButton getStoreTitlesExpanded() {
        return storeTitlesExpanded;
    }

    private void setStoreTitlesExpanded(RadioButton storeTitlesExpanded) {
        getThis().storeTitlesExpanded = storeTitlesExpanded;
    }

    private RadioButton getStoreTitlesContracted() {
        return storeTitlesContracted;
    }

    private void setStoreTitlesContracted(RadioButton storeTitlesContracted) {
        getThis().storeTitlesContracted = storeTitlesContracted;
    }

    private Button getOptionalData() {
        return optionalData;
    }

    private void setOptionalData(Button optionalData) {
        getThis().optionalData = optionalData;
    }

    private CheckBox getOptionalDataQuantity() {
        return optionalDataQuantity;
    }

    private void setOptionalDataQuantity(CheckBox optionalDataQuantity) {
        getThis().optionalDataQuantity = optionalDataQuantity;
    }

    private CheckBox getOptionalDataPrice() {
        return optionalDataPrice;
    }

    private void setOptionalDataPrice(CheckBox optionalDataPrice) {
        getThis().optionalDataPrice = optionalDataPrice;
    }

    private CheckBox getOptionalDataLocation() {
        return optionalDataLocation;
    }

    private void setOptionalDataLocation(CheckBox optionalDataLocation) {
        getThis().optionalDataLocation = optionalDataLocation;
    }

    private CheckBox getOptionalDataNote() {
        return optionalDataNote;
    }

    private void setOptionalDataNote(CheckBox optionalDataNote) {
        getThis().optionalDataNote = optionalDataNote;
    }

    private Button getSwiping() {
        return swiping;
    }

    private void setSwiping(Button swiping) {
        getThis().swiping = swiping;
    }

    private RadioButton getSwipingOn() {
        return swipingOn;
    }

    private void setSwipingOn(RadioButton swipingOn) {
        getThis().swipingOn = swipingOn;
    }

    private RadioButton getSwipingOff() {
        return swipingOff;
    }

    private void setSwipingOff(RadioButton swipingOff) {
        getThis().swipingOff = swipingOff;
    }

    private Button getPictures() {
        return pictures;
    }

    private void setPictures(Button pictures) {
        getThis().pictures = pictures;
    }

    private RadioButton getPicturesOn() {
        return picturesOn;
    }

    private void setPicturesOn(RadioButton picturesOn) {
        getThis().picturesOn = picturesOn;
    }

    private RadioButton getPicturesOff() {
        return picturesOff;
    }

    private void setPicturesOff(RadioButton picturesOff) {
        getThis().picturesOff = picturesOff;
    }

    private Button getReorderEmojis() {
        return reorderEmojis;
    }

    private void setReorderEmojis(Button reorderEmojis) {
        getThis().reorderEmojis = reorderEmojis;
    }

    private TextView getReorderCategoryEmojiLabel() {
        return reorderCategoryEmojiLabel;
    }

    private void setReorderCategoryEmojiLabel(TextView reorderCategoryEmojiLabel) {
        getThis().reorderCategoryEmojiLabel = reorderCategoryEmojiLabel;
    }

    private EditText getReorderCategoryEmojiBox() {
        return reorderCategoryEmojiBox;
    }

    private void setReorderCategoryEmojiBox(EditText reorderCategoryEmojiBox) {
        getThis().reorderCategoryEmojiBox = reorderCategoryEmojiBox;
    }

    private TextView getReorderItemByCategoryEmojiLabel() {
        return reorderItemByCategoryEmojiLabel;
    }

    private void setReorderItemByCategoryEmojiLabel(TextView reorderItemByCategoryEmojiLabel) {
        getThis().reorderItemByCategoryEmojiLabel = reorderItemByCategoryEmojiLabel;
    }

    private EditText getReorderItemByCategoryEmojiBox() {
        return reorderItemByCategoryEmojiBox;
    }

    private void setReorderItemByCategoryEmojiBox(EditText reorderItemByCategoryEmojiBox) {
        getThis().reorderItemByCategoryEmojiBox = reorderItemByCategoryEmojiBox;
    }

    private TextView getReorderItemByStoreEmojiLabel() {
        return reorderItemByStoreEmojiLabel;
    }

    private void setReorderItemByStoreEmojiLabel(TextView reorderItemByStoreEmojiLabel) {
        getThis().reorderItemByStoreEmojiLabel = reorderItemByStoreEmojiLabel;
    }

    private EditText getReorderItemByStoreEmojiBox() {
        return reorderItemByStoreEmojiBox;
    }

    private void setReorderItemByStoreEmojiBox(EditText reorderItemByStoreEmojiBox) {
        getThis().reorderItemByStoreEmojiBox = reorderItemByStoreEmojiBox;
    }

    private TextView getReorderStoreEmojiLabel() {
        return reorderStoreEmojiLabel;
    }

    private void setReorderStoreEmojiLabel(TextView reorderStoreEmojiLabel) {
        getThis().reorderStoreEmojiLabel = reorderStoreEmojiLabel;
    }

    private EditText getReorderStoreEmojiBox() {
        return reorderStoreEmojiBox;
    }

    private void setReorderStoreEmojiBox(EditText reorderStoreEmojiBox) {
        getThis().reorderStoreEmojiBox = reorderStoreEmojiBox;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setView(inflater.inflate(R.layout.home_screen, container, false));
        setShopping((Shopping) getActivity());

        setMenuOptionsVisible(false);

        setHomeScreenDeveloperOptionsButton(getView().findViewById(R.id.homeScreenDeveloperOptions));
        setHomeScreenEditButton(getView().findViewById(R.id.homeScreenEditButton));
        setHomeScreenTitle(getView().findViewById(R.id.homeScreenTitle));
        setShoppingSettingsBackground(getView().findViewById(R.id.shoppingSettingsBackground)) ;
        setClearAllData(getView().findViewById(R.id.clearAllData));
        setLoadSampleData1(getView().findViewById(R.id.loadSampleData1));
        setLoadSampleData2(getView().findViewById(R.id.loadSampleData2));

        setChangeDefaultSortBy(getView().findViewById(R.id.changeDefaultSortBy));
        setSortAlphabetical(getView().findViewById(R.id.sortAlphabetical));
        setSortByCategory(getView().findViewById(R.id.sortByCategory));
        setSortByStore(getView().findViewById(R.id.sortByStore));

        setChangeReorderingMethod(getView().findViewById(R.id.changeReorderingMethod));
        setDragAndDrop(getView().findViewById(R.id.dragAndDrop));
        setUpAndDownArrows(getView().findViewById(R.id.upAndDownArrows));
        setWithNumbers(getView().findViewById(R.id.withNumbers));

        setChangeColorScheme(getView().findViewById(R.id.changeColorScheme));
        setColorScheme1(getView().findViewById(R.id.colorScheme1));
        setColorScheme2(getView().findViewById(R.id.colorScheme2));
        setColorScheme3(getView().findViewById(R.id.colorScheme3));

        setChangeDefaultCategoryTitles(getView().findViewById(R.id.changeDefaultCategoryTitles));
        setCategoryTitlesExpanded(getView().findViewById(R.id.categoryTitlesExpanded));
        setCategoryTitlesContracted(getView().findViewById(R.id.categoryTitlesContracted));

        setChangeDefaultStoreTitles(getView().findViewById(R.id.changeDefaultStoreTitles));
        setStoreTitlesExpanded(getView().findViewById(R.id.storeTitlesExpanded));
        setStoreTitlesContracted(getView().findViewById(R.id.storeTitlesContracted));

        setOptionalData(getView().findViewById(R.id.optionalData));
        setOptionalDataQuantity(getView().findViewById(R.id.optionalDataQuantity));
        setOptionalDataPrice(getView().findViewById(R.id.optionalDataPrice));
        setOptionalDataLocation(getView().findViewById(R.id.optionalDataLocation));
        setOptionalDataNote(getView().findViewById(R.id.optionalDataNote));

        setSwiping(getView().findViewById(R.id.swiping));
        setSwipingOn(getView().findViewById(R.id.swipingOn));
        setSwipingOff(getView().findViewById(R.id.swipingOff));

        setPictures(getView().findViewById(R.id.pictures));
        setPicturesOn(getView().findViewById(R.id.picturesOn));
        setPicturesOff(getView().findViewById(R.id.picturesOff));

        setReorderEmojis(getView().findViewById(R.id.reorderEmojis));
        setReorderCategoryEmojiLabel(getView().findViewById(R.id.reorderCategoryEmojiLabel));
        setReorderCategoryEmojiBox(getView().findViewById(R.id.reorderCategoryEmojiBox));
        setReorderItemByCategoryEmojiLabel(getView().findViewById(R.id.reorderItemByCategoryEmojiLabel));
        setReorderItemByCategoryEmojiBox(getView().findViewById(R.id.reorderItemByCategoryEmojiBox));
        setReorderItemByStoreEmojiLabel(getView().findViewById(R.id.reorderItemByStoreEmojiLabel));
        setReorderItemByStoreEmojiBox(getView().findViewById(R.id.reorderItemByStoreEmojiBox));
        setReorderStoreEmojiLabel(getView().findViewById(R.id.reorderStoreEmojiLabel));
        setReorderStoreEmojiBox(getView().findViewById(R.id.reorderStoreEmojiBox));

        setInstructions(getView().findViewById(R.id.instructions));

        getHomeScreenDeveloperOptionsButton().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (developerOptionsVisible()) {
                    getClearAllData().setVisibility(View.GONE);
                    getLoadSampleData1().setVisibility(View.GONE);
                    getLoadSampleData2().setVisibility(View.GONE);
                    setDeveloperOptionsVisible(false);
                } else {
                    getClearAllData().setVisibility(View.VISIBLE);
                    getLoadSampleData1().setVisibility(View.VISIBLE);
                    getLoadSampleData2().setVisibility(View.VISIBLE);
                    setDeveloperOptionsVisible(true);
                }
            }
        });

        getHomeScreenEditButton().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (menuOptionsVisible()) {
                    getHomeScreenTitle().setText(getLastMainTitle());

                    getChangeDefaultSortBy().setVisibility(View.GONE);
                    getSortAlphabetical().setVisibility(View.GONE);
                    getSortByCategory().setVisibility(View.GONE);
                    getSortByStore().setVisibility(View.GONE);

                    getChangeReorderingMethod().setVisibility(View.GONE);
                    getDragAndDrop().setVisibility(View.GONE);
                    getUpAndDownArrows().setVisibility(View.GONE);
                    getWithNumbers().setVisibility(View.GONE);

                    getChangeColorScheme().setVisibility(View.GONE);
                    getColorScheme1().setVisibility(View.GONE);
                    getColorScheme2().setVisibility(View.GONE);
                    getColorScheme3().setVisibility(View.GONE);

                    getChangeDefaultCategoryTitles().setVisibility(View.GONE);
                    getCategoryTitlesExpanded().setVisibility(View.GONE);
                    getCategoryTitlesContracted().setVisibility(View.GONE);

                    getChangeDefaultStoreTitles().setVisibility(View.GONE);
                    getStoreTitlesExpanded().setVisibility(View.GONE);
                    getStoreTitlesContracted().setVisibility(View.GONE);

                    getOptionalData().setVisibility(View.GONE);
                    getOptionalDataQuantity().setVisibility(View.GONE);
                    getOptionalDataPrice().setVisibility(View.GONE);
                    getOptionalDataLocation().setVisibility(View.GONE);
                    getOptionalDataNote().setVisibility(View.GONE);

                    getSwiping().setVisibility(View.GONE);
                    getSwipingOn().setVisibility(View.GONE);
                    getSwipingOff().setVisibility(View.GONE);

                    getPictures().setVisibility(View.GONE);
                    getPicturesOn().setVisibility(View.GONE);
                    getPicturesOff().setVisibility(View.GONE);

                    getReorderEmojis().setVisibility(View.GONE);
                    getReorderCategoryEmojiLabel().setVisibility(View.GONE);
                    getReorderCategoryEmojiBox().setVisibility(View.GONE);
                    getReorderItemByCategoryEmojiLabel().setVisibility(View.GONE);
                    getReorderItemByCategoryEmojiBox().setVisibility(View.GONE);
                    getReorderItemByStoreEmojiLabel().setVisibility(View.GONE);
                    getReorderItemByStoreEmojiBox().setVisibility(View.GONE);
                    getReorderStoreEmojiLabel().setVisibility(View.GONE);
                    getReorderStoreEmojiBox().setVisibility(View.GONE);

                    getShoppingSettingsBackground().setVisibility(View.GONE);
                    setMenuOptionsVisible(false);

                } else {

                    setLastMainTitle(getHomeScreenTitle().getText().toString());
                    getHomeScreenTitle().setText(getString(R.string.settings));

                    if (Shopping.SORT_ALPHABETICAL.equals(getShopping().getDefaultSortBy())) {
                        getSortAlphabetical().setChecked(true);
                    } else if (Shopping.SORT_BY_CATEGORY.equals(getShopping().getDefaultSortBy())) {
                        getSortByCategory().setChecked(true);
                    } else if (Shopping.SORT_BY_STORE.equals(getShopping().getDefaultSortBy())) {
                        getSortByStore().setChecked(true);
                    }
                    if (Shopping.DRAG_AND_DROP.equals(getShopping().getReorderingMethod())) {
                        getDragAndDrop().setChecked(true);
                    } else if (Shopping.UP_AND_DOWN_ARROWS.equals(getShopping().getReorderingMethod())) {
                        getUpAndDownArrows().setChecked(true);
                    } else if (Shopping.WITH_NUMBERS.equals(getShopping().getReorderingMethod())) {
                        getWithNumbers().setChecked(true);
                    }

                    if (Shopping.COLOR_SCHEME_1.equals(getShopping().getColorScheme())) {
                        getColorScheme1().setChecked(true);
                    } else if (Shopping.COLOR_SCHEME_2.equals(getShopping().getColorScheme())) {
                        getColorScheme2().setChecked(true);
                    } else if (Shopping.COLOR_SCHEME_3.equals(getShopping().getColorScheme())) {
                        getColorScheme3().setChecked(true);
                    }

                    if (Shopping.CATEGORY_TITLES_EXPANDED.equals(getShopping().getDefaultCategoryTitles())) {
                        getCategoryTitlesExpanded().setChecked(true);
                    } else if (Shopping.CATEGORY_TITLES_CONTRACTED.equals(getShopping().getDefaultCategoryTitles())) {
                        getCategoryTitlesContracted().setChecked(true);
                    }

                    if (Shopping.STORE_TITLES_EXPANDED.equals(getShopping().getDefaultStoreTitles())) {
                        getStoreTitlesExpanded().setChecked(true);
                    } else if (Shopping.STORE_TITLES_CONTRACTED.equals(getShopping().getDefaultStoreTitles())) {
                        getStoreTitlesContracted().setChecked(true);
                    }

                    if (Shopping.SWIPING_ON.equals(getShopping().getSwipingOption())) {
                        getSwipingOn().setChecked(true);
                    } else if (Shopping.SWIPING_OFF.equals(getShopping().getSwipingOption())) {
                        getSwipingOff().setChecked(true);
                    }

                    if (Shopping.PICTURES_ON.equals(getShopping().getPicturesOption())) {
                        getPicturesOn().setChecked(true);
                    } else if (Shopping.PICTURES_OFF.equals(getShopping().getPicturesOption())) {
                        getPicturesOff().setChecked(true);
                    }

                    if (Shopping.OPTIONAL_DATA_ON.equals(getShopping().getOptionalDataQuantity())) {
                        getOptionalDataQuantity().setChecked(true);
                    } else if (Shopping.OPTIONAL_DATA_OFF.equals(getShopping().getOptionalDataQuantity())) {
                        getOptionalDataQuantity().setChecked(false);
                    }

                    if (Shopping.OPTIONAL_DATA_ON.equals(getShopping().getOptionalDataPrice())) {
                        getOptionalDataPrice().setChecked(true);
                    } else if (Shopping.OPTIONAL_DATA_OFF.equals(getShopping().getOptionalDataPrice())) {
                        getOptionalDataPrice().setChecked(false);
                    }

                    if (Shopping.OPTIONAL_DATA_ON.equals(getShopping().getOptionalDataLocation())) {
                        getOptionalDataLocation().setChecked(true);
                    } else if (Shopping.OPTIONAL_DATA_OFF.equals(getShopping().getOptionalDataLocation())) {
                        getOptionalDataLocation().setChecked(false);
                    }

                    if (Shopping.OPTIONAL_DATA_ON.equals(getShopping().getOptionalDataNote())) {
                        getOptionalDataNote().setChecked(true);
                    } else if (Shopping.OPTIONAL_DATA_OFF.equals(getShopping().getOptionalDataNote())) {
                        getOptionalDataNote().setChecked(false);
                    }

                    getReorderCategoryEmojiBox().setText(getShopping().getReorderCategoryEmoji());
                    getReorderItemByCategoryEmojiBox().setText(getShopping().getReorderItemByCategoryEmoji());
                    getReorderItemByStoreEmojiBox().setText(getShopping().getReorderItemByStoreEmoji());
                    getReorderStoreEmojiBox().setText(getShopping().getReorderStoreEmoji());

                    getChangeDefaultSortBy().setVisibility(View.VISIBLE);
                    getSortAlphabetical().setVisibility(View.VISIBLE);
                    getSortByCategory().setVisibility(View.VISIBLE);
                    getSortByStore().setVisibility(View.VISIBLE);

                    getChangeDefaultCategoryTitles().setVisibility(View.VISIBLE);
                    getCategoryTitlesExpanded().setVisibility(View.VISIBLE);
                    getCategoryTitlesContracted().setVisibility(View.VISIBLE);

                    getChangeDefaultStoreTitles().setVisibility(View.VISIBLE);
                    getStoreTitlesExpanded().setVisibility(View.VISIBLE);
                    getStoreTitlesContracted().setVisibility(View.VISIBLE);

                    getChangeReorderingMethod().setVisibility(View.VISIBLE);
                    getDragAndDrop().setVisibility(View.VISIBLE);
                    getUpAndDownArrows().setVisibility(View.VISIBLE);
                    getWithNumbers().setVisibility(View.VISIBLE);

                    getChangeColorScheme().setVisibility(View.VISIBLE);
                    getColorScheme1().setVisibility(View.VISIBLE);
                    getColorScheme2().setVisibility(View.VISIBLE);
                    getColorScheme3().setVisibility(View.VISIBLE);

                    getOptionalData().setVisibility(View.VISIBLE);
                    getOptionalDataQuantity().setVisibility(View.VISIBLE);
                    getOptionalDataPrice().setVisibility(View.VISIBLE);
                    getOptionalDataLocation().setVisibility(View.VISIBLE);
                    getOptionalDataNote().setVisibility(View.VISIBLE);

                    getSwiping().setVisibility(View.VISIBLE);
                    getSwipingOn().setVisibility(View.VISIBLE);
                    getSwipingOff().setVisibility(View.VISIBLE);

                    getPictures().setVisibility(View.VISIBLE);
                    getPicturesOn().setVisibility(View.VISIBLE);
                    getPicturesOff().setVisibility(View.VISIBLE);

                    getReorderEmojis().setVisibility(View.VISIBLE);
                    getReorderCategoryEmojiLabel().setVisibility(View.VISIBLE);
                    getReorderCategoryEmojiBox().setVisibility(View.VISIBLE);
                    getReorderItemByCategoryEmojiLabel().setVisibility(View.VISIBLE);
                    getReorderItemByCategoryEmojiBox().setVisibility(View.VISIBLE);
                    getReorderItemByStoreEmojiLabel().setVisibility(View.VISIBLE);
                    getReorderItemByStoreEmojiBox().setVisibility(View.VISIBLE);
                    getReorderStoreEmojiLabel().setVisibility(View.VISIBLE);
                    getReorderStoreEmojiBox().setVisibility(View.VISIBLE);

                    getShoppingSettingsBackground().setVisibility(View.VISIBLE);
                    setMenuOptionsVisible(true);
                }
            }
        });

        getClearAllData().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getShopping().clearAllData();
                getShopping().initializeData();
            }
        });

        getLoadSampleData1().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getShopping().loadStoresAndCategories();
                getShopping().loadCategoryData1();
                getShopping().loadStoreData1();
                getShopping().initializeData();
            }
        });

        getLoadSampleData2().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getShopping().loadStoresAndCategories();
                getShopping().loadCategoryData2();
                getShopping().loadStoreData2();
                getShopping().initializeData();
            }
        });

        getSortAlphabetical().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getShopping().setDefaultSortBy(Shopping.SORT_ALPHABETICAL);
                getShopping().setInventorySortBy(Shopping.SORT_ALPHABETICAL);
                SharedPreferences sharedPref = getContext().getSharedPreferences(getString(R.string.spPreferencesFile), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.sp_default_sort_by), getString(R.string.spSortAlphabetically));
                editor.apply();
            }
        });

        getSortByCategory().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getShopping().setDefaultSortBy(Shopping.SORT_BY_CATEGORY);
                getShopping().setInventorySortBy(Shopping.SORT_BY_CATEGORY);
                SharedPreferences sharedPref = getContext().getSharedPreferences(getString(R.string.spPreferencesFile), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.sp_default_sort_by), getString(R.string.spSortByCategory));
                editor.apply();
            }
        });

        getSortByStore().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getShopping().setDefaultSortBy(Shopping.SORT_BY_STORE);
                getShopping().setInventorySortBy(Shopping.SORT_BY_STORE);
                SharedPreferences sharedPref = getContext().getSharedPreferences(getString(R.string.spPreferencesFile), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.sp_default_sort_by), getString(R.string.spSortByStore));
                editor.apply();
            }
        });

        getDragAndDrop().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getShopping().setReorderingMethod(Shopping.DRAG_AND_DROP);
                SharedPreferences sharedPref = getContext().getSharedPreferences(getString(R.string.spPreferencesFile), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.sp_reorder_method), getString(R.string.spDragAndDrop));
                editor.apply();
            }
        });

        getUpAndDownArrows().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getShopping().setReorderingMethod(Shopping.UP_AND_DOWN_ARROWS);
                SharedPreferences sharedPref = getContext().getSharedPreferences(getString(R.string.spPreferencesFile), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.sp_reorder_method), getString(R.string.spUpAndDownArrows));
                editor.apply();
            }
        });

        getWithNumbers().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getShopping().setReorderingMethod(Shopping.WITH_NUMBERS);
                SharedPreferences sharedPref = getContext().getSharedPreferences(getString(R.string.spPreferencesFile), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.sp_reorder_method), getString(R.string.spWithNumbers));
                editor.apply();
            }
        });

        getColorScheme1().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getShopping().setColorScheme(Shopping.COLOR_SCHEME_1);
                SharedPreferences sharedPref = getContext().getSharedPreferences(getString(R.string.spPreferencesFile), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.sp_color_scheme), getString(R.string.spColorScheme1));
                editor.apply();
            }
        });

        getColorScheme2().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getShopping().setColorScheme(Shopping.COLOR_SCHEME_2);
                SharedPreferences sharedPref = getContext().getSharedPreferences(getString(R.string.spPreferencesFile), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.sp_color_scheme), getString(R.string.spColorScheme2));
                editor.apply();
            }
        });

        getColorScheme3().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getShopping().setColorScheme(Shopping.COLOR_SCHEME_3);
                SharedPreferences sharedPref = getContext().getSharedPreferences(getString(R.string.spPreferencesFile), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.sp_color_scheme), getString(R.string.spColorScheme3));
                editor.apply();
            }
        });

        getCategoryTitlesExpanded().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getShopping().setDefaultCategoryTitles(Shopping.CATEGORY_TITLES_EXPANDED);
                getShopping().setCategoryTitles(Shopping.CATEGORY_TITLES_EXPANDED);
                SharedPreferences sharedPref = getContext().getSharedPreferences(getString(R.string.spPreferencesFile), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.sp_default_category_titles), getString(R.string.spCategoryTitlesExpanded));
                editor.apply();
            }
        });

        getCategoryTitlesContracted().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getShopping().setDefaultCategoryTitles(Shopping.CATEGORY_TITLES_CONTRACTED);
                getShopping().setCategoryTitles(Shopping.CATEGORY_TITLES_CONTRACTED);
                SharedPreferences sharedPref = getContext().getSharedPreferences(getString(R.string.spPreferencesFile), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.sp_default_category_titles), getString(R.string.spCategoryTitlesContracted));
                editor.apply();
            }
        });

        getStoreTitlesExpanded().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getShopping().setDefaultStoreTitles(Shopping.STORE_TITLES_EXPANDED);
                getShopping().setStoreTitles(Shopping.STORE_TITLES_EXPANDED);
                SharedPreferences sharedPref = getContext().getSharedPreferences(getString(R.string.spPreferencesFile), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.sp_default_store_titles), getString(R.string.spStoreTitlesExpanded));
                editor.apply();
            }
        });

        getStoreTitlesContracted().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getShopping().setDefaultStoreTitles(Shopping.STORE_TITLES_CONTRACTED);
                getShopping().setStoreTitles(Shopping.STORE_TITLES_CONTRACTED);
                SharedPreferences sharedPref = getContext().getSharedPreferences(getString(R.string.spPreferencesFile), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.sp_default_store_titles), getString(R.string.spStoreTitlesContracted));
                editor.apply();
            }
        });

        getSwipingOn().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getShopping().setSwipingOption(Shopping.SWIPING_ON);
                SharedPreferences sharedPref = getContext().getSharedPreferences(getString(R.string.spPreferencesFile), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.sp_swiping_option), getString(R.string.spSwipingOn));
                editor.apply();
            }
        });

        getSwipingOff().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getShopping().setSwipingOption(Shopping.SWIPING_OFF);
                SharedPreferences sharedPref = getContext().getSharedPreferences(getString(R.string.spPreferencesFile), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.sp_swiping_option), getString(R.string.spSwipingOff));
                editor.apply();
            }
        });

        getPicturesOn().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getShopping().setSwipingOption(Shopping.PICTURES_ON);
                SharedPreferences sharedPref = getContext().getSharedPreferences(getString(R.string.spPreferencesFile), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.sp_pictures_option), getString(R.string.spPicturesOn));
                editor.apply();
            }
        });

        getPicturesOff().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getShopping().setSwipingOption(Shopping.PICTURES_OFF);
                SharedPreferences sharedPref = getContext().getSharedPreferences(getString(R.string.spPreferencesFile), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.sp_pictures_option), getString(R.string.spPicturesOff));
                editor.apply();
            }
        });

        getOptionalDataQuantity().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences sharedPref = getContext().getSharedPreferences(getString(R.string.spPreferencesFile), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                if (getShopping().getOptionalDataQuantity().equals(Shopping.OPTIONAL_DATA_ON)) {
                    getShopping().setOptionalDataQuantity(Shopping.OPTIONAL_DATA_OFF);
                    editor.putString(getString(R.string.sp_optional_data_quantity), getString(R.string.spOptionalDataOff));
                } else if (getShopping().getOptionalDataQuantity().equals(Shopping.OPTIONAL_DATA_OFF)) {
                    getShopping().setOptionalDataQuantity(Shopping.OPTIONAL_DATA_ON);
                    editor.putString(getString(R.string.sp_optional_data_quantity), getString(R.string.spOptionalDataOn));
                }
                editor.apply();
            }
        });

        getOptionalDataPrice().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences sharedPref = getContext().getSharedPreferences(getString(R.string.spPreferencesFile), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                if (getShopping().getOptionalDataPrice().equals(Shopping.OPTIONAL_DATA_ON)) {
                    getShopping().setOptionalDataPrice(Shopping.OPTIONAL_DATA_OFF);
                    editor.putString(getString(R.string.sp_optional_data_price), getString(R.string.spOptionalDataOff));
                } else if (getShopping().getOptionalDataPrice().equals(Shopping.OPTIONAL_DATA_OFF)) {
                    getShopping().setOptionalDataPrice(Shopping.OPTIONAL_DATA_ON);
                    editor.putString(getString(R.string.sp_optional_data_price), getString(R.string.spOptionalDataOn));
                }
                editor.apply();
            }
        });

        getOptionalDataLocation().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences sharedPref = getContext().getSharedPreferences(getString(R.string.spPreferencesFile), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                if (getShopping().getOptionalDataLocation().equals(Shopping.OPTIONAL_DATA_ON)) {
                    getShopping().setOptionalDataLocation(Shopping.OPTIONAL_DATA_OFF);
                    editor.putString(getString(R.string.sp_optional_data_location), getString(R.string.spOptionalDataOff));
                } else if (getShopping().getOptionalDataLocation().equals(Shopping.OPTIONAL_DATA_OFF)) {
                    getShopping().setOptionalDataLocation(Shopping.OPTIONAL_DATA_ON);
                    editor.putString(getString(R.string.sp_optional_data_location), getString(R.string.spOptionalDataOn));
                }
                editor.apply();
            }
        });

        getOptionalDataNote().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences sharedPref = getContext().getSharedPreferences(getString(R.string.spPreferencesFile), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                if (getShopping().getOptionalDataNote().equals(Shopping.OPTIONAL_DATA_ON)) {
                    getShopping().setOptionalDataNote(Shopping.OPTIONAL_DATA_OFF);
                    editor.putString(getString(R.string.sp_optional_data_note), getString(R.string.spOptionalDataOff));
                } else if (getShopping().getOptionalDataNote().equals(Shopping.OPTIONAL_DATA_OFF)) {
                    getShopping().setOptionalDataNote(Shopping.OPTIONAL_DATA_ON);
                    editor.putString(getString(R.string.sp_optional_data_note), getString(R.string.spOptionalDataOn));
                }
                editor.apply();
            }
        });

        getReorderCategoryEmojiBox().addTextChangedListener(new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void afterTextChanged(Editable s) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SharedPreferences sharedPref = getContext().getSharedPreferences(getString(R.string.spPreferencesFile), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.sp_reorder_category_emoji), reorderCategoryEmojiBox.getText().toString());
                editor.apply();
            }

        });

        getReorderItemByCategoryEmojiBox().addTextChangedListener(new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void afterTextChanged(Editable s) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SharedPreferences sharedPref = getContext().getSharedPreferences(getString(R.string.spPreferencesFile), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.sp_reorder_item_by_category_emoji), reorderItemByCategoryEmojiBox.getText().toString());
                editor.apply();
            }

        });

        getReorderItemByStoreEmojiBox().addTextChangedListener(new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void afterTextChanged(Editable s) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SharedPreferences sharedPref = getContext().getSharedPreferences(getString(R.string.spPreferencesFile), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.sp_reorder_item_by_store_emoji), reorderItemByStoreEmojiBox.getText().toString());
                editor.apply();
            }

        });

        getReorderStoreEmojiBox().addTextChangedListener(new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void afterTextChanged(Editable s) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SharedPreferences sharedPref = getContext().getSharedPreferences(getString(R.string.spPreferencesFile), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.sp_reorder_store_emoji), reorderStoreEmojiBox.getText().toString());
                editor.apply();
            }

        });

        getInstructions().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (instructionsVisible()) {
                    getHomeScreenTitle().setText(getLastMainTitle());
                    setInstructionsVisible(false);
                } else {
                    setLastMainTitle(getHomeScreenTitle().getText().toString());
                    getHomeScreenTitle().setText(getString(R.string.instructions));
                    setInstructionsVisible(true);
                }
            }
        });

        return getView();
    }
}