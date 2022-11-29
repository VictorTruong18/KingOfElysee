package fr.epita.android.kingofelysee

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import fr.epita.android.kingofelysee.sections.PlayerProfileSection
import fr.epita.android.kingofelysee.objects.Character

class MainActivity : AppCompatActivity(), Communicator {
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window,
            window.decorView.findViewById(android.R.id.content)).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())

            // When the screen is swiped up at the bottom
            // of the application, the navigationBar shall
            // appear for some time
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    override fun passCharacterToFragment(id: Int, i: Int) {
        val bundle = Bundle()
        bundle.putInt("index", i)
        val transaction = this.supportFragmentManager.beginTransaction()

        val fragment = PlayerProfileSection()
        fragment.arguments = bundle
        transaction.replace(resources.getIdentifier("profile_$id", "id", this.packageName), fragment)
        transaction.commit()
    }
}