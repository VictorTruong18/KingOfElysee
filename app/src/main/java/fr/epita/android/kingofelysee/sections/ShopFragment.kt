package fr.epita.android.kingofelysee.sections

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.activityViewModels
import fr.epita.android.kingofelysee.GameBrain
import fr.epita.android.kingofelysee.R
import fr.epita.android.kingofelysee.objects.Card
import fr.epita.android.kingofelysee.objects.Effect

/**
 * A simple [Fragment] subclass.
 * Use the [ShopFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ShopFragment : Fragment() {
    private val gameBrain: GameBrain by activityViewModels()

    lateinit var card1: Card
    lateinit var card2: Card

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shop, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gameBrain.shopCards.observe(this.viewLifecycleOwner) {
            val cards = gameBrain.shopCards

            this.card1 = cards.value!!.first
            this.card2 = cards.value!!.second

            view.findViewById<ImageView>(R.id.card1Image).setImageResource(this.card1.id)
            view.findViewById<ImageView>(R.id.card2Image).setImageResource(this.card2.id)
        }

        view.findViewById<Button>(R.id.reset_shop_button).setOnClickListener {
            gameBrain.renewShopCards()
        }

        view.findViewById<Button>(R.id.shop_buy_card_1).setOnClickListener {
            val card = gameBrain.shopCards.value!!.first
            val player = gameBrain.characters[gameBrain.characterTurnIndex]
            if (card.effect == Effect.IMMEDIATE) {
                if (card.hasToChooseTarget) {
                    val dialog = ChooseTargetDialogFragment(card, 0)
                    dialog.show(this.parentFragmentManager, "Toto")
                } else {
                    gameBrain.useShopCard(0, player)
                }
            } else {
                gameBrain.buyCard(0)
            }
        }

        view.findViewById<Button>(R.id.shop_buy_card_2).setOnClickListener {
            val card = gameBrain.shopCards.value!!.second
            val player = gameBrain.characters[gameBrain.characterTurnIndex]
            if (card.effect == Effect.IMMEDIATE) {
                if (card.hasToChooseTarget) {
                    val dialog = ChooseTargetDialogFragment(card, 1)
                } else {
                    gameBrain.useShopCard(1, player)
                }
            } else {
                gameBrain.buyCard(1)
            }
        }
    }

    private fun updateShopViews(view: View) {
        val cards = gameBrain.shopCards

        this.card1 = cards.value!!.first
        this.card2 = cards.value!!.second

        view.findViewById<ImageView>(R.id.card1Image).setImageResource(this.card1.id)
        view.findViewById<ImageView>(R.id.card2Image).setImageResource(this.card2.id)
    }
}