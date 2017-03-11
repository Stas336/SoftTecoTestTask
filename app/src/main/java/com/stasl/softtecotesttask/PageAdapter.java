package com.stasl.softtecotesttask;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

public class PageAdapter extends PagerAdapter
{
    private Context context;
    private List<DataObject> dataObjectList;
    private LayoutInflater layoutInflater;

    public PageAdapter(Context context, List<DataObject> dataObjectList){
        this.context = context;
        this.layoutInflater = (LayoutInflater)this.context.getSystemService(this.context.LAYOUT_INFLATER_SERVICE);
        this.dataObjectList = dataObjectList;
    }
    @Override
    public int getCount() {
        return dataObjectList.size();
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
    @Override
    public Object instantiateItem(final ViewGroup container, int position)
    {
        View view = this.layoutInflater.inflate(R.layout.two_line_list, container, false);
        for (int i = 0;i < dataObjectList.get(position).size();i++)
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
            final Button button = (Button)view.findViewById(resID);
            button.setVisibility(View.VISIBLE);
            button.setText(dataObjectList.get(position).getName(i));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, SecondActivity.class);
                    intent.putExtra("name", button.getText().toString());
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
