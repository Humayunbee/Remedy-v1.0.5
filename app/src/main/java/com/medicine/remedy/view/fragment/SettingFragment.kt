package com.medicine.remedy.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import butterknife.ButterKnife
import butterknife.OnClick
import com.medicine.remedy.R
import com.medicine.remedy.config.BaseFragment
import com.medicine.remedy.config.SharedPreferenceManager
import com.medicine.remedy.databinding.LayoutSettingBinding
import com.medicine.remedy.utils.AppConstants
import com.medicine.remedy.utils.AppConstants.Companion.menuToolbar
import com.medicine.remedy.utils.ExtraUtils
import com.medicine.remedy.utils.ExtraUtils.Companion.makePhoneCall
import com.medicine.remedy.utils.ExtraUtils.Companion.startActivityIn
import com.medicine.remedy.utils.ExtraUtils.Companion.startActivityOut
import com.medicine.remedy.utils.ImageUtils.Companion.displayImage
import com.medicine.remedy.view.activity.*
import com.medicine.remedy.view_model.SettingViewModel

/**
 * Date 12/22/2020.
 * Created by Md Atik Faysal(Android Developer)
 *
 */
class SettingFragment : BaseFragment()
{
    private lateinit var binding : LayoutSettingBinding
    private lateinit var viewModel : SettingViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_setting, container, false)
        val view: View = binding.root
        viewModel = ViewModelProviders.of(this).get(SettingViewModel::class.java)
        binding.lifecycleOwner = this
        binding.setting = viewModel
        ButterKnife.bind(this, view)

        initialize()
        return view
    }

    override fun onStart() {
        super.onStart()
        loginStatusObserver()
        viewModel.getCurrentUserInformation()
    }

    override fun initialize() {
        mContext = requireContext()
        mActivity = requireActivity()
        spManager = SharedPreferenceManager(mContext)
        (activity as SettingActivity).setToolbarTitle(menuToolbar)//set toolbar title
        (activity as SettingActivity).hideToolbar()//hide white toolbar

        /**
         * ...display current user cover image
         * ...observe user image url
         */
        viewModel.mlUserImage.observe(viewLifecycleOwner, {
            displayImage(mActivity, binding.ivImage,it, viewModel.mlUserName.value!!)
        })
    }

    @OnClick(value = [R.id.rl_login,R.id.rl_logout,R.id.rl_shipping_address,R.id.rl_order_history,R.id.rl_change_password, R.id.rl_favourites, R.id.fab_cell_no])
    fun onClickListener(view : View)
    {
        when(view.id)
        {
            R.id.rl_login -> startActivityIn(requireActivity(), Intent(requireActivity(), UserAuthenticationActivity::class.java))
            R.id.rl_shipping_address -> startActivityIn(requireActivity(), Intent(requireActivity(), AddressActivity::class.java))
            R.id.rl_order_history -> startActivityIn(requireActivity(), Intent(requireActivity(), OrderHistoryActivity::class.java))
            R.id.fab_cell_no -> makePhoneCall("01736199559", mActivity)
            R.id.rl_change_password -> {
                val fragment: Fragment = ChangePasswordFragment()
                ExtraUtils.showFragment(context as FragmentActivity, fragment)
            }

            R.id.rl_favourites -> startActivityIn(mActivity,Intent(mActivity, FavouriteProductActivity::class.java))

            R.id.rl_logout ->{
                spManager.isLoggedIn(false)//set login status FALSE
                spManager.clearData()//clear user Information while user logged out
                ExtraUtils.closeAllActivity(mActivity, UserAuthenticationActivity::class.java)
            }
        }
    }

    /**
     * ...login status observer
     * ...hide and visible component based login status
     */
    private fun loginStatusObserver()
    {
        viewModel.isUserLoggedIn()
        viewModel.isUserLoggedIn.observe(viewLifecycleOwner, {
            visibleAndHideComponent(it)
        })
    }

    /**
     * ...hide user information layout while no user is logged in
     * ...visible user information layout while a user is logged in
     * ...change login and logout text based on user logged in status
     */
    private fun visibleAndHideComponent(isLoggedIn: Boolean)
    {
        if(isLoggedIn)//if user already logged in
        {
            binding.rlLogin.visibility = View.GONE//hide login view
            binding.viewLogin.visibility = View.GONE

            binding.rlLogout.visibility = View.VISIBLE//visible logout view
            binding.viewLogout.visibility = View.VISIBLE
            binding.lnUserInformation.visibility = View.VISIBLE
        }else //if user is not logged in
        {
            binding.rlLogin.visibility = View.VISIBLE//visible login view
            binding.viewLogin.visibility = View.VISIBLE

            binding.rlLogout.visibility = View.GONE//hide logout view
            binding.viewLogout.visibility = View.GONE
            binding.lnUserInformation.visibility = View.GONE
        }
    }
}