package com.example.penguin.timetagger.Fragments;

import android.os.Bundle;
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
import com.example.penguin.timetagger.Database.DatabaseHelper;
import com.example.penguin.timetagger.Note;
import com.example.penguin.timetagger.R;

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

        DatabaseHelper.getInstance(getActivity());
        // TODO: 1회 실행후, 다음 줄은 주석 처리 할 것.
        //DatabaseHelper.loadDummyNotes();
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

    private void movetoNoteFragment(Note note){
        NoteFragment fragment = NoteFragment.newInstance(note);

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_content, fragment)
                .addToBackStack(null)
                .commit();

    }
}
