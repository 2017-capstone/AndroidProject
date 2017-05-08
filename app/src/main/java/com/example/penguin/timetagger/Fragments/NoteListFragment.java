package com.example.penguin.timetagger.Fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.penguin.timetagger.Note;
import com.example.penguin.timetagger.R;

import java.util.Arrays;
import java.util.List;

public class NoteListFragment extends Fragment {
    private List<Note> notes;
    RecyclerView rv;
    LinearLayoutManager lm;
    public static NoteListFragment newInstance(Note note){
        Bundle bundle = new Bundle();
        bundle.putSerializable("NOTE", note);
        NoteListFragment fragment = new NoteListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_note_list, container, false);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        // 새로운 노트 작성
        fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v){
                        movetoNoteFragment(new Note());
                    }
                });
        // 기존 노트 작성

/*
        loadNotes()

        lm = new LinearLayoutManager(getActivity());
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        rv = (RecyclerView)view.findViewById(R.id.noteItemsRV);
        rv.setLayoutManager(lm);
        NIAdapter nia = new NIAdapter(ni);
        rv.setAdapter(nia);
        nia.notifyDataSetChanged();
*/
        setHasOptionsMenu(true);
        return view;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.listview_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_save) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume(){
        super.onResume();
        getActivity().invalidateOptionsMenu();
    }

    private void loadNotes(){
        // TODO: sample을 DB와 연결 할 것
        notes = Arrays.asList(
                new Note("body1", "title1"),
                new Note("body2", "title2"),
                new Note("body3", "title3"));
    }

    private void movetoNoteFragment(Note note){
        NoteFragment fragment = NoteFragment.newInstance(note);

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_content, fragment)
                .addToBackStack(null)
                .commit();

    }
}
