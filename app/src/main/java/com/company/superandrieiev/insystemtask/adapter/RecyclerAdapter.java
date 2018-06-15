package com.company.superandrieiev.insystemtask.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.company.superandrieiev.insystemtask.R;
import com.company.superandrieiev.insystemtask.model.ImageWithTag;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by DIMON on 15.06.2018.
 */

public class RecyclerAdapter  extends RecyclerView.Adapter<RecyclerAdapter.ImageWithTagViewHolder>  {

    private ArrayList<ImageWithTag> listImageWithTag;
    private Context mContext;


    public RecyclerAdapter(ArrayList<ImageWithTag> recordsList, Context mContext) {
        this.listImageWithTag = recordsList;
        this.mContext = mContext;
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
            final InputStream imageStream = mContext.getContentResolver().openInputStream(listImageWithTag.get(position).getImageURI());
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
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
}

