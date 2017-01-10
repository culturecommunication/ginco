package fr.mcc.ginco.utils;

import java.io.Serializable;

import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IntegralDataTypeHolder;
import org.hibernate.id.IdentifierGeneratorHelper.BigDecimalHolder;
import org.hibernate.id.SequenceGenerator;

public class StringSequenceGenerator extends SequenceGenerator {
	@Override
    public Serializable generate(SessionImplementor session, Object obj) {
        return super.generate( session, obj ).toString();
    }

    protected IntegralDataTypeHolder buildHolder() {
        return new BigDecimalHolder();
    }

}
