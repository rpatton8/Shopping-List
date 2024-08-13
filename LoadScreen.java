package ryan.android.shopping;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LoadScreen extends Fragment {

    private View view;
    private Shopping shopping;

    private String currentBottomMenu;
    private static final String MENU_ITEM = "item";
    private static final String MENU_CATEGORY = "category";
    private static final String MENU_STORE = "store";
    private static final String MENU_NONE = "none";

    private boolean menuOptionsVisible;
    private TextView loadScreenEditButton;
    private Button clearAllData;
    private Button loadSampleData;

    private Button addRemoveCategory;
    private Button addEditItem;
    private Button addRemoveStore;
    private TextView editDataBreak;
    private Button addPopup;
    private Button editPopup;
    private Button removePopup;
    private Button reorderPopup;
    private Boolean popupButtonsVisible;

    public LoadScreen() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.load_screen, container, false);

        shopping = (Shopping) getActivity();

        currentBottomMenu = MENU_NONE;

        popupButtonsVisible = false;

        addRemoveCategory = view.findViewById(R.id.addRemoveCategory);
        addEditItem = view.findViewById(R.id.addEditItem);
        addRemoveStore = view.findViewById(R.id.addRemoveStore);
        editDataBreak = view.findViewById(R.id.editDataBreak);
        addPopup = view.findViewById(R.id.addPopup);
        editPopup = view.findViewById(R.id.editPopup);
        removePopup = view.findViewById(R.id.removePopup);
        reorderPopup = view.findViewById(R.id.reorderPopup);

        menuOptionsVisible = false;

        loadScreenEditButton = view.findViewById(R.id.loadScreenEditButton);
        clearAllData = view.findViewById(R.id.clearAllData);
        loadSampleData = view.findViewById(R.id.loadSampleData);

        loadScreenEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (menuOptionsVisible) {
                    clearAllData.setVisibility(View.GONE);
                    loadSampleData.setVisibility(View.GONE);
                    menuOptionsVisible = false;
                } else {
                    clearAllData.setVisibility(View.VISIBLE);
                    loadSampleData.setVisibility(View.VISIBLE);
                    menuOptionsVisible = true;
                }
            }
        });

        clearAllData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopping.clearAllData();
            }
        });

        loadSampleData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopping.loadSampleData();
            }
        });

        addRemoveCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentBottomMenu == MENU_NONE) {
                    editDataBreak.setText("Category");
                    editDataBreak.setVisibility(View.VISIBLE);
                    addPopup.setVisibility(View.VISIBLE);
                    editPopup.setVisibility(View.VISIBLE);
                    removePopup.setVisibility(View.VISIBLE);
                    reorderPopup.setVisibility(View.VISIBLE);
                    currentBottomMenu = MENU_CATEGORY;

                } else if (currentBottomMenu == MENU_CATEGORY) {
                    editDataBreak.setVisibility(View.GONE);
                    addPopup.setVisibility(View.GONE);
                    editPopup.setVisibility(View.GONE);
                    removePopup.setVisibility(View.GONE);
                    reorderPopup.setVisibility(View.GONE);
                    currentBottomMenu = MENU_NONE;
                } else {
                    editDataBreak.setText("Category");
                    currentBottomMenu = MENU_CATEGORY;
                }
            }
        });

        addEditItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentBottomMenu == MENU_NONE) {
                    editDataBreak.setText("Item");
                    editDataBreak.setVisibility(View.VISIBLE);
                    addPopup.setVisibility(View.VISIBLE);
                    editPopup.setVisibility(View.VISIBLE);
                    removePopup.setVisibility(View.VISIBLE);
                    reorderPopup.setVisibility(View.VISIBLE);
                    currentBottomMenu = MENU_ITEM;

                } else if (currentBottomMenu == MENU_ITEM) {
                    editDataBreak.setVisibility(View.GONE);
                    addPopup.setVisibility(View.GONE);
                    editPopup.setVisibility(View.GONE);
                    removePopup.setVisibility(View.GONE);
                    reorderPopup.setVisibility(View.GONE);
                    currentBottomMenu = MENU_NONE;
                } else {
                    editDataBreak.setText("Item");
                    currentBottomMenu = MENU_ITEM;
                }
            }
        });

        addRemoveStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentBottomMenu == MENU_NONE) {
                    editDataBreak.setText("Store");
                    editDataBreak.setVisibility(View.VISIBLE);
                    addPopup.setVisibility(View.VISIBLE);
                    editPopup.setVisibility(View.VISIBLE);
                    removePopup.setVisibility(View.VISIBLE);
                    reorderPopup.setVisibility(View.VISIBLE);
                    currentBottomMenu = MENU_STORE;

                } else if (currentBottomMenu == MENU_STORE) {
                    editDataBreak.setVisibility(View.GONE);
                    addPopup.setVisibility(View.GONE);
                    editPopup.setVisibility(View.GONE);
                    removePopup.setVisibility(View.GONE);
                    reorderPopup.setVisibility(View.GONE);
                    currentBottomMenu = MENU_NONE;
                } else {
                    editDataBreak.setText("Store");
                    currentBottomMenu = MENU_STORE;
                }
            }
        });


        addPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentBottomMenu == MENU_CATEGORY) {
                    shopping.loadFragment(new AddCategory());
                } else if (currentBottomMenu == MENU_ITEM) {
                    shopping.loadFragment(new AddItem());
                } else if (currentBottomMenu == MENU_STORE) {
                    shopping.loadFragment(new AddStore());
                }
            }
        });

        editPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentBottomMenu == MENU_CATEGORY) {
                    shopping.loadFragment(new EditCategory());
                } else if (currentBottomMenu == MENU_ITEM) {
                    if (shopping.itemIsSelected) {
                        shopping.loadFragment(new EditItem());
                    } else {
                        Toast.makeText(getActivity(), "Please select an item to edit.", Toast.LENGTH_SHORT).show();
                    }
                } else if (currentBottomMenu == MENU_STORE) {
                    shopping.loadFragment(new EditStore());
                }
            }
        });

        removePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentBottomMenu == MENU_CATEGORY) {
                    shopping.loadFragment(new RemoveCategory());
                } else if (currentBottomMenu == MENU_ITEM) {
                    if (shopping.itemIsSelected) {
                        shopping.loadFragment(new RemoveItem());
                    } else {
                        Toast.makeText(getActivity(), "Please select an item to remove.", Toast.LENGTH_SHORT).show();
                    }
                } else if (currentBottomMenu == MENU_STORE) {
                    shopping.loadFragment(new RemoveStore());
                }
            }
        });

        reorderPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentBottomMenu == MENU_CATEGORY) {
                    shopping.loadFragment(new ReorderCategories());
                } else if (currentBottomMenu == MENU_ITEM) {
                    shopping.loadFragment(new ReorderItems());
                } else if (currentBottomMenu == MENU_STORE) {
                    shopping.loadFragment(new ReorderStores());
                }
            }
        });

        return view;
    }

}

