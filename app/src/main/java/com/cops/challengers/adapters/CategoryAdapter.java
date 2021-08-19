package com.cops.challengers.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cops.challengers.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;



public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {


    private int[] image = new int[]{R.drawable.islam, R.drawable.geographic, R.drawable.pharaoh,
            R.drawable.puzzle, R.drawable.science, R.drawable.sport,
            R.drawable.technology, R.drawable.math};
    private String[] text;
    private CategoryViewEdit categoryViewEdit;
    private List<String> categories;


    public CategoryAdapter(CategoryViewEdit categoryViewEdit, List<String> categories) {

        this.categoryViewEdit = categoryViewEdit;
        this.categories = categories;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);

        text = new String[]{parent.getContext().getString(R.string.islam), parent.getContext().getString(R.string.geographic),
                parent.getContext().getString(R.string.historical), parent.getContext().getString(R.string.puzzle),
                parent.getContext().getString(R.string.science), parent.getContext().getString(R.string.sport),
                parent.getContext().getString(R.string.technology), parent.getContext().getString(R.string.math)};



        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.categoryAdapterIv.setImageResource(image[position]);
        holder.categoryAdapterTvName.setText(text[position]);


    }


    @Override
    public int getItemCount() {
        return image.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.category_adapter_tv_name)
        TextView categoryAdapterTvName;
        @BindView(R.id.category_adapter_cb_select)
        CheckBox categoryAdapterCbSelect;
        @BindView(R.id.category_adapter_iv)
        ImageView categoryAdapterIv;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);


            categoryAdapterCbSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked) {

                        categories.add(getAdapterPosition() + 1 + "");

                    } else {

                        categories.remove(getAdapterPosition() + 1 + "");
                    }
                    categoryViewEdit.onSelectView(categories);
                }
            });


        }

    }

    public interface CategoryViewEdit {

        void onSelectView(List<String> categories);
    }


}
