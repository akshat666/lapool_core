package com.lapool.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.lapool.data.LocationDetails;
import com.lapool.data.LocationPoint;
import com.lapool.data.TripDetails;
import com.lapool.model.Trip;
import com.lapool.model.TripLocation;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author akshat
 */
public class SystemUtils {

    final static Logger log = LoggerFactory.getLogger(SystemUtils.class);

    private static final SecureRandom random = new SecureRandom();
    private static final String HASH_ALGORITHM = "HmacSHA256";

    public static final Map convertJSONRStringToMap(String json) {
        final String methodName = "convertJSONRStringToMap()";
        log.info("Entering :" + methodName);

        final ObjectMapper mapper = new ObjectMapper();
        final MapType type = mapper.getTypeFactory().constructMapType(
                Map.class, String.class, Object.class);
        Map<String, Object> data = null;

        try {
            data = mapper.readValue(json, type);
        } catch (IOException ex) {
            log.error("Error mapping JSON to MAP :" + ex.getMessage());
        }
        log.info("Exiting :" + methodName);
        return data;

    }

    public static final int calculateAge(Date dateOfBirth) {
        Calendar dob = Calendar.getInstance();
        dob.setTime(dateOfBirth);
        Calendar today = Calendar.getInstance();
        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        if (today.get(Calendar.MONTH) < dob.get(Calendar.MONTH)) {
            age--;
        } else if (today.get(Calendar.MONTH) == dob.get(Calendar.MONTH)
                && today.get(Calendar.DAY_OF_MONTH) < dob.get(Calendar.DAY_OF_MONTH)) {
            age--;
        }
        return age;
    }

    public static final double distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        float dist = (float) (earthRadius * c);

