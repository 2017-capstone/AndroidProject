package com.example.penguin.timetagger.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.penguin.timetagger.Note;
import com.example.penguin.timetagger.TimeTable;
import com.example.penguin.timetagger.TimeTag;
//import com.example.penguin.timetagger.TimeTable;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.jar.Pack200;

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
											+ note.getBody()	+ "','"
											+ note.getPhoto()    + "');";

		SQLiteDatabase db = instance.getWritableDatabase();
		db.execSQL(query);

		Cursor cursor = db.rawQuery("SELECT last_insert_rowid()", null);
		note.setNoteID(cursor.getColumnIndex("NOTE_ID"));
		cursor.close();
		return note;
	}

	public static synchronized void updateNote(Note note){
		if(note.getNoteID() == -1) return;
		String query =  " UPDATE "          + NOTESTABLE_NAME   +
						" SET TAG_ID = "    + note.getTagID()   + "," +
						" TITLE = '"        + note.getTitle()   + "'," +
						" BODY = '"         + note.getBody()    + "'," +
						" PHOTO = '"  		 + note.getPhoto() + "'" +
						" WHERE NOTE_ID = " + note.getNoteID()  + ";";

		SQLiteDatabase db = instance.getWritableDatabase();
		db.execSQL(query);
	}

	public static synchronized List<Note> selectAllNotes(){
		String query;
		query = " SELECT * FROM "   + NOTESTABLE_NAME   + ";";

		SQLiteDatabase db = instance.getReadableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		List<Note> notes = new LinkedList<>();
		if(cursor.moveToFirst()){
			while(!cursor.isAfterLast()) {
				Note note = new Note(cursor.getInt(0), cursor.getInt(1),
						cursor.getString(2), cursor.getString(3), cursor.getString(4));
				notes.add(note);
				cursor.moveToNext();
			}
		}
		cursor.close();
		return notes;
	}
	public static synchronized List<Note> selectNotes(int tag_id){
		String query;
		query = " SELECT * FROM "   + NOTESTABLE_NAME   +
				" WHERE TAG_ID = "  + tag_id            + ";";

		SQLiteDatabase db = instance.getReadableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		List<Note> notes = new LinkedList<>();
		if(cursor.moveToFirst()){
			while(!cursor.isAfterLast()) {
				Note note = new Note(cursor.getInt(0), cursor.getInt(1),
									 cursor.getString(2), cursor.getString(3), cursor.getString(4));
				notes.add(note);
				cursor.moveToNext();
			}
		}
		cursor.close();
		return notes;
	}
	public static synchronized Note selectNote(int note_id){
		/* TODO: TAG is null 에 대해 고민 할 것 */
		String query =  " SELECT * FROM "   + NOTESTABLE_NAME   +
						" WHERE NOTE_ID = " + note_id           + ";";
		SQLiteDatabase db = instance.getReadableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		if(cursor.moveToFirst()){
			Note note = new Note(cursor.getInt(0), cursor.getInt(1),
								 cursor.getString(2), cursor.getString(3), cursor.getString(4));
			return note;
		}
		cursor.close();
		return null;
	}

	public static synchronized void deleteNote(int note_id){}

	public static synchronized void deleteNotes(List<Integer> note_ids){}


	/* TAGS */
	public static synchronized void insertTag(TimeTag tag){
		String query =  " INSERT INTO "     + TAGSTABLE_NAME            +
						" values("    		+ null                      + ",'"
											+ tag.getTag()              + "',"
											+ tag.getStart().getTime()  + ","
                                            + tag.getEnd().getTime()    + ");";

		SQLiteDatabase db = instance.getWritableDatabase();
		db.execSQL(query);

		// TODO: insert tag에서는 tag 에 연동된 타임 테이블도 추가해줘야 함

		ListIterator<TimeTable> iter = tag.getTimes().listIterator();
		while(iter.hasNext()){
			TimeTable t = iter.next();
			query = " INSERT INTO " + TIMETABLES_NAME           +
					" values("      + t.getTimeID()              + ","
									+ t.getTagID()              + ","
									+ t.getStart().getTime()    + ","
									+ t.getEnd().getTime()      + ");";
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
				" GROUP BY "        + TAGSTABLE_NAME    + ".TAG_ID;";

		SQLiteDatabase db = instance.getReadableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		List<TimeTag> timeTags = new LinkedList<>();
		List<TimeTable> timeTables;
		int tmp_id;
		if(cursor.moveToFirst()) {
			for (int i = 0; !cursor.isAfterLast(); i++) {
				timeTags.add(new TimeTag(cursor.getInt(0), cursor.getString(1), cursor.getLong(2), cursor.getLong(3)));
				timeTables = new LinkedList<>();
				tmp_id = cursor.getInt(0);

				while (!cursor.isAfterLast()) {
					if (tmp_id != cursor.getInt(0)) {
						timeTags.get(i).setTimes(timeTables);
						break;
					}
					if (cursor.getInt(4) != 0)
						timeTables.add(new TimeTable(cursor.getInt(4), cursor.getInt(5), cursor.getLong(6), cursor.getLong(7)));
					cursor.moveToNext();
				}
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
				" WHERE "           + TIMETABLES_NAME   +
				".TAG_ID = "        + tag_id            +
				" GROUP BY "        + TAGSTABLE_NAME    + ".TAG_ID;";

		SQLiteDatabase db = instance.getReadableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		TimeTag timeTag = null;
		if(cursor.moveToFirst()) {
			timeTag = new TimeTag(cursor.getInt(0), cursor.getString(1), cursor.getLong(2), cursor.getLong(3));
			List<TimeTable> timeTables = new LinkedList<>();

			while (!cursor.isAfterLast()) {
				if (cursor.getInt(4) != 0)
					timeTables.add(new TimeTable(cursor.getInt(4), cursor.getInt(5), cursor.getLong(6), cursor.getLong(7)));
				cursor.moveToNext();
			}
		}
		cursor.close();
		return timeTag;
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
						"PHOTO TEXT, " +
						"FOREIGN KEY(TAG_ID) REFERENCES tags(TAG_ID) ); ";
		/* TODO: 사진, 음성, 그림 등을 담을 수 있는 데이터베이스를 생성할 것 */
		// String CREATE_ATTACHMENTS = ...
		String INITIALIZE_DATABASE = "INSERT INTO " + TAGSTABLE_NAME +
				" values(0, 'No Tag', null, null);";
		db.execSQL(CREATE_TIMETABLES);
		db.execSQL(CREATE_TAGS);
		db.execSQL(CREATE_NOTES);
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
				new Note("Dummy Note2", "This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. "),
				new Note("Dummy Note3", "This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. "),
				new Note("Dummy Note4", "This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. "),
				new Note("Dummy Note5", "This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. "),
				new Note("Dummy Note6", "This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. "),
				new Note("Dummy Note7", "This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. "),
				new Note("Dummy Note8", "This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. "),
				new Note("Dummy Note9", "This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. "),
				new Note("Dummy Note10", "This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. "),
				new Note("Dummy Note11", "This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. "),
				new Note("Dummy Note12", "This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. "),
				new Note("Dummy Note13", "This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. "),
				new Note("Dummy Note14", "This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. This is Dummy. "));

		for(int i=0; i<notes.size(); i++){
			Note n = new Note(notes.get(i).getTitle(), notes.get(i).getBody());
			DatabaseHelper.insertNote(n);
		}
		dummyNoteLoaded = true;
	}

	public static void loadDummyTags(){
		List<TimeTag> timeTags = Arrays.asList(
				new TimeTag("CAPSTONE", Timestamp.valueOf("2017-03-01 00:00:00"), Timestamp.valueOf("2017-06-24 00:00:00"))
		);
		for(int i=0; i<timeTags.size(); i++){
			DatabaseHelper.insertTag(timeTags.get(i) );
		}
		dummyNoteLoaded = true;
	}
}
