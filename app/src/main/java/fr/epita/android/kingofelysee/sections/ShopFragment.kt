package fr.epita.android.kingofelysee.sections

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import fr.epita.android.kingofelysee.Communicator
import fr.epita.android.kingofelysee.Feedback
import fr.epita.android.kingofelysee.Feedback.*
import fr.epita.android.kingofelysee.GameBrain
import fr.epita.android.kingofelysee.R
import fr.epita.android.kingofelysee.objects.Card
import fr.epita.android.kingofelysee.objects.Effect
import fr.epita.android.kingofelysee.objects.Character

/**
 * A simple [Fragment] subclass.
 * Use the [ShopFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ShopFragment : Fragment() {
    private val gameBrain: GameBrain by activityViewModels()

    lateinit var card1: Card
    lateinit var card2: Card

    lateinit var button1: Button
    lateinit var button2: Button
    lateinit var refreshButton: Button

    private val renewPrice = 2
    private lateinit var communicator: Communicator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        communicator = activity as Communicator
        return inflater.inflate(R.layout.fragment_shop, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gameBrain.shopCards.observe(this.viewLifecycleOwner) {
            this.card1 = it.first
            this.card2 = it.second

            view.findViewById<ImageView>(R.id.card1Image).setImageResource(this.card1.id)
            view.findViewById<ImageView>(R.id.card2Image).setImageResource(this.card2.id)

            button1.isEnabled = gameBrain.hasCurrentPlayerEnoughMoneyToBuyCard(0)
            button2.isEnabled = gameBrain.hasCurrentPlayerEnoughMoneyToBuyCard(1)

            Log.d("Lounes", "Button1 ${button1.isEnabled}")
            Log.d("Lounes", "Button2 ${button2.isEnabled}")

            val card1msg = "Acheter (" + if (card1.price == 0) { "GRATUIT" } else { "${card1.price}\uD83D\uDCB6" } + ")"
            val card2msg = "Acheter (" + if (card2.price == 0) { "GRATUIT" } else { "${card2.price}\uD83D\uDCB6" } + ")"

            button1.text = card1msg
            button2.text = card2msg

            refreshButton.isEnabled = gameBrain.canCurrentPlayerRenew(renewPrice)

            view.requestLayout()
        }

        refreshButton = view.findViewById(R.id.reset_shop_button)

        val refreshMsg = "Renouveler (${renewPrice} \uD83D\uDCB6)"
        refreshButton.text = refreshMsg

        refreshButton.setOnClickListener {
            gameBrain.renewShopCards(renewPrice)
            view.requestLayout()
        }

        button1 = view.findViewById(R.id.shop_buy_card_1)

        button1.setOnClickListener {
            val card = gameBrain.shopCards.value!!.first
            if (card.effect == Effect.IMMEDIATE) {
                if (card.hasToChooseTarget) {
                    val dialog = ChooseTargetDialogFragment(card, 0)
                    dialog.show(this.parentFragmentManager, "Toto")
                } else {
                    val feedback = gameBrain.useShopCard(0)
                    communicator.displayFeedbackModal(feedback, card)
                }
            } else {
                if (gameBrain.buyCard(0))
                    communicator.dialog("Carte ajoutée", "Boutique")
                else
                    communicator.dialog("Vous ne pouvez pas avoir plus de 6 cartes à la fois!", "Boutique")
                view.requestLayout()
            }
        }

        button2 = view.findViewById(R.id.shop_buy_card_2)

        button2.setOnClickListener {
            val card = gameBrain.shopCards.value!!.second
            if (card.effect == Effect.IMMEDIATE) {
                if (card.hasToChooseTarget) {
                    val dialog = ChooseTargetDialogFragment(card, 1)
                    dialog.show(this.parentFragmentManager, "Toto")
                } else {
                    val feedback = gameBrain.useShopCard(1)
                    communicator.displayFeedbackModal(feedback, card)
                }
            } else {
                if (gameBrain.buyCard(1))
                    communicator.dialog("Carte ajoutée", "Boutique")
                else
                    communicator.dialog("Vous ne pouvez pas avoir plus de 6 cartes à la fois!", "Boutique")
                view.requestLayout()
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