        return dist;
    }

    /**
     * /**
     * Calculates the end-point from a given source at a given range (meters)
     * and bearing (degrees). This methods uses simple geometry equations to
     * calculate the end-point.
     *
     *
     * @param latitude
     * @param longitude
     * @param range - range in meters
     * @param bearing - bearing in degrees
     *
     * @return - LocationPoint
     */
    public static LocationPoint calculateDerivedPosition(double latitude, double longitude, double range, double bearing) {
        double EarthRadius = 6371000; // m

        double latA = Math.toRadians(latitude);
        double lonA = Math.toRadians(longitude);
        double angularDistance = range / EarthRadius;
        double trueCourse = Math.toRadians(bearing);

        double lat = Math.asin(
                Math.sin(latA) * Math.cos(angularDistance)
                + Math.cos(latA) * Math.sin(angularDistance)
                * Math.cos(trueCourse));

        double dlon = Math.atan2(
                Math.sin(trueCourse) * Math.sin(angularDistance)
                * Math.cos(latA),
                Math.cos(angularDistance) - Math.sin(latA) * Math.sin(lat));

        double lon = ((lonA + dlon + Math.PI) % (Math.PI * 2)) - Math.PI;

        lat = Math.toDegrees(lat);
        lon = Math.toDegrees(lon);

        LocationPoint point = new LocationPoint();
        point.setLat(lat);
        point.setLng(lon);

        return point;

    }

    public static final <T> List<List<T>> chopped(List<T> list, final int L) {
        List<List<T>> parts = new ArrayList<List<T>>();
        final int N = list.size();
        for (int i = 0; i < N; i += L) {
            parts.add(new ArrayList<T>(
                    list.subList(i, Math.min(N, i + L)))
            );
        }
        return parts;
    }

    public static String hashPassword(String password) {

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException ex) {
        }
        md.update(password.getBytes());

        byte byteData[] = md.digest();

        //convert the byte to hex
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }

    public static int fetchChannelStatusAsInt(String status) {
        if (status.equalsIgnoreCase(ApplicationConstants.STR_ACTIVE)) {
            return ApplicationConstants.STATUS_CHANNEL_ACTIVE;
        } else if (status.equalsIgnoreCase(ApplicationConstants.STR_INACTIVE)) {
            return ApplicationConstants.STATUS_CHANNEL_INACTIVE;
        }
        return ApplicationConstants.STATUS_CHANNEL_INACTIVE;
    }

    public static String fetchChannelStatusAsString(int status) {
        String result = null;
        switch (status) {
            case ApplicationConstants.STATUS_CHANNEL_ACTIVE:
                result = ApplicationConstants.STR_ACTIVE;
                break;
            case ApplicationConstants.STATUS_CHANNEL_INACTIVE:
                result = ApplicationConstants.STR_INACTIVE;
            default:
                break;
        }
        return result;
    }

    public static int fetchSysUserStatusAsInt(String status) {
        if (status.equalsIgnoreCase(ApplicationConstants.STR_ACTIVE)) {
            return ApplicationConstants.STATUS_SYSTEMUSER_ACTIVE;
        } else if (status.equalsIgnoreCase(ApplicationConstants.STR_INACTIVE)) {
            return ApplicationConstants.STATUS_SYSTEMUSER_INACTIVE;
        }
        return ApplicationConstants.STATUS_SYSTEMUSER_INACTIVE;
    }

    public static <T> T initializeAndUnproxy(T entity) {
        if (entity == null) {
            throw new NullPointerException("Entity passed for initialization is null");
        }

        Hibernate.initialize(entity);
        if (entity instanceof HibernateProxy) {
            entity = (T) ((HibernateProxy) entity).getHibernateLazyInitializer()
                    .getImplementation();
        }
        return entity;
    }

    public static String fetchSysUserStatusAsString(int status) {
        String result = null;
        switch (status) {
            case ApplicationConstants.STATUS_SYSTEMUSER_ACTIVE:
                result = ApplicationConstants.STR_ACTIVE;
                break;
            case ApplicationConstants.STATUS_SYSTEMUSER_INACTIVE:
                result = ApplicationConstants.STR_INACTIVE;
            default:
                break;
        }
        return result;
    }

    /**
     * Generates a unique alpha-numeric ID
     *
     * @return
     */
    public static String generateUniqueID() {
        return new BigInteger(130, random).toString(32);
    }

    /**
     * Used specifically for adding items to a HashMap of type HashMap<Long,
     * ArrayList<Integer>> @param m
     *
     * apKey
     *
     * @param value
     * @param map
     */
    public static void addToList(Long mapKey, Integer value, HashMap<Long, ArrayList<Integer>> map) {
        ArrayList<Integer> itemsList = map.get(mapKey);

        // if list does not exist create it
        if (itemsList == null) {
            itemsList = new ArrayList<Integer>();
            itemsList.add(value);
            map.put(mapKey, itemsList);
        } else // add if item is not already in list
        {
            if (!itemsList.contains(value)) {
                itemsList.add(value);
            }
        }
    }

    public static TripDetails copyTripToTripDetails(Trip trip) {
        TripDetails tripDetails = new TripDetails();
        tripDetails.setTripID(trip.getTripID());
        tripDetails.setHopperID(trip.getPickUpRequest().getAuthProvider().getId());
        tripDetails.setHopperName(trip.getPickUpRequest().getAuthProvider().getName());
        tripDetails.setPickAddress(trip.getPickUpRequest().getPickAddress());
        tripDetails.setDropAddress(trip.getPickUpRequest().getDropAddress());
        tripDetails.setTripStartTime(trip.getStartTime().getTime());
        tripDetails.setHopperSex(trip.getPickUpRequest().getAuthProvider().getGender());
        tripDetails.setHopperPhone(trip.getPickUpRequest().getAuthProvider().getPhone());
        tripDetails.setPickupLat(trip.getPickUpRequest().getPickUpLat());
        tripDetails.setPickupLng(trip.getPickUpRequest().getPickUpLng());
        tripDetails.setDropLat(trip.getPickUpRequest().getDropLat());
        tripDetails.setDropLng(trip.getPickUpRequest().getDropLng());
        tripDetails.setPickCreatedAt(trip.getPickUpRequest().getCreated().getTime());
        tripDetails.setHopperProviderID(trip.getPickUpRequest().getAuthProvider().getProviderId());
        tripDetails.setHopperProviderType(trip.getPickUpRequest().getAuthProvider().getProviderType());
        tripDetails.setDropperProviderID(trip.getAuthProvider().getProviderId());
        tripDetails.setDropperProviderType(trip.getAuthProvider().getProviderType());
        tripDetails.setStatus(trip.getStatus());

        ArrayList<LocationDetails> hopperRoute = new ArrayList<LocationDetails>();
        ArrayList<LocationDetails> dropperRoute = new ArrayList<LocationDetails>();

        for (TripLocation loc : trip.getTripLocations()) {
            //If location is of dropper
            if (trip.getAuthProvider().getId() == loc.getAuthProvider().getId()) {
                LocationDetails lp = new LocationDetails();
                lp.setLat(loc.getLat());
                lp.setLng(loc.getLng());
                lp.setCreated(loc.getCreated());
                dropperRoute.add(lp);
            }//else the points are of hopper
            else {
                LocationDetails lp = new LocationDetails();
                lp.setLat(loc.getLat());
                lp.setLng(loc.getLng());
                lp.setCreated(loc.getCreated());
                hopperRoute.add(lp);
            }
        }
        tripDetails.setHopperRoute(hopperRoute);
        tripDetails.setDropperRoute(dropperRoute);

        tripDetails.setDropperID(trip.getAuthProvider().getId());
        tripDetails.setDropperName(trip.getAuthProvider().getName());
        tripDetails.setDropperSex(trip.getAuthProvider().getGender());
        tripDetails.setDropperPhone(trip.getAuthProvider().getPhone());

        return tripDetails;
    }

    public static boolean isAuthProviderActive(int status) {

        boolean result = false;

        switch (status) {
            case ApplicationConstants.STATUS_AUTH_USER_ACTIVE:
                result = true;
                break;
            case ApplicationConstants.STATUS_AUTH_USER_INACTIVE:
                break;
        }
        return result;
    }

    public static String hashMac(String text, String secretKey)
            throws SignatureException {

        try {
            Key sk = new SecretKeySpec(secretKey.getBytes(), HASH_ALGORITHM);
            Mac mac = Mac.getInstance(sk.getAlgorithm());
            mac.init(sk);
            final byte[] hmac = mac.doFinal(text.getBytes());
            return toHexString(hmac);
        } catch (NoSuchAlgorithmException e1) {
            // throw an exception or pick a different encryption method
            throw new SignatureException(
                    "error building signature, no such algorithm in device "
                    + HASH_ALGORITHM);
        } catch (InvalidKeyException e) {
            throw new SignatureException(
                    "error building signature, invalid key " + HASH_ALGORITHM);
        }
    }

    public static String toHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);

        Formatter formatter = new Formatter(sb);
        for (byte b : bytes) {
            formatter.format("%02x", b);
        }

        return sb.toString();
    }

}
