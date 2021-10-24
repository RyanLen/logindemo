package com.example.work;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class AvatarAdapter extends BaseAdapter {
    private Context context;
    private List<Integer> avatars;

    public AvatarAdapter(Context context, List<Integer> avatars) {
        this.context = context;
        this.avatars = avatars;
    }

    @Override
    public int getCount() {
        return avatars.size();
    }

    @Override
    public Object getItem(int i) {
        return avatars.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        MyViewHolder viewHolder;
        if (view == null) {
            view = View.inflate(context,R.layout.item_gridview,null);
            viewHolder = new MyViewHolder();
            viewHolder.imageView = view.findViewById(R.id.iv_avatar);
            viewHolder.textView = view.findViewById(R.id.tv_name);
            view.setTag(viewHolder);
        } else {
            viewHolder = (MyViewHolder) view.getTag();
        }

        Integer avatar = avatars.get(i);
        viewHolder.imageView.setImageResource(avatar);
        viewHolder.textView.setText("头像" + (i+1));

        return view;
    }

    class MyViewHolder {
        ImageView imageView;
        TextView textView;
    }
}
