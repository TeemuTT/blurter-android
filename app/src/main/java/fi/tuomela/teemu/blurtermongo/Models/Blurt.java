package fi.tuomela.teemu.blurtermongo.Models;

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

    public Blurt() {

    }

    public Blurt(String name, String content) {
        this.name = name;
        this.content = content;
    }

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
        DateFormat df = DateFormat.getDateInstance();
        return df.format(date);
    }

    public String getContent() {
        return content;
    }

}
