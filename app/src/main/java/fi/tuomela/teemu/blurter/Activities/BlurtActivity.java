package fi.tuomela.teemu.blurter.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

import fi.tuomela.teemu.blurter.Models.Blurt;
import fi.tuomela.teemu.blurter.Models.Comment;
import fi.tuomela.teemu.blurter.R;
import fi.tuomela.teemu.blurter.RetrofitServices.CommentService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BlurtActivity extends AppCompatActivity {

    private Blurt blurt;
    private ArrayAdapter<Comment> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blurt);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateComment(mAdapter);
            }
        });

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
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        mListView.setAdapter(mAdapter);

        GetComments(mAdapter);
    }

    private void GetComments(final ArrayAdapter<Comment> adapter) {
        CommentService commentService = CommentService.retrofit.create(CommentService.class);
        final Call<List<Comment>> call = commentService.comments(blurt.get_id());
        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                adapter.clear();
                adapter.addAll((response.body()));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                System.out.println("Failed to get data");
            }
        });
    }

    private void CreateComment(final ArrayAdapter<Comment> adapter) {
        CommentService commentService = CommentService.retrofit.create(CommentService.class);
        Comment comment = new Comment();
        comment.setTarget(blurt.get_id());
        comment.setContent("JustSomeFillerContent");
        final Call<Comment> call = commentService.createComment(comment);
        call.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                GetComments(adapter);
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                System.out.println("Failed to create Blurt");
            }
        });
    }
}
