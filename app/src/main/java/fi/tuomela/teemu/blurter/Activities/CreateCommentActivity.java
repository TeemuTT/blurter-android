package fi.tuomela.teemu.blurter.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.MultiAutoCompleteTextView;

import fi.tuomela.teemu.blurter.R;

public class CreateCommentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_comment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public void onPostClicked(View v) {
        String content = ((MultiAutoCompleteTextView) findViewById(R.id.multiAutoCompleteTextView2)).getText().toString();
        Intent intent = new Intent();
        intent.putExtra("CONTENT", content);
        setResult(RESULT_OK, intent);
        finish();
    }

}
