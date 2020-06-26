package com.akosszabo.demo.mancala.domain.dto;

import com.akosszabo.demo.mancala.domain.PitType;
import com.akosszabo.demo.mancala.domain.Player;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PitDto implements Cloneable {
    private int id;
    private PitType type;
    private int stones;
    private Player owner;

    @JsonIgnore
    @Override
    public Object clone() {
        return new PitDto(this.getId(),this.getType(),this.getStones(),this.getOwner());
    }
}
