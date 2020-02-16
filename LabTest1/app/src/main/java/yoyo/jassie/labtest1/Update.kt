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


class Update : AppCompatActivity() {

    var editTextName: EditText? = null
    var editTextAge: EditText? = null
    var editTextTuition: EditText? = null
    var editTextDate: EditText? = null

    var myCalendar: Calendar = Calendar.getInstance()
    var task: Task? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)

        this.setTitle("Update")

        editTextName = findViewById(R.id.editTextName)
        editTextAge = findViewById(R.id.editTextAge)
        editTextTuition = findViewById(R.id.editTextTuition)
        editTextDate = findViewById(R.id.editTextStartDate)

        task = intent.getSerializableExtra("task") as Task

        editTextName?.setText(task?.getName())
        editTextAge?.setText(""+task?.getAge())
        editTextTuition?.setText(""+task?.getTuition())

        val dateFormat = SimpleDateFormat("dd/MMM/yyyy")
        val dateTime = dateFormat.format(task?.getDate())
        editTextDate?.setText("" + dateTime)

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
            override fun onClick(v: View) {
                // TODO Auto-generated method stub
                DatePickerDialog(this@Update, date, myCalendar
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
        inflater.inflate(R.menu.menu_update, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_update -> {
                //saveChanges()

                update()

                return true
            }

            R.id.action_delete -> {
                //saveChanges()

                delete()

                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun update() {

        var task: Task? = this.task

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

                        updateTask(task,name1,age1,tuition1,date1)

                        handler.removeCallbacks(this)
                        Looper.myLooper()?.quit()
                    }
                }, 10)
                Looper.loop()
            }
        }
        thread.start()

    }

    private fun updateTask(task: Task?,name1: String, age1: String, tuition1: String, date1: String)
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
        task?.setName(name1)
        task?.setAge(age2)
        task?.setTuition(tuition2)
        task?.setDate(date2)

        //adding to database
        DatabaseClient.getInstance(applicationContext).appDatabase.taskDao().update(task)

        runOnUiThread {
            //Toast.makeText(this,"Updated...", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun delete() {

        val thread = object:Thread() {
            public override fun run() {
                Looper.prepare()
                val handler = Handler()
                handler.postDelayed(object:Runnable {
                    public override fun run() {
                        // Do Work

                        deleteTask(task)

                        handler.removeCallbacks(this)
                        Looper.myLooper()?.quit()
                    }
                }, 10)
                Looper.loop()
            }
        }
        thread.start()

    }

    private fun deleteTask(task: Task?)
    {
        DatabaseClient.getInstance(applicationContext).appDatabase.taskDao().delete(task)

        runOnUiThread {
            //Toast.makeText(this,"Deleted...", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
