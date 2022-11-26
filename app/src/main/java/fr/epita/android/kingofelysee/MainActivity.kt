package fr.epita.android.kingofelysee

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Replace the FrameLayout of the main activity with the Menu
        val gameMenu = GameMenu()
        supportFragmentManager.beginTransaction().replace(R.id.main_container, gameMenu).commit()
    }
}