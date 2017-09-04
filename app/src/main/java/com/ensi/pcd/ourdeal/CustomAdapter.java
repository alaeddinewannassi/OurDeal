package com.ensi.pcd.ourdeal;

import android.content.Context;
import android.content.Intent;
import android.provider.SyncStateContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.addAll;

/**
 * Created by Ouannassi on 17/04/2017.
 */

public class CustomAdapter extends BaseAdapter implements Filterable {
    Context c;
    ImageView photoUrlTxt;
    ArrayList<Annonce> annonces;
    public CustomAdapter(Context c, ArrayList<Annonce> annonces) {
        this.c = c;
        this.annonces = annonces;
    }
    @Override
    public int getCount() {
        return annonces.size();
    }
    @Override
    public Object getItem(int pos) {
        return annonces.get(pos);
    }
    @Override
    public long getItemId(int pos) {
        return pos;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if(convertView==null)
        {
            convertView= LayoutInflater.from(c).inflate(R.layout.annonce,viewGroup,false);
        }
        TextView nameTxt= (TextView) convertView.findViewById(R.id.nameAnnonce);
        photoUrlTxt= (ImageView) convertView.findViewById(R.id.photoAnnonce);
        TextView descTxt= (TextView) convertView.findViewById(R.id.descAnnonce);
        TextView date = (TextView) convertView.findViewById(R.id.dateAnnonce);
        TextView prix = (TextView) convertView.findViewById(R.id.PrixAnnonce);
        final Annonce s= (Annonce) this.getItem(position);
        nameTxt.setText(s.getTitre());
        prix.setText(String.valueOf(s.getPrix()));
        date.setText((CharSequence) s.getDate_pub());
        Picasso.with(c).load(s.getPhotoUrl()).networkPolicy(NetworkPolicy.OFFLINE).into(photoUrlTxt, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Picasso.with(c).load(s.getPhotoUrl()).into(photoUrlTxt);
            }
        });
        descTxt.setText(s.getDescription());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //OPEN DETAIL
                openDetailActivity(s.getTitre(),s.getDescription(),s.getPhotoUrl());
            }
        });
        return convertView;
    }
    //OPEN DETAIL ACTIVITY
    private void openDetailActivity(String...details)
    {
        Intent i=new Intent(c,DetailActivity.class);
        i.putExtra("NAME_KEY",details[0]);
        i.putExtra("DESC_KEY",details[1]);
        i.putExtra("PHOTO_KEY",details[2]);
        c.startActivity(i);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            ArrayList<Annonce> orig = annonces;
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<Annonce> results = new ArrayList<Annonce>();
                if (orig == null)
                    orig = annonces;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final Annonce g : orig) {
                            if (g.getTitre().toLowerCase()
                                    .contains(constraint.toString()))
                                results.add(g);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                annonces = (ArrayList<Annonce>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();

    }


}
