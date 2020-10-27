package com.example.lab1.controller;

import com.example.lab1.service.DocumentService;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.implementation.bind.annotation.Morph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/document")
public class DocumentController {
    private final DocumentService documentService;

    @PostMapping("/upload")
    public String upload(@RequestParam(value = "fileToUpload") MultipartFile file) {
        documentService.upload(file);
        return "redirect:/";
    }

    @GetMapping("/upload")
    public String getUploadPage() {
        return "upload";
    }

    @GetMapping("{id}")
    public String getDocument(@PathVariable("id") Long DocumentId, Model model){
        model.addAttribute("document", documentService.findDocument(DocumentId));

        return "document";
    }
}
