package com.example.api.service;

import com.example.api.domain.Coupon;
import com.example.api.repository.CouponCountRepository;
import com.example.api.repository.CouponRepository;
import org.springframework.stereotype.Service;

@Service
public class ApplyService {

    private final CouponRepository couponRepository;

    private final CouponCountRepository couponCountRepository;

    public ApplyService(CouponRepository couponRepository, CouponCountRepository couponCountRepository) {
        this.couponRepository = couponRepository;
        this.couponCountRepository = couponCountRepository;
    }

    public void apply(Long userId) {
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

        couponRepository.save(new Coupon(userId));
    }
}
