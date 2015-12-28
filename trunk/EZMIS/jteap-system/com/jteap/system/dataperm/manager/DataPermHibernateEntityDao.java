package com.jteap.system.dataperm.manager;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.system.dataperm.util.DataPermAdviceInterface;

public class DataPermHibernateEntityDao<T> extends HibernateEntityDao<T> implements DataPermAdviceInterface {

}
