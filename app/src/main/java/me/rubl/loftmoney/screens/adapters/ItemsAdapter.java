package me.rubl.loftmoney.screens.adapters;

import android.content.Context;
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
    private Context mContext;

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

        if (mContext == null) mContext = parent.getContext();

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);

        return new ItemsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsViewHolder holder, int position) {
        holder.bind(mDataList.get(position), mContext);
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

        void bind(ItemModel itemModel, Context context) {
            mNameTV.setText(itemModel.getName());
            mValueTV.setText(itemModel.getValue().concat(" ₽"));

            switch (itemModel.getType()) {

                case EXPENSE:
                    mValueTV.setTextColor(context.getResources().getColor(R.color.light_expenses_value_text_color));
                    break;

                case INCOME:
                    mValueTV.setTextColor(context.getResources().getColor(R.color.light_incomes_value_text_color));
                    break;

                default:break;
            }
        }
    }

}
