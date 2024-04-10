package com.frogsocial.auth_domain.repository

interface BiometricRepository {
    fun isBiometricAvailable(): Boolean
}