package cn.springcloud.gray.server.configuration;

import cn.springcloud.gray.api.ApiRes;
import cn.springcloud.gray.server.oauth2.DefaultTokenGranter;
import cn.springcloud.gray.server.oauth2.Oauth2Service;
import cn.springcloud.gray.server.utils.WebHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.KeyPair;

@ConditionalOnProperty(value = "gray.server.security.oauth2.enabled", matchIfMissing = true)
@Configuration
public class OAuth2Config {

    private static final Logger log = LoggerFactory.getLogger(OAuth2Config.class);

    @Autowired
    private AuthorizationServerTokenServices tokenServices;

    @Bean
    public JwtTokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    /**
     * This bean generates an token enhancer, which manages the exchange between JWT acces tokens and Authentication
     * in both direction.
     *
     * @return an access token converter configured with the authorization server's public/private keys
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        KeyPair keyPair = new KeyStoreKeyFactory(
                new ClassPathResource("keystore.jks"), "password".toCharArray())
                .getKeyPair("selfsigned");
        converter.setKeyPair(keyPair);
        return converter;
    }

    @Bean
    public OAuth2RequestFactory requestFactory(ClientDetailsService clientDetailsService) {
        return new DefaultOAuth2RequestFactory(clientDetailsService);
    }


    @Bean
    public RefreshTokenGranter refreshTokenGranter(
            ClientDetailsService clientDetailsService,
            OAuth2RequestFactory requestFactory) {
        return new RefreshTokenGranter(tokenServices, clientDetailsService, requestFactory);
    }

    @Bean
    public DefaultTokenGranter defaultTokenGranter(
            ClientDetailsService clientDetailsService,
            OAuth2RequestFactory requestFactory) {
        return new DefaultTokenGranter(tokenServices, clientDetailsService, requestFactory);
    }


    @Bean
    public Oauth2Service oauth2Service(
            ClientDetailsService clientDetailsService,
            OAuth2RequestFactory requestFactory,
            DefaultTokenGranter defaultTokenGranter) {
        return new Oauth2Service(clientDetailsService, requestFactory, defaultTokenGranter);
    }

    @Configuration
    public class SecurityConfig extends WebSecurityConfigurerAdapter {

        @Bean
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {
            AuthenticationManager manager = super.authenticationManagerBean();
            return manager;
        }

    }


    @Configuration
    @EnableResourceServer
    public static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

        public String[] permitPathPatterns = {
                "/**.html", "/**.js", "/**.css", "/**.png",
                "/swagger-resources/**", "/v2/api-docs", "/webjars/**",
                "/gray/user/login", "/gray/user/login", "/gray/instances/enable",
                "/gray/instances", "/gray/trackDefinitions", "/gray/v1/**", "/gray/v2/**",
                "/server/synch/accept"
        };

        @Autowired
        private TokenStore tokenStore;

        @Override
        public void configure(HttpSecurity http) throws Exception {
            http.formLogin().and()
                    .authorizeRequests()
//                    .antMatchers("/gray/user/login").permitAll()
//                    .antMatchers("/gray/user/login").permitAll()
//                    .antMatchers("/gray/instances/enable").permitAll()
//                    .antMatchers("/gray/instances").permitAll()
//                    .antMatchers("/gray/trackDefinitions").permitAll()
//                    .antMatchers("/gray/v1/**", "/gray/v2/**").permitAll()
                    .antMatchers(permitPathPatterns).permitAll()

                    .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//                    .antMatchers("/gray/service/**").authenticated()
//                    .antMatchers("/gray/policy/**").authenticated()
//                    .antMatchers("/gray/decision/**").authenticated()
//                    .antMatchers("/gray/discover/**").authenticated()
//                    .antMatchers("/gray/track/**").authenticated()
//                    .antMatchers("/route/**").authenticated()
                    .anyRequest().authenticated()
                    .and()
                    .csrf().disable();
        }

        @Override
        public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
            resources.authenticationEntryPoint(new AuthenticationEntryPoint() {
                @Override
                public void commence(HttpServletRequest request, HttpServletResponse response,
                                     AuthenticationException authException)
                        throws IOException {
                    log.debug("path:{} -> {}", request.getServletPath(), authException.getMessage());

                    response.setContentType("application/json");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

                    ApiRes<Void> res = ApiRes.<Void>builder()
                            .code(String.valueOf(HttpServletResponse.SC_UNAUTHORIZED)).message("无权访问").build();
                    WebHelper.response(response, res);
                }
            }).accessDeniedHandler(new AccessDeniedHandler() {
                @Override
                public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
                    log.debug("path:{} -> {}", request.getServletPath(), accessDeniedException.getMessage());

                    response.setContentType("application/json");
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    ApiRes<Void> res = ApiRes.<Void>builder()
                            .code(String.valueOf(HttpServletResponse.SC_BAD_REQUEST)).message("无权访问").build();
                    WebHelper.response(response, res);
                }
            });
        }
    }


    @Configuration
    @EnableAuthorizationServer
    public static class AuthorizationConfig extends AuthorizationServerConfigurerAdapter {
        @Autowired
        private AuthenticationManager authenticationManager;
        @Autowired
        private JwtTokenStore tokenStore;
        @Autowired
        private JwtAccessTokenConverter jwtAccessTokenConverter;


        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            clients.inMemory()
                    .withClient("gray-server")
                    .secret("V@JA-#i+6BkDhhq9")
                    .authorizedGrantTypes("client_credentials", "refresh_token", "default")
                    .accessTokenValiditySeconds(3600 * 24 * 30)
                    .refreshTokenValiditySeconds(3600 * 24 * 30 * 2)
            ;
        }


        @Override
        public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
            security.tokenKeyAccess("permitAll()").checkTokenAccess(
                    "isAuthenticated()");
        }

        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//        super.configure(endpoints);
            endpoints.tokenStore(tokenStore).tokenEnhancer(jwtAccessTokenConverter).authenticationManager(authenticationManager);
            endpoints.exceptionTranslator(null);

//            endpoints.getFrameworkEndpointHandlerMapping().setMappings("/oauth/confirm_access", "/oauth/confirm_access2"); //oatuh2 授权码确认页面

        }
    }
}
