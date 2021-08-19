package com.cops.challengers.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cops.challengers.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;


public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {


    private List<String> image;
    private String mImage;
    private ImageAdapterClick homeAdapterClick;

    public ImageAdapter(String mImage,List<String> image, ImageAdapterClick homeAdapterClick) {

        this.mImage=mImage;
        this.image = image;
        this.homeAdapterClick = homeAdapterClick;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profile_image, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        Glide.with(holder.itemView.getContext()).load(image.get(position)).into(holder.imageAdapterCivImage);
        if (mImage.equals(image.get(position)))
            holder.imageAdapterCivCheck.setVisibility(View.VISIBLE);

    }

    public void setSelectedImage(String image){
        this.mImage=image;
    }
    @Override
    public int getItemCount() {
        return image.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        @BindView(R.id.image_adapter_civ_image)
        CircleImageView imageAdapterCivImage;
        @BindView(R.id.image_adapter_civ_check)
        CircleImageView imageAdapterCivCheck;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            homeAdapterClick.itemLetter(String.valueOf(image.get(getAdapterPosition())));
        }
    }


    public interface ImageAdapterClick {
        void itemLetter(String image);
    }

}
