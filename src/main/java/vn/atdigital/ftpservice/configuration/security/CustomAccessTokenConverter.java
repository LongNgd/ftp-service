package vn.atdigital.ftpservice.configuration.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import vn.atdigital.ftpservice.common.Constants;
import vn.atdigital.ftpservice.common.LocalVariable;
import vn.atdigital.ftpservice.common.ThreadLocalContext;
import vn.atdigital.ftpservice.common.utils.JsonMapper;
import vn.atdigital.ftpservice.domain.dto.ActionUserDTO;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class CustomAccessTokenConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

	@Override
	public Collection<GrantedAuthority> convert(Jwt jwt) {
		Map<String, Object> claims = jwt.getClaims();

		String tokenValue = jwt.getTokenValue();
		LocalVariable.setToken(tokenValue);

		try {
			Object rawActionUser = claims.get("actionUser");
			if (rawActionUser != null) {
				ActionUserDTO actionUserDTO = JsonMapper.objectMapper.convertValue(rawActionUser, ActionUserDTO.class);

				// Save to thread-local contexts (like before)
				ThreadLocalContext.put(Constants.SESSION_VARIABLE.CURRENT_ACTION_USER, actionUserDTO);

				ActionUserDTO actionUserCore = new ActionUserDTO();

				int hashToken = claims.hashCode();
				ThreadLocalContext.put("HASH_TOKEN", hashToken);
			}
		} catch (Exception e) {
			log.warn("Failed to map actionUser claim: {}", e.getMessage());
		}
		// Extract scopes or roles from the JWT claim
		Object scopes = jwt.getClaims().get("scope");

		if (scopes instanceof String scopeString) {
			return scopeString
					.split(" ")
					.length == 0 ? Collections.emptyList()
					: java.util.Arrays.stream(scopeString.split(" "))
					.map(s -> new SimpleGrantedAuthority("SCOPE_" + s))
					.collect(Collectors.toList());
		}

		return Collections.emptyList();
	}
}
