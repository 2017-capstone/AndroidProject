package com.example.penguin.timetagger;

import android.os.Parcel;
import android.os.Parcelable;
import android.net.Uri;

import java.sql.Timestamp;

/**
 * Created by penguin on 17. 4. 30.
 */

public class Note implements Parcelable {
    /* TODO: 데이터 베이스에 설계에 맞게 클래스를 생성할 것 */
    private int note_id;
    private int tag_id;
    private String title;
    private String body;
    private String photo;
    private int type; // 0 : text
                      // 1 : photo
                      // 2 : record
    private Timestamp alarm;

    public Note(){note_id = -1; alarm.setTime(0L);}
    public Note(String t, String b){note_id = -1; tag_id = 0; title = t; body = b; type = 0; alarm.setTime(0L);}
    public Note(String t, String b, Timestamp a){note_id = -1; tag_id = 0; title = t; body = b; type = 0; alarm = a;}
    public Note(int ni, int ti, String t, String b){note_id=ni; tag_id=ti; title = t; body = b; type = 0; alarm.setTime(0L);}
    public Note(int ni, int ti, String t, String b, Timestamp a){note_id=ni; tag_id=ti; title = t; body = b; type = 0; alarm = a;}
    public Note(int ni, int ti, String t, String b, String p){note_id=ni; tag_id=ti; title = t; body = b; photo = p; type = 1; alarm.setTime(0L);}
    public Note(int ni, int ti, String t, String b, String p, Timestamp a){note_id=ni; tag_id=ti; title = t; body = b; photo = p; type = 1; alarm = a;}
    public int getNoteID(){return note_id;}
    public int getTagID(){return tag_id;}
    public String getTitle(){return title;}
    public String getBody(){return body;}
    public String getPhoto(){return photo;}
    public int getType(){return type;}
    public Timestamp getAlarm(){return alarm;}

    public void setNoteID(int id){note_id = id;}
    public void setTagID(int id){tag_id = id;}
    public void setTitle(String t){title=t;}
    public void setBody(String b){body=b;}
    public void setPhotoDir(String p){photo=p;}
    public void setType(int t){type=t;}
    public void setAlarm(Timestamp a){alarm = a;}

    public Note(Parcel in){
        readFromParcel(in);
    }
    public Note(Note n){
        note_id = -1;
        title = n.getTitle();
        body = n.getBody();
        photo = n.getPhoto();
        type = n.getType();
        alarm = n.getAlarm();
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
        dest.writeString(photo);
        dest.writeInt(type);
        dest.writeLong(alarm.getTime());
    }

    private void readFromParcel(Parcel in){
        note_id = in.readInt();
        tag_id = in.readInt();
        title = in.readString();
        body = in.readString();
        photo = in.readString();
        type = in.readInt();
        alarm.setTime(in.readLong());
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
