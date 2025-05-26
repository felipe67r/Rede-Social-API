package com.redesocial.rede_social_api.repository;

import com.redesocial.rede_social_api.model.Post;
import com.redesocial.rede_social_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserIdIn(List<Long> userIds);
    List<Post> findByUser_Id(Long userId);
    List<Post> findByUserOrderByCreatedAtDesc(User user);
    List<Post> findAllByOrderByCreatedAtDesc();
}