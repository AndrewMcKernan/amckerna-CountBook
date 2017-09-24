package com.example.chrygore.countbook;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;

import static android.provider.Telephony.Mms.Part.FILENAME;

/**
 * Created by chrygore on 22/09/17.
 */

public class EditRecord extends AppCompatActivity {
    //this class is similar to
    int listPosition;
    ArrayList<ListRecord> listItems = new ArrayList<ListRecord>();
    ListRecord viewingRecord;
    TextView date;
    TextView name;
    TextView initVal;
    TextView currVal;
    TextView comments;

    public void onCreate(Bundle SavedInstanceState){
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.edit_record);
        //get the record that the user selected
        listPosition = getIntent().getIntExtra("position",0);
        loadFromFile();
        viewingRecord = listItems.get(listPosition);
        populatePage();
    }

    public void populatePage(){
        //fill all the fields on the page with the contents of the viewingRecord
        date = (TextView) findViewById(R.id.dateContainer);
        date.setText(viewingRecord.getDateOfCreation());
        name = (TextView) findViewById(R.id.nameContainer);
        name.setText(viewingRecord.getTitle());
        initVal = (TextView) findViewById(R.id.initValContainer);
        initVal.setText(Integer.toString(viewingRecord.getInitialValue()));
        currVal = (TextView) findViewById(R.id.currentValContainer);
        currVal.setText(Integer.toString(viewingRecord.getCurrentValue()));
        comments = (TextView) findViewById(R.id.commentsContainer);
        comments.setText(viewingRecord.getComments());
    }

    public void saveChanges(View v){
        viewingRecord.setTitle(name.getText().toString());
        if (viewingRecord.getCurrentValue() != Integer.parseInt(currVal.getText().toString())){
            viewingRecord.setDate(new Date());
        }
        viewingRecord.setCurrentValue(Integer.parseInt(currVal.getText().toString()));
        viewingRecord.setComments(comments.getText().toString());
        listItems.set(listPosition,viewingRecord);
        saveInFile();
        finish();
    }

    public void resetInitialValue(View v){
        viewingRecord.setInitialValue(Integer.parseInt(currVal.getText().toString()));
        initVal.setText(currVal.getText().toString());
    }

    public void deleteRecord(View v){
        listItems.remove(listPosition);
        saveInFile();
        finish();
    }

    private void loadFromFile() {
        try {
            FileInputStream fis = openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<ListRecord>>() {}.getType();
            listItems = gson.fromJson(in,listType);
            //https://github.com/google/gson/blob/master/UserGuide.md
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            listItems = new ArrayList<ListRecord>();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }
    }

    private void saveInFile() {
        try {
            FileOutputStream fos = openFileOutput(FILENAME,
                    Context.MODE_PRIVATE);
            OutputStreamWriter writer = new OutputStreamWriter(fos);
            Gson gson = new Gson();
            gson.toJson(listItems, writer);
            writer.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }
    }
}