package fr.epita.android.kingofelysee

import android.app.AlertDialog
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat


class GameBoard : Fragment() {

    private val gameBrain : GameBrain by activityViewModels()

    private lateinit var  communicator: Communicator

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            val alertDialog: AlertDialog? = activity?.let {
                val builder = AlertDialog.Builder(it)
                builder.apply {
                    setPositiveButton("Quitter"
                    ) { dialog, id ->
                        WindowCompat.setDecorFitsSystemWindows(it.window, false)
                        WindowInsetsControllerCompat(it.window,
                            it.window.decorView.findViewById(android.R.id.content)).let { controller ->
                            controller.hide(WindowInsetsCompat.Type.systemBars())

                            // When the screen is swiped up at the bottom
                            // of the application, the navigationBar shall
                            // appear for some time
                            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                        }
                        findNavController().navigate(R.id.action_gameBoard_to_gameMenu)
                    }
                    setNegativeButton("Annuler"
                    ) { dialog, id ->
                        WindowCompat.setDecorFitsSystemWindows(it.window, false)
                        WindowInsetsControllerCompat(it.window,
                            it.window.decorView.findViewById(android.R.id.content)).let { controller ->
                            controller.hide(WindowInsetsCompat.Type.systemBars())

                            // When the screen is swiped up at the bottom
                            // of the application, the navigationBar shall
                            // appear for some time
                            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                        }
                    }
                }
                builder.setMessage("Êtes-vous sûr de vouloir quitter la partie en cours ?")
                    .setTitle("Attention")


                // Create the AlertDialog
                builder.create()
                builder.show()
            }
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view : View =inflater.inflate(R.layout.fragment_game_board, container, false)

        val incrementButton : Button = view.findViewById(R.id.mycards_button)

        incrementButton.setOnClickListener{
            gameBrain.characters[0].incrementLifePoints(-10)
        }

        communicator = activity as Communicator

        var id=1;
        for (i in 0..5) {
            val c = gameBrain.characters[i]
            communicator.passCharacterToFragment(if (c.isThePlayer_) 0 else id++, i)

        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val shopButton: Button = view.findViewById(R.id.shop_button)
        val quitShopButton: Button = view.findViewById(R.id.quit_shop)
        quitShopButton.setBackgroundColor(Color.RED)

        shopButton.setOnClickListener {
            communicator.loadShopFragment()
            shopButton.visibility = View.GONE
            quitShopButton.visibility = View.VISIBLE
        }

        quitShopButton.setOnClickListener {
            communicator.unloadShopFragment()
            quitShopButton.visibility = View.GONE
            shopButton.visibility = View.VISIBLE
        }


        communicator.loadMap()
    }




}