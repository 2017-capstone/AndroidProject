package com.example.penguin.timetagger.Fragments;

import android.support.v4.app.Fragment;

/**
 * Created by penguin on 17. 5. 15.
 */

import android.os.Bundle;
import android.support.annotation.*;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.penguin.timetagger.R;
import com.example.penguin.timetagger.TimeTag;

public class TagFragment extends Fragment {
	ActionBar toolbar;
	TimeTag timeTag;

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
		View view = inflater.inflate(R.layout.content_tag, container, false);
		Bundle bundle = this.getArguments();
		if(bundle != null) {
			timeTag = bundle.getParcelable("TAG");
		}
		EditText et = (EditText)getActivity().findViewById(R.id.tagName);
		et.setText(timeTag.getTag(), TextView.BufferType.EDITABLE);

		et = (EditText)view.findViewById(R.id.tagBegin);
		et.setText(timeTag.getStart().toString(), TextView.BufferType.EDITABLE);

		et = (EditText)view.findViewById(R.id.tagEnd);
		et.setText(timeTag.getEnd().toString(), TextView.BufferType.EDITABLE);
		//setHasOptionsMenu(true);
		return view;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
		inflater.inflate(R.menu.noteview_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

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

