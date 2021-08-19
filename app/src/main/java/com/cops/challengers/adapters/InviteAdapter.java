package com.cops.challengers.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cops.challengers.R;
import com.cops.challengers.model.room.Profile;
import com.hbb20.CountryCodePicker;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;


public class InviteAdapter extends RecyclerView.Adapter<InviteAdapter.ViewHolder> {



    private OnInviteClicked onInviteClicked;
    private List<Profile> users;


    public InviteAdapter(List<Profile> users, OnInviteClicked onInviteClicked) {

        this.users = users;
        this.onInviteClicked = onInviteClicked;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_invite, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(holder.itemView.getContext()).load(users.get(position).getImage()).into(holder.inviteAdapterCivImage);
        holder.inviteAdapterCcp.setCountryForNameCode(users.get(position).getFlag());
        holder.inviteAdapterTvLeague.setText(holder.itemView.getContext().getString(R.string.score)+" : "+users.get(position).getScore());
        holder.inviteAdapterTvName.setText(users.get(position).getName());

        if (users.get(position).getActive()) {
            holder.inviteAdapterIvStatus.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.circle_green));
        }

    }


    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.invite_adapter_civ_image)
        CircleImageView inviteAdapterCivImage;
        @BindView(R.id.invite_adapter_tv_name)
        TextView inviteAdapterTvName;
        @BindView(R.id.invite_adapter_ccp)
        CountryCodePicker inviteAdapterCcp;
        @BindView(R.id.invite_adapter_tv_league)
        TextView inviteAdapterTvLeague;
        @BindView(R.id.invite_adapter_iv_status)
        CircleImageView inviteAdapterIvStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onInviteClicked.clicked(users.get(getAdapterPosition()).getUser());
        }
    }

    public interface OnInviteClicked {

        void clicked(int id);
    }


}
