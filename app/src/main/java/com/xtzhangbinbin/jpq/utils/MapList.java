package com.xtzhangbinbin.jpq.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 同时实现List/Map功能
 * 
 * @author LiuKun
 * @param <K>
 *            键
 * @param <V>
 *            值
 */
public class MapList<K, V> {

	private List<K> keyList;

	private Map<Integer, V> valueMap;

	public MapList() {
		keyList = new ArrayList<K>();
		valueMap = new HashMap<Integer, V>();
	}

	public int size() {
		return keyList.size();
	}

	public boolean isEmpty() {
		return keyList.isEmpty();
	}

	public boolean containsKey(Object key) {
		return keyList.contains(key);
	}

	public boolean containsValue(Object value) {
		return valueMap.containsValue(value);
	}

	public K getKey(int index) {
		return keyList.get(index);
	}

	public V getValue(int index) {
		return valueMap.get(index);
	}

	public V get(Object key) {
		return valueMap.get(keyList.indexOf(key));
	}

	public V put(K key, V value) {
		keyList.add(key);
		Integer index = keyList.size() - 1;
		return valueMap.put(index, value);
	}

	public V set(int index, K key, V value) {
		keyList.set(index, key);
		return valueMap.put(index, value);
	}

	public void remove(Object key) {
		valueMap.remove(keyList.indexOf(key));
		keyList.remove(key);
	}

	public void putAll(Map<? extends K, ? extends V> map) {
		if (null != map) {
			for (Entry<? extends K, ? extends V> entry : map.entrySet()) {
				K key = entry.getKey();
				V value = entry.getValue();
				put(key, value);
			}
		}
	}

	public void clear() {
		keyList.clear();
		valueMap.clear();
	}

	public List<K> keyList() {
		return keyList;
	}

	public Collection<V> values() {
		return valueMap.values();
	}

	// start:---list---

	public Iterator<K> iterator() {
		return keyList.iterator();
	}

	public Object[] toArray() {
		return keyList.toArray();
	}

	public <T> T[] toArray(T[] a) {
		return keyList.toArray(a);
	}

	public void add(K e) {
		put(e, null);
	}

	// end:---list---

}
