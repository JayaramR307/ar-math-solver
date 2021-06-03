package com.genedev.mathsolver.data;

import com.google.gson.annotations.SerializedName;

public class WolframResult {
    @SerializedName("queryresult")
    private QueryResult result;

    public QueryResult getResult(){
        return result;
    }
}
