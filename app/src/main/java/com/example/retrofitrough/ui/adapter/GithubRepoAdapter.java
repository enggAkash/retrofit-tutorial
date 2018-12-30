package com.example.retrofitrough.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.retrofitrough.R;
import com.example.retrofitrough.api.model.GithubRepo;

import java.util.List;

public class GithubRepoAdapter extends ArrayAdapter<GithubRepo> {
    private static final String TAG = "GithubRepoAdapter";

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
        TextView idTv = row.findViewById(R.id.list_item_id_text);
        TextView htmlUrl = row.findViewById(R.id.list_item_html_url);


        GithubRepo item = repoList.get(position);
        int id = item.getId();
        String repoName = item.getName();
        String url = item.getHtml_url();

        Log.d(TAG, "getView: id= " + id);
        Log.d(TAG, "getView: repoName= " + repoName);
        Log.d(TAG, "getView: url= " + url);

        idTv.setText(String.valueOf(id));
        textView.setText(repoName);
        htmlUrl.setText(url);

        return row;
    }
}
