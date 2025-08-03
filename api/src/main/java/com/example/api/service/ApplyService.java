package com.example.api.service;

import com.example.api.domain.Coupon;
import com.example.api.producer.CouponCreateProducer;
import com.example.api.repository.AppliedUserRepository;
import com.example.api.repository.CouponCountRepository;
import com.example.api.repository.CouponRepository;
import org.springframework.stereotype.Service;

@Service
public class ApplyService {

    private final CouponRepository couponRepository;

    private final CouponCountRepository couponCountRepository;

    private final CouponCreateProducer couponCreateProducer;

    private final AppliedUserRepository  appliedUserRepository;

    public ApplyService(CouponRepository couponRepository, CouponCountRepository couponCountRepository, CouponCreateProducer couponCreateProducer, AppliedUserRepository appliedUserRepository) {
        this.couponRepository = couponRepository;
        this.couponCountRepository = couponCountRepository;
        this.couponCreateProducer = couponCreateProducer;
        this.appliedUserRepository = appliedUserRepository;
    }

    public void apply(Long userId) {
        // userId 1개당 1개 쿠폰만 발급하도록 validation
        Long apply = appliedUserRepository.add(userId);
        if (apply != 1) {
            return;
        }

        // redis incr key:value
        // 127.0.0.1:6379> incr coupon_count
        // (integer) 1
        // 127.0.0.1:6379> incr coupon_count
        // (integer) 2
        // 실제 db에 저장되어 있는 값이 아니라, redis incr을 활용하여 count를 측정하는 것
        // redis는 싱글스레드 기반으로 동작하므로 데이터에 대한 정합성 유지 가능
        Long count = couponCountRepository.increment();

        //long count = couponRepository.count();

        if (count > 100) {
            return;
        }

        // 기존 로직 주석 처리 후 kafka로 적용
        //couponRepository.save(new Coupon(userId));
        couponCreateProducer.create(userId);
    }
}
