package com.akosszabo.demo.mancala.persistence.entity;


import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "matches")
@Data
public class Match {

    @Id
    @Column( name = "id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column( name = "gamedata")
    private String gameData;

}
