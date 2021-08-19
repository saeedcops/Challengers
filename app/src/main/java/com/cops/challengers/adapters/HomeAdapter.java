package com.cops.challengers.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cops.challengers.R;

import butterknife.BindView;
import butterknife.ButterKnife;



public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private int[] image = new int[]{R.drawable.one, R.drawable.friends, R.drawable.ofline};
    private String[] text ;
    private HomeAdapterClick homeAdapterClick;

    public HomeAdapter(HomeAdapterClick homeAdapterClick) {

        this.homeAdapterClick=homeAdapterClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home, parent, false);

        text = new String[]{parent.getContext().getString(R.string.onetoone),parent.getContext().getString(R.string.playfriend), parent.getContext().getString(R.string.offline)};
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.homeAdapterIv.setImageResource(image[position]);
        holder.homeAdapterTv.setText(text[position]);


    }

    @Override
    public int getItemCount() {
        return image.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        @BindView(R.id.home_adapter_iv)
        ImageView homeAdapterIv;
        @BindView(R.id.home_adapter_tv)
        TextView homeAdapterTv;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            homeAdapterClick.itemClicked(getAdapterPosition(),text[getAdapterPosition()]);
        }
    }


    public interface HomeAdapterClick{
        void itemClicked(int position,String title);
    }

}
