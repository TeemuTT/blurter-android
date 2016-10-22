package fi.tuomela.teemu.blurter.Models;

/**
 * Created by Teemu on 22.10.2016.
 *
 */

public class Blurt {

    public String id;
    public String name;
    public String date;
    public String content;

    public Blurt() {

    }

    public Blurt(String id, String name, String date, String content) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.content = content;
    }

    @Override
    public String toString() {
        return name;
    }
}
