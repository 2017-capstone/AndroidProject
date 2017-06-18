package com.example.penguin.timetagger.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.penguin.timetagger.Alarm;
import com.example.penguin.timetagger.Attachment;
import com.example.penguin.timetagger.Note;
import com.example.penguin.timetagger.TimeTable;
import com.example.penguin.timetagger.TimeTag;
//import com.example.penguin.timetagger.TimeTable;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by penguin on 17. 5. 15.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "TimeTaggerDB.db";
	private static final int DATABASE_VERSION = 1;
	private static DatabaseHelper instance = null;
	private static boolean dummyNoteLoaded = false;
	private static final String  TIMETABLES_NAME = "times";
	private static final String  TAGSTABLE_NAME = "tags";
	private static final String  NOTESTABLE_NAME = "notes";
	private static final String  ATTACHESTABLE_NAME = "attaches";
	private static final String ALARMTABLE_NAME = "alarms";

	private DatabaseHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public static synchronized DatabaseHelper getInstance(Context context){

		if(instance == null)
			instance = new DatabaseHelper(context);
		return instance;
	}

	/* NOTES */
	public static synchronized Note insertNote(Note note){
		String query =  " INSERT INTO "     + NOTESTABLE_NAME   +
						" values(NULL,"     + note.getTagID()   + ",'"
										    + note.getTitle()   + "','"
											+ note.getBody()	+ "',"
											+ note.getType()    + ");";

		SQLiteDatabase db = instance.getWritableDatabase();
		db.execSQL(query);

		Cursor cursor = db.rawQuery("SELECT MAX(NOTE_ID) from notes", null);
		cursor.moveToFirst();
		note.setNoteID(cursor.getInt(0));
		cursor.close();

		// Attachment
		insertAttaches(note.getAttaches());
    // Alarm
		insertAlarms(note.getAlarms());
		return note;
	}
	public static synchronized void updateNote(Note note){
		// noteID == 1 일땐 새로운 노트이므로, 업데이트 할 수 없음
		if(note.getNoteID() == -1) return;
		String query =  " UPDATE "          + NOTESTABLE_NAME   +
						" SET TAG_ID = "    + note.getTagID()   + "," +
						" TITLE = '"        + note.getTitle()   + "'," +
						" BODY = '"         + note.getBody()    + "'," +
						" TYPE = "  		+ note.getType()	+
						" WHERE NOTE_ID = " + note.getNoteID()  + ";";

		SQLiteDatabase db = instance.getWritableDatabase();
		db.execSQL(query);

		// TYPE 1: photo 노트
		for(Attachment attach : note.getAttaches()){
			if(attach.getAttachID() < 0)
				insertAttach(attach);
			else
				updateAttach(attach);
		}

		// ALARM 처리
		for (Alarm alarm : note.getAlarms()) {
			if (alarm.getAlarmID() < 0)
				insertAlarm(alarm);
			else
				updateAlarm(alarm);
		}
	}
	public static synchronized List<Note> selectAllNotes(){
		String query;
		query = " SELECT * FROM "   + NOTESTABLE_NAME   +
				" LEFT OUTER JOIN " + ALARMTABLE_NAME   +
				" ON "              + NOTESTABLE_NAME   + ".NOTE_ID" +
				" = "               + ALARMTABLE_NAME   + ".NOTE_ID" +
				" GROUP BY "        + NOTESTABLE_NAME   + ".NOTE_ID;";

		SQLiteDatabase db = instance.getReadableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		SQLiteDatabase datadb = instance.getReadableDatabase();
		Cursor datacursor;

		List<Note> notes = new LinkedList<>();
		List<Alarm> alarms;
		int tmpID;
		if(cursor.moveToFirst()){
			for(int i=0; !cursor.isAfterLast(); i++){
				tmpID = cursor.getInt(0);
				int tagID = cursor.getInt(1);
				Note note = new Note(tmpID, tagID,
						cursor.getString(2), cursor.getString(3));

				// attachment 관리
				note.setAttaches(selectAttachs(tmpID));

				// alarm 관리
				alarms = new LinkedList<>();
				if(cursor.moveToFirst())
				while(!cursor.isAfterLast()){
					if(tmpID != cursor.getInt(0)) {
						note.setAlarms(alarms);
						break;
					}
					if(cursor.getInt(5) != 0){
						alarms.add(new Alarm(cursor.getInt(5), cursor.getInt(6),
								cursor.getInt(7), cursor.getLong(8), cursor.getInt(9) != 0));
					}
					cursor.moveToNext();
				}
				notes.add(note);
			}
		}
		cursor.close();
		return notes;
	}
	public static synchronized List<Note> selectNotes(int tag_id){
		String query;
		query = " SELECT * FROM "   + NOTESTABLE_NAME   +
				" LEFT OUTER JOIN " + ALARMTABLE_NAME   +
				" ON "              + NOTESTABLE_NAME   + ".NOTE_ID" +
				" = "               + ALARMTABLE_NAME   + ".NOTE_ID" +
				" WHERE "           + "TAG_ID" +
				" = "               + tag_id            +
				" GROUP BY "        + NOTESTABLE_NAME   + ".NOTE_ID" +";";

		SQLiteDatabase db = instance.getReadableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		SQLiteDatabase datadb = instance.getReadableDatabase();
		Cursor datacursor;

		List<Note> notes = new LinkedList<>();
		List<Alarm> alarms;
		int tmpID;
		if(cursor.moveToFirst()){
			for(int i=0; !cursor.isAfterLast(); i++){
				tmpID = cursor.getInt(0);
				int tagID = cursor.getInt(1);
				Note note = new Note(tmpID, tagID,
						cursor.getString(2), cursor.getString(3));

				// attachment 관리
				note.setAttaches(selectAttachs(tmpID));
				// alarm 관리
				alarms = new LinkedList<>();
				while(!cursor.isAfterLast()){
					if(tmpID != cursor.getInt(0)) {
						note.setAlarms(alarms);
						break;
					}
					if(cursor.getInt(5) != 0){
						alarms.add(new Alarm(cursor.getInt(5), cursor.getInt(6),
								cursor.getInt(7), cursor.getLong(8), cursor.getInt(9) != 0));
					}
					cursor.moveToNext();
				}
				notes.add(note);
			}
		}
		cursor.close();
		return notes;
	}
	public static synchronized Note selectNote(int note_id){
		/* TODO: TAG is null 에 대해 고민 할 것 */
		String query;
		query = " SELECT * FROM "   + NOTESTABLE_NAME   +
				" LEFT OUTER JOIN " + ALARMTABLE_NAME   +
				" ON "              + NOTESTABLE_NAME   + ".NOTE_ID" +
				" = "               + ALARMTABLE_NAME   + ".NOTE_ID" +
				" WHERE "           + NOTESTABLE_NAME   + ".NOTE_ID" +
				" = "               + note_id           +
				" GROUP BY "        + NOTESTABLE_NAME   + ".NOTE_ID" + ";";

		SQLiteDatabase db = instance.getReadableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		SQLiteDatabase datadb = instance.getReadableDatabase();
		Cursor datacursor;

		if(cursor.moveToFirst()) {
			Note note = new Note(cursor.getInt(0), cursor.getInt(1),
					cursor.getString(2), cursor.getString(3));

			// attachment 관리
			note.setAttaches(selectAttachs(cursor.getInt(0)));

			// alarm 관리
			List<Alarm> alarms = new LinkedList<>();
			while (!cursor.isAfterLast()) {
				if (cursor.getInt(5) != 0) {
					alarms.add(new Alarm(cursor.getInt(5), cursor.getInt(6),
							cursor.getInt(7), cursor.getLong(8), cursor.getInt(9) != 0));
				}
				cursor.moveToNext();
			}
			note.setAlarms(alarms);
			return note;
		}
		cursor.close();
		return null;
	}
	public static synchronized void deleteNote(int note_id){
		String DELETE_NOTE =
				" DELETE FROM "     + NOTESTABLE_NAME   +
				" WHERE NOTE_ID = " + note_id 			+";";
		String DELETE_ATTACH =
				" DELETE FROM "		+ ATTACHESTABLE_NAME+
				" WHERE NOTE_ID = " + note_id			+ ";";
		String DELETE_ALARM =
				" DELETE FROM "		+ ALARMTABLE_NAME	+
				" WHERE NOTE_ID = " + note_id			+ ";";
		SQLiteDatabase db = instance.getWritableDatabase();
		db.execSQL(DELETE_NOTE);
		db = instance.getWritableDatabase();
		db.execSQL(DELETE_ATTACH);
		db = instance.getWritableDatabase();
		db.execSQL(DELETE_ALARM);
	}
	public static synchronized void deleteNotes(List<Integer> note_ids){
		String query;
		SQLiteDatabase db;

		for(Integer noteID : note_ids) {
			String DELETE_NOTE =
					" DELETE FROM "     + NOTESTABLE_NAME   +
					" WHERE NOTE_ID = " + noteID	        +";";
			String DELETE_ATTACH =
					" DELETE FROM "		+ ATTACHESTABLE_NAME+
					" WHERE NOTE_ID = " + noteID	        + ";";
			String DELETE_ALARM =
					" DELETE FROM "		+ ALARMTABLE_NAME	+
					" WHERE NOTE_ID = " + noteID	        + ";";
			db = instance.getWritableDatabase();
			db.execSQL(DELETE_NOTE);
			db = instance.getWritableDatabase();
			db.execSQL(DELETE_ATTACH);
		}
	}

	/* TAGS */
	public static synchronized void insertTag(TimeTag tag){
		String query =  " INSERT INTO "     + TAGSTABLE_NAME            +
						" values("    		+ " NULL "                  + ",'"
											+ tag.getTag()              + "',"
											+ tag.getStart().getTime()  + ","
                                            + tag.getEnd().getTime()    + ");";

		SQLiteDatabase db = instance.getWritableDatabase();
		db.execSQL(query);

		Cursor cursor = db.rawQuery("SELECT MAX(NOTE_ID) from notes", null);
		cursor.moveToFirst();
		tag.setID(cursor.getInt(0));

		for (TimeTable t : tag.getTimes()) {
			query = " INSERT INTO " + TIMETABLES_NAME   +
					" values("      + "NULL "           + ","
									+ tag.getID()       + ","
									+ t.getStart().getTime() + ","
									+ t.getEnd().getTime() + ");";
			db.execSQL(query);
		}
	}

    public static synchronized void updateTag(TimeTag tag){
        String query =  " UPDATE "      + TAGSTABLE_NAME 	   		+
                		" SET TAG = '"      + tag.getTag()      		+ "',"  +
            		    " LOOP_START = "    + tag.getStart().getTime()	+ ","   +
                		" LOOP_END = "      + tag.getEnd().getTime()	+
                		" WHERE TAG_ID = "  + tag.getID()       		+ ";";

        SQLiteDatabase db = instance.getWritableDatabase();
        db.execSQL(query);

        ListIterator<TimeTable> iter = tag.getTimes().listIterator();
        while(iter.hasNext()){
            TimeTable t = iter.next();
	        if(t.getTimeID()==-1){
		        query = " INSERT INTO " + TIMETABLES_NAME +
						        " values("      + "NULL" + ","
						        + tag.getID()  + ","
						        + t.getStart().getTime() + ","
						        + t.getEnd().getTime() + ");";
	        }else {
		        query = " UPDATE " + TIMETABLES_NAME +
				        " SET TAG_ID = " + t.getTagID() + "," +
				        " START = " + t.getStart().getTime() + "," +
				        " END = " + t.getEnd().getTime() +
				        " WHERE TIME_ID = " + t.getTimeID() + ";";
	        }
            db = instance.getWritableDatabase();
            db.execSQL(query);

        }

        return;
    }

	public static synchronized List<TimeTag> selectAllTags(){
		String query;
		query = " SELECT * FROM "   + TAGSTABLE_NAME    +
				" LEFT OUTER JOIN " + TIMETABLES_NAME   +
				" on "              + TAGSTABLE_NAME    + ".TAG_ID" +
				"="                 + TIMETABLES_NAME   + ".TAG_ID" +
				" ORDER BY "        + TAGSTABLE_NAME    + ".TAG_ID;";

		SQLiteDatabase db = instance.getReadableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		List<TimeTag> timeTags = new LinkedList<>();
		List<TimeTable> timeTables;
		int tmp_id;
		if(cursor.moveToFirst()) {
			for (int i = 0; !cursor.isAfterLast(); i++) {
				timeTags.add(new TimeTag(cursor.getInt(0), cursor.getString(1), cursor.getLong(2), cursor.getLong(3)));
				tmp_id = cursor.getInt(0);

				timeTables = new LinkedList<>();
				while (!cursor.isAfterLast()) {
					if (tmp_id != cursor.getInt(0)) {
						timeTags.get(i).setTimes(timeTables);
						break;
					}
					if (cursor.getInt(5) != 0)
						timeTables.add(new TimeTable(cursor.getInt(4), cursor.getInt(5), cursor.getLong(6), cursor.getLong(7)));
					cursor.moveToNext();
				}
				timeTags.get(i).setTimes(timeTables);
			}
		}
		cursor.close();
		return timeTags;
	}
	public static synchronized TimeTag selectTag(int tag_id){
		String query;
		query = " SELECT * FROM "   + TAGSTABLE_NAME    +
				" LEFT OUTER JOIN " + TIMETABLES_NAME   +
				" on "              + TAGSTABLE_NAME    + ".TAG_ID" +
				"="                 + TIMETABLES_NAME   + ".TAG_ID" +
				" WHERE "           + TAGSTABLE_NAME   +
				".TAG_ID = "        + tag_id            +
				" GROUP BY "        + TAGSTABLE_NAME    + ".TAG_ID;";

		SQLiteDatabase db = instance.getReadableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		TimeTag timeTag = null;
		if(cursor.moveToFirst()) {
			timeTag = new TimeTag(cursor.getInt(0), cursor.getString(1), cursor.getLong(2), cursor.getLong(3));
			List<TimeTable> timeTables = new LinkedList<>();

			while (!cursor.isAfterLast()) {
				if (cursor.getInt(5) != 0)
					timeTables.add(new TimeTable(cursor.getInt(4), cursor.getInt(5), cursor.getLong(6), cursor.getLong(7)));
				cursor.moveToNext();
			}
		}
		cursor.close();
		return timeTag;
	}
	public static synchronized TimeTag selectCurrentTag(){
		List<TimeTag> tags = selectAllTags();
		TimeTag currentTag = null;
		for(TimeTag t : tags){
			Long start = t.getStart().getTime();
			Long end = t.getEnd().getTime();
			Long current = Calendar.getInstance().getTimeInMillis();
			if(start <= current && current <= end){
				// 1차 기간 통과
				for(TimeTable tt : t.getTimes()){
					Calendar cal = Calendar.getInstance();
					int h, m;
					cal.setTime(tt.getStart()); //... 시간과 분만 추출
					h = cal.get(Calendar.HOUR_OF_DAY);
					m = cal.get(Calendar.MINUTE);
					cal = Calendar.getInstance();
					cal.set(Calendar.HOUR_OF_DAY, h);
					cal.set(Calendar.MINUTE,m);
					start = cal.getTimeInMillis();

					cal.setTime(tt.getEnd());
					h = cal.get(Calendar.HOUR_OF_DAY);
					m = cal.get(Calendar.MINUTE);
					cal = Calendar.getInstance();
					cal.set(Calendar.HOUR_OF_DAY, h);
					cal.set(Calendar.MINUTE,m);
					end = cal.getTimeInMillis();

					cal = Calendar.getInstance();
					h = cal.get(Calendar.HOUR_OF_DAY);
					m = cal.get(Calendar.MINUTE);
					cal = Calendar.getInstance();
					cal.set(Calendar.HOUR_OF_DAY, h);
					cal.set(Calendar.MINUTE,m);
					current = cal.getTimeInMillis();

					if(start <= current && current <= end){
						// 2차 통과
						return t;
					}
				}
			}
		}
		return currentTag;
	}

	/* ATTACHMENT */
	public static synchronized void insertAttach(Attachment attach){
		String query =  " INSERT INTO " + ATTACHESTABLE_NAME+
						" values("      + " NULL "           + "," +
										 attach.getNoteID()  + ",'" +
										 attach.getAttach()+ "');";
		SQLiteDatabase db = instance.getWritableDatabase();
		db.execSQL(query);
	}
	public static synchronized void insertAttaches(List<Attachment> attaches){
		String query = "";
		for(Attachment attach : attaches){
			query +=" INSERT INTO " + ATTACHESTABLE_NAME+
					" values("      + " NULL "           + "," +
									 attach.getNoteID()  + ",'" +
									 attach.getAttach()+ "');";
		}
		if(query.equals(""))
			return;
		SQLiteDatabase db = instance.getWritableDatabase();
		db.execSQL(query);

	}
	public static synchronized Attachment selectAttach(int attachID){
		String query =  " SELECT * FROM "   + ATTACHESTABLE_NAME +
						" WHERE ATTACH_ID=" + attachID           + ";";

		SQLiteDatabase db = instance.getReadableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		if(cursor.moveToFirst()) {
			if (!cursor.isAfterLast())
				return new Attachment(cursor.getInt(0), cursor.getInt(1),
						cursor.getString(2));
		}
		return null;
	}
	public static synchronized List<Attachment> selectAttachs(int noteID){
		String query =  " SELECT * FROM "   + ATTACHESTABLE_NAME +
						" WHERE NOTE_ID="   + noteID             + ";";

		SQLiteDatabase db = instance.getReadableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		List<Attachment> attaches = new LinkedList<>();
		if(cursor.moveToFirst())
		while(!cursor.isAfterLast()) {
			attaches.add(new Attachment(cursor.getInt(0), cursor.getInt(1),
					cursor.getString(2)));
			cursor.moveToNext();
		}
		return attaches;
	}
	public static synchronized List<Attachment> selectAllAttaches(int noteID){
		String query =  " SELECT * FROM "   + ATTACHESTABLE_NAME + ";";

		SQLiteDatabase db = instance.getReadableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		List<Attachment> attaches = new LinkedList<>();
		if(cursor.moveToFirst())
		while(!cursor.isAfterLast()) {
			attaches.add(new Attachment(cursor.getInt(0), cursor.getInt(1),
					cursor.getString(2)));
			cursor.moveToNext();
		}
		return attaches;
	}
	public static synchronized void updateAttach(Attachment attach){
		String query =  " UPDATE " + ATTACHESTABLE_NAME  +
				" SET ATTACH_ID =" + attach.getAttachID()+ "," +
				" NOTE_ID ="       + attach.getNoteID()  + "," +
				" DATA ='"          + attach.getAttach()  + "';";
		SQLiteDatabase db = instance.getWritableDatabase();
		db.execSQL(query);
	}
	public static synchronized void updateAttaches(List<Attachment> attaches){
		String query = "";
		for(Attachment attach : attaches){
			query +=" UPDATE " + ATTACHESTABLE_NAME +
					" SET ATTACH_ID =" + attach.getAttachID() + "," +
					" NOTE_ID =" + attach.getNoteID() + "," +
					" DATA ='" + attach.getAttach() + "';";
		}
		SQLiteDatabase db = instance.getWritableDatabase();
		db.execSQL(query);
	}
	public static synchronized void deleteAttach(Attachment attach){
		String query =
				" DELETE FROM "     + ATTACHESTABLE_NAME  +
				" WHERE ATTACH_ID ="+ attach.getAttachID()+ ";";
		SQLiteDatabase db = instance.getWritableDatabase();
		db.execSQL(query);
	}
	public static synchronized void deleteAttaches(List<Attachment> attaches){
		String query = "";
		for(Attachment attach : attaches){
			query +=" DELETE FROM "     + ATTACHESTABLE_NAME  +
					" WHERE ATTACH_ID ="+ attach.getAttachID()+ ";";
		}
		if(query.equals(""))
			return;
		SQLiteDatabase db = instance.getWritableDatabase();
		db.execSQL(query);
	}

	/* ALARM */
	public static synchronized void insertAlarm(Alarm alarm){
		String query =  " INSERT INTO " + ALARMTABLE_NAME   +
						" values("      + "NULL"            + ","
										+ alarm.getNoteID()+ ","
										+ alarm.getBefore() + ","
										+ alarm.getAlarmTime().getTime() + ","
										+ alarm.getSnooze() + ");";
		SQLiteDatabase db = instance.getWritableDatabase();
		db.execSQL(query);
	}
	public static synchronized void insertAlarms(List<Alarm> alarms){
		String query = "";
		for(Alarm alarm: alarms){
			query +=" INSERT INTO " + ALARMTABLE_NAME   +
					" values("      + "NULL"            + ","
					+ alarm.getNoteID()+ ","
					+ alarm.getBefore() + ","
					+ alarm.getAlarmTime().getTime() + ","
					+ alarm.getSnooze() + ");";
		}
		if(query.equals(""))
			return;
		SQLiteDatabase db = instance.getWritableDatabase();
		db.execSQL(query);
	}
	public static synchronized Alarm selectAlarm(int alarmID){
		String query;
		query = " SELECT * FROM "   + ALARMTABLE_NAME   +
				" WHERE "           + " ALARM_ID "      +
				" = "               + alarmID           + ";";

		SQLiteDatabase db = instance.getReadableDatabase();
		Cursor cursor = db.rawQuery(query, null);
		if(cursor.moveToFirst())
			if(!cursor.isAfterLast())
				return new Alarm(cursor.getInt(0), cursor.getInt(1),
						cursor.getInt(2), cursor.getLong(3),
						cursor.getInt(4) != 0);
		return null;
	}
	public static synchronized List<Alarm> selectAlarms(int noteID){
		String query;
		query = " SELECT * FROM "   + ALARMTABLE_NAME   +
				" WHERE "           + " NOTE_ID "       +
				" = "               + noteID            + ";";

		SQLiteDatabase db = instance.getReadableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		List<Alarm> alarms = new LinkedList<>();
		if(cursor.moveToFirst())
		while(!cursor.isAfterLast()){
			alarms.add(new Alarm(cursor.getInt(0), cursor.getInt(1),
					cursor.getInt(2), cursor.getLong(3),
					cursor.getInt(4) != 0));
			cursor.moveToNext();
		}
		return alarms;
	}
	public static synchronized List<Alarm> selectAllAlarms(){
		String query;
		query = " SELECT * FROM "   + ALARMTABLE_NAME   + ";";

		SQLiteDatabase db = instance.getReadableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		List<Alarm> alarms = new LinkedList<>();
		if(cursor.moveToFirst())
		while(!cursor.isAfterLast()){
			alarms.add(new Alarm(cursor.getInt(0), cursor.getInt(1),
					cursor.getInt(2), cursor.getLong(3),
					cursor.getInt(4) != 0));
			cursor.moveToNext();
		}
		return alarms;
	}
	public static synchronized void updateAlarm(Alarm alarm){
		String query =  " UPDATE "  + ALARMTABLE_NAME      +
				" SET ALARM_ID = "  + alarm.getAlarmID()  + "," +
				" NOTE_ID = '"      + alarm.getNoteID()   + "'," +
				" BEFORE = '"       + alarm.getBefore()    + "'," +
				" ALARM = "  		+ alarm.getAlarmTime().getTime() + "," +
				" SNOOZE = "        + alarm.getSnooze()    + "," +
				" WHERE NOTE_ID = " + alarm.getNoteID()   + ";";

		SQLiteDatabase db = instance.getWritableDatabase();
		db.execSQL(query);
	}
	public static synchronized void updateAlarms(List<Alarm> alarms){
		String query = "";
		for(Alarm alarm : alarms){
			query +=" UPDATE "  + ALARMTABLE_NAME      +
					" SET ALARM_ID = "  + alarm.getAlarmID()  + "," +
					" NOTE_ID = '"      + alarm.getNoteID()   + "'," +
					" BEFORE = '"       + alarm.getBefore()    + "'," +
					" ALARM = "  		+ alarm.getAlarmTime().getTime() + "," +
					" SNOOZE = "        + alarm.getSnooze()    + "," +
					" WHERE NOTE_ID = " + alarm.getNoteID()   + ";";
		}
		SQLiteDatabase db = instance.getWritableDatabase();
		db.execSQL(query);
	}
	public static synchronized void deleteAlarm(Alarm alarm){
		String query=
				" DELETE FROM "		+ ALARMTABLE_NAME	+
				" WHERE ALARM_ID = " + alarm.getAlarmID()	+ ";";
		SQLiteDatabase db = instance.getWritableDatabase();
		db.execSQL(query);
	}
	public static synchronized void deleteAlarms(List<Alarm> alarms){
		String query = "";
		for(Alarm alarm : alarms){
			query +=" DELETE FROM "		 + ALARMTABLE_NAME	+
					" WHERE ALARM_ID = " + alarm.getAlarmID()	+ ";";
		}
		SQLiteDatabase db = instance.getWritableDatabase();
		db.execSQL(query);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_TAGS = "CREATE TABLE tags(" +
						"TAG_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
						"TAG TEXT NOT NULL, " +
						"LOOP_START TIMESTAMP, " +
						"LOOP_END TIMESTAMP);";
		String CREATE_TIMETABLES = "CREATE TABLE times(" +
						"TIME_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
						"TAG_ID INTEGER NOT NULL, " +
						"START TIMESTAMP, " +
						"END TIMESTAMP, " +
						"FOREIGN KEY(TAG_ID) REFERENCES tags(TAG_ID) ); ";
		String CREATE_NOTES = "CREATE TABLE notes(" +
						"NOTE_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
						"TAG_ID INTEGER, " +
						"TITLE TEXT, " +
						"BODY TEXT, " +
						"TYPE INTEGER, " +
						"FOREIGN KEY(TAG_ID) REFERENCES tags(TAG_ID) ); ";
		/* TODO: 사진, 음성, 그림 등을 담을 수 있는 데이터베이스를 생성할 것 */
		String CREATE_ATTACHMENTS = "CREATE TABLE attaches(" +
						"ATTACH_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
						"NOTE_ID INTEGER, " +
						"DATA TEXT, " +
						"FOREIGN KEY(NOTE_ID) REFERENCES notes(NOTE_ID) ); ";

		String CREATE_ALARM = "CREATE TABLE alarms(" +
						"ALARM_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
						"NOTE_ID INTEGER, " +
						"BEFORE INTEGER, " +
						"ALARM TIMESTAMP, " +
						"SNOOZE INTEGER DEFAULT 0, " +
						"FOREIGN KEY(NOTE_ID) REFERENCES notes(NOTE_ID) );";

		String INITIALIZE_DATABASE = "INSERT INTO " + TAGSTABLE_NAME +
				" values(0, 'No Tag', null, null);";
		db.execSQL(CREATE_TIMETABLES);
		db.execSQL(CREATE_TAGS);
		db.execSQL(CREATE_NOTES);
		db.execSQL(CREATE_ATTACHMENTS);
		db.execSQL(CREATE_ALARM);
		db.execSQL(INITIALIZE_DATABASE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}



	public static void loadDummyNotes(){
		if(dummyNoteLoaded)
			return;
		// TODO: sample을 DB와 연결 할 것
		List<Note> notes = Arrays.asList(
				new Note("Dummy Note1", "This is Dummy. This is Dummy. "),
				new Note("NOTE 2", "222222222222222222222222 "));
		for(int i=0; i<notes.size(); i++){
			DatabaseHelper.insertNote(notes.get(i));
		}

		String query;
		query = " SELECT * FROM "   + TAGSTABLE_NAME    +
				" LEFT OUTER JOIN " + TIMETABLES_NAME   +
				" on "              + TAGSTABLE_NAME    + ".TAG_ID" +
				"="                 + TIMETABLES_NAME   + ".TAG_ID" +
				" GROUP BY "        + TAGSTABLE_NAME    + ".TAG_ID;";

		SQLiteDatabase db = instance.getReadableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		dummyNoteLoaded = true;
	}

	public static void loadDummyTags(){
		List<TimeTable> timeItems = new LinkedList<>();
		timeItems.add(new TimeTable(0, new Timestamp(0), new Timestamp(0)));
		List<TimeTag> timeTags = Arrays.asList(
				new TimeTag("CAPSTONE", Timestamp.valueOf("2017-03-01 00:00:00"), Timestamp.valueOf("2017-06-24 00:00:00"), timeItems)
		);
		for(int i=0; i<timeTags.size(); i++){
			DatabaseHelper.insertTag(timeTags.get(i) );
		}
		dummyNoteLoaded = true;
	}
}
