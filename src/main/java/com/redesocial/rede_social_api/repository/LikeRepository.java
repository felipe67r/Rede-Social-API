package com.redesocial.rede_social_api.repository;

import com.redesocial.rede_social_api.model.Like;
import com.redesocial.rede_social_api.model.Post;
import com.redesocial.rede_social_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    List<Like> findByPost(Post post);
    List<Like> findByUser(User user);
    boolean existsByUserAndPost(User user, Post post);
    long countByPost(Post post);
    void deleteByUserAndPost(User user, Post post);
}