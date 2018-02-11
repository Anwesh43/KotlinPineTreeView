package ui.anwesome.com.kotlinpinetreeview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import ui.anwesome.com.pinetreeview.PineTreeView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PineTreeView.create(this)
    }
}
