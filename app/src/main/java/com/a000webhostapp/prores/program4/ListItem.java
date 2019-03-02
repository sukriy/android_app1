package com.a000webhostapp.prores.program4;

public class ListItem {
    private String head, desc, imageUrl, level;

    public ListItem(String head, String desc, String imageUrl, String level){
        this.head = head;
        this.desc = desc;
        this.imageUrl = imageUrl;
        this.level = level;
    }

    public String getHead() {
        return head;
    }

    public String getDesc() {
        return desc;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public String getLevel() {
        return level;
    }
}
