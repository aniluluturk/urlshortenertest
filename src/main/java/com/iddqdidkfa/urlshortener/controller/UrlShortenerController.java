package com.iddqdidkfa.urlshortener.controller;

import com.iddqdidkfa.urlshortener.service.ShortenerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.UnknownHostException;

import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@Controller
public class UrlShortenerController {

    private ShortenerService shortenerService;

    @Autowired
    public UrlShortenerController(ShortenerService shortenerService) {
        this.shortenerService = shortenerService;
    }

    @GetMapping(value = "/{id}")
    public void redirect(@PathVariable String id, HttpServletResponse response) throws IOException {
        final String url = shortenerService.getOriginalUrl(id);
        if (url != null)
            response.sendRedirect(url);
        else
            response.sendError(SC_NOT_FOUND);
    }

    @PostMapping("/")
    public ResponseEntity<String> save(@RequestBody String url) {
        try {
            String shortenedUrl = shortenerService.generateShortUrl(url);
            return new ResponseEntity<>(shortenedUrl, OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (UnknownHostException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Cannot generate url");
        }
    }
}
