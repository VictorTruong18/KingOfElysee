package fr.epita.android.kingofelysee.sections

import android.media.Image
import android.os.Bundle
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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

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

        view.findViewById<Button>(R.id.reset_shop_button).setOnClickListener {


            updateShopViews(view)
        }

        view.findViewById<Button>(R.id.shop_buy_card_1).setOnClickListener {
            gameBrain.buyCard(0)
            updateShopViews(view)
        }

        view.findViewById<Button>(R.id.shop_buy_card_2).setOnClickListener {
            gameBrain.buyCard(1)
            updateShopViews(view)
        }

        gameBrain.renewShopCards()
        updateShopViews(view)
    }

    private fun updateShopViews(view: View) {
        val cards = gameBrain.getShopCards()

        this.card1 = cards.first
        this.card2 = cards.second

        view.findViewById<ImageView>(R.id.card1Image).setImageResource(this.card1.id)
        view.findViewById<ImageView>(R.id.card2Image).setImageResource(this.card2.id)
    }
}