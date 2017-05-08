package com.example.penguin.timetagger.Fragments;

import android.os.Bundle;
import android.support.annotation.*;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.penguin.timetagger.Note;
import com.example.penguin.timetagger.R;

public class NoteFragment extends Fragment {
    ActionBar toolbar;

    public static NoteFragment newInstance(Note note){
        Bundle bundle = new Bundle();
        bundle.putSerializable("NOTE", note);
        NoteFragment fragment = new NoteFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_note, container, false);


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        toolbar = ((AppCompatActivity) getActivity()).getSupportActionBar();

    }
}
