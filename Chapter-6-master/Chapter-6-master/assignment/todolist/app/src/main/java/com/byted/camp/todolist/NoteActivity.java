package com.byted.camp.todolist;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.byted.camp.todolist.db.TodoContract;
import com.byted.camp.todolist.db.TodoDbHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NoteActivity extends AppCompatActivity {

    private EditText editText;
    private Button addBtn;
    private String priority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        setTitle(R.string.take_a_note);

        editText = findViewById(R.id.edit_text);
        editText.setFocusable(true);
        editText.requestFocus();

        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null) {
            inputManager.showSoftInput(editText, 0);
        }

        RadioGroup radioGroup=(RadioGroup)findViewById(R.id.choose);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = radioGroup.getCheckedRadioButtonId();
                RadioButton choise = (RadioButton)findViewById(id);
                priority = choise.getText().toString();
            }
        });

        addBtn = findViewById(R.id.btn_add);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence content = editText.getText();
                if (TextUtils.isEmpty(content)) {
                    Toast.makeText(NoteActivity.this,
                            "No content to add", Toast.LENGTH_SHORT).show();
                    return;
                }
                boolean succeed = saveNote2Database(content.toString().trim(),priority);
                if (succeed) {
                    Toast.makeText(NoteActivity.this,
                            "Note added", Toast.LENGTH_SHORT).show();
                    Intent intent= new Intent();
                    intent.putExtra("number", "true");
                    setResult(Activity.RESULT_OK,intent);
                } else {
                    Toast.makeText(NoteActivity.this,
                            "Error", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private boolean saveNote2Database(String content,String priority) {
        // TODO 插入一条新数据，返回是否插入成功
        TodoDbHelper dbHelper = new TodoDbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TodoContract.TodoEntry.COLUMN_NAME_SUBTITLE, content);
        int pri;
        switch (priority){
            case "high":
                pri=1;
                break;
            case "middle":
                pri=2;
                break;
            default:
                pri=3;
                break;
        }
        values.put(TodoContract.TodoEntry.COLUMN_NAME_PRIORITY, pri);
        DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        Date date=new Date();
        values.put(TodoContract.TodoEntry.COLUMN_NAME_TITLE,df.format(date));
        values.put(TodoContract.TodoEntry.COLUMN_NAME_CONTENT, 0);

        long newRowID = db.insert(TodoContract.TodoEntry.TABLE_NAME,null, values);
        dbHelper.close();

        return true;
    }
}
