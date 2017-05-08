package com.example.penguin.timetagger;

import java.io.Serializable;

/**
 * Created by penguin on 17. 4. 30.
 */

@SuppressWarnings("serial")
public class Note implements Serializable{
    /* TODO: 데이터 베이스에 설계에 맞게 클래스를 생성할 것 */
    public String title;
    public String body;
    public int id;

    public Note(){}
    public Note(String t, String b){title = t; body = b; id=-1;}
    public String getTitle(){return title;}
    public String getBody(){return body;}
}
