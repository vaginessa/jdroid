package com.jdroid.java.repository;

import com.jdroid.java.collections.Lists;
import com.jdroid.java.collections.Maps;
import com.jdroid.java.domain.Identifiable;
import com.jdroid.java.utils.LoggerUtils;
import com.jdroid.java.utils.ReflectionUtils;

import org.slf4j.Logger;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 
 * @param <T>
 */
public class InMemoryRepository<T extends Identifiable> implements Repository<T> {

	private static final Logger LOGGER = LoggerUtils.getLogger(InMemoryRepository.class);
	
	private long nextId = 1;
	private Map<String, T> items = Maps.newLinkedHashMap();
	
	/**
	 * @see com.jdroid.java.repository.Repository#add(com.jdroid.java.domain.Identifiable)
	 */
	@Override
	public void add(T item) {
		if (item.getId() == null) {
			ReflectionUtils.setId(item, nextId++);
		}
		items.put(item.getId(), item);
		LOGGER.debug("Added object in memory: " + item);
	}
	
	/**
	 * @see com.jdroid.java.repository.Repository#addAll(java.util.Collection)
	 */
	@Override
	public void addAll(Collection<T> items) {
		for (T item : items) {
			add(item);
		}
		LOGGER.debug("Stored objects in memory:\n" + items);
	}
	
	/**
	 * @see com.jdroid.java.repository.Repository#update(com.jdroid.java.domain.Identifiable)
	 */
	@Override
	public void update(T item) {
		add(item);
		LOGGER.debug("Updated object in memory: " + item);
	}
	
	/**
	 * @see com.jdroid.java.repository.Repository#remove(com.jdroid.java.domain.Identifiable)
	 */
	@Override
	public void remove(T item) {
		items.remove(item.getId());
		LOGGER.debug("Deleted object from memory: " + item);
	}
	
	/**
	 * @see com.jdroid.java.repository.Repository#removeAll(java.util.Collection)
	 */
	@Override
	public void removeAll(Collection<T> items) {
		for (T item : items) {
			remove(item);
		}
		LOGGER.debug("Deleted objects in memory: " + items);
	}
	
	/**
	 * @see com.jdroid.java.repository.Repository#replaceAll(java.util.Collection)
	 */
	@Override
	public void replaceAll(Collection<T> items) {
		removeAll();
		addAll(items);
	}
	
	/**
	 * @see com.jdroid.java.repository.Repository#getAll()
	 */
	@Override
	public List<T> getAll() {
		List<T> results = Lists.newArrayList(items.values());
		LOGGER.debug("Retrieved all objects [" + results.size() + "] from memory");
		return results;
	}
	
	@Override
	public T get(String id) {
		return items.get(id);
	}
	
	/**
	 * @see com.jdroid.java.repository.Repository#removeAll()
	 */
	@Override
	public void removeAll() {
		items.clear();
		LOGGER.debug("Deleted from memory all objects from this repository");
	}
	
	@Override
	public void remove(String id) {
		items.remove(id);
	}
	
	/**
	 * @see com.jdroid.java.repository.Repository#isEmpty()
	 */
	@Override
	public Boolean isEmpty() {
		return items.isEmpty();
	}
	
	/**
	 * @see com.jdroid.java.repository.Repository#getSize()
	 */
	@Override
	public Long getSize() {
		return (long)items.size();
	}
	
	/**
	 * @see com.jdroid.java.repository.Repository#findByField(java.lang.String, java.lang.Object[])
	 */
	@Override
	public List<T> findByField(String fieldName, Object... values) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * @see com.jdroid.java.repository.Repository#getAll(java.util.List)
	 */
	@Override
	public List<T> getAll(List<String> ids) {
		List<T> itemsList = Lists.newArrayList();
		for (String each : ids) {
			T item = items.get(each);
			if (item != null) {
				itemsList.add(item);
			}
		}
		return itemsList;
	}
	
	/**
	 * @see com.jdroid.java.repository.Repository#getUniqueInstance()
	 */
	@Override
	public T getUniqueInstance() {
		return items.isEmpty() ? null : items.values().iterator().next();
	}
}
