package com.example.finalprojectstudyapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.finalprojectstudyapp.databinding.FragmentHelpBinding

class HelpFragment : Fragment() {

    private lateinit var binding: FragmentHelpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHelpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setClickListeners()
    }

    private fun setClickListeners(){

        binding.faqHead.setOnClickListener{
            if(binding.faqCard.visibility == View.VISIBLE){
                binding.faqCard.visibility = View.GONE
            }else{
                binding.faqCard.visibility = View.VISIBLE
            }
        }

        binding.privacyPolicyHead.setOnClickListener{
            if(binding.privacyPolicyCard.visibility == View.VISIBLE){
                binding.privacyPolicyCard.visibility = View.GONE
            }else{
                binding.privacyPolicyCard.visibility = View.VISIBLE
            }
        }

        binding.feedbackHead.setOnClickListener{
            if(binding.feedbackCard.visibility == View.VISIBLE){
                binding.feedbackCard.visibility = View.GONE
            }else{
                binding.feedbackCard.visibility = View.VISIBLE
            }
        }


    }


}