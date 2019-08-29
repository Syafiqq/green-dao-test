package com.github.syafiqq.greendaotest001.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;

import java.util.Date;

import lombok.Data;

@Entity(
        indexes = {
                @Index(value = "text, date DESC", unique = true),
        },
        generateConstructors = false,
        generateGettersSetters = false
)
@Data
public class Note {
    @Id
    private Long id;
    @NotNull
    private String text;
    private Date date;

    public Note() {
    }
}
