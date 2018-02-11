package ui.anwesome.com.kotlinpinetreeview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import ui.anwesome.com.pinetreeview.PineTreeView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = PineTreeView.create(this)
        view.addPineTreeListener {
            Toast.makeText(this,"selected $it",Toast.LENGTH_SHORT).show()
        }
    }
}
