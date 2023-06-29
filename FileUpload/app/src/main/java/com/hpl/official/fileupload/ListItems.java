package com.hpl.official.fileupload;

/**
 * Created by Pushpender Bhandari on 2/5/2016.
 */
public class ListItems {
    String Name, Date, Type, Size;

    public ListItems(String name, String type, String size, String date) {
        this.Name = name;
        this.Date = date;
        this.Type = type;
        this.Size = size;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getSize() {
        return Size;
    }

    public void setSize(String size) {
        Size = size;
    }
}
