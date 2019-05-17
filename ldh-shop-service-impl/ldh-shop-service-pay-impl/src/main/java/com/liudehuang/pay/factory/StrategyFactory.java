package com.liudehuang.pay.factory;

import com.liudehuang.pay.strategy.PayStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liudehuang
 * @date 2019/5/17 10:28
 */
@Slf4j
public class StrategyFactory {

    /**
     * 存放已实例化的策略类
     */
    private static Map<String,PayStrategy> strategyMap = new ConcurrentHashMap<>();

    public static PayStrategy getPayStrategy(String className){
        try{
            if(StringUtils.isEmpty(className)){
                log.info("className不能为空");
                return null;
            }
            PayStrategy beanPayStrategy = strategyMap.get(className);
            if(beanPayStrategy!=null){
                return beanPayStrategy;
            }
            //使用java的反射机制初始化对象
            PayStrategy payStrategy = (PayStrategy) Class.forName(className).newInstance();
            strategyMap.put(className,payStrategy);
            return payStrategy;
        }catch (Exception e){
            return null;
        }

    }
}
