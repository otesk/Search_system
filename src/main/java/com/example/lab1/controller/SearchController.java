package com.example.lab1.controller;

import com.example.lab1.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/")
public class SearchController {
    private final DocumentService documentService;

    @GetMapping
    public String getStartPage(Model model) {
        return "start";
    }

    @PostMapping
    public String searchDocument(@RequestParam(value = "searchText") String searchText, Model model){
        model.addAttribute("searchResults", documentService.getSearchDocuments(searchText));
        return "start";
    }
}
