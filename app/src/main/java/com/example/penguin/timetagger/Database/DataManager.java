package com.example.penguin.timetagger.Database;

import com.example.penguin.timetagger.Note;

import java.util.List;
import java.util.ListIterator;

/**
 * Created by penguin on 17. 5. 8.
 */

public class DataManager {
    private static final DataManager ourInstance = new DataManager();
    public static DataManager getInstance() {
        return ourInstance;
    }
    private DataManager() {}
    private List<Note> noteItems;
    private int length;
    
    public void insertNote(Note note) {
        note.id = ++length;
        noteItems.add(note);
    }

    public void saveNote(int id, Note note){
        Note n = loadNote(id);
        n.title = note.title;
        n.body = note.body;
    }
    
    public Note loadNote(int id){
        for(ListIterator<Note> it = noteItems.listIterator(noteItems.size()); it.hasPrevious();){
            Note note = it.previous();
            if(note.id == id)
                return note;
        }
        return null;
    }
}
