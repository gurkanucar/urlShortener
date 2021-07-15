package com.gucardev.urlshortener.service;

import com.gucardev.urlshortener.dto.ShortUrlDto;
import com.gucardev.urlshortener.dto.converter.ShortUrlConverter;
import com.gucardev.urlshortener.exception.ShortUrlNotFoundException;
import com.gucardev.urlshortener.exception.UrlAlreadyExistException;
import com.gucardev.urlshortener.model.ShortUrl;
import com.gucardev.urlshortener.repository.ShortUrlRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.SecureRandom;

import org.springframework.http.HttpHeaders;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;

@Service
public class ShortUrlService {

    private final ShortUrlRepository shortUrlRepository;
    private final ShortUrlConverter shortUrlConverter;

    @Value("${codeLength}")
    private int codeLength;

    Logger logger = LoggerFactory.getLogger(ShortUrlService.class);

    public ShortUrlService(ShortUrlRepository shortUrlRepository, ShortUrlConverter shortUrlConverter) {
        this.shortUrlRepository = shortUrlRepository;
        this.shortUrlConverter = shortUrlConverter;
    }

    public HttpHeaders getUrl(String code) throws URISyntaxException {
        ShortUrl shortUrl = getShortUrl(code);
        logger.info("ShortURL info: " + shortUrl.toString());
        URI uri = new URI(shortUrl.getRealValue());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uri);
        return httpHeaders;
    }


    public ShortUrlDto getShortUrlDto(String code) {
        return shortUrlConverter.shortUrlToDto(getShortUrl(code));
    }

    public ShortUrlDto create(ShortUrlDto shortUrlDto) {
        if (shortUrlRepository.existsByCode(shortUrlDto.getCode())) {
            throw new UrlAlreadyExistException("this short url already exist!");
        }
        if (shortUrlDto.getCode() == null || shortUrlDto.getCode().trim().length() <= 0) {
            String code = generateRandomCode();
            shortUrlDto.setCode(code);
            logger.info(code);
        }
        shortUrlRepository.save(shortUrlConverter.dtoToshortUrl(shortUrlDto));
        return shortUrlDto;
    }

    public ShortUrlDto createFast(String realUrl, HttpServletRequest request) {
        ShortUrlDto shortUrlDto = new ShortUrlDto();
        shortUrlDto.setRealValue(convertRealUrl(realUrl, request));
        shortUrlDto.setCode(generateRandomCode());
        shortUrlRepository.save(shortUrlConverter.dtoToshortUrl(shortUrlDto));
        return shortUrlDto;
    }

    protected ShortUrl getShortUrl(String code) {
        return shortUrlRepository.findByCode(code).orElseThrow(
                () -> new ShortUrlNotFoundException("Short url not found!")
        );
    }


    protected String convertRealUrl(String realUrlRaw, HttpServletRequest request) {
        final String path =
                request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString();
        final String bestMatchingPattern =
                request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE).toString();

        String arguments = new AntPathMatcher().extractPathWithinPattern(bestMatchingPattern, path);

        String realUrl;
        if (null != arguments && !arguments.isEmpty()) {
            realUrl = realUrlRaw + '/' + arguments;
        } else {
            realUrl = realUrlRaw;
        }
        return realUrl;
    }


    protected String generateRandomCode() {

        String code = "";

        SecureRandom r = new SecureRandom();

        char chars[] = {'A', 'B', 'C', 'D', 'E', 'F', 'G',
                'H', 'I', 'J', 'K', 'L', 'M', 'N', 'P',
                'R', 'S', 'T', 'U', 'V', 'Y', 'Z', 'W', 'Q', 'X',
                '1', '2', '3', '4', '5', '6', '7', '8', '9',};

        int low = 0;
        int high = chars.length;

        do {
            code = "";
            for (int i = 0; i < codeLength; i++) {
                int index = r.nextInt(high - low) + low;
                code += chars[index];
            }
        } while (shortUrlRepository.existsByCode(code));


        return code;
    }


}
