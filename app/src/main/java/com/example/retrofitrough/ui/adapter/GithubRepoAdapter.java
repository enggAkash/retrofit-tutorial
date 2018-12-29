package com.example.retrofitrough.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.retrofitrough.R;
import com.example.retrofitrough.api.model.GithubRepo;

import java.util.List;

public class GithubRepoAdapter extends ArrayAdapter<GithubRepo> {

    private Context context;
    private List<GithubRepo> repoList;

    public GithubRepoAdapter(Context context, List<GithubRepo> repoList) {
        super(context, R.layout.list_item_pagination, repoList);

        this.context = context;
        this.repoList = repoList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        if (row == null) {
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.list_item_pagination, parent, false);
        }

        TextView textView = row.findViewById(R.id.list_item_pagination_text);

        GithubRepo item = repoList.get(position);
        String message = item.getName();
        textView.setText(message);

        return row;
    }
}
