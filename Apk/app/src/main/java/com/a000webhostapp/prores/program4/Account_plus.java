package com.a000webhostapp.prores.program4;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import static java.lang.System.out;

public class Account_plus extends AppCompatActivity {
    Button GetImageFromGalleryButton, UploadImageOnServerButton;
    ImageView ShowSelectedImage;
    Bitmap FixBitmap;
    ProgressDialog progressDialog ;
    ByteArrayOutputStream byteArrayOutputStream ;
    byte[] byteArray ;
    String ConvertImage ;
    HttpURLConnection httpURLConnection ;
    URL url;
    OutputStream outputStream;
    BufferedWriter bufferedWriter ;
    int RC ;
    BufferedReader bufferedReader ;
    StringBuilder stringBuilder;
    boolean check = true;
    private int GALLERY = 1, CAMERA = 2;
    Button button;
    EditText namalengkap, username, password, password0, alamat, notlpn, email;
    SharedPrefManager sharedPrefManager;
    Spinner level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_plus);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        button = (Button)findViewById(R.id.bnTambah);
        namalengkap = (EditText)findViewById(R.id.etNamalengkap);
        username = (EditText)findViewById(R.id.etUsername);
        password = (EditText)findViewById(R.id.etPassword);
        password0 = (EditText)findViewById(R.id.etPassword0);
        alamat = (EditText)findViewById(R.id.etAlamat);
        notlpn = (EditText)findViewById(R.id.etNotlpn);
        email = (EditText)findViewById(R.id.etEmail);
        level = (Spinner)findViewById(R.id.spLevel);
        String[] opsi = {"Pelanggan","Staff","Manager"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, opsi);
        level.setAdapter(adapter);

        GetImageFromGalleryButton = (Button)findViewById(R.id.buttonSelect);
        UploadImageOnServerButton = (Button)findViewById(R.id.bnTambah);
        ShowSelectedImage = (ImageView)findViewById(R.id.imageView);
        byteArrayOutputStream = new ByteArrayOutputStream();

        sharedPrefManager = new SharedPrefManager(this);
        if (sharedPrefManager.getSPSudahLogin() == false){
            finish();
        }

        GetImageFromGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPictureDialog();
            }
        });


        UploadImageOnServerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String Namalengkap, Username, Password, Password0, Alamat, Notlpn, Email;
                Namalengkap = namalengkap.getText().toString().trim();
                Username = username.getText().toString().trim();
                Password = password.getText().toString().trim();
                Password0 = password0.getText().toString().trim();
                Alamat = alamat.getText().toString().trim();
                Notlpn = notlpn.getText().toString().trim();
                Email = email.getText().toString().trim();

                if(Namalengkap.equals("") || Username.equals("") || Password.equals("") || Email.equals("")) {
                    Toast.makeText(getApplicationContext(), "Harap isi Kolom", Toast.LENGTH_SHORT).show();
                }else if(!(Password.equals(Password0))) {
                    Toast.makeText(getApplicationContext(), "Password not match", Toast.LENGTH_SHORT).show();
                }else if(Namalengkap.length() > 255) {
                    Toast.makeText(getApplicationContext(), "Panjang Nama Lengkap max 255", Toast.LENGTH_SHORT).show();
                }else if(Username.length() > 255) {
                    Toast.makeText(getApplicationContext(), "Panjang Username max 255", Toast.LENGTH_SHORT).show();
                }else if(Password.length() > 255) {
                    Toast.makeText(getApplicationContext(), "Panjang Nama Lengkap max 255", Toast.LENGTH_SHORT).show();
                }else if(Alamat.length() > 255) {
                    Toast.makeText(getApplicationContext(), "Panjang Alamat max 255", Toast.LENGTH_SHORT).show();
                }else if(Notlpn.length() > 255) {
                    Toast.makeText(getApplicationContext(), "Panjang Notlpn max 255", Toast.LENGTH_SHORT).show();
                }else if(Email.length() > 255) {
                    Toast.makeText(getApplicationContext(), "Panjang Email max 255", Toast.LENGTH_SHORT).show();
                }else if(!(android.util.Patterns.EMAIL_ADDRESS.matcher(Email).matches())) {
                    Toast.makeText(getApplicationContext(), "Email not valid", Toast.LENGTH_SHORT).show();
                }else {
                    UploadImageToServer();
                }
            }
        });

        if (ContextCompat.checkSelfPermission(Account_plus.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.CAMERA},
                        5);
            }
        }
    }

    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Photo Gallery",
                "Camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }
    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    FixBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    // String path = saveImage(bitmap);
                    //Toast.makeText(Account_plus.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                    ShowSelectedImage.setImageBitmap(FixBitmap);
                    UploadImageOnServerButton.setVisibility(View.VISIBLE);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(Account_plus.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == CAMERA) {
            FixBitmap = (Bitmap) data.getExtras().get("data");
            ShowSelectedImage.setImageBitmap(FixBitmap);
            UploadImageOnServerButton.setVisibility(View.VISIBLE);
            //  saveImage(thumbnail);
            //Toast.makeText(ShadiRegistrationPart5.this, "Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }

    public void UploadImageToServer(){
        if(ShowSelectedImage.getDrawable() != null){
            FixBitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream);
            byteArray = byteArrayOutputStream.toByteArray();
            ConvertImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
        }else{
            ConvertImage = "";
        }
        class AsyncTaskUploadClass extends AsyncTask<Void,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(Account_plus.this,"Image is Uploading","Please Wait",false,false);
            }

            @Override
            protected void onPostExecute(String string1) {
                super.onPostExecute(string1);
                progressDialog.dismiss();
                if(!string1.toString().trim().equals("")){
                    try {
                        JSONObject jsonObject = new JSONObject(string1);
                        if(!(jsonObject.getString("message").equals(""))){
                            if(jsonObject.getString("error").equals("false")){
                                System.out.println("ok0");
                                Toast.makeText(getApplicationContext(), "Berhasil Input", Toast.LENGTH_SHORT).show();
                                finish();
                            }else{
                                System.out.println("ok1");
                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            System.out.println("ok2");
                            Toast.makeText(getApplicationContext(), "Bermasalah, Harap coba kembali", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        System.out.println("ok3");
                        System.out.println(e.getMessage());
                        Toast.makeText(getApplicationContext(), string1, Toast.LENGTH_SHORT).show();
                    }
                }else{
                    finish();
                }
            }

            @Override
            protected String doInBackground(Void... params) {
                final String Namalengkap, Username, Password, Password0, Alamat, Notlpn, Email, Level;
                Namalengkap = namalengkap.getText().toString().trim();
                Username = username.getText().toString().trim();
                Password = password.getText().toString().trim();
                Password0 = password0.getText().toString().trim();
                Alamat = alamat.getText().toString().trim();
                Notlpn = notlpn.getText().toString().trim();
                Email = email.getText().toString().trim();
                Level = level.getSelectedItem().toString();

                HashMap<String,String> HashMapParams = new HashMap<String,String>();
                HashMapParams.put("nama_lengkap", Namalengkap);
                HashMapParams.put("username", Username);
                HashMapParams.put("password", Password);
                HashMapParams.put("password0", Password0);
                HashMapParams.put("alamat", Alamat);
                HashMapParams.put("notlpn", Notlpn);
                HashMapParams.put("email", Email);
                HashMapParams.put("level", Level);
                if(ConvertImage != "")
                    HashMapParams.put("image_data", ConvertImage);

                ImageProcessClass imageProcessClass = new ImageProcessClass();
                String FinalData = imageProcessClass.ImageHttpRequest("http://prores.000webhostapp.com/account_plus", HashMapParams);
                return FinalData;
            }
        }
        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();
        AsyncTaskUploadClassOBJ.execute();
    }

    public class ImageProcessClass{
        public String ImageHttpRequest(String requestURL,HashMap<String, String> PData) {
            StringBuilder stringBuilder = new StringBuilder();

            try {
                url = new URL(requestURL);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(20000);
                httpURLConnection.setConnectTimeout(20000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                outputStream = httpURLConnection.getOutputStream();
                bufferedWriter = new BufferedWriter(
                        new OutputStreamWriter(outputStream, "UTF-8"));
                bufferedWriter.write(bufferedWriterDataFN(PData));
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                RC = httpURLConnection.getResponseCode();
                if (RC == HttpsURLConnection.HTTP_OK) {
                    bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    stringBuilder = new StringBuilder();
                    String RC2;
                    while ((RC2 = bufferedReader.readLine()) != null){
                        stringBuilder.append(RC2);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }

        private String bufferedWriterDataFN(HashMap<String, String> HashMapParams) throws UnsupportedEncodingException {
            stringBuilder = new StringBuilder();
            for (Map.Entry<String, String> KEY : HashMapParams.entrySet()) {
                if (check)
                    check = false;
                else
                    stringBuilder.append("&");
                stringBuilder.append(URLEncoder.encode(KEY.getKey(), "UTF-8"));
                stringBuilder.append("=");
                stringBuilder.append(URLEncoder.encode(KEY.getValue(), "UTF-8"));
            }
            return stringBuilder.toString();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 5) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Now user should be able to use camera
            }else {
                Toast.makeText(Account_plus.this, "Unable to use Camera..Please Allow us to use Camera", Toast.LENGTH_LONG).show();
            }
        }
    }
}