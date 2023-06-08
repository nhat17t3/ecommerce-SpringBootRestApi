package com.nhat.demoSpringbooRestApi.repositories;

import com.nhat.demoSpringbooRestApi.models.Comment;
import com.nhat.demoSpringbooRestApi.models.UserProductKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepo extends JpaRepository<Comment, Integer> {

    List<Comment> findAllByProductId(int productId);

    Optional<Comment> findById(UserProductKey commentId);
}
