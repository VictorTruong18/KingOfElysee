package fr.epita.android.kingofelysee.sections

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import fr.epita.android.kingofelysee.Feedback
import fr.epita.android.kingofelysee.Feedback.*
import fr.epita.android.kingofelysee.GameBrain
import fr.epita.android.kingofelysee.objects.Card
import fr.epita.android.kingofelysee.objects.Character

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
                            val feedback = gameBrain.useShopCard(
                                fromShop,
                                gameBrain.getCurrentPlayer(),
                                gameBrain.getPlayersWithoutCurrentPlayer()[which]
                            )
                            displayFeedbackMessage(feedback, card, gameBrain.getPlayersWithoutCurrentPlayer()[which])
                        } else {
                            val feedback = gameBrain.useCard(
                                card,
                                gameBrain.getCurrentPlayer(),
                                gameBrain.getPlayersWithoutCurrentPlayer()[which]
                            )
                            gameBrain.getCurrentPlayer().removeCard(card)
                            displayFeedbackMessage(feedback, card, gameBrain.getPlayersWithoutCurrentPlayer()[which])
                            //gameBrain.getCurrentPlayer().cards.value!!.remove(card)
                        }
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun displayFeedbackMessage(feedback: Feedback, card: Card, target: Character?) {
        val msg = when (feedback) {
            VALID -> card.feedbackMessage.replace("%TARGET%", target?.name_ ?: "")
            HAS_TO_CHOOSE_TARGET -> "Vous devez choisir une cible"
            TARGET_NOT_ENOUGH_VP -> "La cible n'a pas assez de points de victoire"
            TARGET_NOT_ENOUGH_ENERGY -> "La cible n'a pas assez d'argent"
            USER_NOT_ENOUGH_HP -> "Vous n'avez pas assez de points de vie"
            USER_NOT_ENOUGH_ENERGY -> "Vous n'avez pas assez d'argent"
            USER_NOT_ENOUGH_VP -> "Vous n'avez pas assez de votes"
        }

        activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setPositiveButton(
                    "Compris"
                ) { _, _ ->
                }
            }
            builder.setMessage(msg)
                .setTitle("Mes cartes")

            // Create the AlertDialog
            builder.create()
            builder.show()
        }
    }
}