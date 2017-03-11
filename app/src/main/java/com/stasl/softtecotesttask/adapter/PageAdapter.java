package com.stasl.softtecotesttask.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.stasl.softtecotesttask.R;
import com.stasl.softtecotesttask.activity.SecondActivity;
import com.stasl.softtecotesttask.post.PostData;
import com.stasl.softtecotesttask.post.PostModel;

import java.util.List;

public class PageAdapter extends PagerAdapter
{
    private Context context;
    private PostData postData;
    private LayoutInflater layoutInflater;

    public PageAdapter(Context context, PostData postData){
        this.context = context;
        this.layoutInflater = (LayoutInflater)this.context.getSystemService(this.context.LAYOUT_INFLATER_SERVICE);
        this.postData = postData;
    }
    @Override
    public int getCount() {
        return postData.size();
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
    @Override
    public Object instantiateItem(final ViewGroup container, int position)
    {
        View view = this.layoutInflater.inflate(R.layout.two_line_list, container, false);
        for (int i = 0; i < postData.getPOSTS_LIMIT_PER_PAGE(); i++)
        {
            int resID = 0;
            switch (i)
            {
                case 0:
                    resID = R.id.button1;
                    break;
                case 1:
                    resID = R.id.button2;
                    break;
                case 2:
                    resID = R.id.button3;
                    break;
                case 3:
                    resID = R.id.button4;
                    break;
                case 4:
                    resID = R.id.button5;
                    break;
                case 5:
                    resID = R.id.button6;
                    break;
            }
            List<PostModel> page = postData.getPage(position);
            final Button button = (Button)view.findViewById(resID);
            button.setVisibility(View.VISIBLE);
            button.setText(String.valueOf(page.get(i).getId()) + "\n" + page.get(i).getTitle());
            button.setTag(String.valueOf(page.get(i).getId()));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, SecondActivity.class);
                    int userID = -1;
                    try {
                        userID = postData.getPost(Integer.parseInt(button.getTag().toString())).getUserId();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    intent.putExtra("userID", String.valueOf(userID));
                    intent.putExtra("postID", button.getTag().toString());
                    context.startActivity(intent);
                }
            });
            Log.d("View pos", String.valueOf(position));
        }
        container.addView(view);
        return view;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
