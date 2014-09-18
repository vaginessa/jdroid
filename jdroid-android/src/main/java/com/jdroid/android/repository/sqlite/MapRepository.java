package com.jdroid.android.repository.sqlite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.jdroid.android.sqlite.Column;
import com.jdroid.android.sqlite.SQLiteHelper;

/**
 * Generic repository to store {@link Map} objects.
 * 
 */
public abstract class MapRepository extends StringEntityRepository {
	
	public MapRepository(SQLiteHelper dbHelper) {
		super(dbHelper);
	}
	
	/**
	 * This method returns the map of children associated with given parent id.
	 * 
	 * @param parentId of parent entity.
	 * @return map of children
	 */
	public Map<Long, String> getChildrenMap(Long parentId) {
		Map<Long, String> map = new HashMap<Long, String>();
		List<StringEntity> children = findByField(Column.PARENT_ID, parentId);
		for (StringEntity stringEntity : children) {
			map.put(stringEntity.getId(), stringEntity.getValue());
		}
		return map;
	}
	
	/**
	 * This method allows to replace all children of a given parent, it will remove any children which are not in the
	 * list, add the new ones and update which are in the list.
	 * 
	 * @param map map of list to replace.
	 * @param parentId id of parent entity.
	 */
	public void replaceMapChildren(Map<Long, String> map, Long parentId) {
		ArrayList<StringEntity> entities = new ArrayList<StringEntity>();
		for (Long key : map.keySet()) {
			StringEntity entity = new StringEntity();
			entity.setParentId(parentId);
			entity.setId(key);
			entity.setValue(map.get(key));
			entities.add(entity);
		}
		replaceChildren(entities, parentId);
	}
}
