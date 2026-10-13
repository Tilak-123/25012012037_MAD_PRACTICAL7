package com.example.a25012012037_mad_practical7

import org.json.JSONObject
import java.io.Serializable

class Person(
    var id: String,
    var name: String,
    var emailId: String,
    var phoneNo: String,
    var address: String,
    var latitude: Double,
    var longitude: Double
) : Serializable {

    // Secondary constructor to parse JSONObject from API response
    constructor(jsonObject: JSONObject) : this(
        id = jsonObject.optString("id", ""),
        name = jsonObject.optJSONObject("profile")?.optString("name", "")
            ?: jsonObject.optString("name", ""),
        emailId = if (jsonObject.has("email")) jsonObject.optString("email", "") else jsonObject.optString("emailId", ""),
        phoneNo = if (jsonObject.has("phone")) jsonObject.optString("phone", "") else jsonObject.optString("phoneNo", ""),
        address = jsonObject.optJSONObject("profile")?.optString("address", "")
            ?: jsonObject.optString("address", ""),
        latitude = jsonObject.optJSONObject("profile")?.optJSONObject("location")?.optDouble("lat", 0.0)
            ?: jsonObject.optDouble("latitude", 0.0),
        longitude = jsonObject.optJSONObject("profile")?.optJSONObject("location")?.optDouble("long", 0.0)
            ?: jsonObject.optDouble("longitude", 0.0)
    )

    override fun toString(): String {
        return "Person(id='$id', name='$name', emailId='$emailId', phoneNo='$phoneNo', address='$address', latitude=$latitude, longitude=$longitude)"
    }
}
