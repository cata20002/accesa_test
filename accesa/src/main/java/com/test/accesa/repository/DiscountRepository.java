package com.test.accesa.repository;

import com.test.accesa.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {

    List<Discount> findAllByStartDateLessThanEqualAndEndDateGreaterThanEqual(Date startDate, Date endDate);
}
