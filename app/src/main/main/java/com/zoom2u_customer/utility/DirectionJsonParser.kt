package com.zoom2u_customer.utility

import com.google.android.gms.maps.model.LatLng
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class DirectionJsonParser {
    //	List<LatLng> listLatLong;
    fun parse(jObject: JSONObject): List<List<HashMap<String, String>>> {
        val routes: MutableList<List<HashMap<String, String>>> = ArrayList()
        var jRoutes: JSONArray? = null
        var jLegs: JSONArray? = null
        var jSteps: JSONArray? = null
        var jDistance: JSONObject? = null
        var jDuration: JSONObject? = null
        //	listLatLong = new ArrayList<LatLng>();
        try {
            jRoutes = jObject.getJSONArray("routes")
            /** Traversing all routes  */
            for (i in 0 until jRoutes.length()) {
                jLegs = (jRoutes[i] as JSONObject).getJSONArray("legs")
                val path: MutableList<HashMap<String, String>> = ArrayList()
                /** Traversing all legs  */
                for (j in 0 until jLegs.length()) {
                    /** Getting distance from the json data  */
                    jDistance = (jLegs[j] as JSONObject)
                        .getJSONObject("distance")
                    val hmDistance = HashMap<String, String>()
                    hmDistance["distance"] = jDistance.getString("text")
                    /** Getting duration from the json data  */
                    jDuration = (jLegs[j] as JSONObject)
                        .getJSONObject("duration")
                    val hmDuration = HashMap<String, String>()
                    hmDuration["duration"] = jDuration.getString("text")
                    /** Adding distance object to the path  */
                    path.add(hmDistance)
                    /** Adding duration object to the path  */
                    path.add(hmDuration)
                    jSteps = (jLegs[j] as JSONObject).getJSONArray("steps")
                    /** Traversing all steps  */
                    for (k in 0 until jSteps.length()) {
                        var polyline = ""
                        polyline =
                            ((jSteps[k] as JSONObject)["polyline"] as JSONObject)["points"] as String
                        val list: List<LatLng> = decodePoly(polyline)
                        /** Traversing all points  */
                        for (l in list.indices) {
                            //	listLatLong.add(new LatLng((list.get(l)).latitude, (list.get(l)).longitude));
                            try {
                                val hm = HashMap<String, String>()
                                hm["lat"] = java.lang.Double.toString(list[l].latitude)
                                hm["lng"] = java.lang.Double.toString(list[l].longitude)
                                path.add(hm)
                            } catch (e: OutOfMemoryError) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
                routes.add(path)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        } catch (e: Exception) {
        }
        return routes
    }

    /**
     * Method to decode polyline points Courtesy :
     * jeffreysambells.com/2010/05/27
     * /decoding-polylines-from-google-maps-direction-api-with-java
     */
    private fun decodePoly(encoded: String): List<LatLng> {
        val poly: MutableList<LatLng> = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0
        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat
            shift = 0
            result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng
            val p = LatLng((lat / 1E5), (lng / 1E5))
            poly.add(p)
        }
        return poly
    }
}
