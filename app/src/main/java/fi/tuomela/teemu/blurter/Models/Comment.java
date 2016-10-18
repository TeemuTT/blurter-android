package fi.tuomela.teemu.blurter.Models;

import java.text.DateFormat;
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

    public String get_id() {
        return _id;
    }

    public String getContent() {
        return content;
    }

    public String getTarget() {
        return target;
    }

    public String getDate() {
        DateFormat asd = DateFormat.getDateInstance();
        return asd.format(date);
    }

    @Override
    public String toString() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
