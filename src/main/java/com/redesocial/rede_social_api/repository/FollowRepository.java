package com.redesocial.rede_social_api.repository;

import com.redesocial.rede_social_api.model.Follow;
import com.redesocial.rede_social_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    List<Follow> findByFollowed(User followed);
    List<Follow> findByFollower(User follower);
    boolean existsByFollowerAndFollowed(User follower, User followed);
    Long countByFollowed(User followed);
    Long countByFollower(User follower);
    void deleteByFollowerAndFollowed(User follower, User followed);
}