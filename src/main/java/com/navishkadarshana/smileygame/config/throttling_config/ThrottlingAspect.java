package com.navishkadarshana.smileygame.config.throttling_config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect
@Component
public class ThrottlingAspect {

    private final ThrottlingManager throttlingManager;
    private final UserIdProvider userIdProvider;

    @Autowired
    public ThrottlingAspect(ThrottlingManager throttlingManager, UserIdProvider userIdProvider) {
        this.throttlingManager = throttlingManager;
        this.userIdProvider = userIdProvider;
    }

    @Pointcut("within(@(@org.springframework.stereotype.Controller *) *)")
    public void controllerPointcut() {
        System.out.println("point cut----");
    }

    @Before("controllerPointcut()")
    public void log(JoinPoint pjp) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        ThrottlingConfig throttlingConfig = getThrottlingConfig(method);
        EndpointMethod  endpointMethod = new EndpointMethod(pjp.getTarget().getClass(), method.getName());
        userIdProvider.getCurrentUserId()
                .ifPresent(id -> throttlingManager.throttleRequest(endpointMethod, id, throttlingConfig));
    }

    private ThrottlingConfig getThrottlingConfig(Method method) {
        return Arrays.stream(method.getDeclaredAnnotations())
                .filter(d -> d.annotationType() == Throttling.class)
                .findFirst()
                .map(d -> {
                    Throttling t = (Throttling) d;
                    return new ThrottlingConfig(t.timeFrameInSeconds(), t.calls());
                })
                .orElse(ThrottlingConfig.DEFAULT);
    }
}
