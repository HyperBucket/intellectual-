package com.intellectual.controller;

import com.intellectual.dto.TagDto;
import com.intellectual.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagRepository tagRepository;

    @GetMapping
    public List<TagDto> list() {
        return tagRepository.findAll().stream().map(TagDto::from).toList();
    }
}
