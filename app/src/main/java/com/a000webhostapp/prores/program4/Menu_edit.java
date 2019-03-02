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
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

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

public class Menu_edit extends AppCompatActivity {
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
    EditText nama, harga, keterangan;
    SharedPrefManager sharedPrefManager;
    Spinner jenis;
    String id_menu;
    TextView ket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_edit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ket = (TextView)findViewById(R.id.textView3);
        button = (Button)findViewById(R.id.bnTambah);
        nama = (EditText)findViewById(R.id.etNama);
        harga = (EditText)findViewById(R.id.etHarga);
        keterangan = (EditText)findViewById(R.id.etKeterangan);
        jenis = (Spinner)findViewById(R.id.spJenis);
        String[] opsi = {"Makanan","Minuman","Dessert"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, opsi);
        jenis.setAdapter(adapter);

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
                final String Nama, Harga, Keterangan;
                Nama = nama.getText().toString().trim();
                Harga = harga.getText().toString().trim();
                Keterangan = keterangan.getText().toString().trim();

                if(Nama.equals("") || Harga.equals("")) {
                    Toast.makeText(getApplicationContext(), "Harap isi Kolom", Toast.LENGTH_SHORT).show();
                }else {
                    UploadImageToServer();
                }
            }
        });

        if (ContextCompat.checkSelfPermission(Menu_edit.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.CAMERA},
                        5);
            }
        }
        load_data();
    }
    public void load_data(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Data");
        progressDialog.show();

        Intent intent = getIntent();
        if(intent.hasExtra("id_menu")){
            id_menu = intent.getStringExtra("id_menu");
            if(!id_menu.equals("")){
                StringRequest stringRequest = new StringRequest(Request.Method.POST, MySingleton.menu_detail,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressDialog.dismiss();
                                if(response.equals("")){
                                    Toast.makeText(getApplicationContext(), "Harap coba lagi", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if(!(jsonObject.getString("message").equals(""))){
                                        if(jsonObject.getString("error").equals("false")){
                                            JSONObject data = jsonObject.getJSONObject("message");

                                            ket.setText("Menu EDIT "+data.getString("id_menu"));
                                            nama.setText(data.getString("nama"));
                                            harga.setText(data.getString("harga"));
                                            keterangan.setText(data.getString("keterangan"));

                                            if(data.getString("jenis").equalsIgnoreCase("Dessert")){
                                                jenis.setSelection(2);
                                            }else if(data.getString("jenis").equalsIgnoreCase("Minuman")){
                                                jenis.setSelection(1);
                                            }
                                        }else{
                                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    }else{
                                        Toast.makeText(getApplicationContext(), jsonObject.getString("Bermasalah, Harap coba kembali"), Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                                finish();
                            }
                        }
                ) {
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("user_input", sharedPrefManager.getSPId());
                        params.put("id_menu", id_menu);
                        return params;
                    }
                };
                MySingleton.getInstance(Menu_edit.this).addToRequestQueue(stringRequest);
            }else{
                Toast.makeText(getApplicationContext(), "kosong", Toast.LENGTH_LONG).show();
                finish();
            }
        }else{
            Toast.makeText(getApplicationContext(), "kosong", Toast.LENGTH_LONG).show();
            finish();
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
                    //Toast.makeText(Menu_edit.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                    ShowSelectedImage.setImageBitmap(FixBitmap);
                    UploadImageOnServerButton.setVisibility(View.VISIBLE);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(Menu_edit.this, "Failed!", Toast.LENGTH_SHORT).show();
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
                progressDialog = ProgressDialog.show(Menu_edit.this,"Image is Uploading","Please Wait",false,false);
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
                final String Nama, Harga, Keterangan, Jenis, Ket;
                Nama = nama.getText().toString().trim();
                Harga = harga.getText().toString().trim();
                Keterangan = keterangan.getText().toString().trim();
                Jenis = jenis.getSelectedItem().toString();
                Ket = ket.getText().toString().trim();

                HashMap<String,String> HashMapParams = new HashMap<String,String>();
                HashMapParams.put("id_menu", Ket);
                HashMapParams.put("nama", Nama);
                HashMapParams.put("harga", Harga);
                HashMapParams.put("keterangan", Keterangan);
                HashMapParams.put("jenis", Jenis);

                if(ConvertImage != "")
                    HashMapParams.put("image_data", ConvertImage);

                ImageProcessClass imageProcessClass = new ImageProcessClass();
                String FinalData = imageProcessClass.ImageHttpRequest("http://prores.000webhostapp.com/menu_edit", HashMapParams);
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
                Toast.makeText(Menu_edit.this, "Unable to use Camera..Please Allow us to use Camera", Toast.LENGTH_LONG).show();
            }
        }
    }
}