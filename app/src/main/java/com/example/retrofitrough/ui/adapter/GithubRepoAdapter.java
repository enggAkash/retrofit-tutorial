package com.example.retrofitrough.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

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
        return super.getView(position, convertView, parent);
    }
}
