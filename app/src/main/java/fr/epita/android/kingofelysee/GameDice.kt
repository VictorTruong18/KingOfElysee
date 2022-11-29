package fr.epita.android.kingofelysee

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import java.util.*

class GameDice : Fragment() {
    private val rollAnimation: Long = 1000;
    private val delayTime: Long = 50;
    private val diceDefaultValues: Array<String> = arrayOf("1", "2", "3", "€", "!", "♥");

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

        // Find the Text views in the layout
        val firstDiceTextView: TextView = view.findViewById(R.id.firstDiceTextView);
        val secondDiceTextView: TextView = view.findViewById(R.id.secondDiceTextView);
        val thirdDiceTextView: TextView = view.findViewById(R.id.thirdDiceTextView);
        val fourthDiceTextView: TextView = view.findViewById(R.id.fourthDiceTextView);
        val fifthDiceTextView: TextView = view.findViewById(R.id.fifthDiceTextView);
        val sixthDiceTextView: TextView = view.findViewById(R.id.sixthDiceTextView);

        // Set a click listener on the button to roll the dice when the user taps the button
        rollButton.setOnClickListener {

            // Start random text animation on UI Thread
            val timer = Timer()
            val monitor = object : TimerTask() {
                override fun run() {
                    activity?.runOnUiThread(Runnable {
                        firstDiceTextView.text = diceDefaultValues[rollDice()];
                        secondDiceTextView.text = diceDefaultValues[rollDice()];
                        thirdDiceTextView.text = diceDefaultValues[rollDice()];
                        fourthDiceTextView.text = diceDefaultValues[rollDice()];
                        fifthDiceTextView.text = diceDefaultValues[rollDice()];
                        sixthDiceTextView.text = diceDefaultValues[rollDice()];
                    })
                }
            }
            timer.schedule(monitor, delayTime, delayTime);

            // Stop the animation
            Handler(Looper.getMainLooper()).postDelayed({
                monitor.cancel();
                timer.cancel();
            }, rollAnimation)
        }
    }

    /**
     * Roll the dice and update the screen with the result.
     */
    private fun rollDice(): Int {
        // Create new Dice object with 6 sides and roll it
        val dice = Dice(6)
        return dice.roll()
    }

}

/**
 * Dice with a fixed number of sides.
 */
class Dice(private val numSides: Int) {

    /**
     * Do a random dice roll and return the result.
     */
    fun roll(): Int {
        return (0 until numSides).random()
    }
}