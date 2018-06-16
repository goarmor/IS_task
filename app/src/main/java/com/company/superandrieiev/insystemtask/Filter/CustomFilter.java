package com.company.superandrieiev.insystemtask.Filter;

import android.widget.Filter;

import com.company.superandrieiev.insystemtask.adapter.RecyclerAdapter;
import com.company.superandrieiev.insystemtask.model.ImageWithTag;

import java.util.ArrayList;

/**
 * Created by DIMON on 16.06.2018.
 */

public class CustomFilter extends Filter {

    RecyclerAdapter adapter;
    ArrayList<ImageWithTag> filterList;


    public CustomFilter(ArrayList<ImageWithTag> filterList, RecyclerAdapter adapter)
    {
        this.adapter=adapter;
        this.filterList=filterList;
    }


    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results=new FilterResults();

        if(constraint != null && constraint.length() > 0)
        {
            constraint=constraint.toString().toUpperCase();
            ArrayList<ImageWithTag> filteredImagesWithTags=new ArrayList<>();

            String[] arr = constraint.toString().split(",");

            for (int i=0;i<filterList.size();i++)
            {
               for (int k = 0; k < arr.length; k++) {
                    String tag = arr[k].trim();
                    if (!tag.contains(" ") && !tag.isEmpty()) {
                        ImageWithTag imageWithTag = filterList.get(i);
                        String[] tagsArr = imageWithTag.getTags().split(",");
                        for (int j = 0; j < tagsArr.length; j++) {
                            tagsArr[j] = tagsArr[j].trim();
                            if(tagsArr[j].toUpperCase().contentEquals(tag)){
                                filteredImagesWithTags.add(imageWithTag);
                                k = arr.length;
                                break;
                            }
                        }
                    }
                }
            }

            results.count=filteredImagesWithTags.size();
            results.values=filteredImagesWithTags;
        } else {
            results.count=filterList.size();
            results.values=filterList;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.setListImageWithTag((ArrayList<ImageWithTag>) results.values);
        adapter.notifyDataSetChanged();
    }
}
