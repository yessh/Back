package backend.backend.controller;

import backend.backend.domain.dto.Response;
import backend.backend.Service.PostService;
import backend.backend.domain.dto.likesDto.LikesRequestDto;
import backend.backend.domain.dto.postDto.PostRequestDto;
import backend.backend.domain.dto.postDto.PostResponseDto;
import backend.backend.global.response.ApiResponse;
import backend.backend.global.util.security.SecurityUtil;
import backend.backend.service.LikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController {

    private final PostService postService;
    private final LikesService likesService;

    @PostMapping("/{id}/like")
    public ApiResponse<Void> likePost(@PathVariable Long id, @RequestBody LikesRequestDto likesRequestDto) {
        likesService.likePost(id, likesRequestDto);
        return ApiResponse.success(null, "좋아요가 성공적으로 추가되었습니다.");
    }

    @DeleteMapping("/{id}/like")
    public ApiResponse<Void> unlikePost(@PathVariable Long id, @RequestBody LikesRequestDto likesRequestDto) {
        likesService.unlikePost(id, likesRequestDto);
        return ApiResponse.success(null, "좋아요가 성공적으로 취소되었습니다.");


    }
    @PostMapping()
    public ResponseEntity<Response<PostResponseDto>> createPost(@Validated @RequestBody PostRequestDto requestDto){
        String memberEmail = SecurityUtil.getLoginEmail();
        PostResponseDto postResponseDto = postService.createPost(requestDto, memberEmail);
        return ResponseEntity.ok(Response.ok(postResponseDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<PostResponseDto>> getPost(@PathVariable Long postId){
        return ResponseEntity.ok(Response.ok(postService.getPost(postId)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<PostResponseDto>>updatePost(@PathVariable Long postId ,
                                                               @RequestBody PostRequestDto requestDto){
        String memberEmail = SecurityUtil.getLoginEmail();
        return ResponseEntity.ok(Response.ok(postService.updatePost(postId, requestDto, memberEmail)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<Void>> deletePost(@PathVariable Long postId){
        String memberEmail = SecurityUtil.getLoginEmail();
        return ResponseEntity.ok(postService.deletePost(postId, memberEmail));

    }


}
