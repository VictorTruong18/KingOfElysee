package fr.epita.android.kingofelysee

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class GameBoard : Fragment() {

    private val gameBrain : GameBrain by activityViewModels()

    private lateinit var  communicator: Communicator



    lateinit var gameStatusTV : TextView

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gameBrain.partyStarted = true
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            val alertDialog: AlertDialog? = activity?.let {

                val builder = AlertDialog.Builder(it)
                builder.apply {
                    setPositiveButton("Quitter"
                    ) { dialog, id ->
                        WindowCompat.setDecorFitsSystemWindows(it.window, false)
                        gameBrain.partyStarted = false
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

        val diceButton: Button = view.findViewById(R.id.navToDiceButton)

        diceButton.setOnClickListener {

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
        val myCardsButton: Button = view.findViewById(R.id.mycards_button)

        gameStatusTV = view.findViewById(R.id.game_status)

        shopButton.setOnClickListener {
            communicator.loadShopFragment()
            shopButton.visibility = View.GONE
            myCardsButton.visibility = View.GONE
            quitShopButton.visibility = View.VISIBLE
        }

        quitShopButton.setOnClickListener {
            communicator.unloadShopFragment()
            quitShopButton.visibility = View.GONE
            shopButton.visibility = View.VISIBLE
            myCardsButton.visibility = View.VISIBLE
        }

        myCardsButton.setOnClickListener {
            communicator.loadMyCardsFragment()
            shopButton.visibility = View.GONE
            myCardsButton.visibility = View.GONE
            quitShopButton.visibility = View.VISIBLE
        }

        communicator.loadMap()

        // Game Loop
        lifecycleScope.launch(){
            while(true) {
                delay(2000)
                while (gameBrain.partyStarted) {

                    // If you open the app you receive this message
                    if(gameBrain.nbTurn == 0){
                        sendBlockingDialogToPlayer("La France va mal ! Trouve le moyen de t'enrichir malgrès tout",
                            "Bienvenue " + (gameBrain.characters.find { it.isThePlayer_}?.name_ )
                        )

                    }

                    // Game checks if there aren't already people on the top of the hill
                    // If there is nobody then it's the person whose it's his turn and the person after to get on the hills
                    // If there is one hill left then it's the person  whose it's his turn  to enter the hill
                    // If we fall back to only four alive characters there is only one hill




                    // Whose turn is it ?
                    // Is he Alive ?
                    if(gameBrain.characters[gameBrain.characterTurnIndex].lifePoints_.value!! > 0) {
                        // Is he the player ?
                        if (gameBrain.characters[gameBrain.characterTurnIndex].isThePlayer_) {
                            gameStatusTV.text = "C'est à toi de convaincre les Français !"
                        } else {
                            gameStatusTV.text =
                                "C'est au tour de " + gameBrain.characters[gameBrain.characterTurnIndex].name_
                            delay(2000)
                        }
                    }

                    Log.d("Loop","Loop is still running")

                    // End of the turn go to the next character
                    if(gameBrain.characterTurnIndex < gameBrain.characters.size - 1) {
                        gameBrain.characterTurnIndex++
                        gameBrain.nbTurn += 1
                    }
                    else {
                        gameBrain.characterTurnIndex = 0
                        gameBrain.nbTurn += 1
                    }
                }
            }
        }
    }


    suspend fun sendBlockingDialogToPlayer(message: String, title: String){
        gameBrain.gamePaused = true
        communicator.dialog(message, title)
        while(gameBrain.gamePaused){delay(1000)}
    }





}