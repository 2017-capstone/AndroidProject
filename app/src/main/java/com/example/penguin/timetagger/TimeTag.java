package com.example.penguin.timetagger;

/**
 * Created by penguin on 17. 5. 15.
 */

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

public class TimeTag implements Parcelable {
	/* TODO: 데이터 베이스에 설계에 맞게 클래스를 생성할 것 */
	private int tag_id;
	private String tag;
	private Timestamp start;
	private Timestamp end;
	private List<TimeTable> times;

	public TimeTag(){}
	public TimeTag(String t){tag = t;}
	public TimeTag(String t, Timestamp t1, Timestamp t2){tag = t; start = t1; end = t2;}
	public TimeTag(String t, Timestamp t1, Timestamp t2, List<TimeTable> tt){tag=t;start=t1;end=t2;times=tt;}

	public int getID(){return tag_id;}
	public String getTag(){return tag;}
	public Timestamp getStart(){return start;}
	public Timestamp getEnd(){return end;}
	public void setID(int i){tag_id = i;}
	public void setTag(String t){tag = t;}
	public void setStart(Timestamp t){start = t;}
	public void setEnd(Timestamp t){end = t;}
    public void setTimes(int Time_id, int Tag_id, Timestamp s, Timestamp e){times.set(times.size(), new TimeTable(Time_id, Tag_id, s, e));}
    public int getListSize(){return times.size();}
    public int getListItemTimeID(int index){return times.get(index).getTimeID();}
    public int getListItemTagID(int index){return times.get(index).getTagID();}
    public Timestamp getListItemStart(int index){return times.get(index).getStart();}
    public Timestamp getListItemEnd(int index){return times.get(index).getEnd();}

	public TimeTag(Parcel in){readFromParcel(in);}
	public TimeTag(TimeTag t){
		tag_id = t.getID();
		tag = t.getTag();
		start = t.getStart();
		end = t.getEnd();
	}

	@Override
	public int describeContents(){
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags){
		dest.writeInt(tag_id);
		dest.writeString(tag);
		dest.writeLong(start.getTime());
		dest.writeLong(end.getTime());
	}

	private void readFromParcel(Parcel in){
		tag_id = in.readInt();
		tag = in.readString();
		start.setTime(in.readLong());
		end.setTime(in.readLong());
	}

	public  static final Creator<TimeTag> CREATOR = new Creator<TimeTag>() {
		@Override
		public TimeTag createFromParcel(Parcel in){
			return new TimeTag(in);
		}
		@Override
		public TimeTag[] newArray(int size){
			return new TimeTag[size];
		}
	};
}

class TimeTable{
	private int time_id;
	private int tag_id;
	private Timestamp start;
	private Timestamp end;

	public TimeTable(){}
	public TimeTable(Timestamp t1, Timestamp t2){
		this.time_id = -1;
		this.tag_id = -1;
		start = t1;
		end = t2;
	}
	public TimeTable(int tag_id, Timestamp t1, Timestamp t2){
		this.time_id = -1;
		this.tag_id=tag_id;
		start = t1;
		end = t2;
	}
	public TimeTable(int time_id, int tag_id, Timestamp t1, Timestamp t2){
		this.time_id = time_id;
		this.tag_id = -1;
		start = t1;
		end = t2;}

	public int getTimeID(){return this.time_id;}
	public int getTagID(){return this.tag_id;}
	public Timestamp getStart(){return start;}
	public Timestamp getEnd(){return end;}
	public void setTimeID(int i){time_id=i;}
	public void setTagID(int i){tag_id=i;}
	public void setStart(Timestamp t){start = t;}
	public void setEnd(Timestamp t){end = t;}
}