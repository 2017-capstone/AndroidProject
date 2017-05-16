package com.example.penguin.timetagger;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by penguin on 17. 4. 30.
 */

public class Note implements Parcelable {
    /* TODO: 데이터 베이스에 설계에 맞게 클래스를 생성할 것 */
    private int tag;
    private String title;
    private String body;
    private int id;

    public Note(){}
    public Note(String t, String b){tag = 0; title = t; body = b; id=-1;}
    public Note(int _t, String t, String b, int i){tag=_t; title = t; body = b; id = i;}
    public int getTag(){return tag;}
    public String getTitle(){return title;}
    public String getBody(){return body;}
    public int getID(){return id;}
    public void setID(int id){this.id = id;}

    public Note(Parcel in){
        readFromParcel(in);
    }
    public Note(Note n){
        this.title = n.getTitle();
        this.body = n.getBody();
        this.id = -1;
    }
    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){
        dest.writeInt(tag);
        dest.writeString(title);
        dest.writeString(body);
        dest.writeInt(id);
    }

    private void readFromParcel(Parcel in){
        tag = in.readInt();
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
