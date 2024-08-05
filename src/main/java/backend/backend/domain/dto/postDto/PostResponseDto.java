package backend.backend.domain.dto.postDto;

import backend.backend.domain.Post;
import backend.backend.domain.dto.commentDto.CommentResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {
    private String title;
    private Long id;
    private String nickName;
    private String emoji;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;
    private List<CommentResponseDto> commentList = new ArrayList<>();
    private int likeCount;
    private int commentCount;

    public PostResponseDto(Post post){
        this.id = post.getId();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
        this.updateAt = post.getUpdatedAt();
        this.nickName = post.getMember().getNickName();

    }
    public PostResponseDto(Post post, List<CommentResponseDto> commentResponseDtos, int commentCount, int likeCount){
        this.id = post.getId();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
        this.updateAt = post.getUpdatedAt();
        this.nickName = post.getMember().getNickName();
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.commentList = commentResponseDtos;

    }

    public PostResponseDto(Post post, int commentCount, int likeCount){
        this.id = post.getId();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
        this.updateAt = post.getUpdatedAt();
        this.nickName = post.getMember().getNickName();
        this.likeCount = likeCount;
        this.commentCount = commentCount;
    }

}
