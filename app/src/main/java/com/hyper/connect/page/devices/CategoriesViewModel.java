package com.hyper.connect.page.devices;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.hyper.connect.app.GlobalApplication;
import com.hyper.connect.app.LocalRepository;
import com.hyper.connect.database.entity.Category;
import com.hyper.connect.database.entity.CategoryRecord;

import java.util.List;


public class CategoriesViewModel extends AndroidViewModel{
    private LocalRepository localRepository;

    public CategoriesViewModel(Application application){
        super(application);
        localRepository=GlobalApplication.getLocalRepository();
    }

    public LiveData<List<Category>> getLiveCategoryList(){
        return localRepository.getLiveCategoryList();
    }

    public LiveData<List<CategoryRecord>> getLiveCategoryRecordList(){
        return localRepository.getLiveCategoryRecordList();
    }

    public LiveData<CategoryRecord> getLiveCategoryRecordByCategoryIdAndAttributeId(int categoryId, int attributeId){
        return localRepository.getLiveCategoryRecordByCategoryIdAndAttributeId(categoryId, attributeId);
    }

    public LiveData<List<CategoryRecord>> getLiveCategoryRecordListByAttributeId(int attributeId){
        return localRepository.getLiveCategoryRecordListByAttributeId(attributeId);
    }

    public LiveData<Category> getLiveCategoryById(int id){
        return localRepository.getLiveCategoryById(id);
    }

    public Category getCategoryById(int id){
        return localRepository.getCategoryById(id);
    }

    public List<CategoryRecord> getCategoryRecordListByAttributeId(int attributeId){
        return localRepository.getCategoryRecordListByAttributeId(attributeId);
    }
}
