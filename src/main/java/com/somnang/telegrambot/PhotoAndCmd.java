package com.somnang.telegrambot;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "photo")
@Getter
@Setter
@NoArgsConstructor
public class PhotoAndCmd {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true)
    private String  cmd;
    private String imageUrl;
}
