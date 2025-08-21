package com.smartcontactmanager.smartcontactmanager.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smartcontactmanager.smartcontactmanager.Entities.Ride;
import com.smartcontactmanager.smartcontactmanager.Entities.User;
@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {
    List<Ride> findByUser(User user);
}
