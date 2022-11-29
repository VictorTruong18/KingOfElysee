package fr.epita.android.kingofelysee

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController


class GameBoard : Fragment() {

    private val gameBrain : GameBrain by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view : View =inflater.inflate(R.layout.fragment_game_board, container, false)

        val incrementButton : Button = view.findViewById(R.id.life_inc_btn)

        incrementButton.setOnClickListener{
            gameBrain.player.incrementLifePoints(1)
        }

        val diceButton: Button = view.findViewById(R.id.navToDiceButton)

        diceButton.setOnClickListener {
            findNavController().navigate(R.id.action_gameBoard_to_gameDice);
        }
        return view
    }


}