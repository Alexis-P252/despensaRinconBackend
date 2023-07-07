package uteclab.despensaRincon.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import uteclab.despensaRincon.annotations.VerificarToken;

import java.io.PrintWriter;

public class VerificarTokenInterceptor implements HandlerInterceptor {

    private JWTUtil jwtUtil;

    public VerificarTokenInterceptor(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;

        if (handlerMethod.getMethod().isAnnotationPresent(VerificarToken.class)) {

            String jwt = request.getHeader("Authorization");

            if(jwt == null){
                response.setContentType("application/json");
                JSONObject json = new JSONObject();
                json.put("msg","No autorizado");

                PrintWriter out = response.getWriter();
                out.print(json);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false;

            }

            jwt = jwt.substring(7, jwt.length());
            if(! jwtUtil.verifyToken(jwt)){

                response.setContentType("application/json");
                JSONObject json = new JSONObject();
                json.put("msg","No autorizado");

                PrintWriter out = response.getWriter();
                out.print(json);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }
            return true;
        }
        // Si la anotacion @VerificarToken no esta en el metodo interceptado
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
    }
}
