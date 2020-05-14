package com.byted.camp.todolist;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.byted.camp.todolist.beans.Note;
import com.byted.camp.todolist.db.TodoContract;
import com.byted.camp.todolist.db.TodoDbHelper;
import com.byted.camp.todolist.operation.activity.DatabaseActivity;
import com.byted.camp.todolist.operation.activity.DebugActivity;
import com.byted.camp.todolist.operation.activity.SettingActivity;
import com.byted.camp.todolist.ui.NoteListAdapter;
import com.byted.camp.todolist.beans.State;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_ADD = 1002;
    private static final String TAG="DATA IN DATABASE： ";

    private RecyclerView recyclerView;
    private NoteListAdapter notesAdapter;

    public TodoDbHelper dbHelper = new TodoDbHelper(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        dbHelper.getWritableDatabase();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(
                        new Intent(MainActivity.this, NoteActivity.class),
                        REQUEST_CODE_ADD);
            }
        });

        recyclerView = findViewById(R.id.list_todo);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        notesAdapter = new NoteListAdapter(new NoteOperator() {
            @Override
            public void deleteNote(Note note) {
                MainActivity.this.deleteNote(note);
            }

            @Override
            public void updateNote(Note note) {
                MainActivity.this.updateNode(note);
            }

        });
        recyclerView.setAdapter(notesAdapter);

        notesAdapter.refresh(loadNotesFromDatabase());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingActivity.class));
                return true;
            case R.id.action_debug:
                startActivity(new Intent(this, DebugActivity.class));
                return true;
            case R.id.action_database:
                startActivity(new Intent(this, DatabaseActivity.class));
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD
                && resultCode == Activity.RESULT_OK) {
            notesAdapter.refresh(loadNotesFromDatabase());
        }
    }

    private List<Note> loadNotesFromDatabase() {
        // TODO 从数据库中查询数据，并转换成 JavaBeans
        Log.d(TAG, "using loadNotesFromDatabase:");
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Note> notes = new ArrayList<>();

        String[] projection={
                BaseColumns._ID,
                TodoContract.TodoEntry.COLUMN_NAME_TITLE,
                TodoContract.TodoEntry.COLUMN_NAME_SUBTITLE,
                TodoContract.TodoEntry.COLUMN_NAME_PRIORITY,
                TodoContract.TodoEntry.COLUMN_NAME_CONTENT
        };
        String sortOder = TodoContract.TodoEntry.COLUMN_NAME_PRIORITY + " ASC";

        Cursor c =db.query(
                TodoContract.TodoEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOder
        );
//        c.moveToFirst();

        while(c.moveToNext())
        {
            Note note = new Note(c.getInt(0));
            SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
            try {
                java.util.Date d1=df.parse(c.getString(1));//这个格式必须按照上面给出的格式进行转化否则出错
                note.setDate(d1);
            } catch (ParseException e) {
                e.printStackTrace();//为啥抛出异常的  不是随便的字符串都可以可以转化为日期吗的
            }
            note.setContent(c.getString(2));
            note.setPriority(c.getInt(3));
            if(c.getInt(4)==0)
                note.setState(State.TODO);
            else
                note.setState(State.DONE);
            Log.d(TAG, "loadNotesFromDatabase cursor:  "+c.getCount());
            notes.add(note);

        }
        Log.d(TAG, "loadNotesFromDatabase count : "+c.getCount());
        for(Note n:notes)
        {
            Log.d(TAG, "loadNotesFromDatabase: "+n.id);
        }
        c.close();
        return notes;
    }

    private void deleteNote(Note note) {
        // TODO 删除数据
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = TodoContract.TodoEntry._ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(note.id)};
        int deleteRows = db.delete(TodoContract.TodoEntry.TABLE_NAME, selection, selectionArgs);
    }

    private void updateNode(Note note) {
        // 更新数据
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TodoContract.TodoEntry.COLUMN_NAME_CONTENT, 1);

        String selection = TodoContract.TodoEntry._ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(note.id)};
        int count = db.update(
                TodoContract.TodoEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs
        );
    }

}
