package fr.epita.android.kingofelysee

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import fr.epita.android.kingofelysee.objects.Character


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

        val macronImage : ImageView = view.findViewById(R.id.macron_img)
        val lepenImage : ImageView = view.findViewById(R.id.lepen_img)
        val melenchonImage : ImageView = view.findViewById(R.id.melenchon_img)
        val hidalgoImage : ImageView = view.findViewById(R.id.hidalgo_img)
        val lasalleImage : ImageView = view.findViewById(R.id.lassalle_img)
        val pecressImageView : ImageView = view.findViewById(R.id.pecress_img)

        //TODO : Clean this please (Sorry)
        //TODO : Mettre le vrai nom de tous les persos
        macronImage.setOnClickListener {
            val userCharacter : Character = Character(
                name = "Macron", image = R.drawable.macron, isThePlayer = true
            )
            var characters = arrayOf<Character>(
                userCharacter,
                Character(
                    name = "Lepen", image = R.drawable.lepen, isThePlayer = false
                ),
                Character(
                    name = "Melenchon", image = R.drawable.melenchon, isThePlayer = false
                ),
                Character(
                    name = "Hidalgo", image = R.drawable.hidalgo, isThePlayer = false
                ),
                Character(
                    name = "Lasalle", image = R.drawable.lasalle, isThePlayer = false
                ),
                Character(
                    name = "Pecresse", image = R.drawable.pecresse, isThePlayer = false
                )
            )

            gameBrain.chooseCharacter(userCharacter)
            findNavController().navigate(R.id.action_gameCharacterChoice_to_gameBoard)
        }
        lepenImage.setOnClickListener {
            val userCharacter : Character = Character(
                name = "Lepen", image = R.drawable.lepen, isThePlayer = true
            )
            var characters = arrayOf<Character>(
                Character(
                    name = "Macron", image = R.drawable.macron, isThePlayer = false
                ),
                userCharacter,
                Character(
                    name = "Melenchon", image = R.drawable.melenchon, isThePlayer = false
                ),
                Character(
                    name = "Hidalgo", image = R.drawable.hidalgo, isThePlayer = false
                ),
                Character(
                    name = "Lasalle", image = R.drawable.lasalle, isThePlayer = false
                ),
                Character(
                    name = "Pecresse", image = R.drawable.pecresse, isThePlayer = false
                )
            )
            gameBrain.chooseCharacter(userCharacter)
            findNavController().navigate(R.id.action_gameCharacterChoice_to_gameBoard)
        }
        melenchonImage.setOnClickListener {
            val userCharacter : Character = Character(
                name = "Melenchon", image = R.drawable.melenchon, isThePlayer = true
            )
            var characters = arrayOf<Character>(
                Character(
                    name = "Macron", image = R.drawable.macron, isThePlayer = false
                ),
                Character(
                    name = "Lepen", image = R.drawable.lepen, isThePlayer = false
                ),
                userCharacter,
                Character(
                    name = "Hidalgo", image = R.drawable.hidalgo, isThePlayer = false
                ),
                Character(
                    name = "Lasalle", image = R.drawable.lasalle, isThePlayer = false
                ),
                Character(
                    name = "Pecresse", image = R.drawable.pecresse, isThePlayer = false
                )
            )
            gameBrain.chooseCharacter(userCharacter)
            findNavController().navigate(R.id.action_gameCharacterChoice_to_gameBoard)
        }
        hidalgoImage.setOnClickListener {
            val userCharacter : Character = Character(
                name = "Hidalgo", image = R.drawable.hidalgo, isThePlayer = true
            )
            var characters = arrayOf<Character>(
                Character(
                    name = "Macron", image = R.drawable.macron, isThePlayer = false
                ),
                Character(
                    name = "Lepen", image = R.drawable.lepen, isThePlayer = false
                ),
                Character(
                    name = "Melenchon", image = R.drawable.melenchon, isThePlayer = false
                ),
                userCharacter,
                Character(
                    name = "Lasalle", image = R.drawable.lasalle, isThePlayer = false
                ),
                Character(
                    name = "Pecresse", image = R.drawable.pecresse, isThePlayer = false
                )
            )
            gameBrain.chooseCharacter(userCharacter)
            findNavController().navigate(R.id.action_gameCharacterChoice_to_gameBoard)
        }
        lasalleImage.setOnClickListener {
            val userCharacter : Character = Character(
                name = "Lasalle", image = R.drawable.lasalle, isThePlayer = true
            )
            var characters = arrayOf<Character>(
                Character(
                    name = "Macron", image = R.drawable.macron, isThePlayer = false
                ),
                Character(
                    name = "Lepen", image = R.drawable.lepen, isThePlayer = false
                ),
                Character(
                    name = "Melenchon", image = R.drawable.melenchon, isThePlayer = false
                ),
                Character(
                    name = "Hidalgo", image = R.drawable.hidalgo, isThePlayer = false
                ),
                userCharacter,
                Character(
                    name = "Pecresse", image = R.drawable.pecresse, isThePlayer = false
                )
            )
            gameBrain.chooseCharacter(userCharacter)
            findNavController().navigate(R.id.action_gameCharacterChoice_to_gameBoard)
        }
        pecressImageView.setOnClickListener {
            val userCharacter : Character = Character(
                name = "Pecresse", image = R.drawable.pecresse, isThePlayer = true
            )
            var characters = arrayOf<Character>(
                Character(
                    name = "Macron", image = R.drawable.macron, isThePlayer = false
                ),
                Character(
                    name = "Lepen", image = R.drawable.lepen, isThePlayer = false
                ),
                Character(
                    name = "Melenchon", image = R.drawable.melenchon, isThePlayer = false
                ),
                Character(
                    name = "Hidalgo", image = R.drawable.hidalgo, isThePlayer = false
                ),
                Character(
                    name = "Lasalle", image = R.drawable.lasalle, isThePlayer = false
                ),
                userCharacter
            )
            gameBrain.chooseCharacter(userCharacter)
            findNavController().navigate(R.id.action_gameCharacterChoice_to_gameBoard)
        }


    }
}