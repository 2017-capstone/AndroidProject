package com.example.penguin.timetagger.Fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.penguin.timetagger.Adapters.NoteGridViewAdapter;
import com.example.penguin.timetagger.Database.DatabaseHelper;
import com.example.penguin.timetagger.Note;
import com.example.penguin.timetagger.R;
import com.example.penguin.timetagger.TimeTag;

import java.util.List;

/**
 * Created by kuman514 on 2017-05-30.
 */

public class AlarmListFragment {
    private TimeTag timeTag;
    private int tag_id;
    private List<Note> notes;
    private NoteGridViewAdapter nia;
    RecyclerView rv;
    LinearLayoutManager lm;

    private Menu menu;
    private Boolean isEditTitle = false;
    public static AlarmListFragment newInstance(TimeTag timeTag){
        Bundle bundle = new Bundle();
        bundle.putParcelable("TIMETAG", timeTag);
        AlarmListFragment fragment = new AlarmListFragment();
        //fragment.setArguments(bundle);
        return fragment;
    }

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
        Bundle bundle = this.getArguments();

        String tag_name;
        if(bundle != null){
            timeTag = bundle.getParcelable("TIMETAG");
            tag_id = timeTag.getID();
            tag_name = timeTag.getTag();
        }else {
            tag_name = "AllTags";
            tag_id = 0;
        }
        EditText etTag = (EditText)(getActivity()).findViewById(R.id.toolbar_et);
        etTag.setText(tag_name);
        etTag.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == 4 || actionId == 6){
                    System.out.println("");
                    /* TODO: Database함수로 tag 업데이트 */
                    return true;
                }
                return false;
            }
        });
        view.requestFocus();

        DatabaseHelper.getInstance(getActivity());
        // TODO: 1회 실행후, 다음 줄은 주석 처리 할 것.
        //DatabaseHelper.loadDummyNotes();
        //DatabaseHelper.loadDummyTags();
        try {
            StaggeredGridLayoutManager sgl =
                    new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

            rv = (RecyclerView) view.findViewById(R.id.noteItemsRV);
            rv.setHasFixedSize(true);
            rv.setLayoutManager(sgl);

            nia = new NoteGridViewAdapter(getActivity(), tag_id);
            rv.setAdapter(nia);
            nia.notifyDataSetChanged();
        }catch (Exception e){
            System.out.println(e);}
        setHasOptionsMenu(true);
        return view;
    }
}

