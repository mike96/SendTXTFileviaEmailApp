package com.example.sendtxtfileviaemailapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Add the buttons
        Button save = findViewById(R.id.saveBtn);
        Button send = findViewById(R.id.sendBtn);

        //Set the On Click Listener for saving
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText content = findViewById(R.id.contentET);
                String stuff = content.getText().toString();

                saveFile(stuff);
            }
        });

        //Set the On Click Listener for sending via email
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText email = findViewById(R.id.emailET);
                Editable emailAddress = email.getText();
                String eAddress = emailAddress.toString();
                Context context = v.getContext();

                String filename = "Sample.txt";
                String message = "Here is the sample data!";
                String subject = "Sample Data From Android!";

                sendEmail(filename, subject, message, context, eAddress);
            }
        });


    }

    //Write function for saving the content as a .txt file
    public void saveFile(String content){
        String filename = "Sample.txt";
        Context context = getApplicationContext();
        try(FileOutputStream fileOutputStream = openFileOutput(filename, MODE_PRIVATE)){
            fileOutputStream.write(content.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Write function for sending the email with the attachment
    public void sendEmail(String attachmentFile, String subject, String message, Context context, String email){
        try{
            final Intent SENDEMAIL = new Intent(Intent.ACTION_SEND);
            SENDEMAIL.setType("text/plain");
            SENDEMAIL.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{email});
            SENDEMAIL.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
            SENDEMAIL.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            SENDEMAIL.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            File path = new File(MainActivity.this.getFilesDir(), "/");
            File newFile = new File(path, attachmentFile);
            Uri uri = FileProvider.getUriForFile(MainActivity.this.getApplicationContext(), "com.example.sendtxtfileviaemailapp", newFile);

            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            SENDEMAIL.putExtra(Intent.EXTRA_STREAM, uri);
            SENDEMAIL.putExtra(android.content.Intent.EXTRA_TEXT, message);
            context.startActivity(Intent.createChooser(SENDEMAIL, "Sending email..."));

        }catch (Throwable t){
            t.printStackTrace();
        }
    }
}
