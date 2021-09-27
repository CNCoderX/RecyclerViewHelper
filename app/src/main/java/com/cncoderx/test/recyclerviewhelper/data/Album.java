package com.cncoderx.test.recyclerviewhelper.data;

import androidx.annotation.NonNull;

/**
 * @author cncoderx
 */
public class Album {
    private int cover;
    private String name;
    private String price;
    private String category;

    public int getCover() {
        return cover;
    }

    public void setCover(int cover) {
        this.cover = cover;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
