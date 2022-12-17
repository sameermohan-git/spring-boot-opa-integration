package com.smohan.opaauthorization.opa;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class OPADecisionManager implements AuthorizationManager<RequestAuthorizationContext> {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	private String opaAuthURL;

	private RestTemplate restTemplate;
	
	public OPADecisionManager() {
		
	}
	
	public OPADecisionManager(String opaAuthURL) {
		this.opaAuthURL = opaAuthURL;
		this.restTemplate = new RestTemplate();
	}

	@Override
	public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext requestAuthorizationContext) {
		
		try {

			//Collection<ConfigAttribute> attributes = this.securityMetadataSource.getAttributes(requestAuthorizationContext);
			
			if (!(requestAuthorizationContext instanceof RequestAuthorizationContext)) {
				return new AuthorizationDecision(false);
			}
			
			Map<String, String> headers = new HashMap<String, String>();

			for (Enumeration<String> headerNames = requestAuthorizationContext.getRequest().getHeaderNames(); headerNames.hasMoreElements();) {
				String header = headerNames.nextElement();
				headers.put(header, requestAuthorizationContext.getRequest().getHeader(header));
			}

			String[] path = requestAuthorizationContext.getRequest().getRequestURI().replaceAll("^/|/$", "").split("/");

			
			Map<String, Object> input = new HashMap<String, Object>();
			input.put("auth", authentication.get());
			input.put("roles", authentication.get().getAuthorities());
			input.put("method", requestAuthorizationContext.getRequest().getMethod());
			input.put("path", path);
			input.put("headers", headers);
			HttpEntity<?> request = new HttpEntity<>(new OPADataRequest(input));
			OPADataResponse response = restTemplate.postForObject(this.opaAuthURL, request, OPADataResponse.class);

			LOGGER.info("OPADataResponse: "+ response);

			if (!response.getResult()) {
				LOGGER.info("OPADataResponse Result Negative: "+ response.getResult());
				return new AuthorizationDecision(false);
			}
			LOGGER.info("OPADataResponse Result Positive: "+ response.getResult());
			return new AuthorizationDecision(true);
		} catch (AccessDeniedException ex) {
			return new AuthorizationDecision(false);
		}
	}

}