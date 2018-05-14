package com.example.mathi.expense.income_category;


public class Income_category {
    String category;
    Integer id;


    public Income_category(String category, Integer id) {
        this.category = category;
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }



}