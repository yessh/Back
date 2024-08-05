package backend.backend.repository;

import backend.backend.domain.Likes;
import backend.backend.domain.Member;
import backend.backend.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {
    Optional<Likes> findByUserAndPost(Member member, Post post);

    @Query("SELECT COUNT(c) FROM Likes l WHERE c.post.id = :postId")
    int countLikesByPostId(@Param("postId") Long postId);
}