package fr.epita.android.kingofelysee.sections

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import fr.epita.android.kingofelysee.GameBrain
import fr.epita.android.kingofelysee.R

/**
 * A simple [Fragment] subclass.
 * Use the [MyCardsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyCardsFragment : Fragment() {
    private val gameBrain: GameBrain by activityViewModels()

    private lateinit var imagesAndButtons : List<Pair<ImageView, Button>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_cards, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        imagesAndButtons = listOf(
            Pair(view.findViewById(R.id.card_image_1), view.findViewById(R.id.card_button_1)),
            Pair(view.findViewById(R.id.card_image_2), view.findViewById(R.id.card_button_2)),
            Pair(view.findViewById(R.id.card_image_3), view.findViewById(R.id.card_button_3)),
            Pair(view.findViewById(R.id.card_image_4), view.findViewById(R.id.card_button_4)),
            Pair(view.findViewById(R.id.card_image_5), view.findViewById(R.id.card_button_5)),
            Pair(view.findViewById(R.id.card_image_6), view.findViewById(R.id.card_button_6)),
        )

        val player = gameBrain.characters[gameBrain.characterTurnIndex]

        player.cards.forEachIndexed { i, card ->
            imagesAndButtons[i].first.setImageResource(card.id)
            imagesAndButtons[i].second.setOnClickListener {
                if (card.hasToChooseTarget) {

                } else {
                    gameBrain.useCard(card, player)
                }
            }
        }


        imagesAndButtons.subList(player.cards.size, imagesAndButtons.size).forEach {
            it.first.visibility = View.GONE
            it.second.visibility = View.GONE
        }
    }
}