package com.redesocial.rede_social_api.repository;

import com.redesocial.rede_social_api.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost_Id(Long postId);
    List<Comment> findByUser_Id(Long userId);
    long countByPost_Id(Long postId);
}