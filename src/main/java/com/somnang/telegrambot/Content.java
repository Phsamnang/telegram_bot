package com.somnang.telegrambot;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class Content {
    private List<Part> parts;
}