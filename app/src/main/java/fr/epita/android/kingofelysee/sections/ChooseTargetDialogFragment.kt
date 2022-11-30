package fr.epita.android.kingofelysee.sections

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import fr.epita.android.kingofelysee.GameBrain
import fr.epita.android.kingofelysee.R
import fr.epita.android.kingofelysee.objects.Card

class ChooseTargetDialogFragment(private val card: Card, private val fromShop: Int?) : DialogFragment() {
    private val gameBrain: GameBrain by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Qui visez-vous?")
                .setItems(gameBrain
                    .getPlayersWithoutCurrentPlayer()
                    .map { it.name_ }
                    .toTypedArray()) { dialog, which ->
                        if (fromShop != null) {
                            Log.d("Lounes", "Bundle not null")

                            gameBrain.useShopCard(
                                fromShop,
                                gameBrain.getCurrentPlayer(),
                                gameBrain.getPlayersWithoutCurrentPlayer()[which]
                            )
                        } else {
                            Log.d("Lounes", "Bundle null")
                            gameBrain.useCard(
                                card,
                                gameBrain.getCurrentPlayer(),
                                gameBrain.getPlayersWithoutCurrentPlayer()[which]
                            )
                            gameBrain.getCurrentPlayer().cards.remove(card)
                        }
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}