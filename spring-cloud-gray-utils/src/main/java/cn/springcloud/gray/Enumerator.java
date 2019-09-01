package cn.springcloud.gray;

import java.util.*;

public class Enumerator<T> implements Enumeration<T> {
    // ~ Instance fields
    // ================================================================================================

    /**
     * The <code>Iterator</code> over which the <code>Enumeration</code> represented by
     * this class actually operates.
     */
    private Iterator<T> iterator = null;

    // ~ Constructors
    // ===================================================================================================

    /**
     * Return an Enumeration over the values of the specified Collection.
     *
     * @param collection Collection whose values should be enumerated
     */
    public Enumerator(Collection<T> collection) {
        this(collection.iterator());
    }

    /**
     * Return an Enumeration over the values of the specified Collection.
     *
     * @param collection Collection whose values should be enumerated
     * @param clone true to clone iterator
     */
    public Enumerator(Collection<T> collection, boolean clone) {
        this(collection.iterator(), clone);
    }

    /**
     * Return an Enumeration over the values returned by the specified Iterator.
     *
     * @param iterator Iterator to be wrapped
     */
    public Enumerator(Iterator<T> iterator) {
        this.iterator = iterator;
    }

    /**
     * Return an Enumeration over the values returned by the specified Iterator.
     *
     * @param iterator Iterator to be wrapped
     * @param clone true to clone iterator
     */
    public Enumerator(Iterator<T> iterator, boolean clone) {

        if (!clone) {
            this.iterator = iterator;
        }
        else {
            List<T> list = new ArrayList<>();

            while (iterator.hasNext()) {
                list.add(iterator.next());
            }

            this.iterator = list.iterator();
        }
    }

    /**
     * Return an Enumeration over the values of the specified Map.
     *
     * @param map Map whose values should be enumerated
     */
    public Enumerator(Map<?, T> map) {
        this(map.values().iterator());
    }

    /**
     * Return an Enumeration over the values of the specified Map.
     *
     * @param map Map whose values should be enumerated
     * @param clone true to clone iterator
     */
    public Enumerator(Map<?, T> map, boolean clone) {
        this(map.values().iterator(), clone);
    }

    // ~ Methods
    // ========================================================================================================

    /**
     * Tests if this enumeration contains more elements.
     *
     * @return <code>true</code> if and only if this enumeration object contains at least
     * one more element to provide, <code>false</code> otherwise
     */
    public boolean hasMoreElements() {
        return (iterator.hasNext());
    }

    /**
     * Returns the next element of this enumeration if this enumeration has at least one
     * more element to provide.
     *
     * @return the next element of this enumeration
     *
     * @exception NoSuchElementException if no more elements exist
     */
    public T nextElement() throws NoSuchElementException {
        return (iterator.next());
    }
}
