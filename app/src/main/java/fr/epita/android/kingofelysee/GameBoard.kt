package fr.epita.android.kingofelysee

import android.app.AlertDialog
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.HorizontalScrollView
import android.widget.TextView
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.core.view.marginStart
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import fr.epita.android.kingofelysee.sections.MapFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import fr.epita.android.kingofelysee.objects.Character


class GameBoard : Fragment() {

    private val gameBrain: GameBrain by activityViewModels()

    private lateinit var communicator: Communicator

    lateinit var gameStatusTV: TextView
    lateinit var scrollBotView: HorizontalScrollView
    var fragmentWidth: Int = 0

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gameBrain.partyStarted = true
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            activity?.let {

                val builder = AlertDialog.Builder(it)
                builder.apply {
                    setPositiveButton(
                        "Quitter"
                    ) { _, _ ->
                        requireActivity().viewModelStore.clear()
                        findNavController().navigate(R.id.action_gameBoard_to_gameMenu)
                    }
                    setNegativeButton(
                        "Annuler"
                    ) { _, _ ->
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
    ): View {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_game_board, container, false)


        val incrementButton : Button = view.findViewById(R.id.mycards_button)


        incrementButton.setOnClickListener {
            gameBrain.characters[0].incrementLifePoints(-10)
        }

        communicator = activity as Communicator

        var id = 1;
        for (i in 0..5) {
            val c = gameBrain.characters[i]
            c.fragment_id_ = if (c.isThePlayer_) 0 else id++
            communicator.passCharacterToFragment(c.fragment_id_, i)

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
        scrollBotView = view.findViewById(R.id.scrollView2)
        val profileView = view.findViewById<View?>(R.id.profile_1)
        fragmentWidth = profileView.layoutParams.width + profileView.marginStart

        shopButton.setOnClickListener {
            communicator.loadShopFragment()
            shopButton.visibility = View.GONE
            myCardsButton.visibility = View.GONE
            quitShopButton.visibility = View.VISIBLE
        }
        shopButton.isClickable = false

        quitShopButton.setOnClickListener {
            communicator.unloadFragment()
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
        myCardsButton.isClickable = false

        communicator.loadMap()

        // Game Loop
        lifecycleScope.launch() {
            while (true) {
                delay(250)
                while (gameBrain.partyStarted) {

                    val character = gameBrain.characters[gameBrain.characterTurnIndex]
                    // If you open the app you receive this message

                    if(gameBrain.nbTurn == 0){
                        gameBrain.gamePaused = true
                        communicator.dialog("La France va mal ! Trouve le moyen de t'enrichir malgré tout !",
                            "Bienvenue " + (gameBrain.characters.find { it.isThePlayer_}?.name_ ),
                        true)
                        while(gameBrain.gamePaused){
                            delay(500)
                        }
                        gameBrain.addToHill(character)
                        val secondCharacter =  gameBrain.characters[(gameBrain.characterTurnIndex + 1) % gameBrain.characters.size]
                        gameBrain.addToHill(secondCharacter)

                        delay(1000)
                        gameBrain.gamePaused = true
                        communicator.dialog(character.name_ + " et " + secondCharacter.name_  + " ont pris possession de l'Elysée" ,"Passage à l'Elysée", true)

                        while(gameBrain.gamePaused){
                            delay(500)
                        }
                    }

                    // Game checks if there aren't already people on the top of the hill
                    // Whose turn is it ?
                    // Is he Alive ?
                    if (character.lifePoints_.value!! > 0) {
                        if(character.onTheHill_) {
                            character.incrementVictoryPoints(2)
                            gameBrain.hillTurn = gameBrain.nbTurn
                        }
                        if(gameBrain.hill.value?.size == 0){
                            gameBrain.addToHill(character)
                        } else if(!character.onTheHill_ && gameBrain.hill.value?.size!! < 2) {
                            if((1..10).random() < character.lifePoints_.value!!) {
                                gameBrain.addToHill(character)
                            }
                        }
                        // Is he the player ?
                        if (character.isThePlayer_) {
                            communicator.toggleShopBtn()

                            communicator.loadDiceFragment()

                            gameBrain.waitNext.value = true
                            while(gameBrain.waitNext.value!!){
                                delay(50)
                            }
                            communicator.toggleShopBtn()

                            delay(1000)
                        } else {
                            scrollBotView.post(Runnable {
                                scrollBotView.scrollTo(
                                    fragmentWidth * (character.fragment_id_ - 1),
                                    0
                                )
                            })
                            gameStatusTV.text = "C'est au tour de " + character.name_

                            delay(1000)
                            gameBrain.play(character, GameDice().getStringDice(), communicator)
                            gameBrain.waitNext.value = true
                            gameStatusTV.text = character.name_ + " a fini son tour"
                            while(gameBrain.waitNext.value!!){
                                delay(50)
                            }
                        }
                    }


                    // Victory / Defeat check

                    // Someone has won
                    if (character.victoryPoints_.value!! >= 20) {
                        if (character.isThePlayer_) {
                            Log.d("Game has ended", "The player has won")
                            displayEndMessage("Bravo !", "Vous avez sauvé la France !")
                            gameBrain.partyStarted = false
                        } else {
                            Log.d("Game has ended", "Another player has won")
                            displayEndMessage(
                                "Honte à vous ! ",
                                "Vous n'avez pas réussi·e à sauver la France."
                            )
                            gameBrain.partyStarted = false
                        }
                    }

                    for (loopedCharacter in gameBrain.characters) {
                        // The player is the last standing
                        if (loopedCharacter.lifePoints_.value!! > 0 && loopedCharacter.isThePlayer_ &&
                            gameBrain.getAllNonPlayerCharacters().sumOf { it.lifePoints_.value!! } == 0
                        ) {
                            displayEndMessage("Bravo !", "Vous avez sauvé la France !")
                            Log.d("Game has ended", "All the other players died")
                            gameBrain.partyStarted = false
                        }

                        // The player has no life points left
                        if (loopedCharacter.isThePlayer_ && loopedCharacter.lifePoints_.value!! <= 0) {
                            Log.d("Game has ended", "The player has no life points left")
                            displayEndMessage(
                                "Honte à vous ! ",
                                "Vous n'avez pas réussi·e à sauver la France."
                            )
                            gameBrain.partyStarted = false
                        }
                    }

                    // End of the turn go to the next character
                    if (gameBrain.characterTurnIndex < gameBrain.characters.size - 1) {
                        gameBrain.characterTurnIndex++
                    } else {
                        gameBrain.characterTurnIndex = 0
                    }
                    gameBrain.nbTurn += 1
                }
            }
        }
    }

    private fun getRankingMessage(characters: List<Character>): String {
        val sortedCharacters = characters.sortedByDescending { it.victoryPoints_.value!! }
        var message =""
        for (character in sortedCharacters) {
            message += character.name_ + "  " +character.victoryPoints_.value!! + " \uD83D\uDDF3 \n"
        }
        return message
    }

    fun displayEndMessage(title: String, msg: String) {
        activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setPositiveButton(
                    "Rejouer"
                ) { _, _ ->
                    requireActivity().viewModelStore.clear()
                    findNavController().navigate(R.id.action_gameBoard_to_gameMenu)
                }
            }
            builder.setMessage(msg + "\n" + getRankingMessage(gameBrain.characters))
                .setTitle(title)

            // Create the AlertDialog
            builder.create()
            builder.show()
        }
    }

}