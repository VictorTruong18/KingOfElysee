package fr.epita.android.kingofelysee.sections

import android.media.Image
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import fr.epita.android.kingofelysee.GameBrain
import fr.epita.android.kingofelysee.R
import org.w3c.dom.Text


class PlayerProfileSection : Fragment() {
    private val gameBrain : GameBrain by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view : View =inflater.inflate(R.layout.fragment_player_profile_section, container, false)
        var textView : TextView = view.findViewById(R.id.name_tv)
        var imageView : ImageView = view.findViewById(R.id.image_img)
        var energyTextView : TextView = view.findViewById(R.id.energypoints_tv)
        var lifeTextView : TextView = view.findViewById(R.id.lifepoints_tv)
        textView.text = gameBrain.player.name_
        imageView.setImageResource(gameBrain.player.image_)
        energyTextView.text = gameBrain.player.energyPoints_.toString()
        lifeTextView.text = gameBrain.player.lifePoints_.toString()

        return view
    }


}