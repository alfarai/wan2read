package com.example.wan2readdigitallibrary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/*
*   This class will instantiate for the card views for the bookmark lists
*
* */

public class bookmarkAdapter extends RecyclerView.Adapter<bookmarkAdapter.MyViewHolder> {
    private Context context;
    private ArrayList bookid_id, subject_id, page_id;

    public bookmarkAdapter(Context context, ArrayList bookid_id, ArrayList subject_id, ArrayList page_id) {
        this.context = context;
        this.bookid_id = bookid_id;
        this.subject_id = subject_id;
        this.page_id = page_id;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.userentry,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bookid_id.setText(String.valueOf(bookid_id.get(position)));
        holder.subject_id.setText(String.valueOf(subject_id.get(position)));
        holder.page_id.setText(String.valueOf(page_id.get(position)));
    }

    @Override
    public int getItemCount() {
        return bookid_id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView bookid_id, subject_id, page_id;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            bookid_id = itemView.findViewById(R.id.textid);
            subject_id = itemView.findViewById(R.id.textsubject);
            page_id = itemView.findViewById(R.id.textpage);

        }
    }


}
