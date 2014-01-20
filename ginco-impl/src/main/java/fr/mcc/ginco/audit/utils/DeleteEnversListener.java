package fr.mcc.ginco.audit.utils;

import org.hibernate.envers.configuration.AuditConfiguration;
import org.hibernate.envers.event.EnversPostDeleteEventListenerImpl;
import org.hibernate.event.spi.PostDeleteEvent;


@SuppressWarnings("serial")
public class DeleteEnversListener extends EnversPostDeleteEventListenerImpl {

	public DeleteEnversListener(AuditConfiguration enversConfiguration) {
		super(enversConfiguration);
		// TODO Auto-generated constructor stub
	}
	
	@Override
    public void onPostDelete(PostDeleteEvent event) {
    }   

}
