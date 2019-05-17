package com.liudehuang.pay.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author liudehuang
 * @date 2019/5/17 16:34
 */
@Component
public class RabbitmqConfig {

    /**
     *  添加积分队列
     *
     */
    public static final String INTEGRAL_DIC_QUEUE = "integral_queue";
    /**
     * 支付补偿队列
      */
    public static final String INTEGRAL_CREATE_QUEUE = "integral_create_queue";
    /**
     * 积分交换机
     */
    private static final String INTEGRAL_EXCHANGE_NAME = "integral_exchange_name";

    /**
     * 1.添加积分队列
     * @return
     */
    @Bean
    public Queue directIntegralDicQueue() {
        return new Queue(INTEGRAL_DIC_QUEUE);
    }

    /**
     * 2.定义支付补偿队列
     * @return
     */
    @Bean
    public Queue directCreateintegralQueue() {
        return new Queue(INTEGRAL_CREATE_QUEUE);
    }

    /**
     * 2.定义交换机
     * @return
     */
    @Bean
    DirectExchange directintegralExchange() {
        return new DirectExchange(INTEGRAL_EXCHANGE_NAME);
    }

    /**
     * 3.积分队列与交换机绑定
     * @return
     */
    @Bean
    Binding bindingExchangeintegralDicQueue() {
        return BindingBuilder.bind(directIntegralDicQueue()).to(directintegralExchange()).with("integralRoutingKey");
    }

    /**
     * 3.补偿队列与交换机绑定
     * @return
     */
    @Bean
    Binding bindingExchangeCreateintegral() {
        return BindingBuilder.bind(directCreateintegralQueue()).to(directintegralExchange()).with("integralRoutingKey");
    }
}
