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

import static com.cops.challengers.utils.Util.format;


public class LeagueAdapter extends RecyclerView.Adapter<LeagueAdapter.ViewHolder> {


private OnLeagueClick onLeagueClick;
    private List<Profile> leaguePlayers;
    private int userId;

    public LeagueAdapter(int userId,List<Profile> leaguePlayers,OnLeagueClick onLeagueClick) {
        this.leaguePlayers = leaguePlayers;
        this.onLeagueClick=onLeagueClick;
        this.userId=userId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_league, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.leagueAdapterTvRank.setText(position+1+"");
        Glide.with(holder.itemView.getContext()).load(leaguePlayers.get(position).getImage()).into(holder.leagueAdapterCivImage);
        holder.leagueAdapterTvName.setText(leaguePlayers.get(position).getName());
        holder.leagueAdapterCcp.setCountryForNameCode(leaguePlayers.get(position).getFlag());
        holder.leagueAdapterTvStatus.setText("Score: "+leaguePlayers.get(position).getScore());

        holder.leagueAdapterTvCoins.setText(format(leaguePlayers.get(position).getLeagueCoins()));

        if (userId==leaguePlayers.get(position).getId()) {

            holder.itemView.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.colorLightGreen));
        }

    }


    @Override
    public int getItemCount() {

            return leaguePlayers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.league_adapter_tv_rank)
        TextView leagueAdapterTvRank;
        @BindView(R.id.league_adapter_civ_image)
        CircleImageView leagueAdapterCivImage;
        @BindView(R.id.league_adapter_tv_name)
        TextView leagueAdapterTvName;
        @BindView(R.id.league_adapter_ccp)
        CountryCodePicker leagueAdapterCcp;
        @BindView(R.id.league_adapter_tv_status)
        TextView leagueAdapterTvStatus;
        @BindView(R.id.league_adapter_tv_coins)
        TextView leagueAdapterTvCoins;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            onLeagueClick.leagueClicked(leaguePlayers.get(getAdapterPosition()).getId());

        }
    }

    public interface OnLeagueClick{

        void leagueClicked(int userId);
    }
}
