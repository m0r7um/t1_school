package mortum.task1.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Aspect
@Slf4j
@Component
public class LoggingAspect {

    @Before("@annotation(mortum.task1.aspects.annotations.IncomingPutRequestLogging)")
    public void logPutMethod(JoinPoint joinPoint) {
        List<Object> incomingRequestParams = List.of(joinPoint.getArgs());
        log.info("""
                        Incoming put request processing by {}
                        Passed arguments: {}""",
                joinPoint.getTarget().getClass(),
                incomingRequestParams
        );
    }

    @Around(value = "@annotation(mortum.task1.aspects.annotations.IncomingPostRequestLogging)")
    public Object logPostMethod(ProceedingJoinPoint pjp) {
        List<Object> incomingRequestParams = List.of(pjp.getArgs());
        log.info(
                """
                        Incoming post request processing by {}
                        Passed arguments: {}""",
                pjp.getTarget(),
                incomingRequestParams
        );

        Object result;

        try {
            result = pjp.proceed();
            log.info("Added resource: {}", result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    @AfterThrowing(pointcut = "@annotation(mortum.task1.aspects.annotations.ExceptionLogging)", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, DataIntegrityViolationException ex) {
        log.info(ex.getClass().toString());
        log.error("""
                        Exception at {} while executing {} with arguments: {}
                        Sql constraint violation:  {}""",
                joinPoint.getTarget().getClass(),
                joinPoint.getSignature(),
                Arrays.toString(joinPoint.getArgs()),
                ex.getMessage()
        );
    }

    @AfterReturning(value = "@annotation(mortum.task1.aspects.annotations.GetMethodLogging)", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        log.info("""
                        Get method is processed by {}, method {}, with arguments {} successfully.
                        Response will include {}""",
                joinPoint.getTarget().getClass(),
                joinPoint.getSignature(),
                Arrays.toString(joinPoint.getArgs()),
                result == null ? "nothing" : result
        );
    }

    @AfterReturning(value = "@annotation(mortum.task1.aspects.annotations.ModifyingOperationLogging)", returning = "result")
    public void modifyingOperationCounter(JoinPoint joinPoint, Object result) {
        log.info("""
                        Modifying was processed by {}, method {}, with arguments {} successfully.
                        Rows affected: {}""",
                joinPoint.getTarget().getClass(),
                joinPoint.getSignature(),
                Arrays.toString(joinPoint.getArgs()),
                result);
    }
}
