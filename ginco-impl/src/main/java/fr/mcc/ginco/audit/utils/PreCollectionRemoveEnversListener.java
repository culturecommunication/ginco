package fr.mcc.ginco.audit.utils;

import org.hibernate.envers.configuration.AuditConfiguration;
import org.hibernate.envers.event.EnversListener;
import org.hibernate.envers.event.EnversPreCollectionRemoveEventListenerImpl;
import org.hibernate.event.spi.PreCollectionRemoveEvent;

@SuppressWarnings("serial")
public class PreCollectionRemoveEnversListener extends
		EnversPreCollectionRemoveEventListenerImpl implements EnversListener{

	public PreCollectionRemoveEnversListener(
			AuditConfiguration enversConfiguration) {
		super(enversConfiguration);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onPreRemoveCollection(PreCollectionRemoveEvent event) {
		// TODO Auto-generated method stub
		if (AuditContext.getAuditStatus()) {
			super.onPreRemoveCollection(event);
		}
	}

}
