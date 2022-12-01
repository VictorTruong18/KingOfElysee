package fr.epita.android.kingofelysee

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.os.Build
import android.os.Bundle
import android.text.Spanned
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import fr.epita.android.kingofelysee.sections.MapFragment
import fr.epita.android.kingofelysee.sections.MyCardsFragment
import fr.epita.android.kingofelysee.sections.PlayerProfileFragment
import fr.epita.android.kingofelysee.sections.ShopFragment

class MainActivity : AppCompatActivity(), Communicator {
    private var defaultProfileHeight: Int = 0
    private var defaultScrollHeight: Int = 0

    private val gameBrain : GameBrain by viewModels()

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        


    }

    override fun passCharacterToFragment(id: Int, i: Int) {
        val bundle = Bundle()
        bundle.putInt("index", i)
        val transaction = this.supportFragmentManager.beginTransaction()

        val fragment = PlayerProfileFragment()
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

    override fun loadDiceFragment() {
        val gameStatus: TextView = findViewById(R.id.game_status)
        gameStatus.text = "À toi de convaincre les français·es !"

        val transaction = this.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.mainFragment, GameDice())
        transaction.commitNow()
    }

    override fun unloadFragment() {
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
        gameStatus.text = "Alors reprenons..."

        val myCardsButton: Button = findViewById(R.id.mycards_button)
        myCardsButton.visibility = View.VISIBLE

        if(gameBrain.characters[gameBrain.characterTurnIndex].isThePlayer_ && gameBrain.characters[gameBrain.characterTurnIndex].lastPlayTurn_ < gameBrain.nbTurn){
            loadDiceFragment()
        }else{
            loadMap()
        }

        animationSet.start()
        animationSet2.start()
    }

    override fun loadMyCardsFragment() {
        val gameStatus: TextView = findViewById(R.id.game_status)
        gameStatus.text = "Voulez-vous utiliser une carte ?"

        val transaction = this.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.mainFragment, MyCardsFragment())
        transaction.commit()

        animateMainFragmentToFullScreen()
    }

    override fun loadMap() {
        val transaction = this.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.mainFragment, MapFragment())
        transaction.commitNow()
    }

    override fun toggleShopBtn(){
        val shopButton: Button = findViewById(R.id.shop_button)
        val myCardsButton: Button = findViewById(R.id.mycards_button)
        if(shopButton.alpha == .5F) {
            shopButton.isClickable = true
            shopButton.alpha = 1F
            myCardsButton.isClickable = true
            myCardsButton.alpha = 1F
        } else {
            shopButton.isClickable = false
            shopButton.alpha = .5F
            myCardsButton.isClickable = false
            myCardsButton.alpha = .5F
        }
    }

    override fun dialog(message: String, title: String, resumeGame: Boolean) {

         MaterialAlertDialogBuilder(this)
            .setTitle(title)
            .setMessage(message).setPositiveButton("Compris"){dialog, id ->
                 if(resumeGame)
                    this.gameBrain.gamePaused = false
                 }
             .setOnDismissListener {
                 if(resumeGame)
                     this.gameBrain.gamePaused = false
             }
            .create()
             .show()



    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            this.window.decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        or View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            )
        }
    }

}