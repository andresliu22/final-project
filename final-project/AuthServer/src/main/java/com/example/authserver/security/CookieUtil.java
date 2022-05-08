package com.example.authserver.security;

import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {
    public static void create(HttpServletResponse res, String name, String value,
                              Boolean isSecure, Integer maxAge, String domain) {
        Cookie cookie = new Cookie(name, value);
        cookie.setSecure(isSecure);
        cookie.setHttpOnly(true);
        cookie.setDomain(domain);
        //Setting maxAge to -1 will preserve it until the browser is closed.
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");
        res.addCookie(cookie);
    }

    public static void clear(HttpServletResponse res, String name, String domain) {
        Cookie cookie = new Cookie(name, null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setDomain(domain);
        // A zero value causes the cookie to be deleted.
        //Setting maxAge to -1 will preserve it until the browser is closed.
        cookie.setMaxAge(0);
        res.addCookie(cookie);
    }

    public static String getValue(HttpServletRequest req, String name) {
        Cookie cookie = WebUtils.getCookie(req, name);
        return cookie!=null ? cookie.getValue() : null;
    }
}
