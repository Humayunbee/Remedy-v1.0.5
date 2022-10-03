package com.medicine.remedy.utils;

import android.annotation.SuppressLint;
import android.content.Context;

import com.medicine.remedy.R;
import com.medicine.remedy.database.entities.CartItemEntities;

import java.text.DecimalFormat;
import java.util.List;

import static com.medicine.remedy.utils.ExtraUtils.commaOnAmount;
import static com.medicine.remedy.utils.ExtraUtils.roundOffDecimal;
import static com.medicine.remedy.view_model.OrderViewModel.mlCouponDiscount;
import static com.medicine.remedy.view_model.OrderViewModel.mlDisplayGrandTotal;
import static com.medicine.remedy.view_model.OrderViewModel.mlDisplaySubTotal;
import static com.medicine.remedy.view_model.OrderViewModel.mlDisplaySubTotalDiscount;
import static com.medicine.remedy.view_model.OrderViewModel.mlGrandTotal;
import static com.medicine.remedy.view_model.OrderViewModel.mlSubTotal;
import static com.medicine.remedy.view_model.OrderViewModel.mlTotalDiscount;
import static com.medicine.remedy.view_model.OrderViewModel.selectedCouponModel;

/**
 * Created by Md. Humayun Farid (Android Developer)
 * Create on 4/5/2021
  * Email: humayunfarid1997@gmail.com
 * *** Happy Coding ***
 */
public class MathematicsUtils
{

    /**
     * get double format value with two decimal places
     * when decimal number is 0 then return int format
     * @param d double value
     * @return double format value
     */
    @SuppressLint("DefaultLocale")
    public static String DOUBLE_VALUE_FORMAT(double d)
    {
        if(d == (long) d)
            return String.format("%d",(long)d);
        else
            return String.format("%.2f",d);
    }

    /**
     * ...add comma for amount
     * @param number decimal amount
     * @return amount with comma
     */
    public static String COMMA_ON_AMOUNT(double number)
    {
        DecimalFormat formatter = new DecimalFormat(AMOUNT_FORMAT(DOUBLE_VALUE_FORMAT(number)));

        return formatter.format(number);
    }

    /**
     * ...get amount format
     * @param amount actual amount
     * @return amount format
     */
    public static String AMOUNT_FORMAT(String amount)
    {
        switch(amount.length())
        {
            case 5:
                return "##,###.##";

            case 6:
                return "#,##,###.##";

            case 7:
                return "##,##,###.##";

            case 8:
                return "#,##,##,###.##";

            case 9:
                return "##,##,##,###.##";

            case 10:
                return "#,##,##,##,###.##";

            case 11:
                return "##,##,##,##,###.##";

            case 4:
            default:
                return "#,###.##";
        }
    }

    /**
     * ...discount calculation
     * @param discountPercent percentage of discount
     * @param flatDiscount flat discount
     * @param maxDiscount maximum discount amount
     * @return discount as amount
     */
    public static Double DISCOUNT_CALCULATION(double discountPercent, double flatDiscount, double maxDiscount, double grandTotal)
    {
        double discount = 0;

        if(flatDiscount == 0)//flat discount 0 means discount may be in percentage
        {
            if(grandTotal == 0 || discountPercent == 0)//while grand total or discount percentage is 0
                return discount;

            discount = (grandTotal * discountPercent) / 100;
            if(discount > maxDiscount && maxDiscount != 0)//while discount is getter than maximum discount amount
                discount = maxDiscount;

            return discount;
        }else
            discount = flatDiscount;

        return discount;
    }

    /**
     * ...calculate total amount
     * @param context application context
     * @param cartItems cart items
     */
    public static void calculateAmount(Context context, List<CartItemEntities> cartItems)
    {
        double subTotal = 0.00;
        double totalDiscount = 0.00;

        for(CartItemEntities model : cartItems)
        {
            subTotal+= model.getTotalPrice();
            totalDiscount+=model.getDiscount();
        }

        mlSubTotal.setValue(subTotal);
        if(selectedCouponModel.getValue() != null && selectedCouponModel.getValue().isApplied() && subTotal >= selectedCouponModel.getValue().getMinOrderAmount())
            mlCouponDiscount.setValue((DISCOUNT_CALCULATION(selectedCouponModel.getValue().getDiscount(),
                    selectedCouponModel.getValue().getFlagDiscount(), selectedCouponModel.getValue().getMaxDiscount(), subTotal) != 0 ?
                    DISCOUNT_CALCULATION(selectedCouponModel.getValue().getDiscount(), selectedCouponModel.getValue().getFlagDiscount(), selectedCouponModel.getValue().getMaxDiscount(), subTotal) : 0));
        else mlCouponDiscount.setValue(0.0);

        mlTotalDiscount.setValue(totalDiscount + mlCouponDiscount.getValue());
        mlGrandTotal.setValue((subTotal - (totalDiscount + mlCouponDiscount.getValue())) + 0);

        mlDisplaySubTotal.setValue(commaOnAmount(roundOffDecimal(subTotal)) +" "+context.getResources().getString(R.string.taka));
        mlDisplaySubTotalDiscount.setValue(commaOnAmount(roundOffDecimal(mlTotalDiscount.getValue())) +" "+context.getResources().getString(R.string.taka));
        mlDisplayGrandTotal.setValue(commaOnAmount(roundOffDecimal(mlGrandTotal.getValue())) +" "+context.getResources().getString(R.string.taka));
    }

    /**
     * ...calculate discount percentage
     * @param salesRate product sales rate
     * @param mrpRate product mrp rate
     */
    public static String calculateDiscountPercentage(double salesRate, double mrpRate)
    {
        double discountAmount = mrpRate - salesRate;
        double discountPct = (discountAmount * 100) / mrpRate;

        return DOUBLE_VALUE_FORMAT(discountPct);
    }


}
