package com.esprit.tourapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.esprit.tourapp.Pliste;
import com.esprit.tourapp.R;
import com.esprit.tourapp.videofull;

import java.util.ArrayList;

public class VideoAdapterrrrr extends RecyclerView.Adapter<videoAdapter> {

    ArrayList<Pliste> arrayList;
    Context context;

    public VideoAdapterrrrr(ArrayList<Pliste> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public videoAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.video,parent, false);
        return new  videoAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull videoAdapter holder, int position) {
        Pliste current = arrayList.get(position);
        holder.webView.loadUrl(current.getLink());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, videofull.class);
                i.putExtra("link",current.getLink());
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
