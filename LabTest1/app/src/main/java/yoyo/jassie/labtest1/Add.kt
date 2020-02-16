package yoyo.jassie.labtest1

import android.app.DatePickerDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import yoyo.jassie.labtest1.Room.DatabaseClient
import yoyo.jassie.labtest1.Room.Task
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*




class Add : AppCompatActivity() {

    var editTextName: EditText? = null
    var editTextAge: EditText? = null
    var editTextTuition: EditText? = null
    var editTextDate: EditText? = null

    var myCalendar: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        this.setTitle("Add")

        editTextName = findViewById(R.id.editTextName)
        editTextAge = findViewById(R.id.editTextAge)
        editTextTuition = findViewById(R.id.editTextTuition)
        editTextDate = findViewById(R.id.editTextStartDate)

        val date = object : DatePickerDialog.OnDateSetListener {

            override fun onDateSet(
                view: DatePicker, year: Int, monthOfYear: Int,
                dayOfMonth: Int
            ) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, monthOfYear)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateLabel()
            }
        }

        editTextDate!!.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v:View) {
                // TODO Auto-generated method stub
                DatePickerDialog(this@Add, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show()
            }
        })
    }

    private fun updateLabel() {
        val myFormat = "dd/MMM/yyyy" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)

        editTextDate?.setText(sdf.format(myCalendar.getTime()))
    }

    override fun onCreateOptionsMenu(menu: android.view.Menu):
            Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_add, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_add -> {

                add()

                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun add() {

        var name1: String = editTextName?.text.toString()
        var age1: String = editTextAge?.text.toString()
        var tuition1: String = editTextTuition?.text.toString()
        var date1: String = editTextDate?.text.toString()

        if (name1.isEmpty()) {
            editTextName?.setError("Name Required")
            editTextName?.requestFocus()
            return
        }

        if (age1.isEmpty()) {
            editTextAge?.setError("Age required")
            editTextAge?.requestFocus()
            return
        }

        if (tuition1.isEmpty()) {
            editTextTuition?.setError("Tuition required")
            editTextTuition?.requestFocus()
            return
        }

        if (date1.isEmpty()) {
            editTextDate?.setError("Date required")
            editTextDate?.requestFocus()
            return
        }

        val thread = object:Thread() {
            public override fun run() {
                Looper.prepare()
                val handler = Handler()
                handler.postDelayed(object:Runnable {
                    public override fun run() {
                        // Do Work

                        addTask(name1,age1,tuition1,date1)

                        handler.removeCallbacks(this)
                        Looper.myLooper()?.quit()
                    }
                }, 10)
                Looper.loop()
            }
        }
        thread.start()

    }

    private fun addTask(name1: String, age1: String, tuition1: String, date1: String)
    {

        var age2: Int = java.lang.Integer.parseInt(age1)
        var tuition2: Double = java.lang.Double.parseDouble(tuition1)
        var date2: Date = Date()

        val format = SimpleDateFormat("dd/MMM/yyyy")
        try
        {
            date2 = format.parse(date1)
        }
        catch (e: ParseException) {
            e.printStackTrace()
        }

        //creating a task
        val task = Task()
        task.setName(name1)
        task.setAge(age2)
        task.setTuition(tuition2)
        task.setDate(date2)

        //adding to database
        DatabaseClient.getInstance(applicationContext).appDatabase.taskDao().insert(task)

        runOnUiThread {
            //Toast.makeText(this,"Saved...", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
