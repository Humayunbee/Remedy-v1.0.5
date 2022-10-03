package com.medicine.remedy.utils;

import android.util.Log;

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;

import static com.medicine.remedy.utils.AppConstants.RESPONSE_SUCCESS_CODE;
import static com.medicine.remedy.utils.AppConstants.SERVER_ERROR_CODE;
import static com.medicine.remedy.utils.AppConstants.UNAUTHORIZED_ERROR_CODE;

/**
 * Date 1/16/2021.
 * Created by Md Atik Faysal(Android Developer)
 */
public class HttpErrorCode
{
    /**
     * ...get http error code
     * ...catch error code from server response
     * @param throwable server response
     * @return error response code
     */
    public static int GET_HTTP_ERROR_CODE(Throwable throwable)
    {
        int responseCode;
        try{
            responseCode = ((HttpException) throwable).code();
            Log.d("http_error_response",throwable.toString());
            if (responseCode == UNAUTHORIZED_ERROR_CODE)
                responseCode = SERVER_ERROR_CODE;
            else if (responseCode == RESPONSE_SUCCESS_CODE)
                responseCode = SERVER_ERROR_CODE;
        }catch (Exception e)
        {
            Log.d("http_error_response",e.toString());
            responseCode = SERVER_ERROR_CODE;
        }

        return responseCode;
    }
}
