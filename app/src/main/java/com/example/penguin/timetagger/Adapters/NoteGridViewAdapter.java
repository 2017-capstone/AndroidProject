package com.example.penguin.timetagger.Adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.penguin.timetagger.Attachment;
import com.example.penguin.timetagger.Database.DatabaseHelper;
import com.example.penguin.timetagger.Fragments.NoteFragment;
import com.example.penguin.timetagger.Note;
import com.example.penguin.timetagger.R;
import com.example.penguin.timetagger.TimeTag;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


/*
 * Created by penguin on 17. 4. 30.
 */

public class NoteGridViewAdapter extends RecyclerView.Adapter<NoteGridViewAdapter.NoteGridViewHolder> {
    Context context;

    private List<Note> noteItems;
    private HashMap<Integer, Integer> checkedItems = new HashMap<>();
    public static boolean checkBoxShow = false;

    public void deleteCheckedNotes() {
        List<Integer> checkedNoteIDs = new LinkedList<>();
        for (int noteID : checkedItems.values()) {
            checkedNoteIDs.add(noteID);
            for (int i = 0; i < noteItems.size(); i++) {
                if (noteID == noteItems.get(i).getNoteID())
                    noteItems.remove(i);
            }
        }
        DatabaseHelper.deleteNotes(checkedNoteIDs);
        checkBoxShow = false;
        notifyDataSetChanged();

    }

    public void setCheckBoxShow(boolean b){
        checkBoxShow = b;
        if(checkedItems == null) checkedItems = new HashMap<>();
        notifyDataSetChanged();
    }

    public class NoteGridViewHolder extends RecyclerView.ViewHolder{
        /* TODO: 데이터 베이스 설계에 맞게 수정됨 1 */
        TextView v_title;
        TextView v_summary;
        CardView cv;
        CheckBox cb;
        ImageView img;
        Button alarmBtn;

        public NoteGridViewHolder(View v){
            super(v);
            /* TODO: 데이터 베이스 설계에 맞게 수정됨 2 */
            v_title = (TextView) v.findViewById(R.id.noteItemTitle);
            v_summary = (TextView) v.findViewById(R.id.noteItemSummary);
            cv = (CardView) v.findViewById(R.id.noteItem);
            cb = (CheckBox) v.findViewById(R.id.noteCheckBox);
            img = (ImageView) v.findViewById(R.id.noteItemImage);
        }
    }

    @Override
    public NoteGridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_note, parent, false);

        // 새로 표시될때 체크박스 표시 여부를 물어봄
        CheckBox cb = (CheckBox) v.findViewById(R.id.noteCheckBox);
        if(checkBoxShow){
            cb.setVisibility(View.VISIBLE);
            cb.setClickable(true);
        }
        else {
            cb.setChecked(false);
            cb.setClickable(false);
            cb.setVisibility(View.INVISIBLE);
        }

        return new NoteGridViewHolder(v);
    }

    public NoteGridViewAdapter(Context context, int tag_id){
        this.context = context;
        if(tag_id != -1) this.noteItems = DatabaseHelper.selectNotes(tag_id);
        else{
            this.noteItems = DatabaseHelper.selectNotes(0);
            List<TimeTag> tagCount = DatabaseHelper.selectAllTags();
            for(int i = 1; i < tagCount.size(); i++){
                this.noteItems.addAll(DatabaseHelper.selectNotes(i));
            }
        }
        //this.noteItems = noteItems;
    }

    @Override
    public void onBindViewHolder(final NoteGridViewHolder holder, final int position) {

        /* TODO: 데이터 베이스에 설계 맞게 수정됨 3 */
        holder.v_title.setText(noteItems.get(position).getTitle());

        /* TODO: 최대 표시 문자길이를 옵션으로 추가할 것(현재값: 100) */
        int MAX_STRING = 100;
        String bodyString = noteItems.get(position).getBody();
        if(bodyString.length() < MAX_STRING)
            holder.v_summary.setText(noteItems.get(position).getBody());
        else{
            bodyString = bodyString.substring(0, MAX_STRING) + "...";
            holder.v_summary.setText(bodyString);
        }

        // 이미지 보여주기
        if(noteItems.get(position).getType() == 1){ // 이미지 타입
            // 권한 받아오기
            //...
            List<Attachment> attaches = noteItems.get(position).getAttaches();
            if(attaches.size() > 0)
                holder.img.setImageURI(Uri.parse(attaches.get(0).getAttach()));
        }

        // 체크박스 표시 또는 숨기기 (클릭커블 포함)
        CheckBox _cb = holder.cb;
        if(checkBoxShow){
            _cb.setVisibility(View.VISIBLE);
            _cb.setClickable(true);

            if(checkedItems.containsKey(position))
                _cb.setChecked(true);
            else
                _cb.setChecked(false);

        }
        else {
            if(checkedItems != null) checkedItems.clear();
            _cb.setChecked(false);
            _cb.setClickable(false);
            _cb.setVisibility(View.INVISIBLE);
        }

        /* 클릭 함수 */
        holder.cv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Note note = noteItems.get(position);
                NoteFragment fragment = NoteFragment.newInstance(note);

                /* 체크 박스 표시 */
                if(checkBoxShow){
                    CheckBox cb = (CheckBox) v.findViewById(R.id.noteCheckBox);
                        // 체크 해제
                    if(checkedItems.containsKey(position)) {
                        checkedItems.remove(position);
                        cb.setChecked(false);
                    }
                    else{
                        // 체크 표시
                        checkedItems.put(position, note.getNoteID());

                        cb.setChecked(true);
                    }
                }
                /* 개별 항목 선택 */
                else {
                    ((FragmentActivity) context).getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame_content, fragment)
                            .addToBackStack(null)
                            .commit();
                }
            }
        });

        // 롱클릭 함수
        holder.cv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // 롱클릭 시 체크 표시 및 해당 터치 노트는 바로 선택됨
                Note note = noteItems.get(position);
                if(!checkBoxShow) {
                    setCheckBoxShow(true);
                    CheckBox cb = (CheckBox) v.findViewById(R.id.noteCheckBox);
                    cb.setVisibility(View.VISIBLE);
                    cb.setChecked(true);
                    if (!checkedItems.containsKey(position)) checkedItems.put(position, note.getNoteID());
                }

                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return noteItems.size();
    }
}