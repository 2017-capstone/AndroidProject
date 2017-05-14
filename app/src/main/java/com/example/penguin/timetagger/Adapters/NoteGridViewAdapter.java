package com.example.penguin.timetagger.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.penguin.timetagger.Note;
import com.example.penguin.timetagger.R;

import java.util.List;

/*
 * Created by penguin on 17. 4. 30.
 */

public class NoteGridViewAdapter extends RecyclerView.Adapter<NoteGridViewAdapter.NoteGridViewHolder> {
    Context context;
    private List<Note> noteItems;
    public class NoteGridViewHolder extends RecyclerView.ViewHolder{
        /* TODO: 데이터 베이스 설계에 맞게 수정됨 1 */
        TextView v_title;
        TextView v_summary;
        CardView cv;

        public NoteGridViewHolder(View v){
            super(v);
            /* TODO: 데이터 베이스 설계에 맞게 수정됨 2 */
            v_title = (TextView) v.findViewById(R.id.noteItemTitle);
            v_summary = (TextView) v.findViewById(R.id.noteItemSummary);
            cv = (CardView)v.findViewById(R.id.noteItem);
        }
    }

    @Override
    public NoteGridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_note, parent, false);
        return new NoteGridViewHolder(v);
    }

    public NoteGridViewAdapter(Context context, List<Note> noteItems){
        this.context = context;
        this.noteItems = noteItems;
    }

    @Override
    public void onBindViewHolder(final NoteGridViewHolder holder, int position) {

        /* TODO: 데이터 베이스에 설계 맞게 수정됨 3 */
        holder.v_title.setText(noteItems.get(position).title);

        /* TODO: 최대 표시 문자길이를 옵션으로 추가할 것(현재값: 100) */
        int MAX_STRING = 100;
        String bodyString = noteItems.get(position).body;
        if(bodyString.length() < MAX_STRING)
            holder.v_summary.setText(noteItems.get(position).body);
        else{
            bodyString = bodyString.substring(0, MAX_STRING) + "...";
            holder.v_summary.setText(bodyString);
        }


    }

    @Override
    public int getItemCount() {
        return noteItems.size();
    }
}

