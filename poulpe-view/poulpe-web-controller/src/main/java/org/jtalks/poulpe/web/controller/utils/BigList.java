package org.jtalks.poulpe.web.controller.utils;

import java.util.AbstractList;

public class BigList extends AbstractList<Integer> {

	private int _sz;
    
    public BigList(int sz) {
        if (sz < 0)
            throw new IllegalArgumentException("Negative not allowed: "+sz);
        _sz = sz;
    }
    
    @Override
    public int size() {
        return _sz;
    }
    
    @Override
    public Integer get(int j) {
        return Integer.valueOf(j);
    }
}