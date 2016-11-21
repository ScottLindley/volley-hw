package com.scottlindley.networkinginandroidlab;

/**
 * Created by Scott Lindley on 11/17/2016.
 */

public class Item {
    private String mName, mPrice;

    public Item(String name, String price) {
        mName = name;
        mPrice = price;
    }

    public String getName() {
        return mName;
    }

    public String getPrice() {
        return mPrice;
    }
}
