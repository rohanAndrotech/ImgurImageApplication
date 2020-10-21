package com.example.imgurimageapplication.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "comment_tbl")
public class CommentModel {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "link")
    public String link;

    @ColumnInfo(name = "position")
    public int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @ColumnInfo(name = "comment")
    public String comment;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

}
