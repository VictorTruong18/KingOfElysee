package fr.epita.android.kingofelysee

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
/**
 * A simple [Fragment] subclass.
 * Use the [ChooseTargetFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChooseTargetFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_choose_target, container, false)
    }
}