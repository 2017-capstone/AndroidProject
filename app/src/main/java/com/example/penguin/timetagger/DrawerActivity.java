package com.example.penguin.timetagger;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.penguin.timetagger.Fragments.SettingsFragment;
import com.example.penguin.timetagger.Fragments.NoteListFragment;

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
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        setTitle(item.getTitle());

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (id == R.id.nav_total_tag) {
            transaction
                    .replace(R.id.frame_content, new NoteListFragment())
                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.nav_total_alarm) {
            transaction
                    .replace(R.id.frame_content, new NoteListFragment())
                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.nav_tag1) {
            transaction
                    .replace(R.id.frame_content, new NoteListFragment())
                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.nav_tag2) {
            transaction
                    .replace(R.id.frame_content, new NoteListFragment())
                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.nav_settings) {
            transaction
                    .replace(R.id.frame_content, new SettingsFragment())
                    .addToBackStack(null)
                    .commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
