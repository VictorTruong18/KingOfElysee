package fr.epita.android.kingofelysee

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fr.epita.android.kingofelysee.objects.Character
import kotlinx.coroutines.delay
import java.util.*
import kotlin.random.Random.Default.nextBoolean
import kotlin.random.Random.Default.nextInt

class GameBrain : ViewModel()  {
    var characters = listOf<Character>()
    var partyStarted : Boolean = false
    var characterTurnIndex : Int = (0..5).random()
    var nbTurn : Int = 0
    var gamePaused : Boolean = false
    val hill = MutableLiveData<MutableSet<Character>>()

    fun initAllCharacters(character : Array<Character>){
        this.characters = character.toList()
        hill.value = mutableSetOf()
    }

    fun addToHill(character: Character){
        character.onTheHill_ = true
        hill.value?.add(character)
        hill.value = hill.value
    }

    fun removeFromHill(character: Character){
        character.onTheHill_ = false
        hill.value?.remove(character)
        hill.value = hill.value
    }

    fun play(character: Character, dice: List<String>, communicator: Communicator) {
        gamePaused = true
        if(character.isThePlayer_){
            communicator.loadMap()
        }
        val count_attack = dice.count { it == "❗" }
        val count_money = dice.count { it == "\uD83D\uDCB6" }
        val count_life = dice.count { it == "♥" }

        val attack_character = characters.filter { c -> c.onTheHill_ != character.onTheHill_ }
        Log.d("attack", attack_character.toString())
        Log.d("attack", count_attack.toString())
        attack_character.forEach {
            it.incrementLifePoints(-count_attack)
            if (it.onTheHill_) {
                if (it.isThePlayer_) {
                    //TODO:dialog
                } else {
                    it.onTheHill_ = nextBoolean()
                }
            }
        }
        character.incrementEnergyPoints(count_money)
        character.incrementLifePoints(if (character.onTheHill_) 0 else count_life)

        //TODO:update incrementvictory
        character.incrementVictoryPoints(0)


        communicator.dialog(dice.toString(),character.name_+(if(character.isThePlayer_) " (Vous)" else ""))
    }

}