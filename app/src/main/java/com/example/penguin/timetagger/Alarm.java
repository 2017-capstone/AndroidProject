package com.example.penguin.timetagger;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Timestamp;

/**
 * Created by penguin on 17. 6. 14.
 */

public class Alarm implements Parcelable {
	private int alarmID = -1;
	private int noteID;
	private int before;
	private Timestamp alarmTime;
	private boolean snoozeOn = false;

	public int getAlarmID(){return alarmID;}
	public int getNoteID(){return noteID;}
	public int getBefore(){return before;}
	public Timestamp getAlarmTime(){return alarmTime;}
	public int getSnooze(){return snoozeOn? 1:0;}
	public void setBefore(int type){before = type;}
	public void setAlarmTime(Timestamp time){alarmTime = time;}
	public void setAlarmTime(long time){alarmTime = new Timestamp(time);}
	public void setSnoozeOn(boolean b){snoozeOn = b;}

	public boolean isSnoozeOn(){return snoozeOn;}

	protected Alarm(Parcel in) {
		readFromParcel(in);
	}

	public Alarm(int nID, int b, Long aTime, boolean s){
		noteID = nID;
		before = b;
		alarmTime = new Timestamp(aTime);
		snoozeOn = s;
	}

	public Alarm(int aID, int nID, int b, Long aTime, boolean s){
		alarmID = aID;
		noteID = nID;
		before = b;
		alarmTime = new Timestamp(aTime);
		snoozeOn = s;
	}
	/* TODO: 생성자는 필요에 따라서 만들기 */
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(alarmID);
		dest.writeInt(noteID);
		dest.writeInt(before);
		dest.writeLong(alarmTime.getTime());
		dest.writeInt((snoozeOn)?1:0);
	}

	public void readFromParcel(Parcel in){
		alarmID = in.readInt();
		noteID = in.readInt();
		before = in.readInt();
		alarmTime.setTime(in.readLong());
		snoozeOn = in.readInt() != 0;
	}

	public static final Creator<Alarm> CREATOR = new Creator<Alarm>() {
		@Override
		public Alarm createFromParcel(Parcel in) {
			return new Alarm(in);
		}

		@Override
		public Alarm[] newArray(int size) {
			return new Alarm[size];
		}
	};
}
