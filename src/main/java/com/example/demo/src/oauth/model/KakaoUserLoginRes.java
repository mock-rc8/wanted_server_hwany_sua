package com.example.demo.src.oauth.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class KakaoUserLoginRes {

    private int userIdx;
    private String jwt;

    public KakaoUserLoginRes(){}
}
