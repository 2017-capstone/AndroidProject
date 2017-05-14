package com.example.penguin.timetagger.Fragments;

import android.os.Bundle;
//import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.penguin.timetagger.Adapters.NoteGridViewAdapter;
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
        bundle.putParcelable("NOTE", note);
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


        loadNotes();
        try {
            //lm = new LinearLayoutManager(getActivity());
            //lm.setOrientation(LinearLayoutManager.VERTICAL);
            StaggeredGridLayoutManager sgl =
                    new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

            rv = (RecyclerView) view.findViewById(R.id.noteItemsRV);
            rv.setHasFixedSize(true);
            rv.setLayoutManager(sgl);

            NoteGridViewAdapter nia = new NoteGridViewAdapter(getActivity(), notes);
            rv.setAdapter(nia);
            nia.notifyDataSetChanged();
        }catch (Exception e){
        System.out.println(e);}
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
                new Note("First Note", "This is a first note of TimeTagger. You can edit this note by click this card."),
                new Note("Second Note", "Size of the note is varies according to the amount of the content of note. Max length of string is 100. We will provide a menu to edit the max string length of summary of this card."),
                new Note("Image Note", "You can also attach an image to the note."),
                new Note("Record Note", "You can also attach an voice record to the note."),
                new Note("Drawing Note", "You can also draw an drawing. And attach the drawing to the note."));
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
