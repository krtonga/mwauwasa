package com.github.codetanzania.feature.company.tariffs;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import com.github.codetanzania.core.model.Tariff;

import java.util.List;

import tz.co.codetanzania.R;

public class TariffsRecyclerViewAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "WaterTariffs";
    private static final int VIEW_TYPE_TARIFF_CONTENT = 0;

    private final List<Tariff> mTariffs;

    TariffsRecyclerViewAdapter(@NonNull List<Tariff> tariffs) {
        mTariffs = tariffs;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.card_view_tariff, parent, false);
        return new TariffDetailsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Tariff tariff = mTariffs.get(position);
        ((TariffDetailsViewHolder) holder).bindData(tariff);
    }

    @Override
    public int getItemCount() {
        return mTariffs.size();
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_TARIFF_CONTENT;
    }

    static class TariffDetailsViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvTariffHeader;
        private final TableLayout tvTariffLevels;

        TariffDetailsViewHolder(View itemView) {
            super(itemView);

            tvTariffHeader = (TextView) itemView.findViewById(R.id.tv_tariffs_header);
            tvTariffLevels = (TableLayout) itemView.findViewById(R.id.tl_tariff_levels);
        }

        void bindData(Tariff tariff) {
            tvTariffHeader.setText(tariff.getServiceType());

            LayoutInflater inflater = LayoutInflater.from(itemView.getContext());
            for (Tariff.Level level : tariff.getLevels()) {
                View tariffRow = inflater.inflate(R.layout.item_tariff, tvTariffLevels, false);
                bindRow(tariffRow, level);
                tvTariffLevels.addView(tariffRow);
            }
        }

        void bindRow(View row, Tariff.Level level) {
            TextView tvCustomerType = (TextView) row.findViewById(R.id.tv_tariffs_customer_type);
            TextView tvQuantity = (TextView) row.findViewById(R.id.tv_tariffs_quantity);
            TextView tvCost = (TextView) row.findViewById(R.id.tv_tariffs_cost);

            tvCustomerType.setText(level.getCustomerType());
            tvQuantity.setText(level.getQuantity());
            tvCost.setText(level.getCost());
        }
    }
}
