package me.rubl.loftmoney.screens.main.adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import me.rubl.loftmoney.R;
import me.rubl.loftmoney.screens.main.interfaces.BudgetItemsAdapterListener;
import me.rubl.loftmoney.screens.main.model.BudgetItemModel;

public class BudgetItemsAdapter extends RecyclerView.Adapter<BudgetItemsAdapter.ItemsViewHolder> {

    private List<BudgetItemModel> mItemsList = new ArrayList<>();
    private Context mContext;
    private BudgetItemsAdapterListener mListener;
    private SparseBooleanArray mSelectedItems = new SparseBooleanArray();

    public void setNewData(List<BudgetItemModel> newData) {
        mItemsList.clear();
        mItemsList.addAll(newData);
        notifyDataSetChanged();
    }

    public void setListener(BudgetItemsAdapterListener listener) {
        this.mListener = listener;
    }

    public int getSelectedCount() {
        int result = 0;

        for (int i = 0; i < mItemsList.size(); i++) {
            if (mSelectedItems.get(i)) result++;
        }

        return result;
    }

    public List<Integer> getSelectedItemIds() {
        List<Integer> ids = new ArrayList<>();

        for (int i = 0; i < mItemsList.size(); i++) {
            if (mSelectedItems.get(i)) {
                ids.add(mItemsList.get(i).getId());
            }
        }

        return ids;
    }

    public void clearSelections() {
        mSelectedItems.clear();
        notifyDataSetChanged();
    }

    public void toggleItem(final int position) {
        mSelectedItems.put(position, !mSelectedItems.get(position));
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (mContext == null) mContext = parent.getContext();

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_budget, parent, false);

        return new ItemsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsViewHolder holder, int position) {
        holder.bind(mContext, mItemsList.get(position), mSelectedItems.get(position));
        holder.setListener(mListener, mItemsList.get(position), position );
    }

    @Override
    public int getItemCount() {
        return mItemsList.size();
    }

    static class ItemsViewHolder extends RecyclerView.ViewHolder {

        View mItemView;
        TextView mNameTV;
        TextView mValueTV;

        ItemsViewHolder(@NonNull View itemView) {
            super(itemView);

            mItemView = itemView;
            mNameTV = itemView.findViewById(R.id.item_budget_tv_name);
            mValueTV = itemView.findViewById(R.id.item_budget_tv_price);
        }

        void bind(Context context, BudgetItemModel itemModel, final Boolean isSelected) {
            mItemView.setSelected(isSelected);
            mNameTV.setText(itemModel.getName());
            mValueTV.setText(context.getResources().getString(R.string.budget_item_value, itemModel.getStringValue()));

            switch (itemModel.getType()) {

                case EXPENSE:
                    mValueTV.setTextColor(ContextCompat.getColor(context, R.color.light_expenses_price_text_color));
                    break;

                case INCOME:
                    mValueTV.setTextColor(ContextCompat.getColor(context, R.color.light_incomes_value_text_color));
                    break;

                default:break;
            }
        }

        void setListener(BudgetItemsAdapterListener listener, final BudgetItemModel itemModel, final int position) {

            mItemView.setOnClickListener(v -> listener.onItemClick(itemModel, position));

            mItemView.setOnLongClickListener(v -> {

                listener.onItemLongClick(itemModel, position);

                return false;
            });
        }
    }

}
