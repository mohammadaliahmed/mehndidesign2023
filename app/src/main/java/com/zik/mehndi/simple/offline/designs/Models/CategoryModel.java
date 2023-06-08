package com.zik.mehndi.simple.offline.designs.Models;

import java.io.Serializable;

public class CategoryModel implements Serializable {
    String categoryName,shortName;
    int img;

    public CategoryModel(String categoryName,String shortName, int img) {
        this.categoryName = categoryName;
        this.shortName = shortName;
        this.img = img;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
