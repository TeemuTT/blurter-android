package fi.tuomela.teemu.blurter.Utilities;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Random;

import fi.tuomela.teemu.blurter.R;

/**
 * Created by Teemu on 19.10.2016.
 *
 */

public class CustomArrayAdapter extends ArrayAdapter {

    private Random random = new Random();

    public CustomArrayAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.custom_list_item_1, null);
        }

        String p = getItem(position).toString();

        ((TextView) v.findViewById(android.R.id.text1)).setText(p);

        v.setBackgroundColor(generateRandomColor());

        return v;
    }

    private int generateRandomColor() {
        int red = random.nextInt(256);
        int green = random.nextInt(256);
        int blue = random.nextInt(256);

        red = (red + 255) / 2;
        green = (green + 255) / 2;
        blue = (blue + 255) / 2;

        int color = Color.argb(255, red, green, blue);
        return color;
    }

}
