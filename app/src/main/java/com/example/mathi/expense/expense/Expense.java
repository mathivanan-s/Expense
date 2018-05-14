package com.example.mathi.expense.expense;


public class Expense {
    String price,category,description,date;
    Integer id;


    public Expense(String price, String category, String description, String date, Integer id) {
        this.price = price;
        this.category = category;
        this.description = description;
        this.date = date;
        this.id = id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }



}