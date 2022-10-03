package com.medicine.remedy.utils

import android.text.TextUtils
import java.util.regex.Pattern

/**
 * Date 12/26/2020.
 * Created by Md Atik Faysal(Android Developer)
 *
 */
class DataValidation
{
    /**
     * Email must be at least 8 chars
     * Does contains any digit or number
     * Can contains lowercase and uppercase
     * Contains only [. _ % @] this special char
     * Maximum 45 chars
     */
    fun emailValidation(email: String): Boolean {
        if (email.contains("<script>") || email.contains("</script>") || email.contains("<?php") || email.contains(
                        "?>"
                ))
            return false

        val regEx = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$"
        val pattern = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(email)

        //with regEx char ,name length is greater than 5 and less than 45
        return matcher.find() && email.length >= 8 && email.length <= 45
    }

    /**
     * PASSWORD POLICY ***
     *
     * ...password must be at least 4 chars
     * ...Contains at least one digit
     * ...Contains at least one lower case and one upper case char
     * ...Contains at least one char with a set of special chars [@#$%^]
     * ...Does not contain space or tab
     */
    fun passwordValidation(pass: String): Boolean {
        return if (pass.contains("<script>") || pass.contains("</script>") || pass.contains("<?php") || pass.contains("?>"))
            false
        else pass.length in 4..16
    }

    /**
     * Name POLICY ***
     *
     * ...Name must be at least 5 chars
     * ...Does not contains no digit
     * ...Can contains uppercase and lowercase
     * ...Contains only [.] this special char
     * ...Maximum 25 chars
     */
    fun nameValidation(name: String): Boolean {
        if (name.contains("<script>") || name.contains("</script>") || name.contains("<?php") || name.contains("?>")) {
            return false
        }
        val regEx = "^[a-zA-Z]{4,}(?: [a-zA-Z]+){0,2}\$"
        val pattern = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(name)

        //with regEx char ,name length is greater than 5 and less than 25
        return matcher.find() && name.length >= 5 && name.length <= 25
    }

    /*
     *** PHONE NUMBER POLICY ***
     * ...Phone number must be 11 digits
     * ...If user use country code,that case phone number must be 14 digits
     * @return true if phone number is valid otherwise return false
     */
    fun phoneNumberValidation(phone: String): Boolean {
        if (TextUtils.isEmpty(phone))
            return false

        return when (phone.length) {
            11 -> {
                (phone.startsWith("013") || phone.startsWith("014") || phone.startsWith("015") || phone.startsWith("016")
                        || phone.startsWith("017") || phone.startsWith("018") || phone.startsWith("019"))
            }
            14 -> {
                (phone.startsWith("+88013") || phone.startsWith("+88014") || phone.startsWith("+88015") || phone.startsWith("+88016")
                        || phone.startsWith("+88017") || phone.startsWith("+88018") || phone.startsWith("+88019"))
            }
            else -> false
        }
    }

    companion object{

        /**
         * ...text must not be empty
         * ...text must not contain any script
         * @return true when all rules are followed
         */
        fun inputTextValidator(text: String): Boolean {

            if(text.isEmpty())
                return false

            if (text.contains("<script>") || text.contains("</script>") || text.contains("<?php") || text.contains("?>"))
                return false

            return true
        }

        /**
         * ...If 0 is not add then add 0 before phone number
         */
        fun modifyPhoneNumber(phoneNumber: String) : String
        {
            return if(phoneNumber.startsWith("0"))
                phoneNumber
            else "0$phoneNumber"
        }

        /*
         *** OTP NUMBER POLICY ***
         * ...OTP number must be 11 digits
         * ...If user use country code,that case OTP number must be
         * @return true if OTP number is valid otherwise return false
         */
        fun otpNumberValidation(otp: String): Boolean {
            if (TextUtils.isEmpty(otp))
                return false

            return otp.length == 6
        }
    }
}