package com.example.funlearnv2.views.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.funlearnv2.R
import com.example.funlearnv2.databinding.FragmentSignUpBinding
import com.example.funlearnv2.models.*
import com.example.funlearnv2.utils.getString
import com.example.funlearnv2.utils.hideKeyBoard
import com.example.funlearnv2.utils.showSnackbar
import com.example.funlearnv2.viewmodels.FireStoreViewModel
import com.example.funlearnv2.viewmodels.actions.FireStoreAction
import com.google.android.gms.location.*
import com.google.android.libraries.places.api.Places
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.card.MaterialCardView
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.GeoPoint
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<MaterialCardView>
    private lateinit var viewModel: FireStoreViewModel
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var geoCoder: Geocoder
    private var locationPermission: Boolean = false
    private var currentAddress = ""

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPlaceSdk()
        initViewModel()
        getLocation()
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
        bottomSheetBehavior.isHideable = true
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        initAutoCompleteTextView()
        initOnclick()
    }

    private fun initAutoCompleteTextView() {
        val itemsGender = listOf(Gender.MALE.type, Gender.FEMALE.type, Gender.NOTOSAY.type)
        val itemsGrade = listOf(Grade.PREKG.type, Grade.LKG.type, Grade.UKG.type)
        val itemsChoices = listOf(Choice.YES.type, Choice.NO.type)
        val itemsParentGrade = listOf(
            ParentGrade.COLLEGE.type,
            ParentGrade.SCHOOL.type,
            ParentGrade.NOCOMMENTS.type
        )

        val adapterGender = ArrayAdapter(requireContext(), R.layout.item_list, itemsGender)
        val adapterParentGender = ArrayAdapter(requireContext(), R.layout.item_list, itemsGender)
        val adapterGrade = ArrayAdapter(requireContext(), R.layout.item_list, itemsGrade)
        val adapterParentGrade = ArrayAdapter(
            requireContext(),
            R.layout.item_list,
            itemsParentGrade
        )
        val adapterVisibility = ArrayAdapter(requireContext(), R.layout.item_list, itemsChoices)
        val adapterParentVisibility = ArrayAdapter(
            requireContext(),
            R.layout.item_list,
            itemsChoices
        )

        (binding.childGender.editText as? AutoCompleteTextView)?.setAdapter(adapterGender)
        (binding.parentGender.editText as? AutoCompleteTextView)?.setAdapter(adapterParentGender)
        (binding.childGrade.editText as? AutoCompleteTextView)?.setAdapter(adapterGrade)
        (binding.parentGrade.editText as? AutoCompleteTextView)?.setAdapter(adapterParentGrade)
        (binding.childOnlineVisibility.editText as? AutoCompleteTextView)?.setAdapter(
            adapterVisibility
        )
        (binding.parentOnlineVisibility.editText as? AutoCompleteTextView)?.setAdapter(
            adapterParentVisibility
        )
    }

    private fun initPlaceSdk() {
        geoCoder = Geocoder(requireContext(), Locale.getDefault())
        locationPermission = ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        if (!Places.isInitialized()) {
            Places.initialize(
                requireActivity(),
                resources.getString(R.string.google_maps_key)
            )
        }
    }

    private fun initOnclick() {
        binding.signUpButton.setOnClickListener {
            initAuth()
        }
        binding.childDob.setOnClickListener {
            requireActivity().hideKeyBoard()

            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Child DoB")
                .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setCalendarConstraints(
                    CalendarConstraints.Builder()
                        .setStart(MaterialDatePicker.todayInUtcMilliseconds()).setOpenAt(
                            MaterialDatePicker.todayInUtcMilliseconds()
                        ).build()
                )
                .build()
            datePicker.addOnPositiveButtonClickListener {
                val myFormat = "dd/MM/yyyy"
                val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
                val cal = Calendar.getInstance()
                cal.timeInMillis = it
                cal.set(Calendar.HOUR_OF_DAY, 0)
                cal.set(Calendar.MINUTE, 0)
                binding.childDob.editText!!.setText(sdf.format(cal.timeInMillis).toString())
            }
            datePicker.showsDialog = true
            datePicker.show(requireActivity().supportFragmentManager, null)
        }
        binding.parenDob.setOnClickListener {
            requireActivity().hideKeyBoard()

            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Parent DoB")
                .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setCalendarConstraints(
                    CalendarConstraints.Builder()
                        .setStart(MaterialDatePicker.todayInUtcMilliseconds()).setOpenAt(
                            MaterialDatePicker.todayInUtcMilliseconds()
                        ).build()
                )
                .build()
            datePicker.addOnPositiveButtonClickListener {
                val myFormat = "dd/MM/yyyy"
                val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
                val cal = Calendar.getInstance()
                cal.timeInMillis = it
                cal.set(Calendar.HOUR_OF_DAY, 0)
                cal.set(Calendar.MINUTE, 0)
                binding.parenDob.editText!!.setText(sdf.format(cal.timeInMillis).toString())
            }
            datePicker.showsDialog = true
            datePicker.show(requireActivity().supportFragmentManager, null)
        }

        binding.currentAddressesButton.setOnClickListener {
            binding.addresses.editText!!.setText(currentAddress)
        }
    }

    private fun initAuth() {
        firebaseAuth.createUserWithEmailAndPassword(
            binding.accountEmail.getString(),
            binding.passwordConform.getString()
        )
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val users = Users(
                        id = it.result.user!!.uid,
                        child_grade = binding.childGrade.getString(),
                        child_name = binding.childName.getString(),
                        child_profile_image = "",
                        child_online_visibility = binding.childOnlineVisibility.getString(),
                        child_online = "online",
                        child_mail = binding.childEmail.getString(),
                        child_dob = binding.childDob.getString(),
                        child_gender = binding.childGender.getString(),
                        child_description = binding.childDescription.getString(),
                        parent_name = binding.parentName.getString(),
                        parent_dob = binding.parenDob.getString(),
                        parent_gender = binding.parentGender.getString(),
                        parent_grade = binding.parentGrade.getString(),
                        parent_online_visibility = binding.parentOnlineVisibility.getString(),
                        parent_online = "online",
                        parent_profile_image = "",
                        parent_mail = binding.parentEmail.getString(),
                        mobile_number = binding.parentPhoneNo.getString(),
                        address = binding.addresses.getString(),
                        geoPoint = GeoPoint(latitude, longitude),
                        account_password = binding.passwordConform.getString(),
                        account_mail = binding.accountEmail.getString(),
                        timestamp = FieldValue.serverTimestamp(),
                        child_lastSeen = FieldValue.serverTimestamp(),
                        parent_lastSeen = FieldValue.serverTimestamp(),
                        score = 0,
                        cash = 0
                    )
                    viewModel.doAction(
                        FireStoreAction.CreateUser(
                            users
                        )
                    )
                    Toast.makeText(context, "created successfully", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_signUpFragment_to_getStartedFragment)
                } else {
                    Toast.makeText(context, "failed to create", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun requestLocationPermission() {
        Dexter.withActivity(activity)
            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    locationPermission = true
                    requestNewLocationData()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    if (response!!.isPermanentlyDenied) {
                        showSettingsDialog()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {
                    token!!.continuePermissionRequest()
                }
            })
            .withErrorListener {
                Toast.makeText(requireContext(), "error occurred", Toast.LENGTH_SHORT).show()
            }
            .onSameThread()
            .check()
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun getLocation() {
        if (!isLocationEnabled()) {
            binding.root.showSnackbar(
                "Your location provider is turned off. Please turn it on.",
                Snackbar.LENGTH_LONG
            )
        } else {
            if (locationPermission) {
                requestNewLocationData()
            } else {
                requestLocationPermission()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {

        val locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 1

        fusedLocationClient.requestLocationUpdates(
            locationRequest, locationCallback,
            Looper.myLooper()
        )
    }

    private fun getAddressFromLatLong(latitude: Double, longitude: Double): String {
        try {
            val addressList: List<Address>? = geoCoder.getFromLocation(latitude, longitude, 1)

            if (addressList != null && addressList.isNotEmpty()) {
                val address: Address = addressList[0]
                val sb = StringBuilder()
                for (i in 0..address.maxAddressLineIndex) {
                    sb.append(address.getAddressLine(i)).append(",")
                }
                sb.deleteCharAt(sb.length - 1)
                return sb.toString()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return ""
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location = locationResult.lastLocation
            latitude = mLastLocation.latitude
            longitude = mLastLocation.longitude
            currentAddress = getAddressFromLatLong(latitude, longitude)
        }
    }

    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", requireContext().packageName, null)
        intent.data = uri
        startActivityForResult(intent, 101)
    }

    private fun showSettingsDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Need Permissions")
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.")
        builder.setPositiveButton(
            "GOTO SETTINGS"
        ) { dialog, _ ->
            dialog.cancel()
            openSettings()
        }
        builder.setNegativeButton(
            "Cancel"
        ) { dialog, _ -> dialog.cancel() }
        builder.show()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(requireActivity()).get(FireStoreViewModel::class.java)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
