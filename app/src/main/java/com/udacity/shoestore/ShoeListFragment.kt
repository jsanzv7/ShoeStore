package com.udacity.shoestore

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.udacity.shoestore.databinding.FragmentShoeListBinding
import com.udacity.shoestore.databinding.ShoeRowBinding
import timber.log.Timber

class ShoeListFragment : Fragment() {

    private val viewModel: DetailsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentShoeListBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_shoe_list, container, false
        )

        viewModel.shoeList.observe(this as LifecycleOwner, Observer {

            viewModel.shoeList.value?.forEach { shoe ->
                val inBinding = ShoeRowBinding.inflate(layoutInflater)
                inBinding.shoeData = shoe
                binding.shoeListLinearLayout.addView(inBinding.root)
            }
        })

        binding.addShoeButton.setOnClickListener {
            val action = ShoeListFragmentDirections.actionShoeListFragmentToDetailsFragment()
            findNavController().navigate(action)
        }


        setHasOptionsMenu(true)

        return binding.root
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.logout_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

}