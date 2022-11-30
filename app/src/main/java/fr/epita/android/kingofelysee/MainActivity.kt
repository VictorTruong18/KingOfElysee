package fr.epita.android.kingofelysee

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import fr.epita.android.kingofelysee.sections.PlayerProfileSection
import fr.epita.android.kingofelysee.objects.Character
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), Communicator {
    private var defaultProfileHeight: Int = 0
    private var defaultScrollHeight: Int = 0

    private val gameBrain : GameBrain by viewModels()

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

    private fun animateMainFragmentToFullScreen() {
        val profile : View = findViewById(R.id.profile_0)
        defaultProfileHeight = profile.layoutParams.height

        val slideAnimator: ValueAnimator = ValueAnimator
            .ofInt(defaultProfileHeight, 0)
            .setDuration(1000);

        slideAnimator.addUpdateListener {
            val value: Int = it.animatedValue as Int
            if (value <= 0) {
                profile.visibility = View.INVISIBLE
            } else {
                profile.layoutParams.height = value
            }
            profile.requestLayout()
        }

        val animationSet = AnimatorSet();
        animationSet.interpolator = AccelerateDecelerateInterpolator()
        animationSet.play(slideAnimator)
        animationSet.startDelay = 200

        val scroll : View = findViewById(R.id.scrollView2)
        defaultScrollHeight = scroll.layoutParams.height
        val slideAnimator2: ValueAnimator = ValueAnimator
            .ofInt(defaultScrollHeight, 0)
            .setDuration(1000);

        slideAnimator2.addUpdateListener {
            val value: Int = it.animatedValue as Int
            if (value <= 0) {
                scroll.visibility = View.INVISIBLE
            } else {
                scroll.layoutParams.height = value
            }
            scroll.requestLayout()
        }

        val animationSet2 = AnimatorSet()
        animationSet2.interpolator = AccelerateDecelerateInterpolator()
        animationSet2.play(slideAnimator2)
        animationSet2.startDelay = 300

        animationSet.start()
        animationSet2.start()
    }

    override fun loadShopFragment() {
        val gameStatus: TextView = findViewById(R.id.game_status)
        gameStatus.text = "Quelles cartes acheter..."

        val myCardsButton: Button = findViewById(R.id.mycards_button)
        myCardsButton.visibility = View.INVISIBLE

        val transaction = this.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.mainFragment, ShopFragment())
        transaction.commit()

        animateMainFragmentToFullScreen()
    }

    override fun unloadShopFragment() {
        val profile : View = findViewById(R.id.profile_0)

        val slideAnimator: ValueAnimator = ValueAnimator
            .ofInt(0, defaultProfileHeight)
            .setDuration(1000);

        slideAnimator.addUpdateListener {
            val value: Int = it.animatedValue as Int
            if (value <= 0) {
                profile.visibility = View.VISIBLE
            } else {
                profile.layoutParams.height = value
            }
            profile.requestLayout()
        }

        val animationSet = AnimatorSet();
        animationSet.interpolator = AccelerateDecelerateInterpolator()
        animationSet.play(slideAnimator)
        animationSet.startDelay = 200

        val scroll : View = findViewById(R.id.scrollView2)
        val slideAnimator2: ValueAnimator = ValueAnimator
            .ofInt(0, defaultScrollHeight)
            .setDuration(1000);

        slideAnimator2.addUpdateListener {
            val value: Int = it.animatedValue as Int
            if (value <= 0) {
                scroll.visibility = View.VISIBLE
            } else {
                scroll.layoutParams.height = value
            }
            scroll.requestLayout()
        }

        val animationSet2 = AnimatorSet()
        animationSet2.interpolator = AccelerateDecelerateInterpolator()
        animationSet2.play(slideAnimator2)
        animationSet2.startDelay = 300

        val gameStatus: TextView = findViewById(R.id.game_status)
        // TODO Change later
        gameStatus.text = "Merci"

        val myCardsButton: Button = findViewById(R.id.mycards_button)
        myCardsButton.visibility = View.VISIBLE

        loadMap()

        animationSet.start()
        animationSet2.start()
    }

    override fun loadMyCardsFragment() {
        val transaction = this.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.mainFragment, MyCardsFragment())
        transaction.commit()

        animateMainFragmentToFullScreen()
    }

    override fun loadMap() {
        val transaction = this.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.mainFragment, MapFragment())
        transaction.commit()
    }

    override fun dialog(message : String, title : String) {

         MaterialAlertDialogBuilder(this)
            .setTitle(title)
            .setMessage(message).setPositiveButton("Compris"){dialog, id ->
                 getRidOfDigustingAndroidTheme()
                 this.gameBrain.gamePaused = false
                 }
            .create()
             .show()



    }

    override fun getRidOfDigustingAndroidTheme() {
        WindowCompat.setDecorFitsSystemWindows(this.window, false)
        WindowInsetsControllerCompat(this.window,
            this.window.decorView.findViewById(android.R.id.content)).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())

            // When the screen is swiped up at the bottom
            // of the application, the navigationBar shall
            // appear for some time
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

}