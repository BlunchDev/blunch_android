package dev.blunch.blunch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import dev.blunch.blunch.R;

public class MainActivity extends AppCompatActivity {

    private Firebase mRef;
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent i = new Intent( MainActivity.this, CollaborativeMenuAnswerView.class);

        startActivity(i);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = new Intent(getApplicationContext(), NewCollaborativeMenuActivity.class);
        startActivity(intent);

        Firebase.setAndroidContext(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mRef = new Firebase("https://blunch.firebaseio.com/chat");
        mRef.setValue("New chat");

        Button send = (Button) findViewById(R.id.send);
        final TextView text = (TextView) findViewById(R.id.textView);
        final EditText editText = (EditText) findViewById(R.id.editText);

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                count = (int) dataSnapshot.getChildrenCount();
                Iterable<DataSnapshot> it = dataSnapshot.getChildren();
                String messages = "";
                while (it.iterator().hasNext()) {
                    String s = (String) it.iterator().next().getValue();
                    messages += s + "\n";
                }
                text.setText(messages);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = editText.getText().toString();
                mRef.child(count + "").setValue(s);
                editText.setText("");
                ++count;
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
