package com.somnang.telegrambot;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class Request {
    private List<Content> contents;
}