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

    private boolean menuOptionsVisible;
    private TextView loadScreenSearchButton;
    private TextView loadScreenEditButton;
    private Button clearAllData;
    private Button loadSampleData;
    private Button instructions;

    public LoadScreen() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.load_screen, container, false);

        shopping = (Shopping) getActivity();

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

        return view;
    }

}

