package com.medicine.remedy.utils

/**
 * Date 12/26/2020.
 * Created by Md Atik Faysal(Android Developer)
 *
 */
class AppConstants
{
    companion object{

        //const val baseUrl = "https://remedy.tushar-das.xyz/"
       const val baseUrl = "https://remedymedicineservices.com/"
       // const val baseUrl = "https://test.lifeline-mc.com/"
        const val baseImageUrl = "https://remedymedicineservices.com/asset/image/product_image/"
        const val dbName = "remedy"

        // server response codes
        const val RESPONSE_SUCCESS_CODE = 200 // success code
        const val RESPONSE_ERROR_CODE = 400 // error code
        const val SMS_CREDIT_OVER = 501 // error code
        const val SERVER_ERROR_CODE = 500 // error code
        const val DUPLICATE_ENTRY = 504 // error code
        const val CONNECTION_TIMEOUT = 505 // error code
        const val UNAUTHORIZED_ERROR_CODE = 505 // error code

        const val OFFSET_SIZE_PER_PAGE = 50 // maximum offset per page

        const val NO_INTERNET_CONNECTION = "No internet connection, please try again"

        //intent values keys
        const val fullName = "fullName"
        const val email = "email"
        const val phoneNumber = "phoneNumber"
        const val password = "password"
        const val productId : String = "product_id"
        const val addressId : String = "address_id"
        const val couponId : String = "coupon_id"
        const val couponCode : String = "coupon_code"
        const val couponDiscount : String = "coupon_discount"
        const val address : String = "address"
        const val deliveryCharge : String = "delivery_charge"
        const val grandTotal : String = "grand_total"
        const val invoiceNo : String = "invoice_no"
        const val orderId : String = "order_id"
        const val categoryId : String = "category_id"
        const val fragmentId : String = "fragment_id"
        const val productList : String = "product_list"
        const val categoryWiseProductList : String = "cat_product_list"
        const val searchKey : String = "search_key"
        const val toolbarTitle : String = "toolbar_title"
        const val isFilterApply : String = "isFilterApply"

        /**
         * all toolbar title
         */
        const val menuToolbar = "Menu"
        const val cart = "Cart"
        const val shippingAddress = "Shipping Address"
        const val addNewAddress = "Add New Address"
        const val editAddress = "Edit Address"
        const val coupon = "Coupons"
        const val checkout = "Checkout"
        const val orderHistory = "Order history"
        const val notification = "Notification"

        /**
         * ...Error messages
         */
        const val nameNotFound : String = "Name not found"
        const val phoneNotFound : String = "Phone not found"
        const val notFound : String = "Not found"
        const val invalid : String = "Invalid"

        var isViewOnly : Boolean = true

        const val homeAddress : Int = 1
        const val officeAddress : Int = 2
        const val otherAddress : Int = 3

        //===================================== DATE FORMAT ==========================
        const val YYYY_MM_DD_HH_MM_SS: String = "yyyy-MM-dd HH:mm:ss"
        const val MM_DD: String = "MMM-dd"
        const val YYYY_MM_DD: String = "yyyy-MM-dd"
        const val MMM_DD_YYYY: String = "MMM dd, yyy"

        //================================================== Dialog type selection ================================
        const val WARNING_DIALOG = 1
        const val SUCCESS_DIALOG = 2
        const val ERROR_DIALOG = 3
        const val INFO_DIALOG = 4

        fun getAlphabets() : List<String>
        {
            var alphabets = ArrayList<String>()

            alphabets.add("A")
            alphabets.add("B")
            alphabets.add("C")
            alphabets.add("D")
            alphabets.add("E")
            alphabets.add("F")
            alphabets.add("G")
            alphabets.add("H")
            alphabets.add("I")
            alphabets.add("J")
            alphabets.add("K")
            alphabets.add("L")
            alphabets.add("M")
            alphabets.add("N")
            alphabets.add("O")
            alphabets.add("P")
            alphabets.add("Q")
            alphabets.add("R")
            alphabets.add("S")
            alphabets.add("T")
            alphabets.add("U")
            alphabets.add("V")
            alphabets.add("W")
            alphabets.add("X")
            alphabets.add("Y")
            alphabets.add("Z")

            return alphabets
        }
    }
}