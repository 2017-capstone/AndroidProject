package com.example.penguin.timetagger;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * Created by penguin on 17. 5. 20.
 */

public class TimeTable{
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
	public int getTimeID(){return this.time_id;}
	public int getTagID(){return this.tag_id;}
	public Timestamp getStart(){return start;}
	public Timestamp getEnd(){return end;}
	public byte getWeekly(){return weekly;}
	public void setTimeID(int i){time_id=i;}
	public void setTagID(int i){tag_id=i;}
	public void setStart(Timestamp t){start = t;}
	public void setEnd(Timestamp t){end = t;}
	public void setWeekly(byte w){weekly = w;}
}
