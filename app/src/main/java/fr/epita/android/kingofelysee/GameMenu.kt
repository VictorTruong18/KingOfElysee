package fr.epita.android.kingofelysee

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController


class GameMenu : Fragment() {

    private lateinit var start_btn : Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_game_menu, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        start_btn = view.findViewById(R.id.start_btn)

        start_btn.setOnClickListener {
            findNavController().navigate(R.id.action_gameMenu_to_gameCharacterChoice)
        }
    }


}