package com.example.consumer.repository;


import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<com.example.consumer.domain.Coupon, Long> {
}
