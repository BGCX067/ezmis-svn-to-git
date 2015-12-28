package com.jteap.jhtj.sjflsz.manager;

import java.util.List;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.jhtj.sjflsz.model.TjItemKindKey;
@SuppressWarnings({ "unchecked", "serial" })
public class TjItemKindKeyManager extends HibernateEntityDao<TjItemKindKey> {
	public TjItemKindKey findTjItemKindKeyByKidAndIcode(String kid,String icode){
		TjItemKindKey key=null;
		String hql="from TjItemKindKey where kid='"+kid+"' and icode='"+icode+"'";
		List<TjItemKindKey>keyList= this.find(hql);
		if(keyList.size()>0){
			key=keyList.get(0);
		}
		return key;
	}
}
