package com.flash.apps.noted.Adapter;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.flash.apps.noted.R;

import com.flash.apps.noted.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapterEVN extends RecyclerView.Adapter<RecyclerViewAdapterEVN.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapterEVN";
    private ArrayList<String> notebookNames = new ArrayList<>();
    private ArrayList<String> notesContent = new ArrayList<>();
    private final Context mContext;


    public RecyclerViewAdapterEVN(ArrayList<String> notebookNames, ArrayList<String> notesContent, Context mContext) {
        this.notebookNames = notebookNames;
        this.notesContent = notesContent;
        this.mContext = mContext;
    }

    //Responsible for inflating the view
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called!");

        holder.notebookTitle.setText(notebookNames.get(position));
        holder.noteContent.setText(notesContent.get(position));
        //holder..setText(notesContent.get(position));
        holder.notebookItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Clicked on: " + notebookNames.get(position));
                Toast.makeText(mContext, notebookNames.get(position), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return notebookNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        //ImageView notebookIcon;
        TextView notebookTitle;
        TextView noteContent;
        RelativeLayout notebookItem;
        public ViewHolder(View itemView) {
            super(itemView);

            //notebookIcon = itemView.findViewById(R.id.notebookIcon);
            notebookTitle = itemView.findViewById(R.id.notebookTitle);
            noteContent = itemView.findViewById(R.id.noteContent);
            notebookItem = itemView.findViewById(R.id.notebookItem);

        }
    }
}
