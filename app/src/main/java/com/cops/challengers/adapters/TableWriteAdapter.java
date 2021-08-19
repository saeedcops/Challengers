package com.cops.challengers.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cops.challengers.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TableWriteAdapter extends RecyclerView.Adapter<TableWriteAdapter.ViewHolder> {


    private String text;
    private HomeAdapterClick homeAdapterClick;

    public TableWriteAdapter(String text,HomeAdapterClick homeAdapterClick) {

        this.text = text;
        this.homeAdapterClick=homeAdapterClick;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_table_choose, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        holder.tableWriteAdapterRl.setVisibility(View.VISIBLE);

       holder.tableWriteAdapterTv.setText(String.valueOf(text.charAt(position)));

    }

    @Override
    public int getItemCount() {
        return text.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        @BindView(R.id.table_write_adapter_tv)
        TextView tableWriteAdapterTv;
        @BindView(R.id.table_write_adapter_rl)
        RelativeLayout tableWriteAdapterRl;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            tableWriteAdapterRl.setVisibility(View.GONE);
            homeAdapterClick.itemLetter(String.valueOf(text.charAt(getAdapterPosition())));
        }
    }


    public interface HomeAdapterClick {
        void itemLetter( String title);
    }

}
