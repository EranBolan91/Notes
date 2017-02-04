package com.world.bolandian.notes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

  static  ArrayList<String> notes = new  ArrayList<>();
  static ArrayAdapter arrayAdapter;

    //this method connect the menu bar with the activity from the R.menu folder
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.addNote){
            Intent intent = new Intent(getApplicationContext(),NoteEditor.class);
            startActivity(intent);
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = (ListView)findViewById(R.id.listView);

        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("com.world.bolandian.notes", Context.MODE_PRIVATE);

        HashSet<String> set = (HashSet<String>)sharedPreferences.getStringSet("notes", null);

        if(set == null){

            notes.add("init note");
        }else{
            notes = new ArrayList(set);
        }


        // connects between the listview to the arrayAdpter
         arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_activated_1,notes);
        listView.setAdapter(arrayAdapter);

        //this method gets in when one of the arrylist item clicked.
        // it jumps to the NoteEditor activity with the position
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(),NoteEditor.class);
                intent.putExtra("notePosition",position); // that tells us which row number was tapped
                startActivity(intent);
            }
        });

        // this method gets in when the user long click on one of the notes in the list
        // and open a dialog box alert to delete the note that he choose
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Remove a note")
                        .setMessage("Are you sure you want to delete this note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                notes.remove(position);
                                arrayAdapter.notifyDataSetChanged();

                                SharedPreferences sharedPreferences = getApplication().getSharedPreferences("com.world.bolandian.notes", Context.MODE_PRIVATE);
                                HashSet<String> set = new HashSet(MainActivity.notes);
                                sharedPreferences.edit().putStringSet("notes",set).apply();

                            }
                        }).setNegativeButton("No",null)
                         .show();

                return true;
            }
        });

    }
}
