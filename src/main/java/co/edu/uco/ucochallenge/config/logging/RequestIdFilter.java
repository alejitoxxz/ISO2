package co.edu.uco.ucochallenge.config.logging;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import co.edu.uco.ucochallenge.crosscuting.helper.TextHelper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RequestIdFilter extends OncePerRequestFilter {

        private static final String REQUEST_ID_HEADER = "X-Request-Id";
        private static final String MDC_KEY = "requestId";

        @Override
        protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
                        final FilterChain filterChain) throws ServletException, IOException {
                String requestId = request.getHeader(REQUEST_ID_HEADER);
                if (TextHelper.isEmpty(requestId)) {
                        requestId = UUID.randomUUID().toString();
                }

                MDC.put(MDC_KEY, requestId);
                response.setHeader(REQUEST_ID_HEADER, requestId);

                try {
                        filterChain.doFilter(request, response);
                } finally {
                        MDC.remove(MDC_KEY);
                }
        }
}
