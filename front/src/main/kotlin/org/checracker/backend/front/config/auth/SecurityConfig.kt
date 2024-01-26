package org.checracker.backend.front.config.auth

import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

@EnableWebSecurity
class SecurityConfig() {

    private val allowedUrls =
        arrayOf(
            "/",
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/v3/api-docs/**",
            "/v1/auth/**",
        )

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .csrf().disable()
            .headers { it.frameOptions().sameOrigin() } // H2 콘솔 사용을 위한 설정
            .authorizeHttpRequests {
                it.requestMatchers(*convertToRequestMatchers(allowedUrls))
                    .permitAll() // requestMatchers의 인자로 전달된 url은 모두에게 허용
                    .requestMatchers(PathRequest.toH2Console()).permitAll() // H2 콘솔 접속은 모두에게 허용
                    .anyRequest().authenticated() // 그 외의 모든 요청은 인증 필요
            }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) } // 세션을 사용하지 않으므로 STATELESS 설정
            .build()!!
    }

    private fun convertToRequestMatchers(paths: Array<String>): Array<AntPathRequestMatcher> {
        return paths.map { AntPathRequestMatcher(it) }.toTypedArray()
    }
}
