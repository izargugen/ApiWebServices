package com.sid.ovli.handlers;

import com.sid.ovli.Ttaq;

public class BtnAnnulerHandler implements IHandler {

	Ttaq ttaq;
	
	public void perform(Ttaq ttaq) {
		this.ttaq = ttaq;
		
		ttaq.exit = true;
	}

}
