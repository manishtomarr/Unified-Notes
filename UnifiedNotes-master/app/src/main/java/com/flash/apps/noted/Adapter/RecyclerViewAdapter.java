package com.flash.apps.noted.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flash.apps.noted.Activity.CreateNote;
import com.flash.apps.noted.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.NoteViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> mTitle = new ArrayList<>();
    private ArrayList<String> mContent = new ArrayList<>();
    private Context mContext;
    public FirebaseAuth fAuth;
    private DatabaseReference ourNoteDatabase;
    private String noteId;
    public List<String> key;
    private String ID;

    public RecyclerViewAdapter(ArrayList<String> title, ArrayList<String> content, Context context){
        mTitle=title;
        mContent=content;
        mContext=context;

    }


    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_note_layout, parent, false);
        NoteViewHolder holder = new NoteViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final NoteViewHolder holder, final int position) {

        Log.d(TAG, "onBindViewHolder: called");
        fAuth = FirebaseAuth.getInstance();
        ourNoteDatabase = FirebaseDatabase.getInstance().getReference().child("Notes").child(fAuth.getCurrentUser().getUid());
        holder.textTitle.setText(mTitle.get(position));
        holder.textContent.setText(mContent.get(position));
        holder.noteCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: "+mTitle.get(position));
                Intent intent = new Intent();
                intent.setClass(mContext,CreateNote.class);
                ID = Integer.toString(position);
                intent.putExtra("noteId",ID);
                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mTitle.size();
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder{
        TextView textTitle, textContent;
        RelativeLayout noteCard;
        public NoteViewHolder(View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.note_title);
            textContent = itemView.findViewById(R.id.note_content);
            noteCard = itemView.findViewById(R.id.note_card);
        }
    }
}
