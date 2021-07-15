package com.gucardev.urlshortener.dto.converter;

import com.gucardev.urlshortener.dto.ShortUrlDto;
import com.gucardev.urlshortener.model.ShortUrl;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class ShortUrlConverter {

    public ShortUrlDto shortUrlToDto(ShortUrl shortUrl) {
        return ShortUrlDto.builder()
                .code(shortUrl.getCode())
                .realValue(shortUrl.getRealValue())
                .build();
    }

    public ShortUrl dtoToshortUrl(ShortUrlDto shortUrlDto) {
        ShortUrl shortUrl = new ShortUrl();
        shortUrl.setCode(shortUrlDto.getCode());
        shortUrl.setRealValue(shortUrlDto.getRealValue());
        shortUrl.setIpAddress(((ServletRequestAttributes)
                RequestContextHolder.currentRequestAttributes())
                .getRequest().getRemoteAddr().toString());
        return shortUrl;
    }

}
