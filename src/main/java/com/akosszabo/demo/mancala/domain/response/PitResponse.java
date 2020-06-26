package com.akosszabo.demo.mancala.domain.response;

import com.akosszabo.demo.mancala.domain.PitType;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@JsonAutoDetect
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PitResponse {
    private int id;
    private PitType type;
    private int stones;
}
