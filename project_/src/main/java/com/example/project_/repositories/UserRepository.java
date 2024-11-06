package com.example.project_.repositories;

import com.example.project_.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository  extends JpaRepository<UserModel, UUID> {
}

