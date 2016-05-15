package com.example.android.birdcoop;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class NewsFragment extends Fragment {

    private ArrayAdapter<String> mNewsAdapter;

    public NewsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_news, container, false);

        String[] news = {
                "Early Bird Boot Camp @ Ponderosa is CANCELLED!",
                "New Person Training Promotion available at SRC",
                "BirdCoop closed on Friday August 14th for new equipment",
                "blah blah blah",
                "Angad is so smart"};

        List<String> latest_news = new ArrayList<String>(Arrays.asList(news));

        mNewsAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_news, R.id.list_item_textview, latest_news);

        ListView listView = (ListView) rootView.findViewById(R.id.listview_news);

        listView.setAdapter(mNewsAdapter);


        return rootView;
    }
}
