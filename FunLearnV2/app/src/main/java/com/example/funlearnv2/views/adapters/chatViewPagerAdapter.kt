package com.example.funlearnv2.views.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.funlearnv2.views.fragments.ChatStatusFragment
import com.example.funlearnv2.views.fragments.GroupChatFragment
import com.example.funlearnv2.views.fragments.PrivateChatFragment
import com.example.funlearnv2.views.fragments.PublicChatFragment

private val TAB_TITLES = arrayOf("public", "private", "group", "status")
private val fragment = arrayOf(PublicChatFragment(), PrivateChatFragment(), GroupChatFragment(), ChatStatusFragment())

class chatViewPagerAdapter(private val context: Context, fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment = fragment[position]
    override fun getPageTitle(position: Int): CharSequence? = TAB_TITLES[position]
    override fun getCount(): Int = 4
}
