package com.frogsocial.auth_domain.use_cases

import android.hardware.biometrics.BiometricManager
import com.frogsocial.auth_domain.repository.BiometricRepository
import javax.inject.Inject

class BiometricAuthenticationUseCase @Inject constructor(private val biometricRepository: BiometricRepository) {

    fun isBiometricAvailable(): Boolean {
        return biometricRepository.isBiometricAvailable()
    }

}