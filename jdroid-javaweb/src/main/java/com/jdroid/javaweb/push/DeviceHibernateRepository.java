package com.jdroid.javaweb.push;

import com.jdroid.javaweb.hibernate.AbstractHibernateRepository;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

public class DeviceHibernateRepository extends AbstractHibernateRepository<Device> implements DeviceRepository {
	
	protected DeviceHibernateRepository() {
		super(Device.class);
	}
	
	@Override
	public Device findByRegistrationToken(String registrationToken, DeviceType deviceType) {
		DetachedCriteria criteria = createDetachedCriteria();
		criteria.add(Restrictions.eq("registrationToken", registrationToken));
		criteria.add(Restrictions.eq("deviceType", deviceType));
		return this.findUnique(criteria);
	}
	
	@Override
	public Device findByInstanceId(String instanceId, DeviceType deviceType) {
		DetachedCriteria criteria = createDetachedCriteria();
		criteria.add(Restrictions.eq("instanceId", instanceId));
		criteria.add(Restrictions.eq("deviceType", deviceType));
		return this.findUnique(criteria);
	}
	
}
