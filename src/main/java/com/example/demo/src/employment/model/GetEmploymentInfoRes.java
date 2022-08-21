package com.example.demo.src.employment.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetEmploymentInfoRes {

    private String employmentImg; // 채용 포지션 이미지
    private String employment; // 채용 포지션 타이틀
    private String company; // 회사 명
    private String nation; // 국가 명
    private String compensation; // 채용보상금


}
