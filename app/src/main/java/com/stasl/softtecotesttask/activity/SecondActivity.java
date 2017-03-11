package com.stasl.softtecotesttask.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.stasl.softtecotesttask.R;
import com.stasl.softtecotesttask.user.Address;
import com.stasl.softtecotesttask.user.Geo;
import com.stasl.softtecotesttask.user.UserModel;

import java.util.concurrent.ExecutionException;

public class SecondActivity extends AppCompatActivity implements View.OnClickListener
{
    private TextView postID, postIDLabel;
    private TextView userName, userNameLabel;
    private TextView userNickname, userNickNameLabel;
    private TextView userEmail, userEmailLabel;
    private TextView userWebsite, userWebsiteLabel;
    private TextView userPhoneNumber, userPhoneNumberLabel;
    private TextView userCity, userCityLabel;
    private Button saveToDBButton;
    private DataBase dbHelper;
    private SQLiteDatabase db;
    private UserModel user;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        dbHelper = new DataBase(this);
        db = dbHelper.getWritableDatabase();
        postID = (TextView)findViewById(R.id.postID);
        postIDLabel = (TextView)findViewById(R.id.postIDLabel);
        userName = (TextView)findViewById(R.id.userName);
        userNameLabel = (TextView)findViewById(R.id.userNameLabel);
        userNickname = (TextView)findViewById(R.id.userNickName);
        userNickNameLabel = (TextView)findViewById(R.id.userNickNameLabel);
        userEmail = (TextView)findViewById(R.id.userEmail);
        userEmail.setOnClickListener(this);
        userEmailLabel = (TextView)findViewById(R.id.userEmailLabel);
        userWebsite = (TextView)findViewById(R.id.userWebsite);
        userWebsite.setOnClickListener(this);
        userWebsiteLabel = (TextView)findViewById(R.id.userWebsiteLabel);
        userPhoneNumber = (TextView)findViewById(R.id.userPhoneNumber);
        userPhoneNumber.setOnClickListener(this);
        userPhoneNumberLabel = (TextView)findViewById(R.id.userPhoneNumberLabel);
        saveToDBButton = (Button)findViewById(R.id.saveToDBButton);
        saveToDBButton.setOnClickListener(this);
        userCity = (TextView)findViewById(R.id.userCity);
        userCity.setOnClickListener(this);
        userCityLabel = (TextView)findViewById(R.id.userCityLabel);
        Intent intent = getIntent();
        getSupportActionBar().setTitle("Contact #" + intent.getStringExtra("userID"));
        postIDLabel.setText("Post #");
        userNameLabel.setText("Name");
        userNickNameLabel.setText("NickName");
        userEmailLabel.setText("Email");
        userWebsiteLabel.setText("Website");
        userPhoneNumberLabel.setText("Phone Number");
        userCityLabel.setText("City");
        postID.setText(intent.getStringExtra("postID"));
        if (!checkUser(Integer.parseInt(intent.getStringExtra("userID"))))
        {
            user = null;
            try {
                user = MainActivity.getUser(Integer.parseInt(intent.getStringExtra("userID")));
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (user != null)
            {
                setUser(user.getName(), user.getUsername(), user.getEmail(), user.getWebsite(), user.getPhone(), user.getAddress().getCity());
                Snackbar.make(findViewById(android.R.id.content), "Loaded user info from site", Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId())
        {
            case R.id.saveToDBButton:
                saveUser(user);
                Snackbar.make(findViewById(android.R.id.content), "Saved user#" + getIntent().getStringExtra("userID") + " into DataBase", Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.userEmail:
                intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + user.getEmail()));
                startActivity(intent);
                break;
            case R.id.userWebsite:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + user.getWebsite()));
                startActivity(intent);
                break;
            case R.id.userPhoneNumber:
                intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + user.getPhone()));
                startActivity(intent);
                break;
            case R.id.userCity:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:"+ user.getAddress().getGeo().getLat() +","+ user.getAddress().getGeo().getLng()));
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);
                break;
        }
    }
    private class DataBase extends SQLiteOpenHelper
    {
        DataBase(Context context) {
            super(context, "usersDB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d("DataBase", "Creating database");
            db.execSQL("create table users (id integer primary key, name text, nickname text, email text, website text, phone text, city text, geolat text, geolng text);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            throw new UnsupportedOperationException();
        }
    }
    private void saveUser(UserModel user)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", user.getId());
        contentValues.put("name", user.getName());
        contentValues.put("nickname", user.getUsername());
        contentValues.put("email", user.getEmail());
        contentValues.put("website", user.getWebsite());
        contentValues.put("phone", user.getPhone());
        contentValues.put("city", user.getAddress().getCity());
        contentValues.put("geolat", user.getAddress().getGeo().getLat());
        contentValues.put("geolng", user.getAddress().getGeo().getLng());
        long rowID = db.insert("users", null, contentValues);
        Log.d("User saved", "row inserted, ID = " + rowID);
    }
    private boolean checkUser(int id)
    {
        Cursor c = db.query("users", null, null, null, null, null, null);
        if (c.moveToFirst())
        {
            int idColIndex = c.getColumnIndex("id");
            int nameColIndex = c.getColumnIndex("name");
            int nickNameColIndex = c.getColumnIndex("nickname");
            int emailColIndex = c.getColumnIndex("email");
            int websiteColIndex = c.getColumnIndex("website");
            int phoneColIndex = c.getColumnIndex("phone");
            int cityColIndex = c.getColumnIndex("city");
            int geoLatColIndex = c.getColumnIndex("geolat");
            int geoLngIndex = c.getColumnIndex("geolng");
            do
            {
                if (c.getInt(idColIndex) == id)
                {
                    user = new UserModel();
                    Address address = new Address();
                    Geo geo = new Geo();
                    geo.setLat(c.getString(geoLatColIndex));
                    geo.setLng(c.getString(geoLngIndex));
                    address.setGeo(geo);
                    address.setCity(c.getString(cityColIndex));
                    user.setAddress(address);
                    user.setEmail(c.getString(emailColIndex));
                    user.setName(c.getString(nameColIndex));
                    user.setUsername(c.getString(nickNameColIndex));
                    user.setWebsite(c.getString(websiteColIndex));
                    user.setPhone(c.getString(phoneColIndex));
                    setUser(user.getName(), user.getUsername(), user.getEmail(), user.getWebsite(), user.getPhone(), user.getAddress().getCity());
                    Snackbar.make(findViewById(android.R.id.content), "Loaded user info from db", Snackbar.LENGTH_SHORT).show();
                    c.close();
                    return true;
                }
            } while (c.moveToNext());
        }
        c.close();
        return false;
    }
    private void setUser(String name, String nickname, String email, String website, String phone, String city)
    {
        userName.setText(name);
        userNickname.setText(nickname);
        userEmail.setText(email);
        userWebsite.setText(website);
        userPhoneNumber.setText(phone);
        userCity.setText(city);
    }
}
