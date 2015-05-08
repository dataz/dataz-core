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
package org.failearly.dataset.internal.generator.standard;

import org.failearly.dataset.generator.support.LimitedGeneratorBase;
import org.failearly.dataset.generator.LoopGenerator;
import org.failearly.dataset.generator.RangeGenerator;

import java.util.Iterator;

/**
 * RangeGeneratorImpl generates {@link org.failearly.dataset.generator.RangeGenerator} and
 * {@link org.failearly.dataset.generator.LoopGenerator}.
*/
// Must be public for Velocity!
public final class RangeGeneratorImpl extends LimitedGeneratorBase<Integer> {

    private final int start;
    private final int end;

    RangeGeneratorImpl(RangeGenerator rangeGenerator) {
        this(rangeGenerator.name(), rangeGenerator.dataset(), rangeGenerator.start(), rangeGenerator.end());
    }

    RangeGeneratorImpl(LoopGenerator rangeGenerator) {
        this(rangeGenerator.name(), rangeGenerator.dataset(), 1, rangeGenerator.size());
    }

    private RangeGeneratorImpl(String name, String dataset, int start, int end) {
        super(name, dataset);

        this.start = start;
        this.end = end;

        assert start <= end : "RangeGenerator: start > end";
    }

    @Override
    public Iterator<Integer> createIterator() {
        return new Iterator<Integer>() {
            private int curr=start;
            @Override
            public boolean hasNext() {
                return curr<=end;
            }

            @Override
            public Integer next() {
                Integer next=curr;
                curr+=1;
                return next;
            }
        };
    }


    @Override
    public String toString() {
        return "RangeGenerator{" +
                "dataset=" + super.dataset() +
                ", name=" + super.name() +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}
