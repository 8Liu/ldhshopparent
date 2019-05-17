package com.liudehuang.pay.service;

import com.liudehuang.pay.output.dto.PaymentChannelDTO;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

public interface PaymentChannelService {
	/**
	 * 查询所有支付渠道
	 * 
	 * @return
	 */
	@GetMapping("/selectAll")
	List<PaymentChannelDTO> selectAll();
}
