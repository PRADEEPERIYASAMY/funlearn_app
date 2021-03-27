package com.example.funlearnv2.views.fragments

import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.example.funlearnv2.R
import com.example.funlearnv2.databinding.FragmentWelcomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeFragment : Fragment() {

    private var _binding: FragmentWelcomeBinding? = null
    private val binding
        get() = _binding!!

    private var myViewPagerAdapter: MyViewPagerAdapter? = null
    private lateinit var dots: Array<TextView?>
    private var layouts = arrayListOf(R.layout.slide_1, R.layout.slide_2, R.layout.slide_3, R.layout.slide_4)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addBottomDots(0)
        changeStatusBarColor()

        myViewPagerAdapter = MyViewPagerAdapter()
        binding.viewPager.adapter = myViewPagerAdapter
        binding.viewPager.addOnPageChangeListener(viewPagerPageChangeListener)
        binding.btnSkip.setOnClickListener(View.OnClickListener { moveToNext() })
        binding.btnNext.setOnClickListener(
            View.OnClickListener {
                val current = getItem(+1)
                if (current < layouts.size) {
                    // move to next screen
                    binding.viewPager.currentItem = current
                } else {
                    moveToNext()
                }
            }
        )
    }

    private fun addBottomDots(currentPage: Int) {
        val colorsActive = resources.getIntArray(R.array.array_dot_active)
        val colorsInactive = resources.getIntArray(R.array.array_dot_inactive)
        dots = arrayOfNulls(layouts.size)
        binding.layoutDots.removeAllViews()
        for (i in dots.indices) {
            dots[i] = TextView(context)
            dots[i]!!.text = Html.fromHtml("&#8226;")
            dots[i]!!.textSize = 35f
            dots[i]!!.setTextColor(colorsInactive[currentPage])
            binding.layoutDots.addView(dots[i])
        }
        if (dots.isNotEmpty()) dots[currentPage]!!.setTextColor(colorsActive[currentPage])
    }

    private fun changeStatusBarColor() {
        val window: Window = requireActivity().window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT
    }

    inner class MyViewPagerAdapter : PagerAdapter() {
        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val view = layoutInflater.inflate(layouts[position], container, false)
            container.addView(view)
            return view
        }

        override fun getCount(): Int = layouts.size

        override fun isViewFromObject(view: View, obj: Any): Boolean = view === obj

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            val view = `object` as View
            container.removeView(view)
        }
    }

    private fun getItem(i: Int): Int {
        return binding.viewPager.currentItem + i
    }

    var viewPagerPageChangeListener: OnPageChangeListener = object : OnPageChangeListener {
        override fun onPageSelected(position: Int) {
            addBottomDots(position)

            if (position == layouts.size - 1) {
                binding.btnNext.text = getString(R.string.start)
                binding.btnSkip.visibility = View.GONE
            } else {
                // still pages are left
                binding.btnNext.text = getString(R.string.next)
                binding.btnSkip.visibility = View.VISIBLE
            }
        }

        override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {}
        override fun onPageScrollStateChanged(arg0: Int) {}
    }

    private fun moveToNext() {
        findNavController().navigate(R.id.action_welcomeFragment_to_getStartedFragment)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
