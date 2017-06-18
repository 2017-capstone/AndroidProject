package com.example.penguin.timetagger;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.penguin.timetagger.Adapters.NoteGridViewAdapter;
import com.example.penguin.timetagger.Database.DatabaseHelper;
import com.example.penguin.timetagger.Fragments.NoteFragment;
import com.example.penguin.timetagger.Fragments.SettingsFragment;
import com.example.penguin.timetagger.Fragments.NoteListFragment;
import com.example.penguin.timetagger.Fragments.TagListFragment;
import com.example.penguin.timetagger.R;

import java.util.List;

public class DrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 네비게이션 생성
        createNavigation();

        DatabaseHelper db = DatabaseHelper.getInstance(this);
        db.loadDummyTags();

        // 초기 뷰(현재 시간에 맞는 뷰가 되야 함)
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction
                .replace(R.id.frame_content, new NoteListFragment())
                .addToBackStack(null)
                .commit();
    }

    protected void createNavigation(){
        // 네비게이션 연결
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // 메뉴 생성
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        DatabaseHelper.getInstance(this);
        List<TimeTag> timeTags = DatabaseHelper.selectAllTags();
        for(int i=0; i<timeTags.size(); i++)
            menu.add(R.id.single_view,
                     timeTags.get(i).getID() << 4,
                     1/*Group1:singleTag*/,
                     timeTags.get(i).getTag())
                    .setIcon(R.drawable.ic_class)
                    .setCheckable(true
                    );

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(NoteGridViewAdapter.checkBoxShow) {
            NoteGridViewAdapter.checkBoxShow = false;
        }
        else {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        setTitle(item.getTitle());

        if (id == R.id.nav_total_tag) {
            movetoFragment(new NoteListFragment());

        } else if (id == R.id.nav_total_alarm) {
            movetoFragment(new NoteListFragment());


        } else if (id == R.id.nav_tagsettings) {
            movetoFragment(new TagListFragment());

        }else if (id == R.id.nav_settings) {
            movetoFragment(new SettingsFragment());
        }else{
            id = id >> 4;
            TimeTag timeTag = DatabaseHelper.selectTag(id);
            NoteListFragment nlFragment = NoteListFragment.newInstance(timeTag);
            movetoFragment(nlFragment);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void movetoFragment(Fragment fragment){
       try{
           getSupportFragmentManager()
                   .beginTransaction().
                   replace(R.id.frame_content, fragment)
                   .addToBackStack(null)
                   .commit();
       }catch (Exception e){
           System.out.println(e);
       }
    }
}
