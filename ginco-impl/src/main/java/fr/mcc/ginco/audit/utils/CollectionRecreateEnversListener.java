package fr.mcc.ginco.audit.utils;

import org.hibernate.envers.configuration.AuditConfiguration;
import org.hibernate.envers.event.EnversListener;
import org.hibernate.envers.event.EnversPostCollectionRecreateEventListenerImpl;
import org.hibernate.event.spi.PostCollectionRecreateEvent;

@SuppressWarnings("serial")
public class CollectionRecreateEnversListener extends
		EnversPostCollectionRecreateEventListenerImpl implements EnversListener {

	public CollectionRecreateEnversListener(
			AuditConfiguration enversConfiguration) {
		super(enversConfiguration);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onPostRecreateCollection(PostCollectionRecreateEvent event) {
		// TODO Auto-generated method stub
		if (AuditContext.getAuditStatus()) {
			super.onPostRecreateCollection(event);
		}
	}

}
