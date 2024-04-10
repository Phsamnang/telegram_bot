package com.somnang.telegrambot;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class GeminiResponse {
    private List<RequestRes>candidates;
}
