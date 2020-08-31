package com.cff.springbootwork.swagger.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import com.cff.springbootwork.swagger.entity.TestEntity;
import com.fasterxml.classmate.TypeResolver;

import springfox.documentation.builders.OperationBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiDescription;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ApiListingScannerPlugin;
import springfox.documentation.spi.service.contexts.DocumentationContext;
import springfox.documentation.spring.web.readers.operation.CachingOperationNameGenerator;

/**
 * 该文件可有可无，手动增加接口的方法
 * @author fufei
 *
 */
@Component
public class SwaggerAddtionScanConfig implements ApiListingScannerPlugin {
	@Override
	public List<ApiDescription> apply(DocumentationContext documentationContext) {
		return new ArrayList<>(
				Arrays.asList(
						new ApiDescription("Login", "/login", "登录接口",
								Collections
										.singletonList(
												new OperationBuilder(new CachingOperationNameGenerator()).authorizations(new ArrayList<>())
														.method(HttpMethod.POST)
														.summary("登录接口")                                                
														.notes("登录接口")//方法描述                                                
														.tags(Collections.singleton("登录登出模块"))//归类标签
														.produces(Collections.singleton("application/json"))
														.consumes(Collections.singleton("application/json"))
														.parameters(
																Arrays.asList(
																		new ParameterBuilder()
																				.description("email")
																				.type(new TypeResolver()
																						.resolve(String.class))
																				.name("email")
																				.parameterType("query")
																				.parameterAccess("access")
																				.required(true)
																				.modelRef(new ModelRef("string"))
																				.build(),
																		new ParameterBuilder()
																		.description("password")
																		.type(new TypeResolver()
																				.resolve(String.class))
																		.name("password")
																		.parameterType("query")
																		.parameterAccess("access")
																		.required(true)
																		.modelRef(new ModelRef("string"))
																		.build()
																				))
														.responseMessages(responseMessages())
														.build()),
								false),
						new ApiDescription("Login",
								"/logout", "登出接口",
								Collections
										.singletonList(
												new OperationBuilder(new CachingOperationNameGenerator()).authorizations(new ArrayList<>())
														.method(HttpMethod.POST)
														.summary("登出接口")                                                
														.notes("登出接口")//方法描述                                                
														.tags(Collections.singleton("登录登出模块"))//归类标签
														.produces(Collections.singleton("application/json"))
														.parameters(
																Collections
																		.singletonList(new ParameterBuilder()
																				.description("token")
																				.type(new TypeResolver()
																						.resolve(String.class))
																				.name("token")
																				.parameterType("query")
																				.parameterAccess("access")
																				.required(true)
																				.modelRef(new ModelRef("string"))
																				.build()))
														.responseMessages(responseMessages())
														
														.build()),
								false)));

	}

	/**
	 * @return Set of response messages that overide the default/global response
	 *         messages
	 */
	private Set<ResponseMessage> responseMessages() { // <8>
		return Collections.singleton(
				new ResponseMessageBuilder().code(200).message("Successfully received bug 1767 or 2219 response")
						.responseModel(new ModelRef(
								TestEntity.class.getSimpleName())
								).build());
	}

	@Override
	public boolean supports(DocumentationType documentationType) {
		return DocumentationType.SWAGGER_2.equals(documentationType);
	}
}
