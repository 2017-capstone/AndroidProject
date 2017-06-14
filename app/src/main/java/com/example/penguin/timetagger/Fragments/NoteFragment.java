package com.example.penguin.timetagger.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
//import android.app.Fragment;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.*;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.penguin.timetagger.Attachment;
import com.example.penguin.timetagger.Database.*;
import com.example.penguin.timetagger.Note;
import com.example.penguin.timetagger.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class NoteFragment extends Fragment {
    ActionBar toolbar;
    Note note;
    Uri imageUri;
    ImageView imgv;

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
        imgv = (ImageView) view.findViewById(R.id.noteContentImage);

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

        if(note.getAttaches() != null){
            List<Attachment> attaches = note.getAttaches();
            if(attaches.size() > 0) {
                imageUri = Uri.parse(attaches.get(0).getAttach());
                imgv.setImageURI(imageUri);
            }
        }
        Log.d("ImageViewURITest", "LoadURI=>"+note.getAttaches());


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

	        if(imageUri != null){
                List<Attachment> attaches = new LinkedList<>();
                attaches.add(new Attachment(note.getNoteID(), imageUri.toString()));
                note.setAttaches(attaches);
                note.setType(1);
            }

            if(note.getNoteID() == -1)
                DatabaseHelper.insertNote(note);
            else
                DatabaseHelper.updateNote(note);
            getFragmentManager().popBackStackImmediate();
            return true;
        }
        else if(id == R.id.action_camera){
            // 카메라 실행
            Intent intent = new Intent();
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            String dir = "TimeTagger-" + new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss")
                    .format(new Date(System.currentTimeMillis()))
                    + ".jpg";
            File photo = new File(Environment.getExternalStorageDirectory(), dir);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
            imageUri = Uri.fromFile(photo);
            startActivityForResult(intent, 1);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1){
            if(resultCode == Activity.RESULT_OK){
                List<Attachment> attaches = new LinkedList<>();
                attaches.add(new Attachment(note.getNoteID(), imageUri.toString()));
                note.setAttaches(attaches);
                Log.d("ImageViewURITest","CameraExitURI=>"+note.getAttaches());
                imgv.setImageURI(imageUri);
            }
        }
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
