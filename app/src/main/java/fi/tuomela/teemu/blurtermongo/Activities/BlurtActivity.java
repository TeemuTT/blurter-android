package fi.tuomela.teemu.blurtermongo.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

import fi.tuomela.teemu.blurtermongo.Models.Blurt;
import fi.tuomela.teemu.blurtermongo.Models.Comment;
import fi.tuomela.teemu.blurtermongo.R;
import fi.tuomela.teemu.blurtermongo.RetrofitServices.CommentService;
import fi.tuomela.teemu.blurtermongo.Utilities.CustomArrayAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BlurtActivity extends AppCompatActivity {

    private Blurt blurt;
    private ArrayAdapter<Comment> mAdapter;
    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blurt);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(fab.getContext(), CreateCommentActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        String message = intent.getStringExtra("BLURT");
        blurt = new Gson().fromJson(message, Blurt.class);

        TextView header = (TextView) findViewById(R.id.header);
        header.setText(blurt.getName());
        TextView date = (TextView) findViewById(R.id.date);
        date.setText(blurt.getDate());
        TextView content = (TextView) findViewById(R.id.content);
        content.setText(blurt.getContent());

        ListView mListView = (ListView) findViewById(R.id.comments);
        mAdapter = new CustomArrayAdapter<>(this, R.layout.custom_list_item_1);
        mListView.setAdapter(mAdapter);

        // Initialize our Retrofit instance.
        retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.api_base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GetComments();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                String content = data.getStringExtra("CONTENT");
                CreateComment(content);
            }
        }
    }

    private void GetComments() {
        CommentService commentService = retrofit.create(CommentService.class);
        final Call<List<Comment>> call = commentService.comments(blurt.get_id());
        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                mAdapter.clear();
                mAdapter.addAll((response.body()));
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                Snackbar.make(findViewById(R.id.content_blurt), R.string.error_get_comment, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void CreateComment(String content) {
        CommentService commentService = retrofit.create(CommentService.class);
        Comment comment = new Comment(content, blurt.get_id());
        final Call<Comment> call = commentService.createComment(comment);
        call.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                GetComments();
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                Snackbar.make(findViewById(R.id.content_blurt), R.string.error_create_comment, Snackbar.LENGTH_LONG).show();
            }
        });
    }
}
