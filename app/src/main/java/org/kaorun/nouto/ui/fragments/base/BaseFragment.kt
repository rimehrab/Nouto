package org.kaorun.nouto.ui.fragments.base

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.transition.Fade
import androidx.transition.Slide
import com.google.android.material.R

@SuppressLint("PrivateResource")
abstract class BaseFragment(contentLayoutId: Int) : Fragment(contentLayoutId) {
    protected val durationAppearing by lazy {
        resources.getInteger(
            R.integer.m3_sys_motion_duration_medium4
        ).toLong()
    }
    private val durationDisappearing by lazy {
        resources.getInteger(
            R.integer.m3_sys_motion_duration_short4
        ).toLong()
    }
    private val interpolatorAppearing by lazy {
        AnimationUtils.loadInterpolator(
            requireContext(),
            R.interpolator.m3_sys_motion_easing_emphasized_decelerate
        )
    }
    private val interpolatorDisappearing by lazy {
        AnimationUtils.loadInterpolator(
            requireContext(),
            R.interpolator.m3_sys_motion_easing_emphasized_accelerate
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = slideAnimation(true)
        returnTransition = slideAnimation(false)
        exitTransition = fadeAnimation(false)
        reenterTransition = fadeAnimation(true)
//        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true).apply {
//            duration = durationAppearing
//            interpolator = interpolatorAppearing
//        }
//        returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false).apply {
//            duration = durationDisappearing
//            interpolator = interpolatorDisappearing
//        }
//        exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true).apply {
//            duration = durationDisappearing
//            interpolator = interpolatorDisappearing
//        }
//        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false).apply {
//            duration = durationAppearing
//            interpolator = interpolatorAppearing
//        }
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