package com.betterfb;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*")
public class CorsFilter implements Filter {

    /**
     * Initializes the filter.
     *
     * This method is called once when the filter is placed into service by a
     * web container. It can be used to set up any resources or state that the
     * filter needs to do its job.
     *
     * @param filterConfig the configuration information from the web.xml file
     * @throws ServletException if the initialization fails
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization logic if needed
    }

    /**
     * Applies CORS (Cross-Origin Resource Sharing) headers to the response.
     *
     * This method intercepts HTTP requests and sets necessary headers to allow
     * cross-origin requests from the specified origin. It allows specific HTTP
     * methods and headers, and supports credentials. It also handles preflight
     * OPTIONS requests by returning an HTTP OK status without passing the request
     * further down the filter chain.
     *
     * @param request the servlet request
     * @param response the servlet response
     * @param chain the filter chain
     * @throws IOException if an I/O error occurs during the processing
     * @throws ServletException if the processing fails
     */

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        // Allow Origin
        httpResponse.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");

        // Allow Methods and Headers
        httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        httpResponse.setHeader("Access-Control-Allow-Headers", 
            "Content-Type, Accept, X-Requested-With, Authorization, remember-me");
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");

        // Handle preflight request
        if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Cleanup logic if needed
    }
}
