package fi.tuomela.teemu.blurtermongo.Models;

import java.util.Date;

/**
 * Created by Teemu on 18.10.2016.
 *
 */

public class Comment {

    private String _id;
    private String content;
    private String target;
    private Date date;

    public Comment() {

    }

    public Comment(String content, String target) {
        this.content = content;
        this.target = target;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return content;
    }

}
