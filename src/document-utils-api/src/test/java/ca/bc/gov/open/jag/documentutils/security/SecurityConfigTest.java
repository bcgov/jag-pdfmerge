package ca.bc.gov.open.jag.documentutils.security;

import org.junit.jupiter.api.Test;
import org.keycloak.adapters.KeycloakConfigResolver;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

import static org.assertj.core.api.Assertions.assertThat;

public class SecurityConfigTest {

    ApplicationContextRunner context = new ApplicationContextRunner()
            .withUserConfiguration(SecurityConfig.class);

    @Test
    public void testConfigure() throws Exception {

        context.run(it -> {
            assertThat(it).hasSingleBean(SessionAuthenticationStrategy.class);
            assertThat(it).hasSingleBean(KeycloakConfigResolver.class);
        });

    }

}
