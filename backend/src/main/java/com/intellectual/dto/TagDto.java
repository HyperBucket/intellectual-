package com.intellectual.dto;

import com.intellectual.domain.Tag;

public record TagDto(Long id, String name) {
    public static TagDto from(Tag tag) {
        return new TagDto(tag.getId(), tag.getName());
    }
}
