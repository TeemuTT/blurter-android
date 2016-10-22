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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import fi.tuomela.teemu.blurter.Models.Blurt;
import fi.tuomela.teemu.blurter.Models.Comment;
import fi.tuomela.teemu.blurter.R;
import fi.tuomela.teemu.blurter.Utilities.CustomArrayAdapter;

public class BlurtActivity extends AppCompatActivity {

    private Blurt blurt;
    private ArrayAdapter<Comment> mAdapter;
    private DatabaseReference database;

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

        // Get the Blurt from the intent that launched this activity.
        Intent intent = getIntent();
        String message = intent.getStringExtra("BLURT");
        blurt = new Gson().fromJson(message, Blurt.class);

        // Set data to layout.
        TextView header = (TextView) findViewById(R.id.header);
        header.setText(blurt.name);
        TextView date = (TextView) findViewById(R.id.date);
        DateFormat df = DateFormat.getDateInstance();
        Date d;
        try {
            d = df.parse(blurt.date);
        } catch (ParseException pe) {
            d = new Date();
        }
        date.setText(df.format(d));
        TextView content = (TextView) findViewById(R.id.content);
        content.setText(blurt.content);

        // ListView for comments.
        ListView mListView = (ListView) findViewById(R.id.comments);

        // Custom adapter to populate ListView.
        mAdapter = new CustomArrayAdapter<>(this, R.layout.custom_list_item_1);
        mListView.setAdapter(mAdapter);

        // Reference to Firebase database.
        database = FirebaseDatabase.getInstance().getReference();

        // Reference to all comments for a specific blurt.
        DatabaseReference commentsref = database.child("comments").child(blurt.id);

        // Listen to changes in the database.
        commentsref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // Whenever a comment is created, we add it to our adapter.
                Comment comment = dataSnapshot.getValue(Comment.class);
                comment.id = dataSnapshot.getKey();
                mAdapter.insert(comment, 0);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Failed to read data: " + databaseError.getCode());
            }
        });

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

    private void CreateComment(String content) {
        Comment comment = new Comment();
        comment.target = blurt.id;
        comment.date = new Date().toString();
        comment.content = content;

        DatabaseReference commentsref = database.child("comments").child(blurt.id);
        commentsref.push().setValue(comment);
    }
}
