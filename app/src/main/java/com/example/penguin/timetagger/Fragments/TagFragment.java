package com.example.penguin.timetagger.Fragments;

import android.app.DatePickerDialog;
import android.support.v4.app.Fragment;

/**
 * Created by penguin on 17. 5. 15.
 */

import android.os.Bundle;
import android.support.annotation.*;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.penguin.timetagger.Adapters.TagListAdapter;
import com.example.penguin.timetagger.Adapters.TimeListAdapter;
import com.example.penguin.timetagger.Database.DatabaseHelper;
import com.example.penguin.timetagger.R;
import com.example.penguin.timetagger.TimeTable;
import com.example.penguin.timetagger.TimeTag;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.penguin.timetagger.TimeTable.sdf;

public class TagFragment extends Fragment implements DatePickerDialog.OnDateSetListener{
	ActionBar toolbar;
	TimeTag timeTag;
	View view;
	List<TimeTable> times;
	RecyclerView rv;
	LinearLayoutManager lm;
	EditText tagName;
	EditText tagBegin;
	EditText tagEnd;
	EditText curEdit;
	TimeListAdapter tla;

	public static TagFragment newInstance(TimeTag timeTag){
		Bundle bundle = new Bundle();
		bundle.putParcelable("TAG", timeTag);
		TagFragment fragment = new TagFragment();
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = inflater.inflate(R.layout.content_tag, container, false);


		tagName = (EditText)view.findViewById(R.id.tagName);
		tagBegin = (EditText)view.findViewById(R.id.tagBegin);
		tagEnd = (EditText) view.findViewById(R.id.tagEnd);

		Bundle bundle = this.getArguments();
		if(bundle != null) {
			timeTag = bundle.getParcelable("TAG");
			tagName.setText(timeTag.getTag(), TextView.BufferType.EDITABLE);
			tagBegin.setText(sdf.format(timeTag.getStart()), TextView.BufferType.EDITABLE);
			tagEnd.setText(sdf.format(timeTag.getEnd()), TextView.BufferType.EDITABLE);

		}else{
			timeTag = new TimeTag();
			tagName.setHint("새로운 태그...");
			tagBegin.setText("??");
			tagEnd.setText("??");
		}


		tagBegin.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
	               curEdit = tagBegin;
	               int y=0, m=0, d=0;
	               try {
		               Calendar cal = Calendar.getInstance();
		               cal.setTime(sdf.parse(curEdit.getText().toString()));
		               y = cal.get(Calendar.YEAR);
		               m = cal.get(Calendar.MONTH);
		               d = cal.get(Calendar.DAY_OF_MONTH);
	               } catch (ParseException e) {
		               Calendar cal = Calendar.getInstance();
		               y = cal.get(Calendar.YEAR);
		               m = cal.get(Calendar.MONTH);
		               d = cal.get(Calendar.DAY_OF_MONTH);
	               }
                   DatePickerDialog dialog = new DatePickerDialog(
		                   getActivity(), TagFragment.this, y, m, d
                   );
	               dialog.show();
               }
        });

		tagEnd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				curEdit = tagEnd;
				int y=0, m=0, d=0;
				try {
					Calendar cal = Calendar.getInstance();
					cal.setTime(sdf.parse(curEdit.getText().toString()));
					y = cal.get(Calendar.YEAR);
					m = cal.get(Calendar.MONTH);
					d = cal.get(Calendar.DAY_OF_MONTH);
				} catch (ParseException e) {
					Calendar cal = Calendar.getInstance();
					y = cal.get(Calendar.YEAR);
					m = cal.get(Calendar.MONTH);
					d = cal.get(Calendar.DAY_OF_MONTH);
				}
				DatePickerDialog dialog = new DatePickerDialog(
						getActivity(), TagFragment.this, y, m, d
				);
				dialog.show();
			}
		});

		DatabaseHelper.getInstance(getActivity());

		try{
			lm = new LinearLayoutManager(getActivity());
			lm.setOrientation(LinearLayoutManager.VERTICAL);

			rv = (RecyclerView)view.findViewById(R.id.timeItemRV);
			rv.setLayoutManager(lm);

			tla = new TimeListAdapter(getActivity(), timeTag.getTimes());
			rv.setAdapter(tla);
			tla.notifyDataSetChanged();
		}catch (Exception e){
			System.out.println(e);
		}

		view.findViewById(R.id.newTime).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				tla.addTimetable();
			}
		});

		setHasOptionsMenu(true);

		return view;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
		inflater.inflate(R.menu.tagview_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(id==R.id.action_save_tag){
			timeTag.setTimes(tla.getTimes());
			timeTag.setTag(tagName.getText().toString());
			try {
				Calendar cal = Calendar.getInstance();
				cal.setTime(sdf.parse(tagBegin.getText().toString()));
				timeTag.setStart(new Timestamp(cal.getTimeInMillis()));

				cal.setTime(sdf.parse(tagEnd.getText().toString()));
				timeTag.setEnd(new Timestamp(cal.getTimeInMillis()));
			} catch (ParseException e) {
				if(timeTag.getStart() == null ||
						timeTag.getEnd() == null) {
					Toast.makeText(getContext(), "태그 설정기간이 비어있습니다", Toast.LENGTH_SHORT).show();
					return super.onOptionsItemSelected(item);
				}
				e.printStackTrace();
			}
			if(timeTag.getTag() == null || timeTag.getTag().equals("")){
				Toast.makeText(getContext(), "태그 이름을 입력해 주세요", Toast.LENGTH_SHORT).show();
				return super.onOptionsItemSelected(item);
			}

			DatabaseHelper.getInstance(getActivity());
			if(timeTag.getID() == -1){
				DatabaseHelper.insertTag(timeTag);
			}else{
				DatabaseHelper.updateTag(timeTag);
			}
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

	@Override
	public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		curEdit.setText(sdf.format(cal.getTime()));
	}
}

