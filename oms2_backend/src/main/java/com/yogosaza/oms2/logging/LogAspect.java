package com.yogosaza.oms2.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Aspect
public class LogAspect {

    @Around("@annotation(LogInputOutput)")
    public Object loggingInputOutput(ProceedingJoinPoint joinPoint) throws Throwable {

        // 파라미터 값 추출
        String[] parameterNames = null;
        if (joinPoint.getSignature() instanceof CodeSignature) {
            CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();
            parameterNames = codeSignature.getParameterNames();
        }

        String methodName = joinPoint.getSignature().getName();
        String packageName = joinPoint.getSignature().getDeclaringTypeName();

        // 메소드 패캐지, 이름, 입력값 로그
        if (log.isInfoEnabled()) {
            Object[] signatureArgs = joinPoint.getArgs();
            StringBuilder sb = new StringBuilder();
            sb.append("[START] Method : " + packageName + "." + methodName + ",");
            sb.append("Arguments = ");
            if (parameterNames != null) {
                for (int i = 0; i < signatureArgs.length; i++) {
                    sb.append(parameterNames[i] + " : " + signatureArgs[i]);
                    if (i != signatureArgs.length - 1) sb.append(", ");
                }
            }
            else {
                // parameterNames가 null 인 경우, 인자 이름 없이 값만 출력
                for (int i = 0; i < signatureArgs.length; i++) {
                    sb.append(signatureArgs[i]);
                    if (i != signatureArgs.length - 1) sb.append(", ");
                }
            }
            log.info(sb.toString());
        }

        // 메소드 실행
        Object proceed = joinPoint.proceed();
        if (log.isInfoEnabled()) {
            StringBuilder sb = new StringBuilder();
            sb.append("[END] Method =  " + packageName + "." + methodName);
            if (proceed != null) {
                sb.append(", results = " + proceed.toString());
            }
            log.info(sb.toString());
        }
        return proceed;
    }

    @AfterThrowing(value = "@annotation(LogInputOutput)", throwing = "ex")
    public void loggingAfterThrowing(JoinPoint joinPoint, Throwable ex) {
        String methodName = joinPoint.getSignature().getName();
        String packageName = joinPoint.getSignature().getDeclaringTypeName();

        if (log.isInfoEnabled()) {
            StringBuilder sb = new StringBuilder();
            sb.append("[EXCEPTION] ");
            sb.append(packageName + "." + methodName + ", Message : " + ex.getMessage());
            log.info(sb.toString());
        }
    }
}
