package com.esprit.tourapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.esprit.tourapp.R;
import com.esprit.tourapp.Sortie;
import com.esprit.tourapp.Sortiee;
import com.esprit.tourapp.addsortie;
import com.google.android.material.button.MaterialButton;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class SortieListeAdapter extends RecyclerView.Adapter<SortieListeAdapter.ListeAdapterVH> implements Filterable {


    private List<Sortiee> sortiees;
    private List<Sortiee> sortiefull;
    private Context context;

    public SortieListeAdapter() {
    }
    public SortieListeAdapter(List<Sortiee> sortiees) {
        this.sortiees = sortiees;
        sortiefull = new ArrayList<>(sortiees);
    }

    public void setData (List<Sortiee> sortiees){
        this.sortiees = sortiees;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ListeAdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new SortieListeAdapter.ListeAdapterVH(LayoutInflater.from(context).inflate(R.layout.activity_liste_sortie,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ListeAdapterVH holder, int position) {

        Sortiee sortiee = sortiees.get(position);
        String dt_debut = sortiee.getDt_debut();
        String dt_fin = sortiee.getDt_fin();
        String location = sortiee.getLocation();
        String usermail = sortiee.getUser_email();

        holder.textViewDt_debut.setText(dt_debut);
        holder.textViewDt_fin.setText(dt_fin);
        holder.textViewlocation.setText(location);
        holder.usermail.setText(usermail);


       holder.bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(holder.bt_delete.getContext(), addsortie.class);
                    holder.bt_delete.getContext().startActivity(intent);
       }
        });

    }

    @Override
    public int getItemCount() {
        return sortiees.size();
    }

    @Override
    public Filter getFilter() {
        return sortieees;
    }
    private Filter sortieees = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Sortiee> filtredList = new ArrayList<>();
            if (constraint == null || constraint.length()==0){
                filtredList.addAll(sortiefull);
            }else{
                String filePattern = constraint.toString().toLowerCase().trim();

                for (Sortiee item : sortiefull){
                    if (item.getLocation().toLowerCase().contains(filePattern)){
                        filtredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filtredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            sortiees.clear();
            sortiees.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public class ListeAdapterVH extends RecyclerView.ViewHolder {

        TextView textViewDt_debut;
        TextView textViewDt_fin;
        TextView textViewlocation;
        TextView usermail;

        MaterialButton bt_delete;
        public ListeAdapterVH(@NonNull View itemView) {
            super(itemView);
            textViewDt_debut = itemView.findViewById(R.id.textViewDt_debut);
            textViewDt_fin = itemView.findViewById(R.id.textViewDt_fin);
            textViewlocation = itemView.findViewById(R.id.textViewlocation);
            usermail = itemView.findViewById(R.id.usermail);
            bt_delete = itemView.findViewById(R.id.bt_delete);


        }

}

}
