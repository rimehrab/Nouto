package org.kaorun.nouto.ui.fragments

import android.annotation.SuppressLint
import android.view.Gravity
import android.view.animation.AnimationUtils.loadInterpolator
import androidx.fragment.app.Fragment
import androidx.transition.Fade
import androidx.transition.Slide

@SuppressLint("PrivateResource")
abstract class BaseFragment(contentLayoutId: Int) : Fragment(contentLayoutId) {
    protected val durationAppearing by lazy {
        resources.getInteger(
            com.google.android.material.R.integer.m3_sys_motion_duration_medium4
        ).toLong()
    }
    private val durationDisappearing by lazy {
        resources.getInteger(
            com.google.android.material.R.integer.m3_sys_motion_duration_short4
        ).toLong()
    }
    private val interpolatorAppearing by lazy {
        loadInterpolator(
            requireContext(),
            com.google.android.material.R.interpolator.m3_sys_motion_easing_emphasized_decelerate
        )
    }
    private val interpolatorDisappearing by lazy {
        loadInterpolator(
            requireContext(),
            com.google.android.material.R.interpolator.m3_sys_motion_easing_emphasized_accelerate
        )
    }
    protected fun fadeAnimation(isAppearing: Boolean) = Fade().apply {
        if (isAppearing) {
            duration = durationAppearing
            interpolator = interpolatorAppearing
        } else {
            duration = durationDisappearing
            interpolator =interpolatorDisappearing
        }
    }

    protected fun slideAnimation(isAppearing: Boolean) = Slide(Gravity.END).apply {
        if (isAppearing) {
            duration = durationAppearing
            interpolator = interpolatorAppearing
        } else {
            duration = durationDisappearing
            interpolator =interpolatorDisappearing
        }
    }
}