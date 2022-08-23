package com.example.demo.src.follow;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.follow.model.PatchFollowStatusReq;
import com.example.demo.src.follow.model.PostFollowReq;
import com.example.demo.src.follow.model.PostFollowRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.config.BaseResponseStatus.PATCH_FOLLOW_NO_DATA;
import static com.example.demo.config.BaseResponseStatus.POST_FOLLOW_EXISTS;

@RestController
@RequestMapping("/follows")
public class FollowController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final FollowService followService;

    @Autowired
    private final FollowProvider followProvider;
    @Autowired
    private final JwtService jwtService;

    public FollowController(FollowService followService, FollowProvider followProvider, JwtService jwtService) {
        this.followService = followService;
        this.followProvider = followProvider;
        this.jwtService = jwtService;
    }

    // 팔로우 등록하기
    @ResponseBody
    @PostMapping("{userIdx}")
    public BaseResponse<PostFollowRes> createFollow(@PathVariable("userIdx") int userIdx, @RequestBody PostFollowReq postFollowReq) throws BaseException {

        try {

            if ((followProvider.checkFollows(postFollowReq.getUserIdx(), postFollowReq.getCompanyIdx())) == 1) {
                throw new BaseException(POST_FOLLOW_EXISTS);
            }
            PostFollowRes postFollowRes = followService.createFollow(postFollowReq.getUserIdx(), postFollowReq.getCompanyIdx());
            return new BaseResponse<>(postFollowRes);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ResponseBody
    @PatchMapping("/status/{userIdx}")
    public BaseResponse<String> deleteFollow(@PathVariable("userIdx") int userIdx, @RequestBody PatchFollowStatusReq patchFollowStatusReq) {
        try {

            int companyIdx = patchFollowStatusReq.getCompanyIdx();
            if ((followProvider.checkFollows(userIdx, companyIdx)) != 0) {
                followService.deleteFollow(userIdx, companyIdx);
                return new BaseResponse<>("팔로우가 삭제되었습니다.");
            } else {
                return new BaseResponse<>(PATCH_FOLLOW_NO_DATA);
            }
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }

    }

}