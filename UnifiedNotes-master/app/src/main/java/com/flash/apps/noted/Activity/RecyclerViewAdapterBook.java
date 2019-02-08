package com.flash.apps.noted.Activity;

import android.content.Context;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.flash.apps.noted.R;

import java.util.List;



public class RecyclerViewAdapterBook extends RecyclerView.Adapter<RecyclerViewAdapterBook.MyViewHolder> {

    private Context mContext ;
    private List<Book> mData ;



    public RecyclerViewAdapterBook(Context mContext, List<Book> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_item_book,parent,false);
        //view.setOnClickListener(mOnClickListener);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.tv_book_title.setText(mData.get(position).getTitle());
        holder.location.setText(mData.get(position).getLocation());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext , GridView.class);
                intent.putExtra("bookName",holder.tv_book_title.getText().toString());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_book_title;
        ImageView img_book_thumbnail;
        TextView location;
        CardView cardView ;
        private final Context context;

        public MyViewHolder(View itemView) {
            super(itemView);


            context = itemView.getContext();
            tv_book_title = itemView.findViewById(R.id.book_title_id) ;
            location = itemView.findViewById( R.id.loc_id );

            // img_book_thumbnail = (ImageView) itemView.findViewById(R.id.book_img_id);
            cardView = itemView.findViewById(R.id.cardview_id);




        }
    }


}