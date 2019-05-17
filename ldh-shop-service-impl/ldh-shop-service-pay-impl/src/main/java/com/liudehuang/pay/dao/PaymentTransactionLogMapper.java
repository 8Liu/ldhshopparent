package com.liudehuang.pay.dao;

import com.liudehuang.pay.entity.PaymentTransactionLogEntity;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentTransactionLogMapper {

	@Insert("INSERT INTO `payment_transaction_log` (\n" +
			"\tSYNCH_LOG,\n" +
			"\tASYNC_LOG,\n" +
			"\tCHANNEL_ID,\n" +
			"\tTRANSACTION_ID,\n" +
			"\tREVISION,\n" +
			"\tCREATED_BY,\n" +
			"\tCREATED_TIME,\n" +
			"\tUPDATED_BY,\n" +
			"\tuntitled,\n" +
			"\tUPDATED_TIME\n" +
			")\n" +
			"VALUES\n" +
			"\t(\n" +
			"\t\tNULL,\n" +
			"\t\t\"123456\",\n" +
			"\t\tNULL,\n" +
			"\t\t\"1525\",\n" +
			"\t\tNULL,\n" +
			"\t\tNULL,\n" +
			"\t\tNOW(),\n" +
			"\t\tNULL,\n" +
			"\t\tNULL,\n" +
			"\t\tNOW()\n" +
			"\t);")
	public int insertTransactionLog(PaymentTransactionLogEntity paymentTransactionLog);

}