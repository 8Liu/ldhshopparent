package com.liudehuang.integral.mapper;

import com.liudehuang.integral.mapper.entity.IntegralEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * 积分Mapper
 *
 */
@Repository
public interface IntegralMapper {
	@Insert("INSERT INTO `meite_integral` VALUES (NULL, #{userId}, #{paymentId},#{integral}, #{availability}, 0, null, now(), null, now());")
	public int insertIntegral(IntegralEntity eiteIntegralEntity);

	@Select("SELECT  id as id ,USER_ID as userId, PAYMENT_ID as PAYMENTID ,INTEGRAL as INTEGRAL ,AVAILABILITY as AVAILABILITY  FROM meite_integral where PAYMENT_ID=#{paymentId}  AND AVAILABILITY='1';")
	public IntegralEntity findIntegral(String paymentId);
}
