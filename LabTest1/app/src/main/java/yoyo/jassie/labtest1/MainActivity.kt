package yoyo.jassie.labtest1

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import yoyo.jassie.labtest1.Room.DatabaseClient

class MainActivity : AppCompatActivity() {

    private var recyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.setTitle("Lab Test 1")

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView?.setLayoutManager(LinearLayoutManager(this))
    }

    override fun onCreateOptionsMenu(menu: android.view.Menu):
            Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_new -> {

                //addPlaceMark()
                val intent = Intent(this, Add::class.java)
                startActivity(intent)

                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()

        recyclerView?.setAdapter(null)

        get()
    }

    private fun get() {
       // Toast.makeText(this,"Loading...", Toast.LENGTH_SHORT).show()

        val thread = object:Thread() {
            public override fun run() {
                Looper.prepare()
                val handler = Handler()
                handler.postDelayed(object:Runnable {
                    public override fun run() {
                        // Do Work

                        getTask()

                        handler.removeCallbacks(this)
                        Looper.myLooper()?.quit()
                    }
                }, 10)
                Looper.loop()
            }
        }
        thread.start()
    }

    private fun getTask()
    {
        val taskList = DatabaseClient
            .getInstance(applicationContext)
            .appDatabase
            .taskDao()
            .all

        runOnUiThread {
            val adapter = RecyclerAdapter(this, taskList)
            recyclerView?.setAdapter(adapter)
            //Toast.makeText(this,"Loaded...", Toast.LENGTH_SHORT).show()
        }

    }
}
