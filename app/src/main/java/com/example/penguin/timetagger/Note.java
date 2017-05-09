package com.example.penguin.timetagger;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by penguin on 17. 4. 30.
 */

public class Note implements Parcelable {
    /* TODO: 데이터 베이스에 설계에 맞게 클래스를 생성할 것 */
    public String title;
    public String body;
    public int id;

    public Note(){}
    public Note(String t, String b){title = t; body = b; id=-1;}
    public String getTitle(){return title;}
    public String getBody(){return body;}

    public Note(Parcel in){
        readFromParcel(in);
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){
        dest.writeString(title);
        dest.writeString(body);
        dest.writeInt(id);
    }

    private void readFromParcel(Parcel in){
        title = in.readString();
        body = in.readString();
        id = in.readInt();
    }

    public  static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in){
            return new Note(in);
        }
        @Override
        public Note[] newArray(int size){
            return new Note[size];
        }
    };
}
