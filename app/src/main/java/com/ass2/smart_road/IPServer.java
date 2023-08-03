package com.ass2.smart_road;

public class IPServer {
    private static String IP_server = "192.168.43.174"; // IP Address Users's Machine
    private static String route_detect_recommendTrafficSigns = "/detect_recommendTrafficSigns";
    private static String URL = "http://"+IPServer.IP_server+":5000"+route_detect_recommendTrafficSigns; // url link of flask web service
    public static String getIP_server(){
        return IP_server;
    }
    public static String getURL() {
        return URL;
    }
}
