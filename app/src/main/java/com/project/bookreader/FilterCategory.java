package com.project.bookreader;

import android.widget.Filter;

import java.util.ArrayList;
import java.util.Locale;

public class FilterCategory extends Filter {

    //Array in which we want to search
    ArrayList<ModelCategory> filterList;

    //Adapter in which filter
    AdapterCategory adapterCategory;

    //constructor

    public FilterCategory(ArrayList<ModelCategory> filterList, AdapterCategory adapterCategory) {
        this.filterList = filterList;
        this.adapterCategory = adapterCategory;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();

        //Value should not be null and not empty
        if (constraint != null && constraint.length() > 0) {

            //change to upper case or lower case
            constraint = constraint.toString().toUpperCase();
            ArrayList<ModelCategory> filteredModels = new ArrayList<>();
            for(int i=0; i < filterList.size(); i++) {
                //validate
                if (filterList.get(i).getCategory().toUpperCase().contains(constraint)){
                    //add to filtered list
                    filteredModels.add(filterList.get(i));
                }
            }

            results.count = filteredModels.size();
            results.values = filteredModels;
        }
        else {
            results.count = filterList.size();
            results.values = filterList;
        }

        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {

        //apply filter changes
        adapterCategory.categoryArrayList = (ArrayList<ModelCategory>) results.values;

        //notify changes
        adapterCategory.notifyDataSetChanged();

    }
}
