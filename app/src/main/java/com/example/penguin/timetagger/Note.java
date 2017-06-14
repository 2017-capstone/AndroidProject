package com.example.penguin.timetagger;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.LinkedList;
import java.util.List;

import java.sql.Timestamp;

/**
 * Created by penguin on 17. 4. 30.
 */

public class Note implements Parcelable {
    /* TODO: 데이터 베이스에 설계에 맞게 클래스를 생성할 것 */
    private int note_id = -1;
    private int tag_id = 0;
    private String title;
    private String body;
    private List<Attachment> attaches = new LinkedList<>();
    private int type = 0;   // 0 : text
                            // 1 : photo
                            // 2 : record
    private List<Alarm> alarms = new LinkedList<>();

    public Note(){note_id = -1;}
    public Note(String t, String b){note_id = -1; tag_id = 0; title = t; body = b; type = 0;}
    public Note(int ni, int ti, String t, String b){note_id=ni; tag_id=ti; title = t; body = b; type = 0;}
    public Note(int ni, int ti, String t, String b, List<Attachment> a){note_id=ni; tag_id=ti; title = t; body = b; attaches = a; type = 1;}
    public int getNoteID(){return note_id;}
    public int getTagID(){return tag_id;}
    public String getTitle(){return title;}
    public String getBody(){return body;}
    public List<Attachment> getAttaches(){return attaches;}
    public int getType(){return type;}
    public List<Alarm> getAlarms(){return alarms;}

    public void setNoteID(int id){note_id = id;}
    public void setTagID(int id){tag_id = id;}
    public void setTitle(String t){title=t;}
    public void setBody(String b){body=b;}
    public void setAttaches(List<Attachment> a){attaches=a;if(a.size()>0) type = 1;}
    public void setType(int t){type=t;}
    public void setAlarms(List<Alarm> alarms){this.alarms = alarms;}

    public Note(Parcel in){
        readFromParcel(in);
    }
    public Note(Note n){
        note_id = -1;
        title = n.getTitle();
        body = n.getBody();
        attaches = n.getAttaches();
        type = n.getType();
        alarms = n.getAlarms();
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
        dest.writeInt(attaches.size());
        for(int i=0; i<attaches.size(); i++)
            dest.writeParcelable(attaches.get(i), flags);
        dest.writeInt(type);
        dest.writeInt(alarms.size());
        for(int i=0; i<alarms.size(); i++)
            dest.writeParcelable(alarms.get(i), flags);
    }

    private void readFromParcel(Parcel in){
        note_id = in.readInt();
        tag_id = in.readInt();
        title = in.readString();
        body = in.readString();
        int size = in.readInt();
        for(int i=0; i<size; i++) {
            Attachment a = in.readParcelable(Attachment.class.getClassLoader());
            attaches.add(a);
        }
        type = in.readInt();
        size = in.readInt();
        for(int i=0; i<size; i++) {
            Alarm a = in.readParcelable(Alarm.class.getClassLoader());
            alarms.add(a);
        }
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
