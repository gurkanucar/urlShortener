package com.gucardev.urlshortener.controller;

import com.gucardev.urlshortener.dto.ShortUrlDto;
import com.gucardev.urlshortener.model.ShortUrl;
import com.gucardev.urlshortener.service.ShortUrlService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URISyntaxException;

import static org.springframework.http.HttpStatus.MOVED_PERMANENTLY;

@RestController
@RequestMapping("/api/v1/shortUrl")
public class ShortUrlController {

    private final ShortUrlService shortUrlService;


    public ShortUrlController(ShortUrlService shortUrlService) {
        this.shortUrlService = shortUrlService;
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> makeRedirect(@PathVariable String code) throws URISyntaxException {
        return new ResponseEntity<>(shortUrlService.getUrl(code), MOVED_PERMANENTLY);
    }

    @GetMapping("/show/{code}")
    public ResponseEntity<ShortUrlDto> showShortUrl(@PathVariable String code) {
        return ResponseEntity.ok(shortUrlService.getShortUrlDto(code));
    }

    @GetMapping("/fast/{realUrl}/**")
    @ResponseBody
    public ResponseEntity<ShortUrlDto> createFast(@PathVariable String realUrl,
                                                  HttpServletRequest request) {

        return ResponseEntity.ok(shortUrlService.createFast(realUrl,request));
    }

    @PostMapping
    public ResponseEntity<ShortUrlDto> create(@Valid @RequestBody ShortUrlDto shortUrl) {
        return ResponseEntity.ok(shortUrlService.create(shortUrl));
    }



}
