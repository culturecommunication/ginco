/**
 * Copyright or © or Copr. Ministère Français chargé de la Culture
 * et de la Communication (2013)
 * <p/>
 * contact.gincoculture_at_gouv.fr
 * <p/>
 * This software is a computer program whose purpose is to provide a thesaurus
 * management solution.
 * <p/>
 * This software is governed by the CeCILL license under French law and
 * abiding by the rules of distribution of free software. You can use,
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 * <p/>
 * As a counterpart to the access to the source code and rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty and the software's author, the holder of the
 * economic rights, and the successive licensors have only limited liability.
 * <p/>
 * In this respect, the user's attention is drawn to the risks associated
 * with loading, using, modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean that it is complicated to manipulate, and that also
 * therefore means that it is reserved for developers and experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systemsand/or
 * data to be ensured and, more generally, to use and operate it in the
 * same conditions as regards security.
 * <p/>
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 */
package fr.mcc.ginco.journal;

import java.lang.reflect.Field;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;


import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;
import fr.mcc.ginco.beans.IBaseBean;
import fr.mcc.ginco.beans.users.IUser;
import fr.mcc.ginco.journal.GincoLog.Action;
import fr.mcc.ginco.journal.GincoLog.EntityType;
import fr.mcc.ginco.log.Log;
import fr.mcc.ginco.services.ILogJournalService;

public class GincoLogAspectTest {

	@Mock(name = "logJournalService")
	private ILogJournalService logJournalService;

	@InjectMocks
	private GincoLogAspect aspect = new GincoLogAspect();

	@Mock
	private Appender mockAppender;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		Mockito.when(mockAppender.getName()).thenReturn("MOCK");      
		ReflectionUtils.doWithFields(aspect.getClass(),
				new ReflectionUtils.FieldCallback() {

					public void doWith(Field field)
							throws IllegalArgumentException,
							IllegalAccessException {
						ReflectionUtils.makeAccessible(field);

						if (field.getAnnotation(Log.class) != null) {
							/*Logger logger = LoggerFactory.getLogger(aspect
									.getClass());*/
							ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(aspect.getClass());
							root.addAppender(mockAppender);
							field.set(aspect, root);
						}
					}
		});
	}
	
	
	@Test
	public void testLogActionNoArguments() throws Throwable {
		GincoLog gincoLog = Mockito.mock(GincoLog.class);
		Mockito.when(gincoLog.entityType()).thenReturn(EntityType.THESAURUS);
		Mockito.when(gincoLog.action()).thenReturn(Action.CREATE);
		
		ProceedingJoinPoint pjp = Mockito.mock(ProceedingJoinPoint.class);
		Mockito.when(pjp.proceed()).thenReturn(new Object());
		Mockito.when(pjp.getArgs()).thenReturn(new Object[0]);
		aspect.logAction(pjp, gincoLog);		
		
		Mockito.verify(mockAppender).doAppend(Mockito.argThat(new ArgumentMatcher() {
		      @Override
		      public boolean matches(final Object argument) {
		       return ((LoggingEvent)argument).getFormattedMessage().contains("Some data are missing in order to process correctly the GincoLog annotation");
		      }
		}));
		
	
		
	}
	@Test
	public void testLogActionOneArgumentBean() throws Throwable {
		GincoLog gincoLog = Mockito.mock(GincoLog.class);
		Mockito.when(gincoLog.entityType()).thenReturn(EntityType.THESAURUS);
		Mockito.when(gincoLog.action()).thenReturn(Action.CREATE);		
		ProceedingJoinPoint pjp = Mockito.mock(ProceedingJoinPoint.class);
		Mockito.when(pjp.proceed()).thenReturn(new Object());
		
		Object[] args = new Object[1];
		args[0] = Mockito.mock(IBaseBean.class);
		Mockito.when(pjp.getArgs()).thenReturn(args);
		
		aspect.logAction(pjp, gincoLog);
		
		Mockito.verify(mockAppender).doAppend(Mockito.argThat(new ArgumentMatcher() {
		      @Override
		      public boolean matches(final Object argument) {
		       return ((LoggingEvent)argument).getFormattedMessage().contains("Some data are missing in order to process correctly the GincoLog annotation");
		      }
		}));
	}
	
	@Test
	public void testLogActionOneArgumentUser() throws Throwable {
		GincoLog gincoLog = Mockito.mock(GincoLog.class);
		Mockito.when(gincoLog.entityType()).thenReturn(EntityType.THESAURUS);
		Mockito.when(gincoLog.action()).thenReturn(Action.CREATE);
		
		ProceedingJoinPoint pjp = Mockito.mock(ProceedingJoinPoint.class);
		Mockito.when(pjp.proceed()).thenReturn(new Object());
		
		Object[] args = new Object[1];		
		args[0] = Mockito.mock(IUser.class);		
		Mockito.when(pjp.getArgs()).thenReturn(args);
		
		aspect.logAction(pjp, gincoLog);
		
		Mockito.verify(mockAppender).doAppend(Mockito.argThat(new ArgumentMatcher() {
		      @Override
		      public boolean matches(final Object argument) {
		       return ((LoggingEvent)argument).getFormattedMessage().contains("Some data are missing in order to process correctly the GincoLog annotation");
		      }
		}));	
		
	}
	
	@Test
	public void testLogActionAllArguments() throws Throwable {
		GincoLog gincoLog = Mockito.mock(GincoLog.class);
		Mockito.when(gincoLog.entityType()).thenReturn(EntityType.THESAURUS);
		Mockito.when(gincoLog.action()).thenReturn(Action.CREATE);
		
		ProceedingJoinPoint pjp = Mockito.mock(ProceedingJoinPoint.class);
		Mockito.when(pjp.proceed()).thenReturn(new Object());
		
		Object[] args = new Object[2];
		IUser usr = Mockito.mock(IUser.class);
		Mockito.when(usr.getName()).thenReturn("John");
		args[0] = usr;		
		IBaseBean basebean = Mockito.mock(IBaseBean.class);
		Mockito.when(basebean.getId()).thenReturn("beanId");
		args[1] = basebean;		

		Mockito.when(pjp.getArgs()).thenReturn(args);
		
		aspect.logAction(pjp, gincoLog);
		
		Mockito.verify(mockAppender).doAppend(Mockito.argThat(new ArgumentMatcher() {
		      @Override
		      public boolean matches(final Object argument) {
		       return ((LoggingEvent)argument).getFormattedMessage().contains("New LogJournal entry has been saved");
		      }
		}));	
		
	}
	
}
