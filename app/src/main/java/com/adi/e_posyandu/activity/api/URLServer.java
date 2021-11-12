package com.adi.e_posyandu.activity.api;

public class URLServer {
    public static final String BASE_URL = "http://192.168.43.68/e-posyandu/api/";
    public static final String LOGIN = BASE_URL + "auth/login";
    public static final String LOGOUT = BASE_URL + "auth/logout";
    public static final String REGISTER = BASE_URL + "auth/register";
    public static final String GETCATATAN = BASE_URL + "catatan";
    public static final String GETKELURAHAN = BASE_URL + "kelurahan";
    public static final String POSTPROFILE = BASE_URL + "user/profil";
    public static final String POSTPASSWORD = BASE_URL + "user/password";
    public static final String GETDATAIBU = BASE_URL + "dataibu";
    public static final String GETJADWAL = BASE_URL + "jadwal";
}
