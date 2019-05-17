package com.liudehuang.pay.service.impl;


import com.liudehuang.core.base.BaseApiService;
import com.liudehuang.core.utils.MapperUtils;
import com.liudehuang.pay.dao.PaymentChannelMapper;
import com.liudehuang.pay.entity.PaymentChannelEntity;
import com.liudehuang.pay.output.dto.PaymentChannelDTO;
import com.liudehuang.pay.service.PaymentChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PaymentChannelServiceImpl extends BaseApiService<List<PaymentChannelDTO>>
		implements PaymentChannelService {
	@Autowired
	private PaymentChannelMapper paymentChannelMapper;

	@Override
	public List<PaymentChannelDTO> selectAll() {
		List<PaymentChannelEntity> paymentChanneList = paymentChannelMapper.selectAll();
		return MapperUtils.mapAsList(paymentChanneList, PaymentChannelDTO.class);
	}

}
