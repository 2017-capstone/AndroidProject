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
import android.view.MenuItem;

import com.example.penguin.timetagger.Adapters.NoteGridViewAdapter;
import com.example.penguin.timetagger.Fragments.SettingsFragment;
import com.example.penguin.timetagger.Fragments.NoteListFragment;
import com.example.penguin.timetagger.Fragments.TagListFragment;
import com.example.penguin.timetagger.R;

public class DrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction
                .replace(R.id.frame_content, new NoteListFragment())
                .addToBackStack(null)
                .commit();
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

        } else if (id == R.id.nav_tag1) {
            movetoFragment(new NoteListFragment());

        } else if (id == R.id.nav_tag2) {
            movetoFragment(new NoteListFragment());

        } else if (id == R.id.nav_tagsettings) {
            movetoFragment(new TagListFragment());

        }else if (id == R.id.nav_settings) {
            movetoFragment(new SettingsFragment());
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
