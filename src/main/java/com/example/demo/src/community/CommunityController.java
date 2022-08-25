package com.example.demo.src.community;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.community.model.GetOtherRes;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.INVALID_USER_JWT;

@RestController
@RequestMapping("/communities")
public class CommunityController {

    @Autowired
    private final CommunityProvider communityProvider;

    @Autowired
    private final JwtService jwtService;

    public CommunityController(CommunityProvider communityProvider, JwtService jwtService) {
        this.communityProvider = communityProvider;
        this.jwtService = jwtService;
    }

    /**
     * 커뮤니티 그외 탭 조회 (비회원용) API
     * [GET] /communities
     * @return BaseResponse<GetOtherRes>
     */
    @ResponseBody
    @GetMapping()
    public BaseResponse<GetOtherRes> getOtherTabOpen(@RequestParam(name="ctIdx") int ctIdx) throws BaseException {
        try {
            GetOtherRes getOtherRes = communityProvider.getOtherTabOpen(ctIdx);
            return new BaseResponse<>(getOtherRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 커뮤니티 그외 탭 조회 (회원용) API
     * [GET] /communities/:userIdx
     * @return BaseResponse<GetOtherRes>
     */
    @ResponseBody
    @GetMapping("/{userIdx}")
    public BaseResponse<GetOtherRes> getOtherTab(@PathVariable("userIdx") int userIdx, @RequestParam(name="ctIdx") int ctIdx) throws BaseException {
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            GetOtherRes getOtherRes = communityProvider.getOtherTab(userIdx, ctIdx);
            return new BaseResponse<>(getOtherRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}