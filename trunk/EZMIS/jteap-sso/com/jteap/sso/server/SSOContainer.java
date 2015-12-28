package com.jteap.sso.server;

import com.jteap.system.person.model.Person;

/**
 * 
 *描述：
 *     单点登录容器,存放SESSION TOKEN PERSON之间的映射关系
 *时间：2010-2-6
 *作者：谭畅
 *
 */
public class SSOContainer {
	private static SSOContainer sc;
	private SessionTokenMAP stm = new SessionTokenMAP();
	private SessionUserMAP sum = new SessionUserMAP();
	
	private SSOContainer(){
		
	}
	
	public static SSOContainer getInstance(){
		if(sc==null)
			sc = new SSOContainer();
		return sc;
	}
	
	public void putTokenId(String tokenId,String sessionId){
		stm.put(tokenId, sessionId);
	}
	
	public void putUserInfo(String sessionId,Person person){
		sum.put(sessionId, person);
	}
	
	public void clearSessionByToken(String tokenId){
		String sessionId = stm.get(tokenId);
		stm.remove(tokenId);
		sum.remove(sessionId);
	}
	
	public String getSessionIdByToken(String tokenId){
		return stm.get(tokenId);
	}
	
	public Person getPersonBySession(String sessionId){
		return sum.get(sessionId);
	}
	

}
