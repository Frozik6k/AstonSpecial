package ru.Frozik6k.collection;

import java.util.Map;
import java.util.Objects;

public class MyHashMap<K, V> {
    private final int MAX_ARRAY = 128;

    static class Node<K, V> implements Map.Entry<K, V> {
        final int hash;
        final K key;
        private V value;
        MyHashMap.Node<K, V> next;

        Node(int hash, K key, V value) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = null;
        }

        public final K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            this.value = value;
            return value;
        }

        public final String toString() {
            return key.toString() + " " + value.toString();
        }

        public final int hashCode() {
            return Objects.hash(key, value);
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Node<?, ?> node = (Node<?, ?>) o;
            return Objects.equals(key, node.key) && Objects.equals(value, node.value);
        }


    }

    private Node<K, V>[] table = new Node[MAX_ARRAY];

    public V put(K key, V value) {
        int hash = key.hashCode();
        int i = (MAX_ARRAY - 1) & hash;
        Node<K, V> node = table[i];

        if (node == null) {
            table[i] = new Node<>(hash, key, value);
            return value;
        }

        while (true) {
            if (node.getKey().hashCode() == hash && node.getKey().equals(key)) {
                node.setValue(value);
                return value;
            }
            if (node.next == null) {
                node.next = new Node<>(hash, key, value);
                return value;
            }
            node = node.next;
        }
    }


    public V get(Object key) {
        int hash = key.hashCode();
        int i = (MAX_ARRAY - 1) & hash;
        Node<K, V> node = table[i];
        while (node != null) {
            if (node.getKey().hashCode() == hash && node.getKey().equals(key)) {
                return node.getValue();
            }
            node = node.next;
        }
        return null;
    }


    public V remove(Object key) {
        int hash = key.hashCode();
        int i = (MAX_ARRAY - 1) & hash;
        Node<K, V> node = table[i];

        if (node == null) {
            return null;
        }

        if (node.getKey().hashCode() == hash && node.getKey().equals(key)) {
            V value = node.value;
            table[i] = node.next;
            return value;
        }

        Node<K, V> next;
        while ((next = node.next) != null) {
            if (next.getKey().hashCode() == hash && next.getKey().equals(key)) {
                node.next = next.next;
                return next.value;
            }
            node = next;
        }

        return null;

    }
}
