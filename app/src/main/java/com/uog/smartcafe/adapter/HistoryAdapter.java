package com.uog.smartcafe.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uog.smartcafe.OrderListActivity;
import com.uog.smartcafe.R;
import com.uog.smartcafe.entities.OrderObject;
import com.uog.smartcafe.util.Helper;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryViewHolder>{

    private static final String TAG = HistoryAdapter.class.getSimpleName();

    private Context context;
    private List<OrderObject> historyObjectList;

    public HistoryAdapter(Context context, List<OrderObject> historyObjectList) {
        this.context = context;
        this.historyObjectList = historyObjectList;
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_history_layout, parent, false);
        return new HistoryViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position) {
        OrderObject historyObject = historyObjectList.get(position);
        final int mOrderId = historyObject.getId();

        holder.deliveryDate.setText(Helper.dateFormatting(historyObject.getOrderDate()));
        holder.deliveryStatus.setText(historyObject.getOrderStatus());
        holder.orderName.setText("Order num: #" + String.valueOf(historyObject.getId()));
        holder.orderPrice.setText("$" + String.valueOf(historyObject.getOrderPrice()) + "0");
        holder.orderTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent historyIntent = new Intent(context, OrderListActivity.class);
                historyIntent.putExtra("ORDER_ID", mOrderId);
                context.startActivity(historyIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return historyObjectList.size();
    }


}
