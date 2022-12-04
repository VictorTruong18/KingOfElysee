package fr.epita.android.kingofelysee.sections

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import fr.epita.android.kingofelysee.Communicator
import fr.epita.android.kingofelysee.GameBrain
import fr.epita.android.kingofelysee.R

/**
 * A simple [Fragment] subclass.
 * Use the [MyCardsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyCardsFragment : Fragment() {
    private val gameBrain: GameBrain by activityViewModels()
    private lateinit var communicator: Communicator

    private lateinit var layoutImageButton : List<Triple<LinearLayout, ImageView, Button>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        communicator = activity as Communicator
        return inflater.inflate(R.layout.fragment_my_cards, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        layoutImageButton = listOf(
            Triple(view.findViewById(R.id.layout1), view.findViewById(R.id.card_image_1), view.findViewById(R.id.card_button_1)),
            Triple(view.findViewById(R.id.layout2), view.findViewById(R.id.card_image_2), view.findViewById(R.id.card_button_2)),
            Triple(view.findViewById(R.id.layout3), view.findViewById(R.id.card_image_3), view.findViewById(R.id.card_button_3)),
            Triple(view.findViewById(R.id.layout4), view.findViewById(R.id.card_image_4), view.findViewById(R.id.card_button_4)),
            Triple(view.findViewById(R.id.layout5), view.findViewById(R.id.card_image_5), view.findViewById(R.id.card_button_5)),
            Triple(view.findViewById(R.id.layout6), view.findViewById(R.id.card_image_6), view.findViewById(R.id.card_button_6)),
        )

        // Every time the cards list change and the view is visible,
        // we show the new cards and hide the old ones
        val player = gameBrain.characters[gameBrain.characterTurnIndex]
        player.cards.observe(this.viewLifecycleOwner) {
            it.forEachIndexed { i, card ->
                layoutImageButton[i].first.visibility = View.VISIBLE
                layoutImageButton[i].second.setImageResource(card.id)
                layoutImageButton[i].third.setOnClickListener {
                    if (card.hasToChooseTarget) {
                        val dialog = ChooseTargetDialogFragment(card, null)
                        dialog.show(this.parentFragmentManager, "Toto")
                    } else {
                        val feedback = gameBrain.useCard(card)
                        communicator.displayFeedbackModal(feedback, card)
                    }
                }
            }

            layoutImageButton.subList(it.size, layoutImageButton.size).forEach { triple ->
                triple.first.visibility = View.GONE
            }

            view.requestLayout()
        }



    }
}