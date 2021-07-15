package com.gucardev.urlshortener.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@ToString
@Where(clause = "deleted = false")
public class ShortUrl extends BaseEntity implements Serializable {

    private String code;

    @NotNull
    @NotBlank
    @Size(max = 1000)
    private String realValue;

    private String ipAddress;

    @Column(name = "viewCount")
    private Long viewCount = 0L;

}
