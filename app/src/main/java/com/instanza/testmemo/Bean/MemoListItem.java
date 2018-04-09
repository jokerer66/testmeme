package com.instanza.testmemo.Bean;

import java.io.Serializable;

import static android.R.attr.id;

/**
 * Created by apple on 2017/9/30.
 */

public class MemoListItem implements Serializable {
    private int id;
    private String content;
    private String createtime;
    private String type;
    private String tag;

    public MemoListItem() {
    }

    public MemoListItem(int id, String content, String createtime, String type, String tag) {
        this.id = id;
        this.content = content;
        this.createtime = createtime;
        this.type = type;
        this.tag = tag;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
