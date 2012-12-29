/**
 * Copyright (C) 2011  JTalks.org Team
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.jtalks.poulpe.web.controller;

/**
 * We need to mock out external systems like JCommune while starting Poulpe locally or test environment where we don't
 * have real system to integrate with. In order to test integration features, we'll need to mock the responses from
 * these depended-on components. <p>The mock is going to be configurable on one of Poulpe pages whether it should answer
 * with errors or with valid responses and if responses are valid, what data should be returned.</p>
 *
 * @author stanislav bashkirtsev
 */
public class JcommuneMockSettingsVm {
    private boolean jcommuneAvailable;

    /**
     * Determines whether JCommune is available on HTTP.
     *
     * @return false if mock should return errors instead of successful results, true if mock should act as JCommune is
     *         available and works fine
     */
    public boolean isJcommuneAvailable() {
        return jcommuneAvailable;
    }

    /**
     * Sets mock should act as JCommune fails to process requests or as it's working fine.
     *
     * @param jcommuneAvailable set true if you want mock to send errors as responses instead of valid answers
     */
    public void setJcommuneAvailable(boolean jcommuneAvailable) {
        this.jcommuneAvailable = jcommuneAvailable;
    }
}
