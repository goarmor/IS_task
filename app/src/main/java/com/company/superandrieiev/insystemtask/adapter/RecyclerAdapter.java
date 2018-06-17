package com.company.superandrieiev.insystemtask.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.company.superandrieiev.insystemtask.filter.CustomFilter;
import com.company.superandrieiev.insystemtask.R;
import com.company.superandrieiev.insystemtask.model.ImageWithTag;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by DIMON on 15.06.2018.
 */

public class RecyclerAdapter  extends RecyclerView.Adapter<RecyclerAdapter.ImageWithTagViewHolder> implements Filterable {

    private ArrayList<ImageWithTag> listImageWithTag, filterListImageWithTag;;
    private Context mContext;
    CustomFilter filter;

    public RecyclerAdapter(ArrayList<ImageWithTag> recordsList, Context mContext) {
        this.listImageWithTag = recordsList;
        this.filterListImageWithTag = recordsList;
        this.mContext = mContext;
    }


    @Override
    public Filter getFilter() {
        if(filter==null)
        {
            filter=new CustomFilter(filterListImageWithTag, this);
        }

        return filter;
    }


    public class ImageWithTagViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView tags;

        public ImageWithTagViewHolder(View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.image);
            tags = (TextView) view.findViewById(R.id.text_view);
        }
    }


    @Override
    public ImageWithTagViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_with_tag_recyclerview_item, parent, false);
        return new ImageWithTagViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final ImageWithTagViewHolder holder, int position) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            final InputStream imageStream = mContext.getContentResolver().openInputStream(listImageWithTag.get(position).getImageURI());
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream, null, options);
            holder.image.setImageBitmap(selectedImage);
            holder.tags.setText(listImageWithTag.get(position).getTags());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return listImageWithTag.size();
    }


    public ArrayList<ImageWithTag> getFilterListImageWithTag() {
        return filterListImageWithTag;
    }


    public void setListImageWithTag(ArrayList<ImageWithTag> listImageWithTag) {
        this.listImageWithTag = listImageWithTag;
    }
}

