package com.jdroid.javaweb.push;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import com.jdroid.javaweb.hibernate.AbstractHibernateRepository;

public class DeviceHibernateRepository extends AbstractHibernateRepository<Device> implements DeviceRepository {
	
	protected DeviceHibernateRepository() {
		super(Device.class);
	}
	
	/**
	 * @see com.jdroid.javaweb.push.DeviceRepository#find(java.lang.String, com.jdroid.javaweb.push.DeviceType)
	 */
	@Override
	public Device find(String deviceId, DeviceType deviceType) {
		DetachedCriteria criteria = createDetachedCriteria();
		criteria.add(Restrictions.eq("deviceId", deviceId));
		criteria.add(Restrictions.eq("deviceType", deviceType));
		return this.findUnique(criteria);
	}
	
	/**
	 * @see com.jdroid.javaweb.push.DeviceRepository#find(java.lang.String, com.jdroid.javaweb.push.DeviceType,
	 *      java.lang.String)
	 */
	@Override
	public Device find(String deviceId, DeviceType deviceType, String registrationId) {
		DetachedCriteria criteria = createDetachedCriteria();
		criteria.add(Restrictions.eq("deviceId", deviceId));
		criteria.add(Restrictions.eq("deviceType", deviceType));
		criteria.add(Restrictions.eq("registrationId", registrationId));
		return this.findUnique(criteria);
	}
	
}
