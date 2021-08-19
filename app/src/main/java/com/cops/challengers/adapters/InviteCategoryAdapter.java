package com.cops.challengers.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cops.challengers.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class InviteCategoryAdapter extends RecyclerView.Adapter<InviteCategoryAdapter.ViewHolder> {



    private OnInviteClicked onInviteClicked;
    private List<String> categories=new ArrayList<>();

    public InviteCategoryAdapter(OnInviteClicked onInviteClicked) {

        this.onInviteClicked = onInviteClicked;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_invite_category, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        switch (position){

            case 0:
                holder.itemInviteChk.setText(holder.itemView.getContext().getString(R.string.islam));

                categories.add("1");
                categories.add("2");
                categories.add("3");
                categories.add("4");
                categories.add("5");
                categories.add("6");
                categories.add("7");
                categories.add("8");
                onInviteClicked.setCategories(categories);
                break;
            case 1:
                holder.itemInviteChk.setText(holder.itemView.getContext().getString(R.string.geographic));
                break;
            case 2:
                holder.itemInviteChk.setText(holder.itemView.getContext().getString(R.string.historical));
                break;
            case 3:
                holder.itemInviteChk.setText(holder.itemView.getContext().getString(R.string.puzzle));
                break;
            case 4:
                holder.itemInviteChk.setText(holder.itemView.getContext().getString(R.string.science));
                break;
            case 5:
                holder.itemInviteChk.setText(holder.itemView.getContext().getString(R.string.sport));
                break;
            case 6:
                holder.itemInviteChk.setText(holder.itemView.getContext().getString(R.string.technology));
                break;
            case 7:
                holder.itemInviteChk.setText(holder.itemView.getContext().getString(R.string.math));
                break;
        }
    }


    @Override
    public int getItemCount() {
        return 8;
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        @BindView(R.id.item_invite_chk)
        CheckBox itemInviteChk;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);


            itemInviteChk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                    if (isChecked) {

                        categories.add(getAdapterPosition()+1+"");

                    }else {

                        categories.remove(getAdapterPosition()+1+"");

                    }
                    onInviteClicked.setCategories(categories);
                }
            });
        }

    }

    public interface OnInviteClicked {

        void setCategories(List<String> categories);
    }


}
