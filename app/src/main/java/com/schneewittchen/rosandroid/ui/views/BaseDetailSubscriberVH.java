package com.schneewittchen.rosandroid.ui.views;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.model.entities.BaseEntity;
import com.schneewittchen.rosandroid.model.entities.SubscriberEntity;
import com.schneewittchen.rosandroid.model.repositories.rosRepo.message.Topic;
import com.schneewittchen.rosandroid.ui.fragments.details.WidgetChangeListener;
import com.schneewittchen.rosandroid.utility.CustomSpinner;

import java.util.ArrayList;
import java.util.List;


/**
 * TODO: Description
 *
 * @author Nils Rottmann
 * @version 1.0.0
 * @created on 17.09.20
 * @updated on
 * @modified by
 */
public abstract class BaseDetailSubscriberVH<T extends SubscriberEntity> extends BaseDetailViewHolder<T> {

    public static final String TAG = BaseDetailViewHolder.class.getSimpleName();

    CustomSpinner topicNameText;
    CustomSpinner topicTypeText;

    List<String> topicNameList;
    ArrayAdapter<String> topicNameAdapter;

    private List<String> topicTypeList;
    ArrayAdapter<String> topicTypeAdapter;


    public BaseDetailSubscriberVH(@NonNull View view, WidgetChangeListener updateListener) {
        super(view, updateListener);

        //this.topicTypeList = this.getTopicTypes();
    }

    public List<String> getTopicTypes(){
        return null;
    }

    @Override
    protected void baseInitView(View parentView) {
        super.baseInitView(parentView);

        Log.i(TAG, "init");

        // Initialize Views
        topicNameText = parentView.findViewById(R.id.topicNameText);
        topicTypeText = parentView.findViewById(R.id.topicTypeText);

        // Initialize Topic Name Spinner
        topicNameList = new ArrayList<>();
        topicNameAdapter = new ArrayAdapter<>(parentView.getContext(),
                android.R.layout.simple_spinner_dropdown_item, topicNameList);
        topicNameText.setAdapter(topicNameAdapter);

        // Initialize Topic Type Spinner
        topicTypeAdapter = new ArrayAdapter<>(parentView.getContext(),
                android.R.layout.simple_spinner_dropdown_item, topicTypeList);
        topicTypeText.setAdapter(topicTypeAdapter);

        // Define action responses for topic names
        topicNameText.setSpinnerEventsListener(new CustomSpinner.OnSpinnerEventsListener() {
            @Override
            public void onSpinnerOpened() {
                updateTopicNameSpinner();
            }

            @Override
            public void onSpinnerClosed() {
                updateEntity();
            }
        });

        // Define action responses for message type
        topicTypeText.setSpinnerEventsListener(new CustomSpinner.OnSpinnerEventsListener() {
            @Override
            public void onSpinnerOpened() { }

            @Override
            public void onSpinnerClosed() {
                updateEntity();
            }
        });
    }

    @Override
    protected void baseBindEntity(T entity) {
        super.baseBindEntity(entity);

        Log.i(TAG, "bind");
        updateTopicNameSpinner();

        String messageType = entity.topic.type;
        String topicName = entity.topic.name;

        topicTypeText.setSelection(topicTypeList.indexOf(messageType));
        topicNameText.setSelection(topicNameList.indexOf(topicName));
    }

    @Override
    protected void baseUpdateEntity() {
        super.baseUpdateEntity();

        Log.i(TAG, "updateEntity");

        if (topicTypeText.getSelectedItem() != null) {
            entity.topic.type = topicTypeText.getSelectedItem().toString();
        }

        if (topicNameText.getSelectedItem() != null) {
            entity.topic.name = topicNameText.getSelectedItem().toString();
        }
    }

    void updateTopicNameSpinner() {
        // Get the list with all suitable topics
        topicNameList = new ArrayList<>();

        for (Topic rosTopic: mViewModel.getTopicList()) {
            if (rosTopic.type.equals(entity.topic.type)) {
                topicNameList.add(rosTopic.name);
            }
        }

        topicNameAdapter.clear();
        topicNameAdapter.addAll(topicNameList);
    }

    protected void setTopicTypeList(List<String> list) {
        this.topicTypeList = list;
    }
}
