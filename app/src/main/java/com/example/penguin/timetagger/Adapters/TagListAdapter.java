package com.example.penguin.timetagger.Adapters;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.penguin.timetagger.Fragments.TagFragment;
import com.example.penguin.timetagger.TimeTag;
import com.example.penguin.timetagger.R;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

/*
 * Created by penguin on 17. 5. 16.
 */

public class TagListAdapter extends RecyclerView.Adapter<TagListAdapter.TagListViewHolder> {
	private Context context;
	private List<TimeTag> timeTagItems;
	public class TagListViewHolder extends RecyclerView.ViewHolder{
		/* TODO: 데이터 베이스 설계에 맞게 수정됨 1 */
		TextView v_tag;
		TextView v_duration;
		CardView cv;

		public TagListViewHolder(View v){
			super(v);
            /* TODO: 데이터 베이스 설계에 맞게 수정됨 2 */
			v_tag = (TextView) v.findViewById(R.id.tagTag);
			v_duration = (TextView) v.findViewById(R.id.tagDuration);
			cv = (CardView)v.findViewById(R.id.tagItem);
		}
	}

	@Override
	public TagListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		// create a new view
		View v = LayoutInflater
				.from(parent.getContext())
				.inflate(R.layout.item_tag, parent, false);
		return new TagListViewHolder(v);
	}

	public TagListAdapter(Context context){
		this.context = context;
		List<TimeTag> t2 = Arrays.asList(
				new TimeTag("캡스톤 프로젝트", new Timestamp(1488412800), new Timestamp(1498262400)));
		timeTagItems = t2;

		//this.timeTagItems = DatabaseHelper.selectTag();
	}

	@Override
	public void onBindViewHolder(final TagListViewHolder holder, final int position) {
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

