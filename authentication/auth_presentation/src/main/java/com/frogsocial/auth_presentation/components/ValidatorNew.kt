package com.frogsocial.auth_presentation.components

object ValidatorNew {
    fun validateFirstName(fName: String): ValidationResultN {
        return ValidationResultN(
            status = fName.length >= 2,
            message = if (fName.length >= 2) "" else "First name must be at least 2 characters"
        )
    }

    fun validateLastName(lName: String): ValidationResultN {
        return ValidationResultN(
            status = lName.length >= 2,
            message = if (lName.length >= 2) "" else "Last name must be at least 2 characters"
        )
    }

    fun validateEmail(email: String): ValidationResultN {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return ValidationResultN(
            status = email.matches(emailPattern.toRegex()),
            message = if (email.matches(emailPattern.toRegex())) "" else "Invalid email format"
        )
    }

    fun validatePassword(password: String): ValidationResultN {
        return ValidationResultN(
            status = password.length >= 8,
            message = if (password.length >= 8) "" else "Password must be at least 8 characters"
        )
    }

    fun validatePrivacyPolicyAcceptance(accepted: Boolean): ValidationResultN {
        return ValidationResultN(
            status = accepted,
            message = if (accepted) "" else "You must accept the privacy policy"
        )
    }
}

data class ValidationResultN(
    val status: Boolean = false,
    val message: String = ""
)








