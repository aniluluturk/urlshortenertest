package com.iddqdidkfa.urlshortener.service;

import com.google.common.hash.Hashing;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class ShortenerService {
    private final Map<String, String> urlMap;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Value("${server.port}")
    private int port;

    public ShortenerService() {
        this.urlMap = new HashMap<>();
    }

    public String getOriginalUrl(String id) {
        return urlMap.getOrDefault(id, null);
    }

    @SuppressWarnings("UnstableApiUsage")
    public String generateShortUrl(String originalUrl) throws UnknownHostException {
        final UrlValidator urlValidator = new UrlValidator(new String[]{"http", "https"});
        if (urlValidator.isValid(originalUrl)) {
            final String id = Hashing.murmur3_32().hashString(originalUrl, StandardCharsets.UTF_8).toString();
            urlMap.put(id, originalUrl);
            return getHostPrefix() + '/' + id;
        } else {
            throw new IllegalArgumentException("invalid url provided");
        }
    }

    private String getHostPrefix() {
        if(activeProfile.equals("DEV")) {
            return "http://localhost:" + port;
        }
        return "https://myurlshortener.com";
    }
}
