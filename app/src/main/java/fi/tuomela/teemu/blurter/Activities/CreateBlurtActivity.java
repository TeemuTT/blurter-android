package fi.tuomela.teemu.blurter.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;

import fi.tuomela.teemu.blurter.R;

public class CreateBlurtActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_blurt);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public void onPostClicked(View v) {
        String header = ((EditText) findViewById(R.id.editText)).getText().toString();
        String content = ((MultiAutoCompleteTextView) findViewById(R.id.multiAutoCompleteTextView)).getText().toString();
        Intent intent = new Intent();
        intent.putExtra("HEADER", header);
        intent.putExtra("CONTENT", content);
        setResult(RESULT_OK, intent);
        finish();
    }

}
