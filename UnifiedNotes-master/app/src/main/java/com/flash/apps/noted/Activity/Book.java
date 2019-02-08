package com.flash.apps.noted.Activity;


public class Book {

    private String Title;
    private String Location;

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        this.Location = location;
    }

    public Book(String title, String location) {
        Title = title;
        Location = location;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }


}