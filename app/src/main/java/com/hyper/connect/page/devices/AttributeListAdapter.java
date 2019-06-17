package com.hyper.connect.page.devices;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.hyper.connect.R;
import com.hyper.connect.app.GlobalApplication;
import com.hyper.connect.app.LocalRepository;
import com.hyper.connect.database.entity.Attribute;
import com.hyper.connect.database.entity.Category;
import com.hyper.connect.database.entity.CategoryRecord;
import com.hyper.connect.database.entity.enums.AttributeDirection;
import com.hyper.connect.database.entity.enums.AttributeScriptState;
import com.hyper.connect.database.entity.enums.AttributeState;
import com.hyper.connect.database.entity.Device;
import com.hyper.connect.database.entity.enums.AttributeType;
import com.hyper.connect.database.entity.enums.DeviceConnectionState;
import com.hyper.connect.database.entity.enums.DeviceState;
import com.hyper.connect.elastos.ElastosCarrier;
import com.hyper.connect.management.AttributeManagement;
import com.hyper.connect.page.history.HistoryActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class AttributeListAdapter extends RecyclerView.Adapter implements Filterable{
    private static final int VIEW_TYPE_ATTRIBUTE_INPUT=1;
    private static final int VIEW_TYPE_ATTRIBUTE_OUTPUT=2;
    private final LayoutInflater inflater;
    private final Context appContext;
    private List<Attribute> attributeList;
    private List<Attribute> fullAttributeList;
    private static ElastosCarrier elastosCarrier;
    private static LocalRepository localRepository;
    private static AttributeManagement attributeManagement;
    private SensorsActivity sensorsActivity;
    private TextView deviceNameText;
    private String deviceUserId;
    private Device device;
    private AttributesViewModel attributesViewModel;
    private CategoriesViewModel categoriesViewModel;
    private Map<Integer, Boolean> filteredCategoryMap;

    public AttributeListAdapter(Context context, SensorsActivity sensorsActivity, String deviceUserId){
        inflater=LayoutInflater.from(context);
        appContext=context;
        elastosCarrier=GlobalApplication.getElastosCarrier();
        localRepository=GlobalApplication.getLocalRepository();
        attributeManagement=GlobalApplication.getAttributeManagement();
        this.sensorsActivity=sensorsActivity;
        deviceNameText=sensorsActivity.findViewById(R.id.deviceNameText);
        this.deviceUserId=deviceUserId;
        attributesViewModel=ViewModelProviders.of(sensorsActivity).get(AttributesViewModel.class);
        categoriesViewModel=ViewModelProviders.of(sensorsActivity).get(CategoriesViewModel.class);
    }

    public void setAttributeList(List<Attribute> newAttributeList){
        attributeList=newAttributeList;
        fullAttributeList=new ArrayList<Attribute>(attributeList);
        notifyDataSetChanged();
    }

    public void setDevice(Device newDevice){
        device=newDevice;
        notifyDataSetChanged();
    }

    public void setFilteredCategoryMap(Map<Integer, Boolean> filteredCategoryMap){
        this.filteredCategoryMap=filteredCategoryMap;
    }

    @Override
    public int getItemViewType(int position){
        if(attributeList!=null){
            Attribute attribute=attributeList.get(position);
            AttributeDirection direction=attribute.getDirection();
            if(direction==AttributeDirection.INPUT){
                return VIEW_TYPE_ATTRIBUTE_INPUT;
            }
            else if(direction==AttributeDirection.OUTPUT){
                return VIEW_TYPE_ATTRIBUTE_OUTPUT;
            }
        }
        return 0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i){
        if(i==VIEW_TYPE_ATTRIBUTE_INPUT){
            View itemView=inflater.inflate(R.layout.item_recyclerview_attributes_input, viewGroup, false);
            return new AttributeInputViewHolder(itemView);
        }
        else if(i==VIEW_TYPE_ATTRIBUTE_OUTPUT){
            View itemView=inflater.inflate(R.layout.item_recyclerview_attributes_output, viewGroup, false);
            return new AttributeOutputViewHolder(itemView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i){
        if(attributeList!=null && device!=null){
            Attribute attribute=attributeList.get(i);
            int viewHolderType=viewHolder.getItemViewType();
            if(viewHolderType==VIEW_TYPE_ATTRIBUTE_INPUT){
                ((AttributeInputViewHolder)viewHolder).bind(attribute);
            }
            else if(viewHolderType==VIEW_TYPE_ATTRIBUTE_OUTPUT){
                ((AttributeOutputViewHolder)viewHolder).bind(attribute);
            }
        }
    }

    @Override
    public int getItemCount(){
        int count=0;
        if(attributeList!=null){
            count=attributeList.size();
        }
        return count;
    }

    class AttributeInputViewHolder extends RecyclerView.ViewHolder{
        private TextView nameText;
        private TextView valueText;
        private TextView dateTimeText;
        private AppCompatImageView stateImage;
        private TextView stateText;
        private Switch stateSwitch;
        private Button detailsButton;
        private Button historyButton;
        private AppCompatImageButton addCategoryButton;
        private TextView categoryPlaceholder;
        private LinearLayout categoryLayout;

        AttributeInputViewHolder(View itemView){
            super(itemView);
            nameText=itemView.findViewById(R.id.nameText);
            valueText=itemView.findViewById(R.id.valueText);
            dateTimeText=itemView.findViewById(R.id.dateTimeText);
            stateImage=itemView.findViewById(R.id.stateImage);
            stateText=itemView.findViewById(R.id.stateText);
            stateSwitch=itemView.findViewById(R.id.stateSwitch);
            detailsButton=itemView.findViewById(R.id.detailsButton);
            historyButton=itemView.findViewById(R.id.historyButton);
            addCategoryButton=itemView.findViewById(R.id.addCategoryButton);
            categoryPlaceholder=itemView.findViewById(R.id.categoryPlaceholder);
            categoryLayout=itemView.findViewById(R.id.categoryLayout);
        }

        void bind(Attribute attribute){
            if(!device.getDeletedState() && device.getState()==DeviceState.ACTIVE && device.getConnectionState()==DeviceConnectionState.ONLINE){
                if(!attributeManagement.isAttributeRunning(attribute.getId())){
                    attributeManagement.startAttribute(deviceUserId, attribute.getId(), attribute.getEdgeAttributeId());
                }
            }

            nameText.setText(attribute.getName());

            attributesViewModel.getLiveDataRecordByDeviceUserIdAndEdgeAttributeId(deviceUserId, attribute.getEdgeAttributeId()).observe(sensorsActivity, dataRecord -> {
                if(dataRecord!=null){
                    valueText.setText(dataRecord.getValue());
                    dateTimeText.setText(dataRecord.getDateTime());
                }
                else{
                    valueText.setText(R.string.text_no_value);
                    dateTimeText.setText(R.string.text_no_value);
                }
            });

            categoriesViewModel.getLiveCategoryRecordListByAttributeId(attribute.getId()).observe(sensorsActivity, categoryRecordList -> {
                if(categoryRecordList==null || categoryRecordList.isEmpty()){
                    categoryLayout.setVisibility(View.GONE);
                    categoryPlaceholder.setVisibility(View.VISIBLE);
                }
                else{
                    categoryLayout.removeAllViews();
                    for(CategoryRecord categoryRecord : categoryRecordList){
                        TextView categoryText=new TextView(sensorsActivity);
                        categoriesViewModel.getLiveCategoryById(categoryRecord.getCategoryId()).observe(sensorsActivity, category -> {
                            categoryText.setText(category.getName());
                            categoryText.setTextSize(14);
                            categoryText.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(appContext, R.color.colorBlack)));
                            categoryText.setBackgroundResource(R.drawable.button_category_background);
                            categoryText.setClickable(false);
                            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            layoutParams.setMarginEnd(30);
                            categoryText.setLayoutParams(layoutParams);
                        });
                        categoryLayout.addView(categoryText);
                    }
                    categoryLayout.setVisibility(View.VISIBLE);
                    categoryPlaceholder.setVisibility(View.GONE);
                }
            });

            AttributeState attributeState=attribute.getState();
            if(attributeState==AttributeState.ACTIVE){
                stateImage.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(appContext, R.color.colorGreen)));
                stateText.setText(R.string.text_active);
                stateSwitch.setChecked(true);
            }
            else if(attributeState==AttributeState.DEACTIVATED){
                stateImage.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(appContext, R.color.colorRed)));
                stateText.setText(R.string.text_deactivated);
                stateSwitch.setChecked(false);
            }

            stateSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if(device.getConnectionState()==DeviceConnectionState.ONLINE){
                    if(attribute.getScriptState()==AttributeScriptState.VALID){
                        JsonObject jsonObject=new JsonObject();
                        jsonObject.addProperty("command", "changeAttributeState");
                        jsonObject.addProperty("id", attribute.getEdgeAttributeId());
                        jsonObject.addProperty("state", isChecked);
                        String jsonString=jsonObject.toString();
                        boolean sendCheck=elastosCarrier.sendFriendMessage(deviceUserId, jsonString);
                        if(sendCheck){
                            if(isChecked){
                                attribute.setState(AttributeState.ACTIVE);
                            }
                            else{
                                attribute.setState(AttributeState.DEACTIVATED);
                            }
                            localRepository.updateAttribute(attribute);
                            Snackbar.make(deviceNameText, R.string.snack_attribute_state_changed, Snackbar.LENGTH_SHORT).show();
                        }
                        else{
                            Snackbar.make(deviceNameText, R.string.snack_something_went_wrong, Snackbar.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        stateSwitch.setChecked(false);
                        Snackbar.make(deviceNameText, R.string.snack_attribute_script_state_must_be_valid, Snackbar.LENGTH_SHORT).show();
                    }
                }
                else{
                    stateSwitch.setChecked(!isChecked);
                    Snackbar.make(deviceNameText, R.string.snack_device_offline, Snackbar.LENGTH_SHORT).show();
                }
            });

            detailsButton.setOnClickListener(buttonView -> {
                final Dialog dialog=new Dialog(sensorsActivity);
                dialog.setContentView(R.layout.dialog_attribute_details);

                TextView nameText=dialog.findViewById(R.id.nameText);
                TextView directionText=dialog.findViewById(R.id.directionText);
                TextView typeText=dialog.findViewById(R.id.typeText);
                TextView intervalText=dialog.findViewById(R.id.intervalText);
                TextView scriptStateText=dialog.findViewById(R.id.scriptStateText);
                TextView stateText=dialog.findViewById(R.id.stateText);

                nameText.setText(attribute.getName());
                directionText.setText(attribute.getDirection().toString());
                typeText.setText(attribute.getType().toString());
                String secondsLabel=appContext.getResources().getString(R.string.dialog_text_attribute_interval_seconds);
                intervalText.setText(attribute.getInterval()+" "+secondsLabel);
                scriptStateText.setText(attribute.getScriptState().toString());
                stateText.setText(attribute.getState().toString());

                AppCompatButton closeButton=dialog.findViewById(R.id.closeButton);
                closeButton.setOnClickListener(viewButton -> {
                    dialog.dismiss();
                });

                dialog.show();
            });

            if(attribute.getType()==AttributeType.STRING || attribute.getType()==AttributeType.BOOLEAN){
                historyButton.setVisibility(View.GONE);
            }
            else{
                historyButton.setVisibility(View.VISIBLE);
            }
            historyButton.setOnClickListener(buttonView -> {
                Intent intent=new Intent(appContext, HistoryActivity.class);
                intent.putExtra("deviceUserId", device.getUserId());
                intent.putExtra("edgeAttributeId", attribute.getEdgeAttributeId());
                intent.putExtra("attributeName", attribute.getName());
                intent.putExtra("attributeId", attribute.getId());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                appContext.startActivity(intent);
            });

            addCategoryButton.setOnClickListener(buttonView -> {
                FragmentManager fragmentManager=sensorsActivity.getSupportFragmentManager();
                CategoryDialog categoryDialog=new CategoryDialog();
                categoryDialog.init(sensorsActivity, attribute.getId(), device.getId());
                categoryDialog.show(fragmentManager, "CategoryDialog");
            });
        }
    }

    class AttributeOutputViewHolder extends RecyclerView.ViewHolder{
        private TextView nameText;
        private EditText valueInput;
        private Spinner valueSpinner;
        private Button sendButton;
        private AppCompatImageView stateImage;
        private TextView stateText;
        private Switch stateSwitch;
        private Button detailsButton;
        private AppCompatImageButton addCategoryButton;
        private TextView categoryPlaceholder;
        private LinearLayout categoryLayout;

        AttributeOutputViewHolder(View itemView){
            super(itemView);
            nameText=itemView.findViewById(R.id.nameText);
            valueInput=itemView.findViewById(R.id.valueInput);
            valueSpinner=itemView.findViewById(R.id.valueSpinner);
            sendButton=itemView.findViewById(R.id.sendButton);
            stateImage=itemView.findViewById(R.id.stateImage);
            stateText=itemView.findViewById(R.id.stateText);
            stateSwitch=itemView.findViewById(R.id.stateSwitch);
            detailsButton=itemView.findViewById(R.id.detailsButton);
            addCategoryButton=itemView.findViewById(R.id.addCategoryButton);
            categoryPlaceholder=itemView.findViewById(R.id.categoryPlaceholder);
            categoryLayout=itemView.findViewById(R.id.categoryLayout);
        }

        void bind(Attribute attribute){
            nameText.setText(attribute.getName());

            categoriesViewModel.getLiveCategoryRecordListByAttributeId(attribute.getId()).observe(sensorsActivity, categoryRecordList -> {
                if(categoryRecordList==null || categoryRecordList.isEmpty()){
                    categoryLayout.setVisibility(View.GONE);
                    categoryPlaceholder.setVisibility(View.VISIBLE);
                }
                else{
                    categoryLayout.removeAllViews();
                    for(CategoryRecord categoryRecord : categoryRecordList){
                        TextView categoryText=new TextView(sensorsActivity);
                        categoriesViewModel.getLiveCategoryById(categoryRecord.getCategoryId()).observe(sensorsActivity, category -> {
                            categoryText.setText(category.getName());
                            categoryText.setTextSize(14);
                            categoryText.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(appContext, R.color.colorBlack)));
                            categoryText.setBackgroundResource(R.drawable.button_category_background);
                            categoryText.setClickable(false);
                            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            layoutParams.setMarginEnd(30);
                            categoryText.setLayoutParams(layoutParams);
                        });
                        categoryLayout.addView(categoryText);
                    }
                    categoryLayout.setVisibility(View.VISIBLE);
                    categoryPlaceholder.setVisibility(View.GONE);
                }
            });

            AttributeState attributeState=attribute.getState();
            if(attributeState==AttributeState.ACTIVE){
                stateImage.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(appContext, R.color.colorGreen)));
                stateText.setText(R.string.text_active);
                stateSwitch.setChecked(true);
            }
            else if(attributeState==AttributeState.DEACTIVATED){
                stateImage.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(appContext, R.color.colorRed)));
                stateText.setText(R.string.text_deactivated);
                stateSwitch.setChecked(false);
            }

            stateSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if(device.getConnectionState()==DeviceConnectionState.ONLINE){
                    if(attribute.getScriptState()==AttributeScriptState.VALID){
                        JsonObject jsonObject=new JsonObject();
                        jsonObject.addProperty("command", "changeAttributeState");
                        jsonObject.addProperty("id", attribute.getEdgeAttributeId());
                        jsonObject.addProperty("state", isChecked);
                        String jsonString=jsonObject.toString();
                        boolean sendCheck=elastosCarrier.sendFriendMessage(deviceUserId, jsonString);
                        if(sendCheck){
                            if(isChecked){
                                attribute.setState(AttributeState.ACTIVE);
                            }
                            else{
                                attribute.setState(AttributeState.DEACTIVATED);
                            }
                            localRepository.updateAttribute(attribute);
                            Snackbar.make(deviceNameText, R.string.snack_attribute_state_changed, Snackbar.LENGTH_SHORT).show();
                        }
                        else{
                            Snackbar.make(deviceNameText, R.string.snack_something_went_wrong, Snackbar.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        stateSwitch.setChecked(false);
                        Snackbar.make(deviceNameText, R.string.snack_attribute_script_state_must_be_valid, Snackbar.LENGTH_SHORT).show();
                    }
                }
                else{
                    stateSwitch.setChecked(!isChecked);
                    Snackbar.make(deviceNameText, R.string.snack_device_offline, Snackbar.LENGTH_SHORT).show();
                }
            });

            detailsButton.setOnClickListener(buttonView -> {
                final Dialog dialog=new Dialog(sensorsActivity);
                dialog.setContentView(R.layout.dialog_attribute_details);

                TextView nameText=dialog.findViewById(R.id.nameText);
                TextView directionText=dialog.findViewById(R.id.directionText);
                TextView typeText=dialog.findViewById(R.id.typeText);
                TextView intervalText=dialog.findViewById(R.id.intervalText);
                TextView scriptStateText=dialog.findViewById(R.id.scriptStateText);
                TextView stateText=dialog.findViewById(R.id.stateText);

                nameText.setText(attribute.getName());
                directionText.setText(attribute.getDirection().toString());
                typeText.setText(attribute.getType().toString());
                String intervalLabel=appContext.getResources().getString(R.string.dialog_text_attribute_interval_event_driven);
                intervalText.setText(intervalLabel);
                scriptStateText.setText(attribute.getScriptState().toString());
                stateText.setText(attribute.getState().toString());

                AppCompatButton closeButton=dialog.findViewById(R.id.closeButton);
                closeButton.setOnClickListener(viewButton -> {
                    dialog.dismiss();
                });

                dialog.show();
            });

            if(attribute.getType()==AttributeType.BOOLEAN){
                valueInput.setVisibility(View.GONE);
                valueSpinner.setVisibility(View.VISIBLE);

                List<String> optionList=new ArrayList<String>();
                optionList.add("True");
                optionList.add("False");
                ArrayAdapter<String> optionAdapter=new ArrayAdapter<String>(appContext, R.layout.item_spinner_text, R.id.textView, optionList);
                valueSpinner.setAdapter(optionAdapter);
                valueSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                        String value=(String)parent.getItemAtPosition(position);
                        valueInput.setText(value);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent){}
                });
            }
            else{
                valueInput.setVisibility(View.VISIBLE);
                valueSpinner.setVisibility(View.GONE);
            }


            sendButton.setOnClickListener(buttonView -> {
                if(device.getConnectionState()==DeviceConnectionState.ONLINE){
                    String value=valueInput.getText().toString();
                    if(value.isEmpty()){
                        Snackbar.make(deviceNameText, R.string.snack_attribute_input_value_empty, Snackbar.LENGTH_SHORT).show();
                    }
                    else if(value.contains(" ")){
                        Snackbar.make(deviceNameText, R.string.snack_attribute_input_value_whitespace, Snackbar.LENGTH_SHORT).show();
                    }
                    else if(attribute.getState()==AttributeState.DEACTIVATED){
                        Snackbar.make(deviceNameText, R.string.snack_attribute_not_active, Snackbar.LENGTH_SHORT).show();
                    }
                    else{
                        JsonObject jsonObject=new JsonObject();
                        jsonObject.addProperty("command", "executeAttributeAction");
                        jsonObject.addProperty("id", attribute.getEdgeAttributeId());
                        jsonObject.addProperty("triggerValue", value);
                        String jsonString=jsonObject.toString();
                        boolean sendCheck=elastosCarrier.sendFriendMessage(deviceUserId, jsonString);
                        if(sendCheck){
                            Snackbar.make(deviceNameText, R.string.snack_attribute_execute_action, Snackbar.LENGTH_SHORT).show();
                        }
                        else{
                            Snackbar.make(deviceNameText, R.string.snack_something_went_wrong, Snackbar.LENGTH_SHORT).show();
                        }
                        valueInput.setText("");
                    }
                }
                else{
                    Snackbar.make(deviceNameText, R.string.snack_device_offline, Snackbar.LENGTH_SHORT).show();
                }
            });

            addCategoryButton.setOnClickListener(buttonView -> {
                FragmentManager fragmentManager=sensorsActivity.getSupportFragmentManager();
                CategoryDialog categoryDialog=new CategoryDialog();
                categoryDialog.init(sensorsActivity, attribute.getId(), device.getId());
                categoryDialog.show(fragmentManager, "CategoryDialog");
            });
        }
    }

    public static class CategoryDialog extends DialogFragment{
        private SensorsActivity sensorsActivity;
        private int attributeId;
        private int deviceId;
        private RecyclerView recyclerView;
        private CategoryListAdapter categoryListAdapter;
        private CategoriesViewModel categoriesViewModel;
        private LinearLayout emptyPlaceholder;
        private TextView categoryNameInput;
        private AppCompatButton addButton;
        private AppCompatButton closeButton;
        private AppCompatButton updateButton;

        public void init(SensorsActivity sensorsActivity, int attributeId, int deviceId){
            this.sensorsActivity=sensorsActivity;
            this.attributeId=attributeId;
            this.deviceId=deviceId;
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
            View view=inflater.inflate(R.layout.dialog_add_category, container, false);

            categoryNameInput=view.findViewById(R.id.categoryNameInput);
            addButton=view.findViewById(R.id.addButton);

            emptyPlaceholder=view.findViewById(R.id.emptyPlaceholder);
            recyclerView=view.findViewById(R.id.categories_recyclerview);
            categoryListAdapter=new CategoryListAdapter(getContext(), sensorsActivity, attributeId);
            recyclerView.setAdapter(categoryListAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            categoriesViewModel=ViewModelProviders.of(this).get(CategoriesViewModel.class);
            categoriesViewModel.getLiveCategoryList().observe(this, categoryList -> {
                categoryListAdapter.setCategoryList(categoryList);
                if(categoryList==null || categoryList.isEmpty()){
                    recyclerView.setVisibility(View.GONE);
                    emptyPlaceholder.setVisibility(View.VISIBLE);
                }
                else{
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyPlaceholder.setVisibility(View.GONE);
                }

                addButton.setOnClickListener(buttonView -> {
                    boolean canContinue=true;
                    String categoryName=categoryNameInput.getText().toString();
                    if(categoryName.isEmpty()){
                        Snackbar.make(addButton, R.string.snack_category_name_empty, Snackbar.LENGTH_SHORT).show();
                        canContinue=false;
                    }
                    if(categoryList!=null){
                        for(Category category : categoryList){
                            if(category.getName().equals(categoryName)){
                                Snackbar.make(addButton, R.string.snack_category_name_exists, Snackbar.LENGTH_SHORT).show();
                                canContinue=false;
                                break;
                            }
                        }
                    }
                    if(canContinue){
                        Category newCategory=new Category(0, categoryName);
                        localRepository.insertCategory(newCategory);
                    }
                    categoryNameInput.setText("");
                });
            });

            closeButton=view.findViewById(R.id.closeButton);
            closeButton.setOnClickListener(buttonView -> {
                dismiss();
            });

            updateButton=view.findViewById(R.id.updateButton);
            updateButton.setOnClickListener(buttonView -> {
                Map<Integer, Boolean> selectedMap=categoryListAdapter.getSelectedMap();
                for(Map.Entry<Integer, Boolean> entry : selectedMap.entrySet()){
                    int categoryId=entry.getKey();
                    boolean selected=entry.getValue();
                    if(selected){
                        CategoryRecord newCategoryRecord=new CategoryRecord(0, categoryId, attributeId, deviceId);
                        localRepository.insertCategoryRecord(newCategoryRecord);
                    }
                    else{
                        localRepository.deleteCategoryRecordByCategoryIdAndAttributeId(categoryId, attributeId);
                    }
                }
                Snackbar.make(updateButton, R.string.snack_category_updated, Snackbar.LENGTH_SHORT).show();
            });


            return view;
        }
    }

    @Override
    public Filter getFilter(){
        return categoryFilter;
    }

    private Filter categoryFilter=new Filter(){
        @Override
        protected FilterResults performFiltering(CharSequence constraint){
            List<Attribute> filteredList=new ArrayList<Attribute>();
            if(constraint==null || constraint.length()==0){
                filteredList.addAll(fullAttributeList);
            }
            else{
                for(Attribute attribute : fullAttributeList){
                    List<CategoryRecord> categoryRecordList=categoriesViewModel.getCategoryRecordListByAttributeId(attribute.getId());
                    for(CategoryRecord categoryRecord : categoryRecordList){
                        if(filteredCategoryMap!=null){
                            for(Map.Entry<Integer, Boolean> entry : filteredCategoryMap.entrySet()){
                                int categoryId=entry.getKey();
                                boolean selected=entry.getValue();
                                if(selected){
                                    if(categoryRecord.getCategoryId()==categoryId){
                                        filteredList.add(attribute);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            FilterResults results=new FilterResults();
            results.values=filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results){
            attributeList.clear();
            attributeList.addAll((List)results.values);
            attributeManagement.stopAllAttributes();
            notifyDataSetChanged();
        }
    };
}
