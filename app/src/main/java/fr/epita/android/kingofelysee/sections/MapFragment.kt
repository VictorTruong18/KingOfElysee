package fr.epita.android.kingofelysee.sections

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import fr.epita.android.kingofelysee.GameBrain
import fr.epita.android.kingofelysee.R

class MapFragment : Fragment() {
    private val gameBrain : GameBrain by activityViewModels()
    lateinit var hillImg1View : ImageView
    lateinit var hillImg2View : ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
    }
}