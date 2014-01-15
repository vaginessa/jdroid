package com.jdroid.javaweb.push;

import com.jdroid.javaweb.hibernate.AbstractHibernateRepository;

public class DeviceGroupHibernateRepository extends AbstractHibernateRepository<DeviceGroup> implements
		DeviceGroupRepository {
	
	protected DeviceGroupHibernateRepository() {
		super(DeviceGroup.class);
	}
	
}
