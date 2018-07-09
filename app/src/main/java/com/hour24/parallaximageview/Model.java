package com.hour24.parallaximageview;

import lombok.Data;

/**
 * Created by N16326 on 2018. 7. 9..
 */

@Data
public class Model {

    int style;
    String url;

    public Model(int style, String url) {
        this.style = style;
        this.url = url;
    }
}
