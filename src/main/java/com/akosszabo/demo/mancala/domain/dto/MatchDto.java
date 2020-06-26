package com.akosszabo.demo.mancala.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchDto implements Cloneable {
    private Long id;
    private MatchState matchState;

    @Override
    public Object clone() {
        MatchDto clone = new MatchDto();
        clone.setId(this.getId());
        if(this.getMatchState()!=null){
            clone.setMatchState((MatchState) this.matchState.clone());
        }
        return clone;
    }
}
