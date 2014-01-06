package fr.mcc.ginco.audit.utils;

import org.hibernate.envers.configuration.AuditConfiguration;
import org.hibernate.envers.event.EnversPreCollectionUpdateEventListenerImpl;
import org.hibernate.event.spi.PreCollectionUpdateEvent;

@SuppressWarnings("serial")
public class PreCollectionUpdateEnversListener extends
		EnversPreCollectionUpdateEventListenerImpl {

	public PreCollectionUpdateEnversListener(
			AuditConfiguration enversConfiguration) {
		super(enversConfiguration);
	}
	
	@Override
	public void onPreUpdateCollection(PreCollectionUpdateEvent event) {
		// TODO Auto-generated method stub
		if (AuditContext.getAuditStatus()) {
			super.onPreUpdateCollection(event);
		}
	}

}
