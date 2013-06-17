package fr.mcc.ginco.rest.services.exceptions;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response.Status;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.slf4j.Logger;

import fr.mcc.ginco.log.Log;
import fr.mcc.ginco.utils.LabelUtil;


public class GincoOutFaultInterceptor extends AbstractPhaseInterceptor<Message> {
    public GincoOutFaultInterceptor() {
        this(Phase.PRE_STREAM);
    }

    public GincoOutFaultInterceptor(String s) {
        super(Phase.MARSHAL);
        
    }
    
    @Log
	private Logger log;
    
    private boolean handleMessageCalled;

	@Override
	public void handleMessage(Message message) throws Fault {
		handleMessageCalled = true;
        Exception ex = message.getContent(Exception.class);
        if (ex == null) {
            throw new RuntimeException("Exception is expected");
        }
        if (!(ex instanceof Fault)) {
            throw new RuntimeException("Fault is expected");
        }
        log.error("Uncaught exception in REST services : ", ex);
		HttpServletResponse response = (HttpServletResponse)message.getExchange()
	            .getInMessage().get(AbstractHTTPDestination.HTTP_RESPONSE);
	        response.setStatus(200);
	        response.setContentType("text/html");
	        try {
	        	String msg = "{success:false, message: '"+LabelUtil.getResourceLabel("unknown-exception")+"'}";
	            response.getOutputStream().write(msg.getBytes());
	            response.getOutputStream().flush();
	            message.getInterceptorChain().abort();           
	        } catch (IOException ioex) {
	            throw new RuntimeException("Error writing the response");
	        }
		
	} 
	
	protected boolean handleMessageCalled() {
        return handleMessageCalled;
    }
}
