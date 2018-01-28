package com.druger.refresher.activities

import android.os.Bundle
import android.os.PersistableBundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.widget.Toast
import com.druger.refresher.Ads
import com.druger.refresher.App
import com.druger.refresher.R
import com.druger.refresher.adapters.TabAdapter
import com.druger.refresher.alarms.AlarmHelper
import com.druger.refresher.database.RealmHelper
import com.druger.refresher.dialogs.AddingTaskDialogFragment
import com.druger.refresher.dialogs.EditTaskDialogFragment
import com.druger.refresher.fragments.CurrentTaskFragment
import com.druger.refresher.fragments.DoneTaskFragment
import com.druger.refresher.fragments.TaskFragment
import com.druger.refresher.models.ModelTask
import javax.inject.Inject

class MainActivity : AppCompatActivity(),
        AddingTaskDialogFragment.AddingTaskListener,
        CurrentTaskFragment.OnTaskDoneListener,
        DoneTaskFragment.OnTaskRestoreListener,
        EditTaskDialogFragment.EditingTaskListener {

    @Inject
    lateinit var alarmHelper: AlarmHelper
    @Inject
    lateinit var realmHelper: RealmHelper

    private lateinit var tabAdapter: TabAdapter
    private lateinit var currentTaskFragment: TaskFragment
    private lateinit var doneTaskFragment: TaskFragment

    private lateinit var searchView: SearchView
    private lateinit var fab: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_main)
        App.getApplicationComponent().inject(this)

        Ads.showBanner(this)

        setupToolbar()
        setupViewPager()
        setupUI()
        setupUX()
    }

    override fun onPause() {
        super.onPause()
        App.activityPaused()
    }

    override fun onResume() {
        super.onResume()
        App.activityResumed()
    }

    private fun setupUI(){
        currentTaskFragment = tabAdapter.getItem(TabAdapter.CURRENT_TASK_FRAGMENT_POSITION) as TaskFragment
        doneTaskFragment = tabAdapter.getItem(TabAdapter.DONE_TASK_FRAGMENT_POSITION) as TaskFragment

        searchView = findViewById(R.id.search_view)
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                currentTaskFragment.findTasks(newText!!)
                doneTaskFragment.findTasks(newText)
                return false
            }

        })


        fab = findViewById(R.id.fab)
    }

    private fun setupUX() {
        fab.setOnClickListener({
            val addingTaskDialogFragment = AddingTaskDialogFragment()
            addingTaskDialogFragment.show(fragmentManager, "AddingTaskDialogFragment")
        })

    }

    private fun setupViewPager() {
        val tabLayout: TabLayout = findViewById(R.id.tab_layout)
        tabLayout.addTab(tabLayout.newTab().setText(R.string.current_task))
        tabLayout.addTab(tabLayout.newTab().setText(R.string.done_task))

        val viewPager: ViewPager = findViewById(R.id.pager)
        tabAdapter = TabAdapter(fragmentManager, 2)

        viewPager.adapter = tabAdapter
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

        tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
    }

    private fun setupToolbar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        setSupportActionBar(toolbar)
    }

    override fun onTaskRestore(task: ModelTask) {
        currentTaskFragment.addTask(task, false)
    }

    override fun onTaskDone(task: ModelTask) {
        doneTaskFragment.addTask(task, false)
    }

    override fun onTaskAddingCancel() {
        Toast.makeText(this, R.string.task_adding_cancel, Toast.LENGTH_LONG).show()
    }

    override fun onTaskEdited(updatedTask: ModelTask) {
        currentTaskFragment.updateTask(updatedTask)
        realmHelper.updateTask(updatedTask)
    }

    override fun onTaskAdded(newTask: ModelTask) {
        currentTaskFragment.addTask(newTask, true)
    }
}
