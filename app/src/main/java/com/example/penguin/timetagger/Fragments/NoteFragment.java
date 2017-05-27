package com.example.penguin.timetagger.Fragments;

import android.os.Bundle;
//import android.app.Fragment;
import android.support.annotation.*;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.penguin.timetagger.Database.*;
import com.example.penguin.timetagger.Note;
import com.example.penguin.timetagger.R;

public class NoteFragment extends Fragment {
    ActionBar toolbar;
    Note note;

    public static NoteFragment newInstance(Note note){
        Bundle bundle = new Bundle();
        bundle.putParcelable("NOTE", note);
        NoteFragment fragment = new NoteFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.content_note, container, false);
        Bundle bundle = this.getArguments();
        String noteTitle;
        String noteBody;
        if(bundle != null) {
            note = bundle.getParcelable("NOTE");
            noteTitle = note.getTitle();
            noteBody = note.getBody();
        }else{
            noteTitle = null;
            noteBody = null;
        }
        EditText etNoteTitle = (EditText)(getActivity()).findViewById(R.id.toolbar_et);
        etNoteTitle.setText(noteTitle);
        etNoteTitle.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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
        EditText etNoteBody = (EditText)view.findViewById(R.id.edit_text);
        etNoteBody.setText(noteBody, TextView.BufferType.EDITABLE);

        view.requestFocus();
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.noteview_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /*
    * 기능: fragment 별로 다른 메뉴를 사용가능하게 함
    * 매개: 메뉴, 인플레이터
    * 주의:
    * 요구조건 1. onCreateOptionsMenu 에서 메뉴 지정 필요함
    * 요구조건 2. onCreateView 에서 setHasOptionsMenu(true) 가 필요함
    * 요구조건 3. onResume()함수
    */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_save) {
	        EditText etNoteTitle = (EditText)(getActivity()).findViewById(R.id.toolbar_et);
            EditText et = (EditText)getActivity().findViewById(R.id.edit_text);
	        String noteTitle = etNoteTitle.getText().toString();
            String noteBody = et.getText().toString();
	        note.setTitle(noteTitle);
            note.setBody(noteBody);

            if(note.getNoteID() == -1)
                DatabaseHelper.insertNote(note);
            else
                DatabaseHelper.updateNote(note);
            getFragmentManager().popBackStackImmediate();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume(){
        super.onResume();
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        toolbar = ((AppCompatActivity) getActivity()).getSupportActionBar();

    }
}
