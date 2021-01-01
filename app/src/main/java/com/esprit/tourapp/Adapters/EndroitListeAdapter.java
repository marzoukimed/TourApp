package com.esprit.tourapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.esprit.tourapp.R;
import com.esprit.tourapp.Sortiee;
import com.esprit.tourapp.endroitt;
import com.squareup.picasso.Picasso;

import java.util.List;

public class EndroitListeAdapter extends RecyclerView.Adapter<EndroitListeAdapter.ListeAdapterVHH> {
    private List<endroitt> endroitts;
    private Context context;
    ImageLoader imageLoader;

    public EndroitListeAdapter() {
    }

    public void setData (List<endroitt> endroitts){
        this.endroitts = endroitts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EndroitListeAdapter.ListeAdapterVHH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new EndroitListeAdapter.ListeAdapterVHH(LayoutInflater.from(context).inflate(R.layout.listaa,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ListeAdapterVHH holder, int position) {
        endroitt endroittt = endroitts.get(position);



        //holder.imageee.setImageUrl(endroittt.getPhoto(), imageLoader);

        String name = endroittt.getNom();
        String location = endroittt.getLocation();
       // String uri = endroittt.getPhoto();

        holder.name.setText(name);
        holder.locationnn.setText(location);
        //holder.urii.setText(uri);



        Glide.with(context).load(endroittt.getPhoto())
                .into(holder.imageee);

      //  Picasso.with(this).load(endroittt.getPhoto()).into(holder.imageee);

    }

    @Override
    public int getItemCount() {
        return endroitts.size();
    }

    public class ListeAdapterVHH extends RecyclerView.ViewHolder {

        TextView name;
        TextView locationnn;
        TextView urii;
        ImageView imageee;
       // NetworkImageView imageee ;
        public ListeAdapterVHH(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            locationnn = itemView.findViewById(R.id.locationnn);
            urii = itemView.findViewById(R.id.urii);
            imageee =  itemView.findViewById(R.id.imageee) ;

        }
    }
}
