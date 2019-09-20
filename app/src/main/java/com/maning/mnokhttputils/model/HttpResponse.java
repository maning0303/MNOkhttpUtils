package com.maning.mnokhttputils.model;

public class HttpResponse<T> {

    private boolean error;
    private T results;

    public T getResults() {
        return results;
    }

    public void setResults(T results) {
        this.results = results;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }
}
