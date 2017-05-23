package com.example.penguin.timetagger;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by penguin on 17. 4. 30.
 */

public class Note implements Parcelable {
    /* TODO: 데이터 베이스에 설계에 맞게 클래스를 생성할 것 */
    private int note_id;
    private int tag_id;
    private String title;
    private String body;

    public Note(){note_id = -1;}
    public Note(String t, String b){note_id = -1; tag_id = 0; title = t; body = b;}
    public Note(int ni, int ti, String t, String b){note_id=ni; tag_id=ti; title = t; body = b;}
    public int getNoteID(){return note_id;}
    public int getTagID(){return tag_id;}
    public String getTitle(){return title;}
    public String getBody(){return body;}

    public void setNoteID(int id){note_id = id;}
    public void setTagID(int id){tag_id = id;}
    public void setTitle(String t){title=t;}
    public void setBody(String b){body=b;}

    public Note(Parcel in){
        readFromParcel(in);
    }
    public Note(Note n){
        note_id = -1;
        title = n.getTitle();
        body = n.getBody();
    }
    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){
        dest.writeInt(note_id);
        dest.writeInt(tag_id);
        dest.writeString(title);
        dest.writeString(body);
    }

    private void readFromParcel(Parcel in){
        note_id = in.readInt();
        tag_id = in.readInt();
        title = in.readString();
        body = in.readString();
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
