package com.stasl.softtecotesttask;

import android.graphics.Point;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements Animation.AnimationListener, View.OnClickListener {

    ImageView image;
    FloatingActionButton logcatButton;
    ViewPager list;
    ArrayList<DataObject> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        image = (ImageView)findViewById(R.id.imageView);
        logcatButton = (FloatingActionButton)findViewById(R.id.logcatButton);
        logcatButton.setOnClickListener(this);
        list = (ViewPager) findViewById(R.id.viewpager);
        setImageProportions(3,5);
        startImageAnimation();
        /*ArrayList<DataObject> data = new ArrayList<>();
        for (int i = 0;i < 5;i++)
        {
            ArrayList<String> arrayList = new ArrayList<>();
            arrayList.add("BMW");
            arrayList.add("AUDI");
            arrayList.add("BMW");
            arrayList.add("AUDI");
            arrayList.add("BMW");
            arrayList.add("AUDI");
            data.add(new DataObject(arrayList));
        }
        PageAdapter adapter = new PageAdapter(this, data);
        list.setAdapter(adapter);*/
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://jsonplaceholder.typicode.com/") //Базовая часть адреса
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //adapter.registerDataSetObserver(indicator.getDataSetObserver());
        JSONPlaceholderAPI api = retrofit.create(JSONPlaceholderAPI.class);
        api.getUser(1).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                Log.d("User", "success");
                Log.d("User", response.body().toString());
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Log.d("User", "Failure");
            }
        });
        api.getPost(1).enqueue(new Callback<PostModel>() {
            @Override
            public void onResponse(Call<PostModel> call, Response<PostModel> response) {
                Log.d("Post", "success");
                Log.d("Post", response.body().toString());
            }

            @Override
            public void onFailure(Call<PostModel> call, Throwable t) {
                Log.d("Post", "Failure");
            }
        });
        api.getUsers().enqueue(new Callback<List<UserModel>>() {
            @Override
            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                List<UserModel> posts = new ArrayList<>();
                posts.addAll(response.body());
                Log.d("Users", posts.toString());
            }

            @Override
            public void onFailure(Call<List<UserModel>> call, Throwable t) {
                Log.d("Users", "Failure");
            }
        });
        api.getPosts().enqueue(new Callback<List<PostModel>>() {
            @Override
            public void onResponse(Call<List<PostModel>> call, Response<List<PostModel>> response) {
                List<PostModel> posts = new ArrayList<>();
                posts.addAll(response.body());
                ArrayList<String> arrayList = new ArrayList<>();
                for (int i = 0;i<posts.size();i++)
                {
                    arrayList.add(posts.get(i).getId());
                    if (arrayList.size() == 6 || i == posts.size() - 1)
                    {
                        data.add(new DataObject(arrayList));
                        arrayList = new ArrayList<>();
                    }
                }
                PageAdapter adapter = new PageAdapter(MainActivity.this, data);
                list.setAdapter(adapter);
            }
            @Override
            public void onFailure(Call<List<PostModel>> call, Throwable t) {
                Log.d("Posts", "Failure");
            }
        });
    }
    private void setImageProportions(int firstProportion, int secondProportion)
    {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = (size.y * firstProportion) / secondProportion;
        //int width = image.getDrawable().getIntrinsicWidth();
        int width = (size.x * firstProportion) / secondProportion;
        RelativeLayout.LayoutParams parms = new RelativeLayout.LayoutParams(width,height);
        image.setLayoutParams(parms);
    }
    private void startImageAnimation()
    {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.alpha);
        animation.setAnimationListener(this);
        image.startAnimation(animation);
    }

    @Override
    public void onAnimationStart(Animation animation) {
        Log.d("Animation", "Start");
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        Log.d("Animation", "End");
        logcatButton.show();
        Log.d("Image Res X", String.valueOf(image.getWidth()));
        Log.d("Image Res Y", String.valueOf(image.getHeight()));
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        Log.d("Animation", "Repeat");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.logcatButton:
                Log.d("LogcatButton", "Clicked");
                writeLogcatToFile();
                break;
        }
    }
    private void writeLogcatToFile() {
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;
        try {
            Process process = Runtime.getRuntime().exec("logcat -d");
            bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder log = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                log.append(line);
            }
            bufferedWriter = new BufferedWriter(new FileWriter(getApplicationContext().getCacheDir() + "/logs.txt"));
            bufferedWriter.write(log.toString());
            Log.d("LogcatButton", "Logcat successfully writed into " + getApplicationContext().getCacheDir());
            Snackbar.make(findViewById(android.R.id.content), "Logcat successfully writed into " + getApplicationContext().getCacheDir(), Snackbar.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e("IOException", e.getLocalizedMessage());
            finish();
        }
        finally {
            if (bufferedReader != null)
            {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    Log.e("IOException", e.getLocalizedMessage());
                }
            }
            if (bufferedWriter != null)
            {
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    Log.e("IOException", e.getLocalizedMessage());
                }
            }
        }
    }
}
