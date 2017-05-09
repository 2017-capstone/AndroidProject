package com.example.penguin.timetagger.Adapters;

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

public class NIAdapter extends RecyclerView.Adapter<NIAdapter.ViewHolder> {

    private List<Note> noteItems;
    public static class ViewHolder extends RecyclerView.ViewHolder{
        /* TODO: 데이터 베이스 설계에 맞게 수정됨 1 */
        TextView v_title;
        TextView v_summary;
        CardView cv;

        public ViewHolder(View v){
            super(v);
            /* TODO: 데이터 베이스 설계에 맞게 수정됨 2 */
            v_title = (TextView) v.findViewById(R.id.noteItemTitle);
            v_summary = (TextView) v.findViewById(R.id.noteItemSummary);
            cv = (CardView)v.findViewById(R.id.noteItem);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_note, null);
        return new ViewHolder(v);
    }

    public NIAdapter(View view, List<Note> noteItems){
        for(int i=0; i<noteItems.size(); i++)
            this.noteItems.add(noteItems.get(i));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        /* TODO: 데이터 베이스에 설계 맞게 수정됨 3 */
        holder.v_title.setText(noteItems.get(position).title);
        holder.v_summary.setText(noteItems.get(position).body);
    }

    @Override
    public int getItemCount() {
        return noteItems.size();
    }
}

