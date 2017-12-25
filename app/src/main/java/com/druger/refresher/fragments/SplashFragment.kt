package com.druger.refresher.fragments


import android.app.Fragment
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.druger.refresher.R
import java.util.concurrent.TimeUnit


/**
 * A simple {@link Fragment} subclass.
 */
class SplashFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup,
                              savedInstanceState: Bundle): View {
        // Inflate the layout for this fragment
        val splashTask = SplashTask()
        splashTask.execute()

        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    inner class SplashTask : AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg params: Void): Void? {
            try {
                TimeUnit.SECONDS.sleep(2)
            } catch (e: InterruptedException) {
                e.printStackTrace()
                Thread.currentThread().interrupt()
            }

            if (activity != null) {
                activity.fragmentManager.popBackStack()
            }
            return null
        }
    }
}
