package ca.bc.gov.open.jag.documentutils.security;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class JwtAuthConverterTest {

	@Test
	void testNull() {
		Jwt jwt = null;
		JwtAuthConverter converter = new JwtAuthConverter();
		assertThrows(NullPointerException.class, () -> converter.convert(null));
	}

	@Test
	void testJwtMissingResourceAccess() throws Exception {
		Jwt jwt = Mockito.mock(Jwt.class);

		JwtAuthConverter converter = new JwtAuthConverter();
		AbstractAuthenticationToken authenticationToken = converter.convert(jwt);
		Collection<GrantedAuthority> authorities = authenticationToken.getAuthorities();
		assertEquals(0, authorities.size());
	}

	@Test
	void testJwtMissingResource() throws Exception {
		Jwt jwt = Mockito.mock(Jwt.class);
		Map<String, Object> resourceAccess = new HashMap<String, Object>();
		when(jwt.getClaim(JwtAuthConverter.KEYCLOAK_RESOURCE_ATTRIBUTE)).thenReturn(resourceAccess);

		JwtAuthConverter converter = new JwtAuthConverter();
		AbstractAuthenticationToken authenticationToken = converter.convert(jwt);
		Collection<GrantedAuthority> authorities = authenticationToken.getAuthorities();
		assertEquals(0, authorities.size());
	}

	@Test
	void testJwtMissingRoles() throws Exception {
		Jwt jwt = Mockito.mock(Jwt.class);
		
		Map<String, Object> resource = new HashMap<String, Object>();
		
		Map<String, Object> resourceAccess = new HashMap<String, Object>();
		resourceAccess.put("doc-utils", resource);
		when(jwt.getClaim(JwtAuthConverter.KEYCLOAK_RESOURCE_ATTRIBUTE)).thenReturn(resourceAccess);
		
		JwtAuthConverter converter = new JwtAuthConverter();
		AbstractAuthenticationToken authenticationToken = converter.convert(jwt);
		Collection<GrantedAuthority> authorities = authenticationToken.getAuthorities();
		assertEquals(0, authorities.size());
	}


}
