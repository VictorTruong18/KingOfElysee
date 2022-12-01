package fr.epita.android.kingofelysee.sections

import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import fr.epita.android.kingofelysee.GameBrain
import fr.epita.android.kingofelysee.R

class MapFragment : Fragment() {
    private val gameBrain : GameBrain by activityViewModels()
    lateinit var hillImg1View : ImageView
    lateinit var hillImg2View : ImageView
    lateinit var hillBtn : Button
    lateinit var nextBtn : Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hillBtn = view.findViewById(R.id.hill_button)
        hillBtn.visibility = View.GONE
        hillBtn.setOnClickListener {
            val current = gameBrain.characters.filter { c -> c.isThePlayer_ }.first()
            if(current.onTheHill_){
                gameBrain.removeFromHill(current)
            }else{
                gameBrain.addToHill(current)
            }
            gameBrain.waitNext.value = gameBrain.waitNext.value
        }
        nextBtn = view.findViewById(R.id.next_button)
        nextBtn.setOnClickListener {
            gameBrain.waitNext.postValue(false)
        }
        nextBtn.isClickable = false

        hillImg1View = view.findViewById(R.id.hill_img_1)
        hillImg2View = view.findViewById(R.id.hill_img_2)
        val hillImages: Array<ImageView> = arrayOf(hillImg1View, hillImg2View)


        gameBrain.hill.observe(viewLifecycleOwner, Observer {
            hillImages.forEach { imgv ->
                imgv.visibility = View.GONE
            }
            var imageIndex = 0
            for (c in it){
                if(imageIndex < hillImages.size){
                    hillImages[imageIndex].visibility = View.VISIBLE
                    hillImages[imageIndex++].setImageResource(c.image_)
                }
            }
        })

        gameBrain.waitNext.observe(viewLifecycleOwner, Observer { it ->
            if(!it) {
                nextBtn.alpha = .5F
                nextBtn.isClickable = false
            } else {
                nextBtn.alpha = 1F
                nextBtn.isClickable = true
            }
            val character = gameBrain.characters[gameBrain.characterTurnIndex]
            val player = gameBrain.characters.filter { c -> c.isThePlayer_ }.first()
            if(character.isThePlayer_&& gameBrain.nbTurn > 0){
                if(character.onTheHill_){
                    hillBtn.visibility = View.VISIBLE
                    hillBtn.text = "Démissionner"
                    hillBtn.setBackgroundColor(Color.RED)
                } else if(gameBrain.hill.value?.size!! < 2){
                    hillBtn.visibility = View.VISIBLE
                    hillBtn.text = "Prendre le pouvoir"
                    hillBtn.setBackgroundColor((nextBtn.background as ColorDrawable).color)
                } else {
                    hillBtn.visibility = View.GONE
                }
            }else if(player.canResignTurn_ == gameBrain.nbTurn && player.onTheHill_) {
                hillBtn.visibility = View.VISIBLE
                hillBtn.text = "Démissionner"
                hillBtn.setBackgroundColor(Color.RED)
            }else{
                hillBtn.visibility = View.GONE
            }
        })
    }
}