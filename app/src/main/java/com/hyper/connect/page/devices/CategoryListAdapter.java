package com.hyper.connect.page.devices;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hyper.connect.R;
import com.hyper.connect.app.GlobalApplication;
import com.hyper.connect.app.LocalRepository;
import com.hyper.connect.database.entity.Category;
import com.hyper.connect.elastos.ElastosCarrier;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.CategoryViewHolder>{
    private final LayoutInflater inflater;
    private final Context appContext;
    private List<Category> categoryList;
    private static ElastosCarrier elastosCarrier;
    private static LocalRepository localRepository;
    private SensorsActivity sensorsActivity;
    private int attributeId;
    private CategoriesViewModel categoriesViewModel;
    private Map<Integer, Boolean> selectedMap;

    public CategoryListAdapter(Context context, SensorsActivity sensorsActivity, int attributeId){
        inflater=LayoutInflater.from(context);
        appContext=context;
        elastosCarrier=GlobalApplication.getElastosCarrier();
        localRepository=GlobalApplication.getLocalRepository();
        this.sensorsActivity=sensorsActivity;
        this.attributeId=attributeId;
        categoriesViewModel=ViewModelProviders.of(sensorsActivity).get(CategoriesViewModel.class);
        selectedMap=new HashMap<Integer, Boolean>();
    }

    public void setCategoryList(List<Category> newCategoryList){
        categoryList=newCategoryList;
        notifyDataSetChanged();
    }

    public Map<Integer, Boolean> getSelectedMap(){
        return selectedMap;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i){
        View itemView=inflater.inflate(R.layout.item_recyclerview_categories, viewGroup, false);
        return new CategoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder categoryViewHolder, int i){
        if(categoryList!=null){
            Category category=categoryList.get(i);
            categoryViewHolder.bind(category);
        }
    }

    @Override
    public int getItemCount(){
        int count=0;
        if(categoryList!=null){
            count=categoryList.size();
        }
        return count;
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder{
        private ConstraintLayout categoryContainer;
        private TextView nameText;
        private AppCompatImageView selectImage;
        private boolean isSelected;

        CategoryViewHolder(View itemView){
            super(itemView);
            categoryContainer=itemView.findViewById(R.id.categoryContainer);
            nameText=itemView.findViewById(R.id.nameText);
            selectImage=itemView.findViewById(R.id.selectImage);
        }

        void bind(Category category){
            nameText.setText(category.getName());

            categoriesViewModel.getLiveCategoryRecordByCategoryIdAndAttributeId(category.getId(), attributeId).observe(sensorsActivity, categoryRecord -> {
                if(categoryRecord!=null){
                    selectImage.setImageResource(R.drawable.ic_check_box_black_24dp);
                    selectImage.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(appContext, R.color.colorGreen)));
                    selectedMap.put(category.getId(), true);
                }
                else{
                    selectImage.setImageResource(R.drawable.ic_check_box_outline_blank_black_24dp);
                    selectImage.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(appContext, R.color.colorMetal)));
                    selectedMap.put(category.getId(), false);
                }
            });

            categoryContainer.setOnClickListener(clickView -> {
                isSelected=!isSelected;
                if(isSelected){
                    selectImage.setImageResource(R.drawable.ic_check_box_black_24dp);
                    selectImage.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(appContext, R.color.colorGreen)));
                    selectedMap.put(category.getId(), true);
                }
                else{
                    selectImage.setImageResource(R.drawable.ic_check_box_outline_blank_black_24dp);
                    selectImage.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(appContext, R.color.colorMetal)));
                    selectedMap.put(category.getId(), false);
                }
            });
        }
    }


}
