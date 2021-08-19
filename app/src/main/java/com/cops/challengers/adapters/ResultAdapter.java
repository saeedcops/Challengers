package com.cops.challengers.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cops.challengers.R;
import com.cops.challengers.localData.Answers;
import com.cops.challengers.model.room.PlayersAnswer;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ViewHolder> {



    private List<PlayersAnswer> playersAnswers;
    private List<Answers> answers;

    private String lang="";
    private int playerNum;
    private int playerId;
    public ResultAdapter(List<PlayersAnswer> playersAnswers,String lang,int playerNum, int playerId) {
        this.playersAnswers=playersAnswers;
        this.lang=lang;
        this.playerNum=playerNum;
        this.playerId=playerId;
    }

    public ResultAdapter(List<Answers> answers, String lang,int playerNum) {
        this.answers = answers;
        this.playerNum = playerNum;
        this.lang=lang;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_result, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (playerNum ==0) {

            holder.resultAdapterTvP1Answer.setText("" + answers.get(position).getAnswer());

            holder.resultAdapterTvP1Score.setVisibility(View.GONE);
            holder.resultAdapterTvP2Answer.setVisibility(View.GONE);
            holder.resultAdapterTvP2Score.setVisibility(View.GONE);

            holder.resultAdapterTvAnswer.setVisibility(View.GONE);
            holder.resultAdapterTvAnswerOffline.setVisibility(View.VISIBLE);

            String[] parts = answers.get(position).getCorrect().split("[|]");
            String[] qs = answers.get(position).getQuestion().split("[|]");
            if (lang.equals("en")) {
                holder.resultAdapterTvAnswerOffline.setText(parts[0]);
                holder.resultAdapterTvQuestion.setText(qs[0]);
            } else {
                holder.resultAdapterTvAnswerOffline.setText(parts[1]);
                holder.resultAdapterTvQuestion.setText(qs[1]);
            }

        }else{
            holder.resultAdapterTvP1Answer.setText("" + playersAnswers.get(position).getPlayer1Answer());
            holder.resultAdapterTvP1Score.setText("" + playersAnswers.get(position).getPlayer1Score() + "");
            holder.resultAdapterTvP2Answer.setText("" + playersAnswers.get(position).getPlayer2Answer());
            holder.resultAdapterTvP2Score.setText("" + playersAnswers.get(position).getPlayer2Score() + "");

            String[] parts = playersAnswers.get(position).getCorrect().split("[|]");
            String[] qs = playersAnswers.get(position).getQuestion().split("[|]");
            if (lang.equals("en")) {
                holder.resultAdapterTvAnswer.setText(parts[0]);
                holder.resultAdapterTvQuestion.setText(qs[0]);
            } else {
                holder.resultAdapterTvAnswer.setText(parts[1]);
                holder.resultAdapterTvQuestion.setText(qs[1]);
            }
        }

    }


    @Override
    public int getItemCount() {
        if (playerNum ==0) {
            return answers.size();

        }else
             return playersAnswers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

//        @BindView(R.id.result_adapter_tv_num)
//        TextView resultAdapterTvNum;
        @BindView(R.id.result_adapter_tv_question)
        TextView resultAdapterTvQuestion;
        @BindView(R.id.result_adapter_tv_answer)
        TextView resultAdapterTvAnswer;
        @BindView(R.id.result_adapter_tv_answer_offline)
        TextView resultAdapterTvAnswerOffline;
        @BindView(R.id.result_adapter_tv_p1_answer)
        TextView resultAdapterTvP1Answer;
        @BindView(R.id.result_adapter_tv_p2_answer)
        TextView resultAdapterTvP2Answer;
        @BindView(R.id.result_adapter_tv_p1_score)
        TextView resultAdapterTvP1Score;
        @BindView(R.id.result_adapter_tv_p2_score)
        TextView resultAdapterTvP2Score;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);


        }

    }


}
