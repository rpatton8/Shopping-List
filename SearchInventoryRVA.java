package ryan.android.shopping;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SearchInventoryRVA extends RecyclerView.Adapter  {



    public int getItemViewType(final int position) {

        return 1;

    }

    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new RecyclerView.ViewHolder(view) {};

    }

    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }


    public int getItemCount() {

        return  1;
        
    }

}