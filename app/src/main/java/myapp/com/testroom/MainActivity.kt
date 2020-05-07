package myapp.com.testroom

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.PrimaryKey
import kotlinx.android.synthetic.main.activity_main.*
import java.util.zip.Inflater


@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MainActivity : AppCompatActivity() {

    var arr: List<User>? = null

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)




        deletebyID.setOnClickListener {
            var id = edit_id_delete.text.toString()
             deletebyID(id.toInt())
        }

        btnInsert.setOnClickListener {
            insert()
        }

        btnAdd.setOnClickListener {
            cons_add.visibility = View.VISIBLE
            recyview.visibility = View.GONE
        }


        btnReview.setOnClickListener {
            recyview.visibility = View.VISIBLE
            cons_add.visibility = View.GONE

            val userDB = DataRoom.getAppDatabase(this)

            Thread(Runnable {
                runOnUiThread {
                    try {
                        arr = userDB.Dao().AllUsers()
                        recyview.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayout.VERTICAL, false)
                        recyview.adapter = adapter(this, arr!!)
                        Toast.makeText(this@MainActivity, "Gat it!", Toast.LENGTH_LONG).show()

                    } catch (e: Exception) {
                        Toast.makeText(this@MainActivity, "Error ${e.message}", Toast.LENGTH_LONG).show()
                    }

                }
            }).start();

        }
        deleteAll.setOnClickListener {
            DeleteAll(arr!!)
        }

    }


    class adapter(var conx: Context, var list: List<User>) : RecyclerView.Adapter<adapter.MyHolder>() {

        class MyHolder(view: View) : RecyclerView.ViewHolder(view) {
            var id = view.findViewById<TextView>(R.id.txtID)
            var name = view.findViewById<TextView>(R.id.txtName)
            var email = view.findViewById<TextView>(R.id.txtEmail)
            var consCard = view.findViewById<ConstraintLayout>(R.id.linearLayout)

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): adapter.MyHolder {
            val myView = LayoutInflater.from(conx).inflate(R.layout.card_user, parent, false)
            return MyHolder(myView)
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holder: adapter.MyHolder, position: Int) {
            val mylist = list[position]
            holder.id.text = mylist.id.toString()
            holder.name.text = mylist.username
            holder.email.text = mylist.email

        }


    }

    fun deletebyID(id: Int) {
        val userDB = DataRoom.getAppDatabase(this)
        Thread(Runnable {
            runOnUiThread {
                try {
                    userDB.Dao().DeletebyId(id)
                    Toast.makeText(this@MainActivity, "the id$id is Deleted !", Toast.LENGTH_LONG).show()

                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, "Error ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }).start()
    }

    fun DeleteAll(arr: List<User>) {
        val userDB = DataRoom.getAppDatabase(this)
        Thread(Runnable {
            runOnUiThread {
                try {
                    userDB.Dao().deleteAll(arr)
                    Toast.makeText(this@MainActivity, "Inserted !", Toast.LENGTH_LONG).show()


                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, "Error ${e.message}", Toast.LENGTH_LONG).show()
                }

            }
        }).start();
    }

    fun insert() {
        val userDB = DataRoom.getAppDatabase(this)
        val name = editName.text.toString()
        val email = editEmail.text.toString()
        val id = editID.text.toString()
        Thread(Runnable {
            runOnUiThread {
                try {
                    val user = User(id.toInt(), name, email)
                    userDB.Dao().insert(user)
                    Toast.makeText(this@MainActivity, "Inserted !", Toast.LENGTH_LONG).show()
                    editName.text.clear()
                    editEmail.text.clear()
                    editID.text.clear()

                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, "Error ${e.message}", Toast.LENGTH_LONG).show()
                }

            }
        }).start();
    }
}
