package com.ntj.api_batch_job.repository;

import com.ntj.api_batch_job.entity.ApiUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<ApiUser, UUID> {

}
