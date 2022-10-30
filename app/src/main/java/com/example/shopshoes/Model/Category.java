package com.example.shopshoes.Model;

import java.io.Serializable;

public class Category implements Serializable {

    String categoryId, CategoryName;

    public Category() {

    }

    public Category(String categoryId, String categoryName) {
        this.categoryId = categoryId;
        CategoryName = categoryName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    @Override
    public String toString() {
        return "Category{" +
                "categoryId='" + categoryId + '\'' +
                ", CategoryName='" + CategoryName + '\'' +
                '}';
    }
}
