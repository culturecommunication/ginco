package fr.mcc.ginco.audit.utils;

import org.hibernate.envers.configuration.AuditConfiguration;
import org.hibernate.envers.event.EnversPostUpdateEventListenerImpl;
import org.hibernate.event.spi.PostUpdateEvent;

@SuppressWarnings("serial")
public class UpdateEnversListerner extends EnversPostUpdateEventListenerImpl {

	public UpdateEnversListerner(AuditConfiguration enversConfiguration) {
		super(enversConfiguration);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onPostUpdate(PostUpdateEvent event) {
		// TODO Auto-generated method stub
		if (AuditContext.getAuditStatus()) {
			super.onPostUpdate(event);
		}
	}

}
