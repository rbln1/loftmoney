package me.rubl.loftmoney.screens.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import me.rubl.loftmoney.R;
import me.rubl.loftmoney.screens.model.ItemModel;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemsViewHolder> {

    private List<ItemModel> mDataList = new ArrayList<>();

    public void setNewData(List<ItemModel> newData) {
        mDataList.clear();
        mDataList.addAll(newData);
        notifyDataSetChanged();
    }

    public void addDataToTop(ItemModel model) {
        mDataList.add(0, model);
        notifyItemInserted(0);
    }

    @NonNull
    @Override
    public ItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);

        return new ItemsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsViewHolder holder, int position) {
        holder.bind(mDataList.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    static class ItemsViewHolder extends RecyclerView.ViewHolder {

        TextView mNameTV;
        TextView mValueTV;

        public ItemsViewHolder(@NonNull View itemView) {
            super(itemView);

            mNameTV = itemView.findViewById(R.id.txtItemName);
            mValueTV = itemView.findViewById(R.id.txtItemValue);
        }

        void bind(ItemModel itemModel) {
            mNameTV.setText(itemModel.getName());
            mValueTV.setText(itemModel.getValue().concat(" ₽"));

            // TODO: get value text color from resources

            switch (itemModel.getType()) {

                case EXPENSE:
                    mValueTV.setTextColor(Color.parseColor("#4a90e2"));
                    break;

                case INCOME:
                    mValueTV.setTextColor(Color.parseColor("#7ed321"));
                    break;

                default:break;
            }
        }
    }

}
