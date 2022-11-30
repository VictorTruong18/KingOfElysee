package fr.epita.android.kingofelysee

import androidx.lifecycle.ViewModel
import fr.epita.android.kingofelysee.objects.Character
import java.util.*
import kotlin.random.Random.Default.nextInt

class GameBrain : ViewModel()  {
    var characters = listOf<Character>()
    var partyStarted : Boolean = false
    var characterTurnIndex : Int = (0..4).random()
    var nbTurn : Int = 0
    var gamePaused : Boolean = false
    lateinit var hill : ArrayDeque<Character>

    fun initAllCharacters(character : Array<Character>){
        this.characters = character.toList()

    }
}