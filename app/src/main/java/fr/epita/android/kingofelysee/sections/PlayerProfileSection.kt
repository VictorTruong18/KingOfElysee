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
import androidx.lifecycle.Observer
import fr.epita.android.kingofelysee.GameBrain
import fr.epita.android.kingofelysee.R
import org.w3c.dom.Text



class PlayerProfileSection : Fragment() {
    private val gameBrain : GameBrain by activityViewModels()
    lateinit var textView : TextView
    lateinit var imageView : ImageView
    lateinit var energyTextView : TextView
    lateinit var lifeTextView : TextView
    var index: Int? = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view : View =inflater.inflate(R.layout.fragment_player_profile_section, container, false)

        index = arguments?.getInt("index")

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textView = view.findViewById(R.id.name_tv)
        imageView  = view.findViewById(R.id.image_img)
        energyTextView = view.findViewById(R.id.energypoints_tv)
        lifeTextView = view.findViewById(R.id.lifepoints_tv)



        if(this.index != null ) {
            this.textView.text = gameBrain.characters[index!!].name_
            this.imageView.setImageResource(gameBrain.characters[index!!].image_)
            gameBrain.characters[index!!].lifePoints_.observe(viewLifecycleOwner, Observer {
                this.lifeTextView.text = gameBrain.characters[index!!].lifePointsString()
            })

            gameBrain.characters[index!!].energyPoints_.observe(viewLifecycleOwner, Observer {
                this.energyTextView.text = gameBrain.characters[index!!].energyPointsString()
            })
        }
    }

}