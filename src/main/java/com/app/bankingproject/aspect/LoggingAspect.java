package com.app.bankingproject.aspect;

import com.app.bankingproject.account.dto.AccountCreationRequest;
import com.app.bankingproject.customer.dto.CustomerResponse;
import com.app.bankingproject.transaction.dto.TransactionResponse;
import com.app.bankingproject.transaction.dto.WithdrawRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.lang.reflect.WildcardType;
import java.math.BigDecimal;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Pointcut(value = "execution(* com.karthi.bank730.transaction.service.*.*(..))")
    public void transactionService(){}

    @Pointcut(value = "execution(* com.karthi.bank730.customer.service.CustomerManagement.createNewCustomer(..))")
    public void accountCreationService(){}





    @Before("execution(* com.karthi.bank730.account.service.*.*(..))")
    public void logBeforeMethodExecution(JoinPoint joinPoint){
        log.info("Executing Class Name {}", joinPoint.getTarget().getClass().getName());
        log.info("Executing Method {}",joinPoint.getSignature().getName());
        log.info("Args: {}",joinPoint.getArgs());
    }

    @After(value = "transactionService()")
    public void logAfterMethodExecution(JoinPoint joinPoint){
        log.info("Transaction Completed [May be Success Or Failed ] : Method Name {} executed with Args: {}  ",joinPoint.getSignature().getName(),joinPoint.getArgs());
    }

    @AfterReturning(pointcut = "transactionService()", returning = "transactionResponse")
    public void logAfterReturningMethodExecution(JoinPoint joinPoint, TransactionResponse transactionResponse){
        log.info("Transaction Completed | Type  : {} | Amount  : {} | Balance Amount {} ",
                transactionResponse.type(),
                transactionResponse.amount(),
                transactionResponse.balanceAfter()
        );
    }

    @AfterReturning(pointcut = "accountCreationService()", returning = "customerResponse")
    public void logAfterCustomerCreation(JoinPoint joinPoint, CustomerResponse customerResponse){
        log.info("Account Creation method Returned {}", joinPoint.getSignature().getName());
        log.info("Account Created for :  Cust ID {} | Name {} ",customerResponse.customerId(),customerResponse.fullName());
    }

    // AfterThrowing
    // for insufficient funds


    @Around(value = "transactionService()")
    public Object logUsingAround(ProceedingJoinPoint pjp) throws Throwable {
        log.info("Executing Class Name {}", pjp.getTarget().getClass().getName());
        log.info("Executing Method {}",pjp.getSignature().getName());
        return pjp.proceed();
    }

    @Around(value = "transactionService()")
    public Object performanceAnalysis(ProceedingJoinPoint pjp) throws Throwable {
        long start =  System.currentTimeMillis();
        Object result = pjp.proceed();
        long end = System.currentTimeMillis();
        log.info("{} method executed in {}s",pjp.getSignature().getName(),(end-start)/1000.0);
        return result;
    }

//    @Around(value = "execution(* com.karthi.bank730.transaction.service.TransactionService.withdraw(..))")
//    public Object validateAmount(ProceedingJoinPoint pjp) throws Throwable {
//        Object[] args = pjp.getArgs();
//        for(Object arg : args){
//            if(arg instanceof WithdrawRequest request){
//                if(request.amount().compareTo(
//                        BigDecimal.valueOf(10_000)
//                )>0){
//                    throw new RuntimeException("Withdraw Limit Exceeded");
//                }
//            }
//        }
//        return pjp.proceed();
//    }


    @Around(value = "execution(* com.karthi.bank730.transaction.service.TransactionService.withdraw(..))")
    public Object validateAmountAndChangeToDefault(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        for(Object arg : args){
            if(arg instanceof WithdrawRequest request){
                if(request.amount().compareTo(
                        BigDecimal.valueOf(10_000)
                )>0){
                    args[0] = new  WithdrawRequest(
                            request.accountNumber(),
                            BigDecimal.valueOf(10000),
                            request.remarks()
                    );
                    log.info("Limit Exceeded so converting to lower amount");
                    log.info("Withdraw Request place with default amount of 10,000");
                }
            }
        }
        Object result = pjp.proceed(args);
        TransactionResponse transactionResponse = (TransactionResponse) result;
        TransactionResponse updatedResp = new TransactionResponse(
                transactionResponse.reference(),
                transactionResponse.type(),
                transactionResponse.amount(),
                BigDecimal.valueOf(0)
        );
        return updatedResp;
    }
}