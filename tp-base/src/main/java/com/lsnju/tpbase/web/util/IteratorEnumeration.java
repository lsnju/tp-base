package com.lsnju.tpbase.web.util;

import java.util.Enumeration;
import java.util.Iterator;

/**
 *
 * @author lisong
 * @since 2022/12/12 14:43
 * @version V1.0
 */
public class IteratorEnumeration<E> implements Enumeration<E> {

    /** The iterator being decorated. */
    private Iterator<? extends E> iterator;

    public IteratorEnumeration() {
    }

    public IteratorEnumeration(final Iterator<? extends E> iterator) {
        this.iterator = iterator;
    }

    // Iterator interface
    //-------------------------------------------------------------------------

    @Override
    public boolean hasMoreElements() {
        return iterator.hasNext();
    }

    @Override
    public E nextElement() {
        return iterator.next();
    }

    public Iterator<? extends E> getIterator() {
        return iterator;
    }

    public void setIterator(final Iterator<? extends E> iterator) {
        this.iterator = iterator;
    }

}
