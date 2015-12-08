package com.jdroid.java.firebase;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.domain.Entity;
import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.repository.Repository;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

import java.util.Collection;
import java.util.List;

public abstract class FirebaseRepository<T extends Entity> implements Repository<T> {

	private static final Logger LOGGER = LoggerUtils.getLogger(FirebaseRepository.class);

	protected abstract String getFirebaseUrl();

	protected abstract String getPath();

	protected abstract Class<T> getEntityClass();

	private Firebase createFirebase() {
		Firebase firebase = new Firebase(getFirebaseUrl());
		firebase = firebase.child(getPath());
		return firebase;
	}

	@Override
	public T get(String id) {
		Firebase firebase = createFirebase();
		firebase = firebase.child(id);
		final FirebaseCountDownLatch done = new FirebaseCountDownLatch();
		firebase.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot snapshot) {
				done.setDataSnapshot(snapshot);
				done.countDown();
			}
			@Override
			public void onCancelled(FirebaseError firebaseError) {
				done.setFirebaseException(new FirebaseException(firebaseError));
				done.countDown();
			}
		});
		try {
			done.await();
			if (done.getFirebaseException() != null) {
				throw done.getFirebaseException();
			} else {
				T result = done.getDataSnapshot().getValue(getEntityClass());
				LOGGER.info("Retrieved object from database of path: " + getPath() + ". [ " + result + " ]");
				return result;
			}

		} catch (InterruptedException e) {
			throw new UnexpectedException(e);
		}
	}

	@Override
	public void add(T item) {
		Firebase firebase = createFirebase();
		if (item.getId() != null) {
			firebase = firebase.child(item.getId());
		} else {
			firebase = firebase.push();
		}
		final FirebaseCountDownLatch done = new FirebaseCountDownLatch();
		firebase.setValue(item, new Firebase.CompletionListener() {
			@Override
			public void onComplete(FirebaseError firebaseError, Firebase firebase) {
				if (firebaseError != null) {
					done.setFirebaseException(new FirebaseException(firebaseError));
				}
				done.countDown();
			}
		});

		try {
			done.await();
			if (done.getFirebaseException() != null) {
				throw done.getFirebaseException();
			} else {
				item.setId(firebase.getKey());
				LOGGER.info("Stored object in database: " + item);
			}

		} catch (InterruptedException e) {
			throw new UnexpectedException(e);
		}
	}

	@Override
	public void addAll(Collection<T> items) {
		for(T each : items) {
			add(each);
		}
	}

	@Override
	public void update(T item) {
		add(item);
	}

	@Override
	public List<T> findByField(String fieldName, Object... values) {
		Firebase firebase = createFirebase();
		Query query = firebase.orderByChild(fieldName);

		if (values == null) {
			throw new UnexpectedException("Null value type not supported");
		} else if (values.length > 1) {
			throw new UnexpectedException("Just one value is supported");
		}
		Object value = values[0];
		if (value instanceof String) {
			query = query.equalTo((String)value);
		} else if (value instanceof Long) {
			query = query.equalTo((Long)value);
		} else if (value instanceof Double) {
			query = query.equalTo((Double)value);
		} else if (value instanceof Integer) {
			query = query.equalTo((Integer)value);
		} else if (value instanceof Boolean) {
			query = query.equalTo((Boolean)value);
		} else {
			throw new UnexpectedException("Value type not supported");
		}

		final FirebaseCountDownLatch done = new FirebaseCountDownLatch();
		query.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot snapshot) {
				done.setDataSnapshot(snapshot);
				done.countDown();
			}
			@Override
			public void onCancelled(FirebaseError firebaseError) {
				done.setFirebaseException(new FirebaseException(firebaseError));
				done.countDown();
			}
		});
		try {
			done.await();
			if (done.getFirebaseException() != null) {
				throw done.getFirebaseException();
			} else {
				List<T> results = Lists.newArrayList();
				for (DataSnapshot eachSnapshot: done.getDataSnapshot().getChildren()) {
					results.add(eachSnapshot.getValue(getEntityClass()));
				}
				LOGGER.info("Retrieved objects [" + results.size() + "] from database of path: " + getPath() + " field: " + fieldName);
				return results;
			}

		} catch (InterruptedException e) {
			throw new UnexpectedException(e);
		}
	}

	@Override
	public List<T> getAll() {
		Firebase firebase = createFirebase();
		final FirebaseCountDownLatch done = new FirebaseCountDownLatch();
		firebase.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot snapshot) {
				done.setDataSnapshot(snapshot);
				done.countDown();
			}

			@Override
			public void onCancelled(FirebaseError firebaseError) {
				done.setFirebaseException(new FirebaseException(firebaseError));
				done.countDown();
			}
		});
		try {
			done.await();
			if (done.getFirebaseException() != null) {
				throw done.getFirebaseException();
			} else {
				List<T> results = Lists.newArrayList();
				for (DataSnapshot eachSnapshot: done.getDataSnapshot().getChildren()) {
					results.add(eachSnapshot.getValue(getEntityClass()));
				}
				LOGGER.info("Retrieved all objects [" + results.size() + "] from path: " + getPath());
				return results;
			}

		} catch (InterruptedException e) {
			throw new UnexpectedException(e);
		}
	}

	@Override
	public List<T> getAll(List<String> ids) {
		Firebase firebase = createFirebase();
		final FirebaseCountDownLatch done = new FirebaseCountDownLatch();
		firebase.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot snapshot) {
				done.setDataSnapshot(snapshot);
				done.countDown();
			}
			@Override
			public void onCancelled(FirebaseError firebaseError) {
				done.setFirebaseException(new FirebaseException(firebaseError));
				done.countDown();
			}
		});
		try {
			done.await();
			if (done.getFirebaseException() != null) {
				throw done.getFirebaseException();
			} else {
				List<T> results = Lists.newArrayList();
				for (DataSnapshot eachSnapshot: done.getDataSnapshot().getChildren()) {
					T each = eachSnapshot.getValue(getEntityClass());
					if (ids.contains(each.getId())) {
						results.add(each);
					}
				}
				LOGGER.info("Retrieved all objects [" + results.size() + "] from path: " + getPath() + " and ids: " + ids);
				return results;
			}
		} catch (InterruptedException e) {
			throw new UnexpectedException(e);
		}
	}


	@Override
	public void remove(T item) {
		remove(item.getId());
	}

	@Override
	public void removeAll() {
		innerRemove(null);
	}

	@Override
	public void removeAll(Collection<T> items) {
		for(T each : items) {
			remove(each);
		}
	}

	@Override
	public void remove(String id) {
		if (id != null) {
			innerRemove(id);
		}
	}

	private void innerRemove(String id) {
		Firebase firebase = createFirebase();
		if (id != null) {
			firebase = firebase.child(id);
		}
		final FirebaseCountDownLatch done = new FirebaseCountDownLatch();
		firebase.removeValue(new Firebase.CompletionListener() {
			@Override
			public void onComplete(FirebaseError firebaseError, Firebase firebase) {
				if (firebaseError != null) {
					done.setFirebaseException(new FirebaseException(firebaseError));
				}
				done.countDown();
			}
		});

		try {
			done.await();
			if (done.getFirebaseException() != null) {
				throw done.getFirebaseException();
			} else {
				LOGGER.trace("Deleted object in database: with id: " + id);
			}

		} catch (InterruptedException e) {
			throw new UnexpectedException(e);
		}
	}

	@Override
	public Boolean isEmpty() {
		return getSize() == 0;
	}

	@Override
	public Long getSize() {
		Firebase firebase = createFirebase();
		final FirebaseCountDownLatch done = new FirebaseCountDownLatch();
		firebase.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot snapshot) {
				done.setDataSnapshot(snapshot);
				done.countDown();
			}
			@Override
			public void onCancelled(FirebaseError firebaseError) {
				done.setFirebaseException(new FirebaseException(firebaseError));
				done.countDown();
			}
		});
		try {
			done.await();
			if (done.getFirebaseException() != null) {
				throw done.getFirebaseException();
			} else {
				return done.getDataSnapshot().getChildrenCount();
			}

		} catch (InterruptedException e) {
			throw new UnexpectedException(e);
		}
	}

	@Override
	public void replaceAll(Collection<T> items) {
		for(T each : items) {
			update(each);
		}
	}

	@Override
	public T getUniqueInstance() {
		List<T> results = getAll();
		if (!results.isEmpty()) {
			return results.get(0);
		}
		return null;
	}
}
