package com.example.demo.src.community;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.community.model.*;
import com.example.demo.src.like.model.PatchLikeReq;
import com.example.demo.src.like.model.PostLikeReq;
import com.example.demo.src.like.model.PostLikeRes;
import com.example.demo.utils.JwtService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/communities")
public class CommunityController {

    @Autowired
    private final CommunityProvider communityProvider;

    @Autowired
    private final CommunityService communityService;

    @Autowired
    private final JwtService jwtService;

    private final ImageUploader imageUploader;

    public CommunityController(CommunityProvider communityProvider, CommunityService communityService, JwtService jwtService, ImageUploader imageUploader) {
        this.communityProvider = communityProvider;
        this.communityService = communityService;
        this.jwtService = jwtService;
        this.imageUploader = imageUploader;
    }

    /**
     * 커뮤니티 그외 탭 조회 (비회원용) API
     * [GET] /communities
     * @return BaseResponse<GetOtherOpenRes>
     */
    @ResponseBody
    @GetMapping()
    public BaseResponse<GetOtherOpenRes> getOtherTabOpen(@RequestParam(name="ctIdx") int ctIdx) throws BaseException {
        try {
            GetOtherOpenRes getOtherRes = communityProvider.getOtherTabOpen(ctIdx);
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

    /**
     * 커뮤니티 전체 탭 조회 (비회원용) API
     * [GET] /communities/totals
     * @return BaseResponse<GetAllOpenRes>
     */
    @ResponseBody
    @GetMapping("/totals")
    public BaseResponse<GetAllOpenRes> getAllTabOpen() throws BaseException {
        try {
            GetAllOpenRes getAllRes = communityProvider.getAllTabOpen();
            return new BaseResponse<>(getAllRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 커뮤니티 전체 탭 조회 (회원용) API
     * [GET] /communities/totals/:userIdx
     * @return BaseResponse<GetAllRes>
     */
    @ResponseBody
    @GetMapping("/totals/{userIdx}")
    public BaseResponse<GetAllRes> getAllTab(@PathVariable("userIdx") int userIdx) throws BaseException {
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            GetAllRes getAllRes = communityProvider.getAllTab(userIdx);
            return new BaseResponse<>(getAllRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 커뮤니티 추천 탭 조회 (비회원용) API
     * [GET] /communities/recommends
     * @return BaseResponse<GetRecommOpenRes>
     */
    @ResponseBody
    @GetMapping("/recommends")
    public BaseResponse<GetRecommOpenRes> getRecommTabOpen() throws BaseException {
        try {
            GetRecommOpenRes getRecommRes = communityProvider.getRecommTabOpen();
            return new BaseResponse<>(getRecommRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 커뮤니티 추천 탭 조회 (회원용) API
     * [GET] /communities/recommends/:userIdx
     * @return BaseResponse<GetRecommRes>
     */
    @ResponseBody
    @GetMapping("/recommends/{userIdx}")
    public BaseResponse<GetRecommRes> getRecommTab(@PathVariable("userIdx") int userIdx) throws BaseException {
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            GetRecommRes getRecommRes = communityProvider.getRecommTab(userIdx);
            return new BaseResponse<>(getRecommRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 커뮤니티 게시글 상세 조회 (비회원용) API
     * [GET] /communities/postings/:postingIdx
     * @return BaseResponse<GetPostingOpenRes>
     */
    @ResponseBody
    @GetMapping("/postings/{postingIdx}")
    public BaseResponse<GetPostingOpenRes> getPostingOpen(@PathVariable("postingIdx") int postingIdx) throws BaseException {
        try {
            GetPostingOpenRes getPostingRes = communityProvider.getPostingOpen(postingIdx);
            return new BaseResponse<>(getPostingRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 커뮤니티 게시글 상세 조회 (회원용) API
     * [GET] /communities/postings/:postingIdx/:userIdx
     * @return BaseResponse<GetPostingRes>
     */
    @ResponseBody
    @GetMapping("/postings/{postingIdx}/{userIdx}")
    public BaseResponse<GetPostingRes> getPosting(@PathVariable("postingIdx") int postingIdx, @PathVariable("userIdx") int userIdx) throws BaseException {
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            GetPostingRes getPostingRes = communityProvider.getPosting(postingIdx, userIdx);
            return new BaseResponse<>(getPostingRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 커뮤니티 프로필 설정 조회 API
     * [GET] /communities/profiles/:userIdx
     * @return BaseResponse<GetProfileRes>
     */
    @ResponseBody
    @GetMapping("/profiles/{userIdx}")
    public BaseResponse<GetProfileRes> getProfile(@PathVariable("userIdx") int userIdx) throws BaseException {
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            GetProfileRes getProfileRes = communityProvider.getProfile(userIdx);
            return new BaseResponse<>(getProfileRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 커뮤니티 프로필 설정 API
     * [PATCH] /communities/profiles/:userIdx
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/profiles/{userIdx}")
    public BaseResponse<String> setProfile(@PathVariable("userIdx") int userIdx, @RequestBody PatchProfileReq patchProfileReq) throws BaseException {
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            communityService.setProfile(userIdx, patchProfileReq);
            return new BaseResponse<>("변경 되었습니다.");
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 커뮤니티 게시글 작성 API
     * [POST] /communities/:userIdx
     * @return BaseResponse<PostPostingRes>
     */
    @ResponseBody
    @PostMapping(value = "/{userIdx}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResponse<PostPostingRes> createPosting(@PathVariable("userIdx") int userIdx, @RequestPart(value = "image", required = false) MultipartFile multipartFile, @RequestPart(value = "json") PostPostingReq json) throws BaseException {
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            if(json.getTags().size() < 1) {
                return new BaseResponse<>(POST_POSTING_NO_TAG);
            }
            if(json.getTitle().equals("")) {
                return new BaseResponse<>(POST_POSTING_NO_TITLE);
            }
            if(json.getContent().equals("")) {
                return new BaseResponse<>(POST_POSTING_NO_CONTENT);
            }
            System.out.println(multipartFile);
            String imageUrl = "";
            if(multipartFile != null) {
                if(!multipartFile.isEmpty()) {
                    imageUrl = imageUploader.upload(multipartFile, "static");
                }
            }

            PostPostingRes postPostingRes;
            if(imageUrl.equals("") || imageUrl.isEmpty()) {
                postPostingRes = communityService.createPosting(userIdx, json);
            } else {
                postPostingRes = communityService.createPostingWithImage(userIdx, imageUrl, json);
            }
            return new BaseResponse<>(postPostingRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        } catch (IOException exception) {
            return new BaseResponse<>(FAIL_IMAGE_UPLOAD);
        }
    }

    /**
     * 커뮤니티 게시글 삭제 API
     * [PATCH] /communities/:userIdx
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{userIdx}")
    public BaseResponse<String> deletePosting(@PathVariable("userIdx") int userIdx, @RequestBody PatchPostingReq patchPostingReq) throws BaseException {
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            if(communityProvider.checkPosting(userIdx, patchPostingReq.getPostingIdx()) != 0) {
                communityService.deletePosting(userIdx, patchPostingReq.getPostingIdx());
                return new BaseResponse<>("삭제되었습니다.");
            } else {
                return new BaseResponse<>(PATCH_POSTING_NO_DATA);
            }
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 커뮤니티 게시글 수정 API
     * [PUT] /communities/:userIdx
     * @return BaseResponse<PutPostingRes>
     */
    @ResponseBody
    @PutMapping(value = "/{userIdx}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResponse<PutPostingRes> updatePosting(@PathVariable("userIdx") int userIdx, @RequestPart(value = "image", required = false) MultipartFile multipartFile, @RequestPart("json") PutPostingReq json) throws BaseException {
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            if(communityProvider.checkPostingActive(userIdx, json.getPostingIdx()) != 0) {
                if(json.getTags().size() < 1) {
                    return new BaseResponse<>(POST_POSTING_NO_TAG);
                }
                if(json.getTitle().equals("")) {
                    return new BaseResponse<>(POST_POSTING_NO_TITLE);
                }
                if(json.getContent().equals("")) {
                    return new BaseResponse<>(POST_POSTING_NO_CONTENT);
                }
                System.out.println(multipartFile);
                String imageUrl = "";
                if(multipartFile != null) {
                    if(!multipartFile.isEmpty()) {
                        imageUrl = imageUploader.upload(multipartFile, "static");
                    }
                }

                PutPostingRes putPostingRes;
                if(imageUrl.equals("") || imageUrl.isEmpty()) {
                    putPostingRes = communityService.updatePosting(userIdx, json);
                } else {
                    putPostingRes = communityService.updatePostingWithImage(userIdx, imageUrl, json);
                }
                return new BaseResponse<>(putPostingRes);
            } else {
                return new BaseResponse<>(PUT_POSTING_NO_DATA);
            }


        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        } catch (IOException exception) {
            return new BaseResponse<>(FAIL_IMAGE_UPLOAD);
        }
    }

    /**
     * 게시글 수정 태그 조회 API
     * [GET] /communities/tags/:postingIdx/:userIdx
     * @return BaseResponse<GetProfileRes>
     */
    @ResponseBody
    @GetMapping("/tags/{postingIdx}/{userIdx}")
    public BaseResponse<GetTagRes> getTag(@PathVariable("postingIdx") int postingIdx, @PathVariable("userIdx") int userIdx) throws BaseException {
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            GetTagRes getTagRes = communityProvider.getTag(postingIdx, userIdx);
            return new BaseResponse<>(getTagRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    /**
     * 커뮤니티 댓글 작성 API
     * [POST] /communities/:postingIdx/:userIdx
     * @return BaseResponse<PostCommentRes>
     */
    @ResponseBody
    @PostMapping(value = "/{postingIdx}/{userIdx}")
    public BaseResponse<PostCommentRes> createComment(@PathVariable("postingIdx") int postingIdx, @PathVariable("userIdx") int userIdx, @RequestBody PostCommentReq postCommentReq) throws BaseException {
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            if(postCommentReq.getContent().equals("")) {
                return new BaseResponse<>(POST_COMMENT_NO_CONTENT);
            }
            if(communityProvider.checkPostingForComment(postingIdx) != 0) {
                PostCommentRes postCommentRes = communityService.createComment(postingIdx, userIdx, postCommentReq);
                return new BaseResponse<>(postCommentRes);
            } else {
                return new BaseResponse<>(POST_POSTING_NO_DATA);
            }
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 커뮤니티 댓글 삭제 API
     * [PATCH] /communities/:postingIdx/:userIdx
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{postingIdx}/{userIdx}")
    public BaseResponse<String> deleteComment(@PathVariable("postingIdx") int postingIdx, @PathVariable("userIdx") int userIdx, @RequestBody PatchCommentReq patchCommentReq) throws BaseException {
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            if(communityProvider.checkComment(userIdx, postingIdx, patchCommentReq.getCommentIdx()) != 0) {
                communityService.deleteComment(userIdx, postingIdx, patchCommentReq.getCommentIdx());
                return new BaseResponse<>("삭제되었습니다.");
            } else {
                return new BaseResponse<>(PATCH_COMMENT_NO_DATA);
            }
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 커뮤니티 게시글 좋아요 등록 API
     * [POST] /likes/:userIdx
     * @return BaseResponse<PostLikePostingRes>
     */
    @ResponseBody
    @PostMapping("/likes/{userIdx}")
    public BaseResponse<PostLikePostingRes> likes(@PathVariable("userIdx") int userIdx, @RequestBody PostLikePostingReq postLikePostingReq) throws BaseException {
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            if(communityProvider.checkLikes(userIdx, postLikePostingReq.getPostingIdx()) != 0) {
                int likePostIdx = communityService.modifyLikes(userIdx, postLikePostingReq.getPostingIdx());
                return new BaseResponse<>(new PostLikePostingRes(likePostIdx));
            } else {
                int likePostIdx = communityService.createLikes(userIdx, postLikePostingReq.getPostingIdx());
                return new BaseResponse<>(new PostLikePostingRes(likePostIdx));
            }

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 커뮤니티 게시글 좋아요 삭제 API
     * [PATCH] /likes/:userIdx
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/likes/{userIdx}")
    public BaseResponse<String> deletelikes(@PathVariable("userIdx") int userIdx, @RequestBody PatchLikePostingReq patchLikePostingReq) throws BaseException {
        try {
            if(communityProvider.checkLikes(userIdx, patchLikePostingReq.getPostingIdx()) != 0) {
                communityService.deleteLikes(userIdx, patchLikePostingReq.getPostingIdx());
                return new BaseResponse<>("삭제되었습니다.");
            } else {
                return new BaseResponse<>(PATCH_LIKES_POSTING_NO_DATA);
            }

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * My 커뮤니티 조회 (작성글 탭이 디폴트) API
     * [GET] /communities/activities/:userIdx
     * @return BaseResponse<GetMyPostRes>
     */
    @ResponseBody
    @GetMapping("/activities/{userIdx}")
    public BaseResponse<GetMyPostRes> getMyPost(@PathVariable("userIdx") int userIdx) throws BaseException {
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            GetMyPostRes getMyPostRes = communityProvider.getMyPost(userIdx);
            return new BaseResponse<>(getMyPostRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * My 커뮤니티 조회 (작성댓글 탭) API
     * [GET] /communities/activities/comments/:userIdx
     * @return BaseResponse<GetMyCommentRes>
     */
    @ResponseBody
    @GetMapping("/activities/comments/{userIdx}")
    public BaseResponse<GetMyCommentRes> getMyComment(@PathVariable("userIdx") int userIdx) throws BaseException {
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            GetMyCommentRes getMyCommentRes = communityProvider.getMyComment(userIdx);
            return new BaseResponse<>(getMyCommentRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * My 커뮤니티 조회 (좋아요 탭) API
     * [GET] /communities/activities/likes/:userIdx
     * @return BaseResponse<GetMyCommentRes>
     */
    @ResponseBody
    @GetMapping("/activities/likes/{userIdx}")
    public BaseResponse<GetMyLikeRes> getMyLike(@PathVariable("userIdx") int userIdx) throws BaseException {
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            GetMyLikeRes getMyLikeRes = communityProvider.getMyLike(userIdx);
            return new BaseResponse<>(getMyLikeRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
