package com.example.fridaydubailottery;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.example.fridaydubailottery.utils.Constants;
import me.ibrahimsn.lib.SmoothBottomBar;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    private SmoothBottomBar smoothBottomBar;
    private NavController navController;
    private ImageView ivMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        ivMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(MainActivity.this, view);
                popup.setOnMenuItemClickListener(MainActivity.this);
                popup.inflate(R.menu.more_menu);
                popup.show();
            }
        });
    }

    private void initView() {
        smoothBottomBar = findViewById(R.id.bottomBar);
        ivMore = findViewById(R.id.ivMore);
        navController = Navigation.findNavController(MainActivity.this, R.id.mainFragment);

//        NavigationUI.setupActionBarWithNavController(MainActivity.this,navController);
        setupSmoothBottomMenu();
    }


    private void setupSmoothBottomMenu() {
        PopupMenu popupMenu = new PopupMenu(this, null);
        popupMenu.inflate(R.menu.menu);
        smoothBottomBar.setupWithNavController(popupMenu.getMenu(), navController);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                Constants.INSTANCE.logout(MainActivity.this);
                return true;

            default:
                return false;
        }
    }
}