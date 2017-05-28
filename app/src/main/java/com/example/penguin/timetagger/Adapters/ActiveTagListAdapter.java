package com.example.penguin.timetagger.Adapters;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.penguin.timetagger.Database.DatabaseHelper;
import com.example.penguin.timetagger.Fragments.TagFragment;
import com.example.penguin.timetagger.R;
import com.example.penguin.timetagger.TimeTag;

import java.util.List;

/**
 * Created by penguin on 17. 5. 28.
 */


public class ActiveTagListAdapter extends RecyclerView.Adapter<ActiveTagListAdapter.ActiveTagListViewHolder> {
	private Context context;
	private List<TimeTag> timeTagItems;
	public class ActiveTagListViewHolder extends RecyclerView.ViewHolder{
		TextView v_tag;
		TextView v_duration;
		CardView cv;

		public ActiveTagListViewHolder(View v){
			super(v);
			v_tag = (TextView) v.findViewById(R.id.tagTag);
			v_duration = (TextView) v.findViewById(R.id.tagDuration);
			cv = (CardView)v.findViewById(R.id.tagItem);
		}
	}

	@Override
	public ActiveTagListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		// create a new view
		View v = LayoutInflater
				.from(parent.getContext())
				.inflate(R.layout.item_tag, parent, false);
		return new ActiveTagListViewHolder(v);
	}

	public ActiveTagListAdapter(Context context){
		this.context = context;
		timeTagItems = DatabaseHelper.selectAllTags();
	}

	@Override
	public void onBindViewHolder(final ActiveTagListViewHolder holder, final int position) {
		TimeTag tt = timeTagItems.get(0);
		String tmpstr = timeTagItems.get(position).getTag();
		holder.v_tag.setText(tmpstr);

		String duration = timeTagItems.get(position).getStart().toString() +
				"~" + timeTagItems.get(position).getEnd().toString();
		holder.v_duration.setText(duration);

        /* 클릭 함수 */
		holder.cv.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				TimeTag timeTag = timeTagItems.get(position);
				TagFragment fragment = TagFragment.newInstance(timeTag);

				((FragmentActivity)context).getSupportFragmentManager()
						.beginTransaction()
						.replace(R.id.frame_content, fragment)
						.addToBackStack(null)
						.commit();
			}
		});
	}

	@Override
	public int getItemCount() {
		return timeTagItems.size();
	}
}