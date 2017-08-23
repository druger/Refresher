package com.druger.refresher.activities;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.druger.refresher.Ads;
import com.druger.refresher.App;
import com.druger.refresher.R;
import com.druger.refresher.adapters.TabAdapter;
import com.druger.refresher.alarms.AlarmHelper;
import com.druger.refresher.database.DBHelper;
import com.druger.refresher.dialogs.AddingTaskDialogFragment;
import com.druger.refresher.dialogs.EditTaskDialogFragment;
import com.druger.refresher.fragments.CurrentTaskFragment;
import com.druger.refresher.fragments.DoneTaskFragment;
import com.druger.refresher.fragments.SplashFragment;
import com.druger.refresher.fragments.TaskFragment;
import com.druger.refresher.models.ModelTask;
import com.druger.refresher.utils.PreferenceHelper;

public class MainActivity extends AppCompatActivity
        implements AddingTaskDialogFragment.AddingTaskListener,
        CurrentTaskFragment.OnTaskDoneListener,
        DoneTaskFragment.OnTaskRestoreListener,
        EditTaskDialogFragment.EditingTaskListener {

    private FragmentManager fragmentManager;

    private PreferenceHelper preferenceHelper;
    private TabAdapter tabAdapter;

    private TaskFragment currentTaskFragment;
    private TaskFragment doneTaskFragment;

    private SearchView searchView;
    private FloatingActionButton fab;

    public DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Ads.showBanner(this);
        setupPreference();
        AlarmHelper.getInstance().init(getApplicationContext());
        dbHelper = new DBHelper(getApplicationContext());
        fragmentManager = getFragmentManager();
        runSplash();
        setupToolbar();
        setupViewPager();
        setupUI();
        setupUX();
    }

    private void setupPreference() {
        PreferenceHelper.getInstance().init(getApplicationContext());
        preferenceHelper = PreferenceHelper.getInstance();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        App.activityPaused();
    }

    @Override
    protected void onResume() {
        super.onResume();
        App.activityResumed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem splashItem = menu.findItem(R.id.action_splash);
        splashItem.setChecked(preferenceHelper.getBoolean(PreferenceHelper.SPLASH_IS_INVISIBLE));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_splash) {
            item.setChecked(!item.isChecked());
            preferenceHelper.putBoolean(PreferenceHelper.SPLASH_IS_INVISIBLE, item.isChecked());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void runSplash() {
        if (!preferenceHelper.getBoolean(PreferenceHelper.SPLASH_IS_INVISIBLE)) {
            SplashFragment splashFragment = new SplashFragment();

            fragmentManager.beginTransaction()
                    .replace(R.id.coordinator, splashFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void setupUI(){
        currentTaskFragment = (CurrentTaskFragment) tabAdapter.getItem(TabAdapter.CURRENT_TASK_FRAGMENT_POSITION);
        doneTaskFragment = (DoneTaskFragment) tabAdapter.getItem(TabAdapter.DONE_TASK_FRAGMENT_POSITION);

        searchView = (SearchView) findViewById(R.id.search_view);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                currentTaskFragment.findTasks(newText);
                doneTaskFragment.findTasks(newText);
                return false;
            }
        });

        fab = (FloatingActionButton) findViewById(R.id.fab);
    }

    private void setupUX() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment addingTaskDialogFragment = new AddingTaskDialogFragment();
                addingTaskDialogFragment.show(fragmentManager, "AddingTaskDialogFragment");
            }
        });

    }

    private void setupViewPager() {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.current_task));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.done_task));

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        tabAdapter = new TabAdapter(fragmentManager, 2);

        viewPager.setAdapter(tabAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null){
            toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
            setSupportActionBar(toolbar);
        }
    }

    @Override
    public void onTaskAdded(ModelTask newTask) {
        currentTaskFragment.addTask(newTask, true);
    }

    @Override
    public void onTaskAddingCancel() {
        Toast.makeText(this, R.string.task_adding_cancel, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onTaskDone(ModelTask task) {
        doneTaskFragment.addTask(task, false);
    }

    @Override
    public void onTaskRestore(ModelTask task) {
        currentTaskFragment.addTask(task, false);

    }

    @Override
    public void onTaskEdited(ModelTask updatedTask) {
        currentTaskFragment.updateTask(updatedTask);
        dbHelper.update().updateTask(updatedTask);
    }
}
