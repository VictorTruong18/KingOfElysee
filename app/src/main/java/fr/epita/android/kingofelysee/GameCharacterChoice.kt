package fr.epita.android.kingofelysee

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import fr.epita.android.kingofelysee.objects.Character
import org.w3c.dom.Text
import java.util.Objects


class GameCharacterChoice : Fragment() {

    private val gameBrain : GameBrain by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(
            R.layout.fragment_game_character_choice, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var characters = arrayOf<Character>(
            Character(
                name = "Emanuella Macron", image = R.drawable.macron
            ),
            Character(
                name = "Martin Le Pen", image = R.drawable.lepen
            ),
            Character(
                name = "Lucie Melanchon", image = R.drawable.melenchon
            ),
            Character(
                name = "Yann Hidalgo", image = R.drawable.hidalgo
            ),
            Character(
                name = "Jeanne Lassale", image = R.drawable.lasalle
            ),
            Character(
                name = "Vladimir Pecresse", image = R.drawable.pecresse
            )
        )
        characters.shuffle()

        for (i in 1..6) {
            val image : ImageView = view.findViewById(resources.getIdentifier("img_$i", "id", context?.packageName))
            val text : TextView = view.findViewById(resources.getIdentifier("text_$i", "id", context?.packageName))
            image.setImageResource(characters[i-1].image_)
            text.text = characters[i-1].name_
            image.setOnClickListener {
                characters[i-1].isThePlayer_ = true
                gameBrain.chooseCharacter(characters[i-1])
                findNavController().navigate(R.id.action_gameCharacterChoice_to_gameBoard)
            }
        }

        val imageRandom : ImageView = view.findViewById(R.id.img_7)
        imageRandom.setOnClickListener {
            var randomCharacter = characters[(0..characters.size - 1).random()]
            randomCharacter.isThePlayer_ = true
            gameBrain.chooseCharacter(randomCharacter)
            findNavController().navigate(R.id.action_gameCharacterChoice_to_gameBoard)
        }


    }
}