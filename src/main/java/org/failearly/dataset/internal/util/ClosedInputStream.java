/*
 * dataSet - Test Support For Datastores.
 *
 * Copyright (C) 2014-2014 Marko Umek (http://fail-early.com/contact)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.failearly.dataset.internal.util;

import java.io.IOException;
import java.io.InputStream;

/**
 * {@link org.failearly.dataset.internal.util.ClosedInputStream#read} returns always -1.
 */
final class ClosedInputStream extends InputStream {

    public static final InputStream CLOSED_INPUT_STREAM=new ClosedInputStream();

    private ClosedInputStream() {
    }

    @Override
    public int read() throws IOException {
        return -1;
    }
}
