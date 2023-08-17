package com.tizfaver.lucky.utils;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class Stats {

    private int kills;
    private int finals;
    private int nexus;

    public Stats(){
        this.kills = 0;
        this.finals = 0;
        this.nexus = 0;
    }
}
