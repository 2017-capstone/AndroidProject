package com.example.penguin.timetagger;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by penguin on 17. 6. 14.
 */
// TODO: 구현을 미룰 것
public class Attachment implements Parcelable {
	private int attachID = -1;
	private int noteID;
	private String attach;
	private int position;

	protected Attachment(Parcel in) {readFromParcel(in);}
	public Attachment(int nID, String a){noteID=nID; attach=a;}
	public Attachment(int aID, int nID, String a){attachID=aID; noteID=nID; attach=a;}

	public static final Creator<Attachment> CREATOR = new Creator<Attachment>() {
		@Override
		public Attachment createFromParcel(Parcel in) {
			return new Attachment(in);
		}

		@Override
		public Attachment[] newArray(int size) {
			return new Attachment[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
	}

	public void readFromParcel(Parcel in){

	}

	public int getAttachID() {
		return attachID;
	}

	public void setAttachID(int attachID) {
		this.attachID = attachID;
	}

	public int getNoteID() {
		return noteID;
	}

	public void setNoteID(int noteID) {
		this.noteID = noteID;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}
}
