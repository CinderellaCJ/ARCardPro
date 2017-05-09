package com.cj.arcard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cj.arcard.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SelectAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> list;

    public SelectAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolderSelect viewHolderSelect;
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.listview_selectitem, null);
            viewHolderSelect = new ViewHolderSelect(view);
            view.setTag(viewHolderSelect);
        } else {
            view = convertView;
            viewHolderSelect = (ViewHolderSelect) view.getTag();
        }
        viewHolderSelect.itemSelect.setText(list.get(position));
        return view;

    }

    static class ViewHolderSelect {
        @BindView(R.id.item_select)
        TextView itemSelect;

        ViewHolderSelect(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
