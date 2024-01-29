package org.checracker.backend.front.config.auth

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

@EnableWebSecurity
class SecurityConfig(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
) {
    private val allowedUrls =
        arrayOf(
            "/",
            "/v1/auth/**",
        )

    private val SwaggerPatterns = arrayOf(
        "/swagger-resources/**",
        "/swagger-ui/**",
        "/v3/api-docs/**",
        "/v3/api-docs",
    )

    @Value("\${springdoc.swagger-user}")
    private val swaggerUser: String = ""

    @Value("\${springdoc.swagger-password}")
    private val swaggerPassword: String = ""

    // 인메모리로 스웨거용 유저 세팅
    @Bean
    fun userDetailsService(): InMemoryUserDetailsManager? {
        val user: UserDetails = User.withUsername(swaggerUser)
            .password(passwordEncoder().encode(swaggerPassword))
            .roles("SWAGGER")
            .build()
        return InMemoryUserDetailsManager(user)
    }

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .csrf().disable()
            .headers { it.frameOptions().sameOrigin() } // H2 콘솔 사용을 위한 설정
            .authorizeHttpRequests {
                it.mvcMatchers(*SwaggerPatterns).authenticated().and().httpBasic() // TODO : 스웨거 보안 설정 보완하기 - 쿠키 및 ROLE 설정
                it.requestMatchers(*convertToRequestMatchers(allowedUrls))
                    .permitAll() // requestMatchers의 인자로 전달된 url은 모두에게 허용
//                    .requestMatchers(PathRequest.toH2Console()).permitAll() // H2 콘솔 접속은 모두에게 허용
                    .anyRequest().authenticated() // 그 외의 모든 요청은 인증 필요
            }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) } // 세션을 사용하지 않으므로 STATELESS 설정
            .addFilterBefore(
                jwtAuthenticationFilter,
                BasicAuthenticationFilter::class.java,
            ) // security 로직에 Jwt 인증 Filter 등록
            .build()!!
    }

    private fun convertToRequestMatchers(paths: Array<String>): Array<AntPathRequestMatcher> {
        return paths.map { AntPathRequestMatcher(it) }.toTypedArray()
    }
}
