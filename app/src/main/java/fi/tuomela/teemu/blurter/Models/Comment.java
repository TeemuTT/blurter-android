package fi.tuomela.teemu.blurter.Models;

/**
 * Created by Teemu on 22.10.2016.
 *
 */

public class Comment {

    public String id;
    public String content;
    public String target;
    public String date;

    public Comment() {

    }

    public Comment(String id, String content, String target, String date) {
        this.id = id;
        this.content = content;
        this.target = target;
        this.date = date;
    }

    @Override
    public String toString() {
        return content;
    }
}
