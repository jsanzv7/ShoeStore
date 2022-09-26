package com.udacity.shoestore

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.whenCreated
import androidx.navigation.fragment.findNavController
import com.udacity.shoestore.SaveState.*
import com.udacity.shoestore.databinding.FragmentDetailsBinding
import com.udacity.shoestore.models.Shoe

class DetailsFragment : Fragment() {

    private val viewModel: DetailsViewModel by activityViewModels()

    private val shoeData = Shoe("", 0.0, "", "")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentDetailsBinding.inflate(
            inflater, container, false
        )

        binding.shoeViewModel = viewModel
        binding.lifecycleOwner = this
        binding.shoeData = shoeData

        binding.cancelButton.setOnClickListener {
            val action = DetailsFragmentDirections.actionDetailsFragmentToShoeListFragment()
            findNavController().navigate(action)
        }

        viewModel.saveState.observe(this as LifecycleOwner, Observer{ ss ->
            when {
                ss == SAVE -> {
                    navigateToShoeList()
                    viewModel.onEventSaveComplete()
                }
            }
        })

        return binding.root

    }


    private  fun navigateToShoeList() {
        val action = DetailsFragmentDirections.actionDetailsFragmentToShoeListFragment()
        findNavController().navigate(action)

    }
}