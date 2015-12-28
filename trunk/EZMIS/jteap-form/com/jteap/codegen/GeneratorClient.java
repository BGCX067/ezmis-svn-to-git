package com.jteap.codegen;



/**
 * 
 * @author tantyou
 */
@SuppressWarnings("unused")
public class GeneratorClient {
	public static void main(String[] args) throws Exception {
		JteapGenerator pg = new JteapGenerator();
		pg.clean();
		pg.generateServerSide("TB_FORM_12750207766433", "system", "model", "CL12750207766433", false);
	}
}
