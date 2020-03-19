package com.sid.ovli.handlers;

import com.sid.ovli.Ttaq;

/**
 * Permet de mettre chaque traitement dans une classe à part
 * afin de faciliter la maintenance.
 * 
 * @author Sid
 *
 */
public interface IHandler {
	public void perform(Ttaq ttaq);
}
