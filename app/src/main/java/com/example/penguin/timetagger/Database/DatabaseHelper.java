package com.example.penguin.timetagger.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.penguin.timetagger.Note;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by penguin on 17. 5. 15.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "TimeTaggerDB.db";
	private static final int DATABASE_VERSION = 1;
	private static DatabaseHelper instance = null;

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
		String query =  " INSERT INTO " + NOTESTABLE_NAME   +
						"values(NULL, " + note.getTag()     +","
										+ note.getTitle()   + ","
										+ note.getBody()    + ");";

		SQLiteDatabase db = instance.getWritableDatabase();
		db.execSQL(query);

		Cursor cursor = db.rawQuery("SELECT last_insert_rowid()", null);
		note.setID(cursor.getColumnIndex("NOTE_ID"));
		cursor.close();
		return note;
	}

	public static synchronized void updateNote(Note note, int note_id){
		if(note_id == -1) return;
		String query =  " UPDATE "      + NOTESTABLE_NAME   +
						" SET TAG = "   + note.getTag()     + "," +
						" SET TITLE = " + note.getTitle()   + "," +
						" SET BODY = "  + note.getBody()    + ");";;

		SQLiteDatabase db = instance.getWritableDatabase();
		db.execSQL(query);
	}

	public static synchronized List<Note> selectNote(String tag){
		/* TODO: TAG is null 에 대해 고민 할 것 */
		if(tag == null)
			tag = "NULL";
		String query =  " SELECT FROM "  + NOTESTABLE_NAME   +
						" WHERE TAG = "  + tag               + ";";
		SQLiteDatabase db = instance.getReadableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		List<Note> notes = new LinkedList<>();
		if(cursor.moveToFirst()){
			Note note = new Note(cursor.getString(1),cursor.getString(2),
								 cursor.getString(3), cursor.getInt(0));
			notes.add(note);
		}
		cursor.close();
		return notes;
	}

	public static synchronized Note selectNote(int note_id){
		/* TODO: TAG is null 에 대해 고민 할 것 */
		String query =  " SELECT FROM "     + NOTESTABLE_NAME   +
						" WHERE NOTE_ID = " + note_id           + ";";
		SQLiteDatabase db = instance.getReadableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		if(cursor.moveToFirst()){
			Note note = new Note(cursor.getString(1),cursor.getString(2),
					cursor.getString(3), cursor.getInt(0));
			return note;
		}
		cursor.close();
		return null;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_TIMETABLES = "CREATE TABLE times(" +
						"TIME_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
						"TAG_ID INTEGER NOT NULL, " +
						"START TIMESTAMP, " +
						"END TIMESTAMP);";
		String CREATE_TAGS = "CREATE TABLE tags(" +
						"TAG_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
						"TAG TEXT NOT NULL, " +
						"LOOP_START TIMESTAMP, " +
						"LOOP_END TIMESTAMP, " +
						"FOREIGN KEY(TAG_ID) REFERENCES times(TAG_ID) ); ";
		String CREATE_NOTES = "CREATE TABLE notes(" +
						"NOTE_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
						"TAG TEXT, " +
						"TITLE TEXT, " +
						"BODY TEXT, " +
						"FOREIGN KEY(TAG) REFERENCES tags(TAG) ); ";
		/* TODO: 사진, 음성, 그림 등을 담을 수 있는 데이터베이스를 생성할 것 */
		// String CREATE_ATTACHMENTS = ...
		db.execSQL(CREATE_TIMETABLES);
		db.execSQL(CREATE_TAGS);
		db.execSQL(CREATE_NOTES);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

}
