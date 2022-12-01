package fr.epita.android.kingofelysee

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import java.util.*

class GameDice : Fragment() {
    private val gameBrain : GameBrain by activityViewModels()

    private var tryNumber: Int = 3
    private val rollAnimation: Long = 1000;
    private val delayTime: Long = 50;
    private val diceDefaultValues: Array<String> = arrayOf("1", "2", "3", "\uD83D\uDCB6", "❗", "♥")
    private var lastRoll: List<Int> = randomDice()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(
            R.layout.fragment_game_dice, container, false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Find the Button in the layout
        val rollButton: Button = view.findViewById(R.id.rollButton);
        val playButton: Button = view.findViewById(R.id.playButton);

        val communicator = activity as Communicator

        // Find the Text views in the layout
        val firstDiceTextView: TextView = view.findViewById(R.id.firstDiceTextView);
        val secondDiceTextView: TextView = view.findViewById(R.id.secondDiceTextView);
        val thirdDiceTextView: TextView = view.findViewById(R.id.thirdDiceTextView);
        val fourthDiceTextView: TextView = view.findViewById(R.id.fourthDiceTextView);
        val fifthDiceTextView: TextView = view.findViewById(R.id.fifthDiceTextView);
        val sixthDiceTextView: TextView = view.findViewById(R.id.sixthDiceTextView);

        val diceStatus: TextView = view.findViewById(R.id.dice_status)

        diceStatus.text = "$tryNumber essai"+(if (tryNumber > 1) "s" else "")+" restant"+(if (tryNumber > 1) "s" else "")

        playButton.setOnClickListener {
            communicator.toggleShopBtn()
            gameBrain.play(gameBrain.characters[gameBrain.characterTurnIndex], getStringDice(), communicator)
        }

        // Set a click listener on the button to roll the dice when the user taps the button
        rollButton.setOnClickListener {
            if(tryNumber == 3){
                rollButton.text = "Relancer"
                playButton.visibility = View.VISIBLE
                communicator.toggleShopBtn()
            }

            tryNumber--
            diceStatus.text = "$tryNumber essai"+(if (tryNumber > 1) "s" else "")+" restant"+(if (tryNumber > 1) "s" else "")

            if(tryNumber == 0){
                rollButton.isClickable = false
                rollButton.alpha = .5F
            }

            // Start random text animation on UI Thread
            val timer = Timer()
            val monitor = object : TimerTask() {
                override fun run() {
                    activity?.runOnUiThread(Runnable {
                        playButton.alpha = .5F
                        playButton.isClickable = false
                        lastRoll = randomDice()
                        firstDiceTextView.text = diceDefaultValues[lastRoll[0]];
                        secondDiceTextView.text = diceDefaultValues[lastRoll[1]];
                        thirdDiceTextView.text = diceDefaultValues[lastRoll[2]];
                        fourthDiceTextView.text = diceDefaultValues[lastRoll[3]];
                        fifthDiceTextView.text = diceDefaultValues[lastRoll[4]];
                        sixthDiceTextView.text = diceDefaultValues[lastRoll[5]];
                    })
                }
            }
            timer.schedule(monitor, delayTime, delayTime);

            // Stop the animation
            Handler(Looper.getMainLooper()).postDelayed({
                monitor.cancel();
                timer.cancel();
                playButton.alpha = 1F
                playButton.isClickable = true
            }, rollAnimation)
        }
    }

    private fun randomDice(): List<Int> {
        return (1..6).map { (0 .. 5).random() }
    }

    fun getStringDice(): List<String> {
        return lastRoll.map { d -> diceDefaultValues[d] }
    }

}
