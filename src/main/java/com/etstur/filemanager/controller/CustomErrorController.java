package com.etstur.filemanager.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomErrorController implements ErrorController {

    /***
     *
     * @return 404 Not Found
     */
    @RequestMapping("/error")
    public ResponseEntity<?> handleError() {
        return ResponseEntity.notFound().build();
    }
}