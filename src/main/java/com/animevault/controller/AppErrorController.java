package com.animevault.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class AppErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object statusCode = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (statusCode != null) {
            int status = Integer.parseInt(statusCode.toString());
            if (status == HttpStatus.NOT_FOUND.value()) {
                return "error/404";
            } else if (status == HttpStatus.FORBIDDEN.value()) {
                return "error/403";
            } else if (status == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                model.addAttribute("errorMessage",
                        request.getAttribute(RequestDispatcher.ERROR_MESSAGE));
                return "error/500";
            }
        }
        return "error/generic";
    }

    @GetMapping("/error/403")
    public String accessDenied() {
        return "error/403";
    }
}
