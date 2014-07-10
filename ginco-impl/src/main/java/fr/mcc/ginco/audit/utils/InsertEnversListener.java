package fr.mcc.ginco.audit.utils;

import org.hibernate.envers.configuration.AuditConfiguration;
import org.hibernate.envers.event.EnversListener;
import org.hibernate.envers.event.EnversPostInsertEventListenerImpl;
import org.hibernate.event.spi.PostInsertEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
public class InsertEnversListener extends EnversPostInsertEventListenerImpl implements EnversListener {

	private static Logger logger = LoggerFactory.getLogger(InsertEnversListener.class);
	
	public InsertEnversListener(AuditConfiguration enversConfiguration) {
		super(enversConfiguration);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onPostInsert(PostInsertEvent event) {
		// TODO Auto-generated method stub
		if (AuditContext.getAuditStatus()) {
			super.onPostInsert(event);
		}
	}

}
