package com.kukido.eclipser.configuration;

import com.kukido.eclipser.EclipserException;

public interface PreFlight {
    public void check() throws EclipserException;
}
