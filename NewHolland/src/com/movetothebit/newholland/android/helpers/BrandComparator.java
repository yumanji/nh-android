package com.movetothebit.newholland.android.helpers;

import java.util.Comparator;

import com.movetothebit.newholland.android.model.Brand;


public class BrandComparator implements Comparator<Brand> {
    @Override
    public int compare(Brand o1, Brand o2) {
        return (o1.getCount()>o2.getCount() ? -1 : (o1.getCount()==o2.getCount() ? 0 : 1));
    }
}