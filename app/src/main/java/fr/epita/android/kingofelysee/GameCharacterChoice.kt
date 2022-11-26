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

        macronImage.setOnClickListener {
            gameBrain.chooseCharacter("Macron")
            findNavController().navigate(R.id.action_gameCharacterChoice_to_gameBoard)
        }
        lepenImage.setOnClickListener {
            gameBrain.chooseCharacter("LePen")
            findNavController().navigate(R.id.action_gameCharacterChoice_to_gameBoard)
        }
        melenchonImage.setOnClickListener {
            gameBrain.chooseCharacter("Melenchon")
            findNavController().navigate(R.id.action_gameCharacterChoice_to_gameBoard)
        }
        hidalgoImage.setOnClickListener {
            gameBrain.chooseCharacter("Hidalgo")
            findNavController().navigate(R.id.action_gameCharacterChoice_to_gameBoard)
        }
        lasalleImage.setOnClickListener {
            gameBrain.chooseCharacter("Lasalle")
            findNavController().navigate(R.id.action_gameCharacterChoice_to_gameBoard)
        }
        pecressImageView.setOnClickListener {
            gameBrain.chooseCharacter("Pecress")
            findNavController().navigate(R.id.action_gameCharacterChoice_to_gameBoard)
        }


    }
}