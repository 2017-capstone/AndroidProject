package com.example.penguin.timetagger.Adapters;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.penguin.timetagger.Database.DatabaseHelper;
import com.example.penguin.timetagger.DrawerActivity;
import com.example.penguin.timetagger.Fragments.TagFragment;
import com.example.penguin.timetagger.R;
import com.example.penguin.timetagger.TimeTable;
import com.example.penguin.timetagger.TimeTag;

import java.text.ParseException;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import static com.example.penguin.timetagger.TimeTable.sdf;

/*
 * Created by penguin on 17. 5. 16.
 */

public class TimeListAdapter extends RecyclerView.Adapter<TimeListAdapter.TimeListViewHolder> implements DatePickerDialog.OnDateSetListener {
	private TextView curEdit;
	private Context context;
	private List<TimeTable> timeItems;

	public class TimeListViewHolder extends RecyclerView.ViewHolder{
		TextView v_repeat;
		TextView v_begin;
		TextView v_end;
		LinearLayout v_reserved;
		LinearLayout v_void;
		CardView cv;

		public TimeListViewHolder(View v){
			super(v);
			v_repeat = (TextView) v.findViewById(R.id.timeRepeat);
			v_begin = (TextView) v.findViewById(R.id.timeBegin);
			v_end = (TextView) v.findViewById(R.id.timeEnd);
			v_reserved = (LinearLayout)v.findViewById(R.id.timeReserved);
			v_void = (LinearLayout)v.findViewById(R.id.timeVoid);
			cv = (CardView)v.findViewById(R.id.timeItem);
		}
	}

	@Override
	public TimeListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		// create a new view
		View v = LayoutInflater
				.from(parent.getContext())
				.inflate(R.layout.item_time, parent, false);
		return new TimeListViewHolder(v);
	}

	public TimeListAdapter(Context context, List<TimeTable> times){
		this.context = context;
		timeItems = times;
		if(timeItems == null)
			timeItems = new LinkedList<>();
		timeItems.add(new TimeTable());
	}

	@Override
	public void onBindViewHolder(final TimeListViewHolder holder, final int position) {
		TimeTable duration = timeItems.get(position);
        // TODO: Database에 반복 주기 추가해야 함
		if(duration.getTimeID() == -1){

			LinearLayout.LayoutParams loparams;
			loparams = (LinearLayout.LayoutParams) holder.v_reserved.getLayoutParams();
			loparams.weight = 0;
			loparams.width = 0;
			loparams.height = 0;
			holder.v_repeat.setLayoutParams(loparams);

			loparams = (LinearLayout.LayoutParams) holder.v_void.getLayoutParams();
			loparams.weight = 1;
			loparams.width = LinearLayout.LayoutParams.MATCH_PARENT;
			loparams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
			holder.v_void.setLayoutParams(loparams);
		}else {
			holder.v_repeat.setText("매일");
			holder.v_begin.setText(sdf.format(duration.getStart()));
			holder.v_end.setText(sdf.format(duration.getEnd()));
		}

        /* 클릭 함수 */
		holder.cv.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				TimeTable duration1 = timeItems.get(position);
				switch (v.getId()){
					case R.id.timeRepeat:
						// TODO: Database 반복 추가후 사용
						break;
					case R.id.timeBeginFrame: {
						curEdit = (EditText) v.findViewById(R.id.timeBegin);
						int y = 0, m = 0, d = 0;
						try {
							Calendar cal = Calendar.getInstance();
							cal.setTime(sdf.parse(duration1.getStart().toString()));
							y = cal.get(Calendar.YEAR);
							m = cal.get(Calendar.MONTH);
							d = cal.get(Calendar.DAY_OF_MONTH);
						} catch (ParseException e) {
							e.printStackTrace();
						}
						DatePickerDialog dialog = new DatePickerDialog(
								context, TimeListAdapter.this, y, m, d
						);
						dialog.show();
					}break;
					case R.id.timeEndFrame: {
						curEdit = (EditText) v.findViewById(R.id.timeBegin);
						int y = 0, m = 0, d = 0;
						try {
							Calendar cal = Calendar.getInstance();
							cal.setTime(sdf.parse(duration1.getStart().toString()));
							y = cal.get(Calendar.YEAR);
							m = cal.get(Calendar.MONTH);
							d = cal.get(Calendar.DAY_OF_MONTH);
						} catch (ParseException e) {
							e.printStackTrace();
						}
						DatePickerDialog dialog = new DatePickerDialog(
								context, TimeListAdapter.this, y, m, d
						);
						dialog.show();
					}break;
					case R.id.newTime: {
						LinearLayout.LayoutParams loparams;
						loparams = (LinearLayout.LayoutParams) holder.v_void.getLayoutParams();
						loparams.weight = 0;
						loparams.width = 0;
						loparams.height = 0;
						holder.v_void.setLayoutParams(loparams);

						loparams = (LinearLayout.LayoutParams) holder.v_reserved.getLayoutParams();
						loparams.weight = 1;
						loparams.width = LinearLayout.LayoutParams.MATCH_PARENT;
						loparams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
						holder.v_repeat.setLayoutParams(loparams);
					}break;
				}
			}
		});
	}

	@Override
	public int getItemCount() {
		return timeItems.size();
	}

	@Override
	public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		curEdit.setText(sdf.format(cal.getTime()));
	}
}

