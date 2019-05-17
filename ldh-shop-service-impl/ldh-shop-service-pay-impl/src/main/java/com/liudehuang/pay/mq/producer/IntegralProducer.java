package com.liudehuang.pay.mq.producer;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author liudehuang
 * @date 2019/5/17 16:40
 */
@Component
@Slf4j
public class IntegralProducer implements RabbitTemplate.ConfirmCallback {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Transactional(rollbackFor = Exception.class)
    public void send(JSONObject jsonObject) {
        String jsonString = jsonObject.toJSONString();
        System.out.println("jsonString:" + jsonString);
        String paymentId = jsonObject.getString("paymentId");
        // 封装消息
        Message message = MessageBuilder.withBody(jsonString.getBytes())
                .setContentType(MessageProperties.CONTENT_TYPE_JSON).setContentEncoding("utf-8").setMessageId(paymentId)
                .build();
        // 构建回调返回的数据（消息id）
        this.rabbitTemplate.setMandatory(true);
        this.rabbitTemplate.setConfirmCallback(this);
        CorrelationData correlationData = new CorrelationData(jsonString);
        rabbitTemplate.convertAndSend("integral_exchange_name", "integralRoutingKey", message, correlationData);
    }

    /**
     * 生产消息确认机制,生产者向服务器端发送消息时，采用应答机制
     * @param correlationData
     * @param ack
     * @param s
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String s) {
        String jsonString = correlationData.getId();
        System.out.println("消息id:" + correlationData.getId());
        if (ack) {
            log.info(">>>使用MQ消息确认机制确保消息一定要投递到MQ中成功");
            return;
        }
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        // 生产者消息投递失败的话，采用递归重试机制
        send(jsonObject);
        log.info(">>>使用MQ消息确认机制投递到MQ中失败");
    }
}
