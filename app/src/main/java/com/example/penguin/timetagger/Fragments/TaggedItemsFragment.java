package com.example.penguin.timetagger.Fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.penguin.timetagger.Adapters.NIAdapter;
import com.example.penguin.timetagger.NoteItem;
import com.example.penguin.timetagger.R;

public class TaggedItemsFragment extends Fragment {
    RecyclerView rv;
    LinearLayoutManager lm;

    /* View */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_tagged_list, container, false);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_content, new NoteFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        NoteItem[] ni = {
                new NoteItem("first note","this is first note"),
                new NoteItem("second note","this is second note"),
                new NoteItem("third note","this is third note"),
        };

        lm = new LinearLayoutManager(getActivity());
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        rv = (RecyclerView)view.findViewById(R.id.noteItemsRV);
        rv.setLayoutManager(lm);
        NIAdapter nia = new NIAdapter(ni);
        rv.setAdapter(nia);
        nia.notifyDataSetChanged();

        return view;
    }
    /* Model */

    /* Control */
}
