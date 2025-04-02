package com.example.encryptionactivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

public class MainActivity extends AppCompatActivity {
    private static final String KEY_ALIAS = "MyKeyAlias";
    private static final int REQUEST_PERMISSION = 1;
    private EditText inputText;
    private static final String CE_FILE = "ce_data.txt";
    private static final String DE_FILE = "de_data.txt";
    private static final String EXT_FILE = "ext_data.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputText = findViewById(R.id.inputText);

        Button encryptBtn = findViewById(R.id.encryptBtn);
        Button ceBtn = findViewById(R.id.ceBtn);
        Button deBtn = findViewById(R.id.deBtn);
        Button extBtn = findViewById(R.id.extBtn);

        encryptBtn.setOnClickListener(view -> encryptData());
        ceBtn.setOnClickListener(view -> readData(getFilesDir(), CE_FILE, "CE Data"));
        deBtn.setOnClickListener(view -> readData(getDataDir(), DE_FILE, "DE Data"));
        extBtn.setOnClickListener(view -> {
            if (checkPermission()) {
                File extDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
                readData(extDir, EXT_FILE, "EXT Data");
            } else {
                requestPermission();
            }
        });
    }

    private boolean checkPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
    }

    private void encryptData() {
        try {
            String text = inputText.getText().toString();
            byte[] encrypted = encrypt(text);

            writeFile(getFilesDir(), CE_FILE, encrypted);
            writeFile(getDataDir(), DE_FILE, encrypted);
            if (checkPermission()) {
                File extDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
                writeFile(extDir, EXT_FILE, encrypted);
            }

            Toast.makeText(this, "Data encrypted and saved!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeFile(File dir, String fileName, byte[] data) throws Exception {
        FileOutputStream fos = new FileOutputStream(new File(dir, fileName));
        fos.write(data);
        fos.close();
    }

    private void readData(File dir, String fileName, String label) {
        try {
            File file = new File(dir, fileName);
            FileInputStream fis = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            fis.read(data);
            fis.close();

            String decrypted = decrypt(data);
            Toast.makeText(this, label + ": " + decrypted, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateKey() throws Exception {
        KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
        keyStore.load(null);

        if (!keyStore.containsAlias(KEY_ALIAS)) {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            KeyGenParameterSpec spec = new KeyGenParameterSpec.Builder(KEY_ALIAS,
                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .build();
            keyGenerator.init(spec);
            keyGenerator.generateKey();
        }
    }

    private byte[] encrypt(String text) throws Exception {
        generateKey();
        KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
        keyStore.load(null);
        SecretKey key = ((KeyStore.SecretKeyEntry) keyStore.getEntry(KEY_ALIAS, null)).getSecretKey();

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] iv = cipher.getIV();
        byte[] encrypted = cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));

        byte[] combined = new byte[iv.length + encrypted.length];
        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(encrypted, 0, combined, iv.length, encrypted.length);
        return combined;
    }

    private String decrypt(byte[] encryptedData) throws Exception {
        generateKey();
        KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
        keyStore.load(null);
        SecretKey key = ((KeyStore.SecretKeyEntry) keyStore.getEntry(KEY_ALIAS, null)).getSecretKey();

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(128, encryptedData, 0, 12);
        cipher.init(Cipher.DECRYPT_MODE, key, spec);
        byte[] decrypted = cipher.doFinal(encryptedData, 12, encryptedData.length - 12);

        return new String(decrypted, StandardCharsets.UTF_8);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission granted!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Permission denied.", Toast.LENGTH_SHORT).show();
        }
    }
}
