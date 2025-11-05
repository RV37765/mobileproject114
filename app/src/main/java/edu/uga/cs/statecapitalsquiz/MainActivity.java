package edu.uga.cs.statecapitalsquiz;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.appbar.MaterialToolbar;

/**
 * MainActivity
 *
 * <p><b>Purpose:</b> Hosts the app’s single-Activity architecture and the Navigation Component
 * NavHostFragment. It also provides a top App Bar (MaterialToolbar) that NavigationUI
 * uses to display destination titles and handle the Up button.</p>
 *
 * <p><b>Key responsibilities:</b></p>
 * <ul>
 *   <li>Inflate <code>activity_main.xml</code>, which contains a {@link androidx.navigation.fragment.NavHostFragment}.</li>
 *   <li>Call {@link #setSupportActionBar} with the MaterialToolbar so NavigationUI can manage titles/up nav.</li>
 *   <li>Connect the Activity’s ActionBar to the {@link androidx.navigation.NavController} via NavigationUI.</li>
 *   <li>Delegate the “navigate up” action to the NavController.</li>
 * </ul>
 *
 * <p><b>Lifecycle notes:</b> This Activity contains no custom state. All screen content is provided
 * by fragments controlled through the navigation graph. If you add global UI (menus, etc.),
 * this is the place to wire them to the NavController.</p>
 */
public class MainActivity extends AppCompatActivity {
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
            NavigationUI.setupActionBarWithNavController(this, navController);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController != null && navController.navigateUp()
                || super.onSupportNavigateUp();
    }
}