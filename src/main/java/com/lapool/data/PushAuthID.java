/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lapool.data;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.owlike.genson.annotation.JsonProperty;

/**
 *
 * @author akshat
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        setterVisibility = JsonAutoDetect.Visibility.NONE)
public class PushAuthID {

    @JsonProperty("$in")
    private Long[] in;

    @com.owlike.genson.annotation.JsonIgnore
    public Long[] getIn() {
        return in;
    }

    @com.owlike.genson.annotation.JsonIgnore
    public void setIn(Long[] in) {
        this.in = in;
    }

}
