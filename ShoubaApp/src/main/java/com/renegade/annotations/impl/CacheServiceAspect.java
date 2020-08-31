package com.renegade.annotations.impl;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import com.renegade.annotations.EnableCacheService;
import com.renegade.annotations.EnableCacheService.CacheOperation;

/**
 * EnableRedisService 注解切面处理
 * 
 * @author: liujingzhong
 * @date: 2018年9月29日
 */
@Aspect
@Component
@SuppressWarnings("all")
public class CacheServiceAspect {

	private static final Logger log = LoggerFactory.getLogger(CacheServiceAspect.class);

	@Pointcut("@annotation(com.renegade.annotations.EnableCacheService)")
	public void dealCacheServiceCut() {
	}

	@Autowired
	private RedisTemplate redisTemplate;

	@Around(value = "dealCacheServiceCut()")
	@SuppressWarnings("all")
	public Object dealCacheService(ProceedingJoinPoint point) throws Throwable {
		Method method = getMethod(point);
		// 获取该注解EnableCacheService实例对象
		EnableCacheService cacheServiceAnnotation = method.getAnnotation(EnableCacheService.class);
		// 所有参数
		Object[] args = point.getArgs();
		String cacheKey = cacheServiceAnnotation.keyPrefix()
				+ parseKey(cacheServiceAnnotation.fieldKey(), method, args);
		log.info("{} enable cache service,cacheKey:{}", point.getSignature(), cacheKey);
		CacheOperation cacheOperation = cacheServiceAnnotation.cacheOperation();
		if (cacheOperation == CacheOperation.QUERY) {
			return processQuery(point, cacheServiceAnnotation, cacheKey);
		}
		if (cacheOperation == CacheOperation.UPDATE || cacheOperation == CacheOperation.DELETE) {
			return processUpdateAndDelete(point, cacheKey);
		}
		return point.proceed();
	}

	/**
	 * 查询处理
	 */
	private <T> Object processQuery(ProceedingJoinPoint point, EnableCacheService cacheServiceAnnotation,
			String cacheKey) throws Throwable {
		if (redisTemplate.hasKey(cacheKey)) {
			  Object  wObject = redisTemplate.opsForValue().get(cacheKey);
			if (wObject == null) {
				try {
					return wObject = point.proceed();
				} finally {
					if(cacheServiceAnnotation.expireTime()==0){
						redisTemplate.opsForValue().set(cacheKey, wObject);
					}else{
						redisTemplate.opsForValue().
							set(cacheKey, wObject, cacheServiceAnnotation.expireTime(), TimeUnit.SECONDS);
					}
					log.info("after {} proceed,save result to cache,redisKey:{},save content:{}", point.getSignature(),
							cacheKey, wObject);
				}
			}
			log.info("{} enable cache service{},has cacheKey:{}, return{}", point.getSignature(), cacheKey,wObject);
//			return redisTemplate.getObject(cacheKey);
//			return redisTemplate.gettGenericity(cacheKey, returnType);
			return wObject;
		} else {
			Object result = null;
			try {
				return result = point.proceed();
			} finally {
				if(cacheServiceAnnotation.expireTime()==0){
					redisTemplate.opsForValue().set(cacheKey, result);
				}else{
					redisTemplate.opsForValue().
						set(cacheKey, result, cacheServiceAnnotation.expireTime(), TimeUnit.SECONDS);
				}
				log.info("after {} proceed,save result to cache,redisKey:{},save content:{}", point.getSignature(),
						cacheKey, result);
			}
		}
	}

	/**
	 * 删除和修改处理
	 */
	private Object processUpdateAndDelete(ProceedingJoinPoint point, String cacheKey) throws Throwable {
		// 通常来讲,数据库update操作后,只需删除掉原来在缓存中的数据,下次查询时就会刷新
		try {
			return point.proceed();
		} finally {
			redisTemplate.delete(cacheKey);
		}
	}

//获取当前切面需要执行的方法
	private Method getMethod(JoinPoint joinPoint) throws Exception {
		// 获取接口方法实现类的具体方法
		/*
		 * Signature joinPoints = ((JoinPoint) joinPoint).getSignature();
		 * MethodSignature msig = null; if (!(msig instanceof MethodSignature)) { throw
		 * new IllegalArgumentException("该注解只能用于方法"); } msig = (MethodSignature)
		 * joinPoints; Object target = joinPoint.getTarget(); Method currentMethod =
		 * target.getClass().getMethod(msig.getName(), msig.getParameterTypes());
		 */

		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();// 获取方法一系列表示，如包名，方法名，修饰符，组件名即类名

		Method method = methodSignature.getMethod();// 获取接口的方法

		return method;
	}

	/**
	 * 获取redis的key
	 */
	private String parseKey(String fieldKey, Method method, Object[] args) {
		// 获取被拦截方法参数名列表(使用Spring支持类库)
		LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
		String[] paraNameArr = u.getParameterNames(method);

		// 使用SPEL进行key的解析
		ExpressionParser parser = new SpelExpressionParser();
		// SPEL上下文
		StandardEvaluationContext context = new StandardEvaluationContext();
		// 把方法参数放入SPEL上下文中
		for (int i = 0; i < paraNameArr.length; i++) {
			context.setVariable(paraNameArr[i], args[i]);
		}
		return parser.parseExpression(fieldKey).getValue(context, String.class);
	}

}