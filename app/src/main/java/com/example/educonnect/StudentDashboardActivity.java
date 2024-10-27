package com.example.educonnect;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class StudentDashboardActivity extends AppCompatActivity {

    private static final String TAG = "StudentDashboardActivity";

    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        
        setupViewPager();

        if (getIntent().getBooleanExtra("open_chats_tab", false)) {
            viewPager.setCurrentItem(1); // Switch to Chats tab
        }
    }

    private void setupViewPager() {
        try {
            StudentDashboardPagerAdapter adapter = new StudentDashboardPagerAdapter(this);
            viewPager.setAdapter(adapter);

            new TabLayoutMediator(tabLayout, viewPager,
                    (tab, position) -> tab.setText(position == 0 ? "Posts" : "Chats")
            ).attach();
        } catch (Exception e) {
            Log.e(TAG, "Error setting up ViewPager", e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tutor_dashboard_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_profile) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_logout) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
