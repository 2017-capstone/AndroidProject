package com.example.penguin.timetagger;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * Created by penguin on 17. 5. 20.
 */

public class TimeTable implements Parcelable{
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	private int time_id;
	private int tag_id;
	private Timestamp start;
	private Timestamp end;
	private byte weekly;

	public TimeTable(){this.time_id=-1;}
	public TimeTable(Timestamp t1, Timestamp t2){
		this.time_id = -1;
		this.tag_id = -1;
		start = t1;
		end = t2;
		weekly = 1;
	}
	public TimeTable(int tag_id, Timestamp t1, Timestamp t2){
		this.time_id = -1;
		this.tag_id=tag_id;
		start = t1;
		end = t2;
		weekly = 1;
	}
	public TimeTable(int time_id, int tag_id, Timestamp t1, Timestamp t2){
		this.time_id = time_id;
		this.tag_id = -1;
		start = t1;
		end = t2;
		weekly = 1;}
	public TimeTable(int i, int i2, long t1, long t2){time_id=i; tag_id=i2; start=new Timestamp(t1); end=new Timestamp(t2);}

	public TimeTable(Parcel in) { readFromParcel(in);}

	public static final Creator<TimeTable> CREATOR = new Creator<TimeTable>() {
		@Override
		public TimeTable createFromParcel(Parcel in) {
			return new TimeTable(in);
		}

		@Override
		public TimeTable[] newArray(int size) {
			return new TimeTable[size];
		}
	};

	public int getTimeID(){return this.time_id;}
	public int getTagID(){return this.tag_id;}
	public Timestamp getStart(){return start;}
	public Timestamp getEnd(){return end;}
	public byte getWeekly(){return weekly;}
	public void setTimeID(int i){time_id=i;}
	public void setTagID(int i){tag_id=i;}
	public void setStart(Timestamp t){start = t;}
	public void setEnd(Timestamp t){end = t;}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(time_id);
		dest.writeInt(tag_id);
		dest.writeLong(start.getTime());
		dest.writeLong(end.getTime());
	}

	public void readFromParcel(Parcel in){
		time_id = in.readInt();
		tag_id = in.readInt();
		start = new Timestamp(in.readLong());
		end = new Timestamp(in.readLong());
	}
}
