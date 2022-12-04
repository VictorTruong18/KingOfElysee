package fr.epita.android.kingofelysee.sections

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import fr.epita.android.kingofelysee.Communicator
import fr.epita.android.kingofelysee.Feedback
import fr.epita.android.kingofelysee.Feedback.*
import fr.epita.android.kingofelysee.GameBrain
import fr.epita.android.kingofelysee.objects.Card
import fr.epita.android.kingofelysee.objects.Character

class ChooseTargetDialogFragment(private val card: Card, private val fromShop: Int?) : DialogFragment() {
    private val gameBrain: GameBrain by activityViewModels()
    private lateinit var communicator: Communicator

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        communicator = activity as Communicator
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Qui visez-vous?")
                .setItems(gameBrain
                    .getPlayersWithoutCurrentPlayer()
                    .map { it.name_ }
                    .toTypedArray()) { dialog, which ->
                        if (fromShop != null) {
                            val feedback = gameBrain.useShopCard(
                                fromShop,
                                gameBrain.getPlayersWithoutCurrentPlayer()[which]
                            )
                            communicator.displayFeedbackModal(feedback, card, gameBrain.getPlayersWithoutCurrentPlayer()[which])
                        } else {
                            val feedback = gameBrain.useCard(
                                card,
                                gameBrain.getPlayersWithoutCurrentPlayer()[which]
                            )
                            communicator.displayFeedbackModal(feedback, card, gameBrain.getPlayersWithoutCurrentPlayer()[which])
                            //gameBrain.getCurrentPlayer().removeCard(card)
                            //gameBrain.getCurrentPlayer().cards.value!!.remove(card)
                        }
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}