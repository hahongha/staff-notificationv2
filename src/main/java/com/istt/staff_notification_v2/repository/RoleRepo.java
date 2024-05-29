package com.istt.staff_notification_v2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.istt.staff_notification_v2.entity.Role;

@Repository
public interface RoleRepo extends JpaRepository<Role, String> {

}
