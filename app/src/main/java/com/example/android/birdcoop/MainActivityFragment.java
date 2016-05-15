package com.example.android.birdcoop;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private ArrayAdapter<String> mButtonsAdapter;

    private String[] buttonNames = {"Traffic", "Find Us", "Club Info", "Class Schedule", "Memberships",
            "Personal Training", "Latest News"};

    private Integer[] mImageIds = {
            R.drawable.ic_directions_run_black_24dp,
            R.drawable.ic_location,
            R.drawable.ic_folder_open_black_24dp,
            R.drawable.ic_calendar,
            R.drawable.ic_info,
            R.drawable.ic_pt,
            R.drawable.ic_news
    };

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        GridView gridView = (GridView) rootView.findViewById(R.id.gridview);

        CustomAdapter customAdapter = new CustomAdapter(getActivity(), buttonNames, mImageIds);

        gridView.setAdapter(customAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;

                switch(position) {
                    case 0:
                        intent = new Intent(getActivity(), Traffic.class);
                        break;
                    case 1:
                        intent = new Intent(getActivity(), FindUs.class);
                        break;
                    case 2:
                        intent = new Intent(getActivity(), ClubInfo.class);
                        break;
                    case 3:
                        intent = new Intent(getActivity(), ClassSchedule.class);
                        break;
                    case 4:
                        intent = new Intent(getActivity(), Memberships.class);
                        break;
                    case 5:
                        intent = new Intent(getActivity(), PersonalTraining.class);
                        break;
                    case 6:
                        intent = new Intent(getActivity(), News.class);
                        break;
                    default:
                        return;

                }

                startActivity(intent);


            }
        }

        );


        return rootView;
    }

    public class CustomAdapter extends BaseAdapter {
        private Context mContext;
        private String[] buttons;
        private Integer[] imageIds;

        public CustomAdapter(Context context, String[] buttons, Integer[] images) {
            mContext = context;
            this.buttons = buttons;
            this.imageIds = images;
        }

        @Override
        public int getCount() {

            return imageIds.length;
        }

        @Override
        public Object getItem(int position) {

            return imageIds[position];
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View grid;
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                grid = new View(mContext);
                grid = inflater.inflate(R.layout.grid_item_button, null);
            }
            else {
                grid = (View) convertView;
            }
            TextView textView = (TextView) grid.findViewById(R.id.grid_button_text);
            textView.setText(buttons[position]);

            ImageView imageView = (ImageView) grid.findViewById(R.id.grid_button_image);
            imageView.setImageResource(imageIds[position]);

            return grid;
        }
    }


}
