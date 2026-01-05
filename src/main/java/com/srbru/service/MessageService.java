package com.srbru.service;

public interface MessageService
{
	public void loadMessagesFromDB();

	public String getMessage(String windowId, String eventId);
}
