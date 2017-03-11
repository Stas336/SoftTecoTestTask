package com.stasl.softtecotesttask.activity;

import android.graphics.Point;
import android.os.AsyncTask;
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


import com.stasl.softtecotesttask.post.PostData;
import com.stasl.softtecotesttask.api.JSONPlaceholderAPI;
import com.stasl.softtecotesttask.adapter.PageAdapter;
import com.stasl.softtecotesttask.R;
import com.stasl.softtecotesttask.post.PostModel;
import com.stasl.softtecotesttask.user.UserModel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements Animation.AnimationListener, View.OnClickListener {

    private ImageView image;
    private FloatingActionButton logcatButton;
    private ViewPager list;
    private PostData data;

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
        /*ArrayList<PostData> data = new ArrayList<>();
        for (int i = 0;i < 5;i++)
        {
            ArrayList<String> arrayList = new ArrayList<>();
            arrayList.add("BMW");
            arrayList.add("AUDI");
            arrayList.add("BMW");
            arrayList.add("AUDI");
            arrayList.add("BMW");
            arrayList.add("AUDI");
            data.add(new PostData(arrayList));
        }
        PageAdapter adapter = new PageAdapter(this, data);
        list.setAdapter(adapter);*/
        //adapter.registerDataSetObserver(indicator.getDataSetObserver());
        updateViewPager();
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
    public static JSONPlaceholderAPI getAPI()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(JSONPlaceholderAPI.class);
    }
    private static class JSONPlaceholderGetPosts extends AsyncTask<Void, Void, List<PostModel>>
    {
        @Override
        protected List<PostModel> doInBackground(Void... voids) {
            JSONPlaceholderAPI api = getAPI();
            Response<List<PostModel>> response = null;
            try {
                response = api.getPosts().execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (response == null)
            {
                return null;
            }
            else
            {
                return response.body();
            }
        }
    }
    private static class JSONPlaceholderGetPost extends AsyncTask<Integer, Void, PostModel>
    {
        @Override
        protected PostModel doInBackground(Integer... integers) {
            JSONPlaceholderAPI api = getAPI();
            Response<PostModel> response = null;
            try {
                response = api.getPost(integers[0]).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (response == null)
            {
                return null;
            }
            else
            {
                return response.body();
            }
        }
    }
    private static class JSONPlaceholderGetUsers extends AsyncTask<Void, Void, List<UserModel>>
    {
        @Override
        protected List<UserModel> doInBackground(Void... voids)
        {
            JSONPlaceholderAPI api = getAPI();
            Response<List<UserModel>> response = null;
            try {
                response = api.getUsers().execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (response == null)
            {
                return null;
            }
            else
            {
                return response.body();
            }
        }
    }
    private static class JSONPlaceholderGetUser extends AsyncTask<Integer, Void, UserModel>
    {
        @Override
        protected UserModel doInBackground(Integer... integers) {
            JSONPlaceholderAPI api = getAPI();
            Response<UserModel> response = null;
            try {
                response = api.getUser(integers[0]).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (response == null)
            {
                return null;
            }
            else
            {
                return response.body();
            }
        }
    }
    public static UserModel getUser(int number) throws ExecutionException, InterruptedException {
        JSONPlaceholderGetUser jsonPlaceholderGetUser = new JSONPlaceholderGetUser();
        jsonPlaceholderGetUser.execute(number);
        return jsonPlaceholderGetUser.get();
    }
    public static List<UserModel> getUsers() throws ExecutionException, InterruptedException {
        JSONPlaceholderGetUsers jsonPlaceholderGetUsers = new JSONPlaceholderGetUsers();
        jsonPlaceholderGetUsers.execute();
        return jsonPlaceholderGetUsers.get();
    }
    public static PostModel getPost(int number) throws ExecutionException, InterruptedException {
        JSONPlaceholderGetPost jsonPlaceholderGetPost = new JSONPlaceholderGetPost();
        jsonPlaceholderGetPost.execute(number);
        return jsonPlaceholderGetPost.get();
    }
    public static List<PostModel> getPosts() throws ExecutionException, InterruptedException {
        JSONPlaceholderGetPosts jsonPlaceholderGetPosts = new JSONPlaceholderGetPosts();
        jsonPlaceholderGetPosts.execute();
        return jsonPlaceholderGetPosts.get();
    }
    public void updateViewPager()
    {
        data = new PostData();
        List<PostModel> posts = null;
        try {
            posts = getPosts();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (posts != null)
        {
            for (int i = 0;i<posts.size();i++)
            {
                data.addPost(posts.get(i));
            }
            PageAdapter adapter = new PageAdapter(MainActivity.this, data);
            list.setAdapter(adapter);
        }
        else
        {
            Log.d("ViewPager", "Update failure");
        }
    }
}
