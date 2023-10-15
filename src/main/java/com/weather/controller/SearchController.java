package com.weather.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.config.ThymeleafConfiguration;
import com.weather.dao.IUserDAO;
import com.weather.dao.UserDAO;
import com.weather.model.Location;
import com.weather.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.weather.util.PropertiesUtil.get;

@WebServlet(urlPatterns = "/search")
public class SearchController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = createUrl(req);
        HttpRequest request = createHttpRequest(url);
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = null;

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (InterruptedException e) {
            throw new RuntimeException("Error sending request to: " + url, e);
        }

        List<Location> locations = mapLocationsFromResponse(response);

        TemplateEngine templateEngine = (TemplateEngine) getServletContext().getAttribute(
                ThymeleafConfiguration.TEMPLATE_ENGINE_ATTR);
        IWebExchange webExchange = JakartaServletWebApplication.buildApplication(getServletContext())
                .buildExchange(req, resp);
        WebContext context = new WebContext(webExchange);

        String userId = req.getParameter("userId");
        long id;
        if (userId != null) {
            id = Long.parseLong(userId);
            IUserDAO userDAO = new UserDAO();
            Optional<User> user = userDAO.getById(id);
            context.setVariable("user", user.get());
        }

        context.setVariable("locations", locations);
//        templateEngine.process("search-result.html", context, resp.getWriter());
        templateEngine.process("without-auth.html", context, resp.getWriter());
    }

    private static List<Location> mapLocationsFromResponse(HttpResponse<String> response) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return mapper.readValue(response.body(), new TypeReference<>() {
        });
    }

    private static HttpRequest createHttpRequest(String url) {
        return HttpRequest.newBuilder()
                .GET()
                .header("accept", "application/json")
                .uri(URI.create(url))
                .build();
    }

    private static String createUrl(HttpServletRequest req) {
        String city = req.getParameter("city").replace(" ", "+");
        return get("apiUrl") + city + "&limit=" + get("limit") + "&appid=" + get("appid");
    }
}
