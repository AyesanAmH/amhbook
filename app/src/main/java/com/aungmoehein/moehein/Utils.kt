package com.aungmoehein.moehein

import me.myatminsoe.mdetect.MDetect
import me.myatminsoe.mdetect.Rabbit

object Utils {

    fun roomText(text:String) =
        if(MDetect.isUnicode()) text else Rabbit.zg2uni(text)

    fun myanNum(num:String):String{
        val mNum = StringBuilder()
        num.forEach {
            when(it){
                '0' -> mNum.append("၀")
                '1' -> mNum.append("၁")
                '2' -> mNum.append(("၂"))
                '3' -> mNum.append(("၃"))
                '4' -> mNum.append(("၄"))
                '5' -> mNum.append(("၅"))
                '6'-> mNum.append(("၆"))
                '7' -> mNum.append(("၇"))
                '8' -> mNum.append(("၈"))
                '9' -> mNum.append(("၉"))
            }
        }

        return mNum.toString()

    }

}