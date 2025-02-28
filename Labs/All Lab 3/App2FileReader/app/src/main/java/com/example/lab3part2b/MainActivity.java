package com.example.lab3part2b;

import android.os.Bundle;


import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private final String FILE_PATH = "/data/data/com.example.lab3part2a/files/app1_data.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void readFromFile(View view) {
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            Toast.makeText(this, "File not found!", Toast.LENGTH_SHORT).show();
            return;
        }

        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] data = new byte[(int) file.length()];
            fis.read(data);
            String content = new String(data);

            Toast.makeText(this, "File Content: " + content, Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to read!", Toast.LENGTH_SHORT).show();
        }
    }
}