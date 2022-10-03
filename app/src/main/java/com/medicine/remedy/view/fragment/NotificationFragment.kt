package com.medicine.remedy.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import butterknife.ButterKnife
import com.medicine.remedy.R
import com.medicine.remedy.adapter.NotificationAdapter
import com.medicine.remedy.config.BaseFragment
import com.medicine.remedy.config.SharedPreferenceManager
import com.medicine.remedy.databinding.LayoutRecyclerViewBinding
import com.medicine.remedy.interfaces.OnRetryCallback
import com.medicine.remedy.utils.*
import com.medicine.remedy.view.activity.NotificationActivity
import com.medicine.remedy.view_model.UserInformationViewModel

/**
 * Created by Md. Humayun Farid (Android Developer)
 * Create on 4/9/2021
  * Email: humayunfarid1997@gmail.com
 * *** Happy Coding ***
 */
class NotificationFragment : BaseFragment()
{
    private lateinit var binding : LayoutRecyclerViewBinding
    private lateinit var viewModel : UserInformationViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_recycler_view, container, false)
        val view: View = binding.root
        viewModel = ViewModelProviders.of(this).get(UserInformationViewModel::class.java)
        binding.lifecycleOwner = this
        binding.notification = viewModel
        ButterKnife.bind(this, view)

        initialize()
        return view
    }

    override fun onStart() {
        super.onStart()
        if(!ExtraUtils.isOnline(requireContext()))
        {
            DialogUtils.noInternetDialogFullScreen(mActivity, callBack)
            return
        }else notificationObserver()
    }

    override fun initialize() {
        mContext = requireContext()
        mActivity = requireActivity()
        spManager = SharedPreferenceManager(mContext)
        loadingDialog = LoadingUtils.loadingView(mContext)
        (activity as NotificationActivity).setToolbarTitle(AppConstants.notification)//set toolbar title
    }

    /**
     * ...display order notification list on recyclerview
     * ...check internet connection
     * ...display/hide loading view
     */
    private fun notificationObserver()
    {
        if(!ExtraUtils.isOnline(requireContext()))
        {
            DialogUtils.noInternetDialogFullScreen(mActivity, callBack)
            return
        }

        val adapter = NotificationAdapter { model ->
           if (model.readStatus ==  null) viewModel.viewNotification(model.notificationId)
            AlertDialog.Builder(requireContext()).apply {
                this.setTitle(model.title.ifEmpty { "Details" })
                this.setPositiveButton("Close"){ _, _ -> }
                this.setMessage(model.description)
            }.show()
        }
        ViewUtils.verticalRecyclerView(mActivity, binding.rvList).adapter = adapter
        binding.rvList.isNestedScrollingEnabled = false

        loadingDialog.show()
        viewModel.loadNotifications()

        viewModel.notifications.observe(viewLifecycleOwner) {
            if (it.responseCode == AppConstants.RESPONSE_SUCCESS_CODE) {
                binding.emptyView.visibility = View.GONE
                binding.rvList.visibility = View.VISIBLE
                adapter.items = it.notificationList
            } else {
                binding.emptyView.visibility = View.VISIBLE
                binding.rvList.visibility = View.GONE
            }

            loadingDialog.dismiss()
        }


    }


    private val callBack = (object : OnRetryCallback {
        override fun onRetry() {
            notificationObserver()
        }
    })
}