package com.frogsocial.auth_data.repository

import android.content.Context
import android.os.Build
import androidx.biometric.BiometricManager
import com.frogsocial.auth_domain.repository.BiometricRepository
import javax.inject.Inject

class BiometricRepositoryImpl @Inject constructor(private val context: Context) :
    BiometricRepository {

    private val biometricManager: BiometricManager = BiometricManager.from(context)

      override fun isBiometricAvailable(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
                BiometricManager.BIOMETRIC_SUCCESS -> true
                else -> false
            }
        } else {
           return false
        }
    }
}