package com.example.penguin.timetagger.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.penguin.timetagger.Adapters.TagListAdapter;
import com.example.penguin.timetagger.Database.DatabaseHelper;
import com.example.penguin.timetagger.TimeTag;
import com.example.penguin.timetagger.R;

import java.util.List;

public class TagListFragment extends Fragment {
    ActionBar toolbar;
    List<TimeTag> timeTagItems;
    RecyclerView rv;
    LinearLayoutManager lm;

    public static TagFragment newInstance(TimeTag timeTag){
        Bundle bundle = new Bundle();
        bundle.putParcelable("TAG", timeTag);
        TagFragment fragment = new TagFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_tag_list, container, false);

        DatabaseHelper.getInstance(getActivity());

        try{
            lm = new LinearLayoutManager(getActivity());
            lm.setOrientation(LinearLayoutManager.VERTICAL);

            rv = (RecyclerView)view.findViewById(R.id.tagItemsRV);
            rv.setLayoutManager(lm);

            TagListAdapter tla = new TagListAdapter(getActivity());
            rv.setAdapter(tla);
            tla.notifyDataSetChanged();
        }catch (Exception e){
            System.out.println(e);
        }
        setHasOptionsMenu(false);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.listview_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume(){
        super.onResume();
        getActivity().invalidateOptionsMenu();
    }

    private void movetoTagFragment(TimeTag timeTag){
        TagFragment fragment = TagFragment.newInstance(timeTag);

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_content, fragment)
                .addToBackStack(null)
                .commit();

    }
}
