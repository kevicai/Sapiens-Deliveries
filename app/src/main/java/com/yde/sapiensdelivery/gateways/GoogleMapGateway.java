package com.yde.sapiensdelivery.gateways;

//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationServices;
import com.yde.sapiensdelivery.use_cases.Locator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
        import java.nio.charset.StandardCharsets;
import java.util.HashMap;

        import org.json.JSONException;
import org.json.JSONObject;

/**
 * A gateway method that gets data from the GoogleMap Direction API
 *
 * Implements the Locator Interface.
 */
public class GoogleMapGateway implements Locator {
    //    private FusedLocationProviderClient fusedLocationClient;

    public GoogleMapGateway() {
    }

    // Private enum for different types of information
    private enum infoType{
        duration, distance, endAddress, startAddress
    }



    /**
     * Return a hashmap of the duration (in hours) and the distance (in km) of the route
     * between origin and destination according to the specified transportation.
     *  @param origin The starting location of the route
     * @param destination the ending location of the route
     * @param transportation the type of transportation used.
     * @return a hashMap of the following structure:
     * {"Distance": double, "Duration": double}
     */
    @Override
    public HashMap<String, Double> findRouteInfo(String origin,
                                                String destination,
                                                transportation transportation)
                                                 throws IOException, JSONException {

        HashMap<String, Double> routeInfo = new HashMap<String, Double>();

        String url = urlFactory(origin, destination, transportation);
        JSONObject json = this.readJsonFromUrl(url);

        routeInfo.put("Duration", routeInfoParser(json, infoType.duration));
        routeInfo.put("Distance", routeInfoParser(json, infoType.distance));
        return routeInfo;
    }

    /**
     * Return a string representation of the specified information type for findRouteInfo.
     */
    private double routeInfoParser(JSONObject infoJson, infoType type) throws JSONException{
        double returnInfo = 0;
        JSONObject info = infoJson.getJSONArray("routes").
                getJSONObject(0).
                getJSONArray("legs").
                getJSONObject(0);

        switch (type){
            case duration:
                returnInfo = info.getJSONObject("duration").getDouble("value");
                returnInfo = Math.round(returnInfo / 3600 * 100 / 100);
                break;
            case distance:
                returnInfo = info.getJSONObject("distance").getDouble("value");
                returnInfo = Math.round(returnInfo / 1000 * 100 / 100);
                break;
        }
        return returnInfo;
    }

    /**
     * A factory method for building different types of url required for findRouteInfo.
     */
    private String urlFactory(String origin,
                              String destination,
                              transportation transportation){
        String url = "";

        origin = origin.replaceAll(" ", "%");
        destination = destination.replaceAll(" ", "%");

        switch (transportation){
            case walking:
                url = "https://maps.googleapis.com/maps/api/directions/json?origin=" +
                        origin +
                        "&destination=" +
                        destination +
                        "&mode=walking" +
                        "&key=AIzaSyAxeqdWPsIhW7KXVSef1uH0OmAX8Pnqb2M";
                break;
            case driving:
                url = "https://maps.googleapis.com/maps/api/directions/json?origin=" +
                        origin +
                        "&destination=" +
                        destination +
                        "&mode=driving" +
                        "&key=AIzaSyAxeqdWPsIhW7KXVSef1uH0OmAX8Pnqb2M";
                break;
            case bicycling:
                url = "https://maps.googleapis.com/maps/api/directions/json?origin=" +
                        origin +
                        "&destination=" +
                        destination +
                        "&mode=bicycling" +
                        "&key=AIzaSyAxeqdWPsIhW7KXVSef1uH0OmAX8Pnqb2M";
                break;
        }
        return url;
    }

    /**
     * read the content in a json file.
     */
    private String readAll(Reader reader) throws IOException {
        StringBuilder builder = new StringBuilder();
        int line;
        while ((line = reader.read()) != -1) {
            builder.append((char) line);
        }
        return builder.toString();
    }

    /**
     * Return a JSONObject representing the json file get from url.
     */
    private JSONObject readJsonFromUrl(String url) throws IOException, JSONException {

        try (InputStream is = new URL(url).openStream()) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String jsonText = this.readAll(rd);
            return new JSONObject(jsonText);
        }
    }
}