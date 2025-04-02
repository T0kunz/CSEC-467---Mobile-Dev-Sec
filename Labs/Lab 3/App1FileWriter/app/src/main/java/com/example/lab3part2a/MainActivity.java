package com.example.lab3part2a;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private EditText inputText;
    private final String FILE_NAME = "app1_data.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputText = findViewById(R.id.inputText);
    }

    public void saveToFile(View view) {
        String text = inputText.getText().toString();

        if (text.isEmpty()) {
            Toast.makeText(this, "Enter text to save!", Toast.LENGTH_SHORT).show();
            return;
        }

        File file = new File(getFilesDir(), FILE_NAME);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(text.getBytes());
            Toast.makeText(this, "Saved to " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to save!", Toast.LENGTH_SHORT).show();
        }
    }
}
