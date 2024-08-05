package backend.backend.service;

import backend.backend.domain.Comment;
import backend.backend.domain.Member;
import backend.backend.domain.Post;
import backend.backend.domain.common.BusinessException;
import backend.backend.domain.common.ResponseCode;
import backend.backend.domain.dto.Response;
import backend.backend.domain.dto.commentDto.CommentResponseDto;
import backend.backend.domain.dto.postDto.PostRequestDto;
import backend.backend.domain.dto.postDto.PostResponseDto;
import backend.backend.repository.CommentRepository;
import backend.backend.repository.LikesRepository;
import backend.backend.repository.MemberRepository;
import backend.backend.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final LikesRepository likesRepository;
    private final CommentRepository commentRepository;

    public PostResponseDto createPost(PostRequestDto postRequestDto, String memberEmail) {
        Member member = memberRepository.findByEmail(memberEmail)
                .orElseThrow(() -> new BusinessException(ResponseCode.POS_AUTHENTICATION_FAIL));

        Post post = postRepository.save(new Post(postRequestDto, member));
        return new PostResponseDto(post);
    }

    public PostResponseDto getPost(Long postId){
        Post post = postRepository.findById(postId).
                orElseThrow(() -> new BusinessException(ResponseCode.CMT_AUTHENTICATION_FAIL));

        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
        int likeCount = likesRepository.countLikesByPostId(post.getId());
        int commentCount = commentRepository.countCommentByPostId(post.getId());
        for(Comment comment : post.getComments()){
            List<CommentResponseDto> childCommentList = new ArrayList<>();
            if (comment.getParent() == null) {
                for(Comment childComment : comment.getChildren()){
                    if(postId.equals(childComment.getPost().getId())){
                        childCommentList.add(new CommentResponseDto(childComment));
                    }
                }
                commentResponseDtoList.add(new CommentResponseDto(comment,childCommentList));
            }
        }
        return new PostResponseDto(post, commentResponseDtoList,commentCount,likeCount);
    }


    public PostResponseDto updatePost(Long postId, PostRequestDto postRequestDto, String memberEmail){
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new BusinessException(ResponseCode.POS_AUTHENTICATION_FAIL)
        );
        LocalDate postCreationDate = post.getCreatedAt().toLocalDate();
        LocalDate currentDate = LocalDate.now();
        if(memberEmail.equals(post.getMember().getEmail())){
            if(!currentDate.isEqual(postCreationDate)){
                throw new BusinessException(ResponseCode.POS_AUTHENTICATION_FAIL);
            }else{
                post.update(postRequestDto);
                return new PostResponseDto(post);
            }
        }else{
            throw new BusinessException(ResponseCode.POS_AUTHENTICATION_FAIL);
        }
    }

    public Response<Void> deletePost(Long postId, String memberEmail){
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new BusinessException(ResponseCode.POS_AUTHENTICATION_FAIL)
        );

        if(post.getMember().getEmail().equals(memberEmail)){
            postRepository.deleteById(postId);
        }else{
            throw new BusinessException(ResponseCode.POS_AUTHENTICATION_FAIL);

        }
        return Response.ok();
    }


}