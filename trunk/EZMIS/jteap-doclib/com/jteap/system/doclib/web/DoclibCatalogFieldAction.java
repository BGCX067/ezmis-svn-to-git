package com.jteap.system.doclib.web;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.web.AbstractAction;
import com.jteap.system.doclib.manager.DoclibCatalogFieldManager;

@SuppressWarnings( { "serial", "unchecked", "unused" })
public class DoclibCatalogFieldAction extends AbstractAction {

	private DoclibCatalogFieldManager doclibCatalogFieldManager;

	public DoclibCatalogFieldManager getDoclibCatalogFieldManager() {
		return doclibCatalogFieldManager;
	}

	public void setDoclibCatalogFieldManager(
			DoclibCatalogFieldManager doclibCatalogFieldManager) {
		this.doclibCatalogFieldManager = doclibCatalogFieldManager;
	}

	@Override
	public HibernateEntityDao getManager() {
		return doclibCatalogFieldManager;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[] { "id", "name", "type", "emunValue", "format",
				"sortno" };
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[] { "id", "name", "type", "emunValue", "format",
				"sortno" };
	}

}
