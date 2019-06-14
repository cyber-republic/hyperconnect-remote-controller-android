package com.hyper.connect.page.events;

import static com.hyper.connect.MainActivity.ACTION_REQUEST_ADD_EVENT;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hyper.connect.R;


public class EventsFragment extends Fragment{
    private RecyclerView recyclerView;
    private EventListAdapter eventListAdapter;
    private EventsViewModel eventsViewModel;
    private FloatingActionButton addEventButton;
    private LinearLayout emptyPlaceholder;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.fragment_events, container, false);

        addEventButton=view.findViewById(R.id.addEventButton);
        emptyPlaceholder=view.findViewById(R.id.emptyPlaceholder);
        recyclerView=view.findViewById(R.id.events_recyclerview);
        eventListAdapter=new EventListAdapter(getContext(), this, addEventButton);
        recyclerView.setAdapter(eventListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        eventsViewModel=ViewModelProviders.of(this).get(EventsViewModel.class);
        eventsViewModel.getLiveEventList().observe(this, eventList -> {
            eventListAdapter.setEventList(eventList);
            if(eventList==null || eventList.isEmpty()){
                recyclerView.setVisibility(View.GONE);
                emptyPlaceholder.setVisibility(View.VISIBLE);
            }
            else{
                recyclerView.setVisibility(View.VISIBLE);
                emptyPlaceholder.setVisibility(View.GONE);
            }
        });


        addEventButton.setOnClickListener(buttonView -> {
            Intent intent=new Intent(getActivity(), AddEventActivity.class);
            getActivity().startActivityForResult(intent, ACTION_REQUEST_ADD_EVENT);
        });

        return view;
    }
}
