package fi.tuomela.teemu.blurter.Models;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by Teemu on 18.10.2016.
 *
 */

public class Blurt {

    private String _id;
    private String name;
    private Date date;
    private String content;

    @Override
    public String toString() {
        return name;
    }

    public String get_id() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
//        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat asd = DateFormat.getDateInstance();
        return asd.format(date);
    }

    public String getContent() {
        return content;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
