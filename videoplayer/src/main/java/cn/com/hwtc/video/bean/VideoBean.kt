package cn.com.hwtc.video.bean

import org.json.JSONObject

data class VideoBean(val name : String , val totalTime : Long , val width : Int , val height : Int , val size : Long , val path : String) {
    val KB = 1024
    val MB = 1024 * KB
    val GB = 1024 * MB

    override fun toString(): String {
        val json = JSONObject()
        json.put("name",name)
        json.put("totalTime",totalTime)
        json.put("width",width)
        json.put("height",height)
        json.put("size",size)
        json.put("path",path)
        return json.toString()
    }

    fun parseTotalTimeToString() : String{
        val second = totalTime / 1000
        val h = second / 3600
        val m = (second % 3600) / 60
        val s = second % 60
        return String.format("%02d:%02d:%02d",h,m,s)
    }

    fun parseResolutionToString() : String{
        return "$width x $height"
    }

    fun parseSizeToString() : String{
        var sizeString : String
        val length : Double = size.toDouble()
        if (size > GB){
            sizeString = String.format("%.2fG", length / GB)
        }else if (size > MB){
            sizeString = String.format("%.2fM", length / MB)
        }else{
            sizeString = String.format("%.2fM", length / KB)
        }
        return sizeString
    }
}