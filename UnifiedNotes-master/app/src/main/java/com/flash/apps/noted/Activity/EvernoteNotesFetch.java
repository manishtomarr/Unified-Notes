package com.flash.apps.noted.Activity;


import android.content.Intent;
import android.util.Log;

import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.asyncclient.EvernoteCallback;
import com.evernote.client.android.asyncclient.EvernoteNoteStoreClient;
import com.evernote.edam.notestore.NoteFilter;
import com.evernote.edam.notestore.NoteList;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.Notebook;
import com.flash.apps.noted.Activity.EvernoteNB;
import com.flash.apps.noted.Activity.Login;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EvernoteNotesFetch {

    EvernoteNoteStoreClient noteStoreClient = EvernoteSession.getInstance().getEvernoteClientFactory().getNoteStoreClient();
    public List<String> guidList = new ArrayList<>();
    public HashMap<String, String> mapOfGuidNoteBook = new HashMap<>();



    public HashMap<String, String> getNotebookList(){


        noteStoreClient.listNotebooksAsync(new EvernoteCallback<List<Notebook>>() {
            @Override
            public void onSuccess(List<Notebook> result) {


                for (int i = 0; i < result.size(); i++)
                    guidList.add(result.get(i).getGuid());
                ArrayList<String> namesList = new ArrayList<>(result.size());

                for (Notebook notebook : result) {
                    namesList.add(notebook.getName());
                    mapOfGuidNoteBook.put(notebook.getName(),notebook.getGuid());


                }

            }

            @Override
            public void onException(Exception exception) {
                System.out.println("Error retrieving notebooks"+exception);
            }
        });



        return mapOfGuidNoteBook;


    }

    public void getNotesInNotebooks(){
        NoteFilter filter = new NoteFilter();
        filter.setNotebookGuid("4b697d26-175d-4240-85f8-f352f5dbca4f");

        noteStoreClient.findNotesAsync(filter, 0, 10, new EvernoteCallback<NoteList>() {
            @Override
            public void onSuccess(NoteList result) {
                List<Note> notes = result.getNotes();
                for (Note note : notes)
                {
                    System.out.println("NoteTitle:"+note.getTitle());
                }
            }

            @Override
            public void onException(Exception exception) {

            }
        });
    }


}
