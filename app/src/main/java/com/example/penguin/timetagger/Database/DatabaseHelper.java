package com.example.penguin.timetagger.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.penguin.timetagger.Note;
import com.example.penguin.timetagger.TimeTag;
//import com.example.penguin.timetagger.TimeTable;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
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

	public static synchronized Note insertNote(Note note){
		String query =  " INSERT INTO "     + NOTESTABLE_NAME   +
						" values(NULL,'"    + note.getTagID()     + "','"
										    + note.getTitle()   + "','"
											+ note.getBody()    + "');";

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
						" BODY = '"         + note.getBody()    + "'" +
						" WHERE NOTE_ID = " + note.getNoteID()  + ";";

		SQLiteDatabase db = instance.getWritableDatabase();
		db.execSQL(query);
	}

	public static synchronized List<Note> selectNotes(int tag_id){
		/* TODO: TAG is null 에 대해 고민 할 것 */
		String query;
		query = " SELECT * FROM "   + NOTESTABLE_NAME   +
				" WHERE TAG_ID = "  + tag_id            + ";";

		SQLiteDatabase db = instance.getReadableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		List<Note> notes = new LinkedList<>();
		if(cursor.moveToFirst()){
			while(!cursor.isAfterLast()) {
				Note note = new Note(cursor.getInt(0), cursor.getInt(1),
									 cursor.getString(2), cursor.getString(3));
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
								 cursor.getString(2), cursor.getString(3));
			return note;
		}
		cursor.close();
		return null;
	}

	public static synchronized void insertTag(TimeTag newtag){
		String query =  " INSERT INTO "     + TAGSTABLE_NAME     +
						" values('"    		+ newtag.getID()     + "','"
											+ newtag.getTag()    + "',"
											+ newtag.getStart()  + ","
                                            + newtag.getEnd()    + ");";

		SQLiteDatabase db = instance.getWritableDatabase();
		db.execSQL(query);

		// TODO: insert tag에서는 tag 에 연동된 타임 테이블도 추가해줘야 함

        for (int i = 0; i < newtag.getListSize(); i++) {
            query = " INSERT INTO " + TIMETABLES_NAME +
                    " values('" + newtag.getListItemTimeID(i) + "','"
                                + newtag.getListItemTagID(i)  + "',"
                                + newtag.getListItemStart(i)  + ","
                                + newtag.getListItemEnd(i)    + ");";
            db.execSQL(query);
        }

        return;
	}

	public static synchronized TimeTag selectTag(int tag_id){
        String query;
		query = " SELECT * FROM "   + TAGSTABLE_NAME   +
				" WHERE TAG_ID = "  + tag_id            + ";";

		SQLiteDatabase db = instance.getReadableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		if(cursor.moveToFirst()) {
            Timestamp start = new Timestamp(cursor.getLong(1));
            Timestamp end = new Timestamp(cursor.getLong(2));
            TimeTag tag = new TimeTag(cursor.getString(0), start, end);

            String query2 = " SELECT * FROM "   + TIMETABLES_NAME   +
                    " WHERE TAG_ID = "  + tag_id            + ";";
            Cursor cursor2 = db.rawQuery(query2, null);

            if(cursor2.moveToFirst()){
                while(!cursor2.isAfterLast()) {
                    start = new Timestamp(cursor2.getLong(2));
                    end = new Timestamp(cursor2.getLong(3));
                    tag.setTimes(cursor2.getInt(0), cursor2.getInt(1), start, end);
                    cursor2.moveToNext();
                }
            }
            cursor2.close();

            return tag;
        }

		cursor.close();
		// TODO: selectTags는 tag안에 연동된 타임테이블을 포함해서 리턴해 주어야 함.

        return null;
	}

	public void InitializeDB(SQLiteDatabase db) {
		String query = "INSERT INTO " + TAGSTABLE_NAME +
				" values(0, 'No Tag', null, null);";
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
						"FOREIGN KEY(TAG_ID) REFERENCES tags(TAG_ID) ); ";
		/* TODO: 사진, 음성, 그림 등을 담을 수 있는 데이터베이스를 생성할 것 */
		// String CREATE_ATTACHMENTS = ...
		db.execSQL(CREATE_TIMETABLES);
		db.execSQL(CREATE_TAGS);
		db.execSQL(CREATE_NOTES);
		InitializeDB(db);
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
}
