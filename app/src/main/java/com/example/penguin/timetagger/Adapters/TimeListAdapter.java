package com.example.penguin.timetagger.Adapters;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.TimePicker;

import com.example.penguin.timetagger.Database.DatabaseHelper;
import com.example.penguin.timetagger.DrawerActivity;
import com.example.penguin.timetagger.Fragments.TagFragment;
import com.example.penguin.timetagger.R;
import com.example.penguin.timetagger.TimeTable;
import com.example.penguin.timetagger.TimeTag;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import static com.example.penguin.timetagger.TimeTable.sdf;

/*
 * Created by penguin on 17. 5. 16.
 */

public class TimeListAdapter extends RecyclerView.Adapter<TimeListAdapter.TimeListViewHolder> implements TimePickerDialog.OnTimeSetListener {
	private TextView curEdit;
	private Context context;
	public List<TimeTable> timeItems;
	SimpleDateFormat stf = new SimpleDateFormat("h:mm");

	int curEditNum;
	int curEditSColumn;

	public class TimeListViewHolder extends RecyclerView.ViewHolder{
		TextView v_repeat;
		TextView v_begin;
		TextView v_end;
		CardView cv;

		LinearLayout vf_begin;
		LinearLayout vf_end;

		public TimeListViewHolder(View v){
			super(v);
			v_repeat = (TextView) v.findViewById(R.id.timeRepeat);
			v_begin = (TextView) v.findViewById(R.id.timeBegin);
			v_end = (TextView) v.findViewById(R.id.timeEnd);
			vf_begin =(LinearLayout)v.findViewById(R.id.timeBeginFrame);
			vf_end = (LinearLayout)v.findViewById(R.id.timeEndFrame);
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
		holder.v_repeat.setText("매일");
		if(duration.getStart() == null)
			holder.v_begin.setText("??");
		else
			holder.v_begin.setText(stf.format(duration.getStart()));

		if(duration.getEnd() == null)
			holder.v_end.setText("??");
		else
			holder.v_end.setText(stf.format(duration.getEnd()));


		holder.vf_begin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				curEdit = holder.v_begin;
				curEditNum = position;
				curEditSColumn = 1;
				Calendar cal = Calendar.getInstance();
				int h, m;
				try {
					cal.setTime(stf.parse(curEdit.getText().toString()));
					h = cal.get(Calendar.HOUR_OF_DAY);
					m = cal.get(Calendar.MINUTE);
				} catch (ParseException e) {
					h = 0;
					m = 0;
				}
				TimePickerDialog dialog =
						new TimePickerDialog(context,
								TimeListAdapter.this,
								h, m, true);
				dialog.setTitle("시간을 정하세요");
				dialog.show();
			}
		});
		holder.vf_end.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				curEdit = holder.v_end;
				curEditNum = position;
				curEditSColumn = 2;
				Calendar cal = Calendar.getInstance();
				int h, m;
				try {
					cal.setTime(stf.parse(curEdit.getText().toString()));
					h = cal.get(Calendar.HOUR_OF_DAY);
					m = cal.get(Calendar.MINUTE);
				} catch (ParseException e) {
					h = 0;
					m = 0;
				}
				TimePickerDialog dialog =
						new TimePickerDialog(context,
								TimeListAdapter.this,
								h, m, true);
				dialog.setTitle("시간을 정하세요");
				dialog.show();
			}
		});
	}

	@Override
	public int getItemCount() {
		return timeItems.size();
	}

	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
		cal.set(Calendar.MINUTE, minute);
		curEdit.setText(stf.format(cal.getTime()));
		switch (curEditSColumn){
			case 0:
				break;
			case 1:
				timeItems.get(curEditNum).setStart(new Timestamp(cal.getTimeInMillis()));
				break;
			case 2:
				timeItems.get(curEditNum).setEnd(new Timestamp(cal.getTimeInMillis()));
				break;
		}
	}

	public void addTimetable(){
		timeItems.add(new TimeTable());
		notifyDataSetChanged();
	}

	public List<TimeTable> getTimes(){
		List<TimeTable> items = new LinkedList<>();
		for(TimeTable t : timeItems){
			if(t.getStart() == null || t.getEnd() == null)
				continue;
			else
				items.add(t);
		}
		return items;
	}
}

