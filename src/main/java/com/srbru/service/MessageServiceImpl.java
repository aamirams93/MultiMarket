package com.srbru.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.srbru.entity.MsgEntity;
import com.srbru.repo.MsgRepo;

@Service
public class MessageServiceImpl implements MessageService
{
	private final MsgRepo msgRepository;

	private final Map<String, String> messageCache = new ConcurrentHashMap<>();

	public MessageServiceImpl(MsgRepo msgRepository)
	{
		this.msgRepository = msgRepository;
	}

	@Override
	@Scheduled(fixedRate = 5 * 60 * 1000)
	public void loadMessagesFromDB()
	{
		Map<String, String> tempCache = new ConcurrentHashMap<>();
		List<MsgEntity> messages = msgRepository.findAll();

		for (MsgEntity msg : messages)
		{
			String key = buildKey(msg.getWindowId(), msg.getEventId());
			tempCache.put(key, msg.getMessage());
		}

		// Atomic swap to update cache
		messageCache.clear();
		messageCache.putAll(tempCache);
		System.out.println("♻ Messages refreshed. Total count: " + messageCache.size());

	}

	// Build a unique key for each message
	private String buildKey(String windowId, String eventId)
	{
		return windowId + "::" + eventId;
	}

	@Override
	public String getMessage(String windowId, String eventId)
	{
		String key = buildKey(windowId, eventId);
		String message = messageCache.get(key);

		if (message == null)
		{
			// Lazy-load from DB
			MsgEntity msg = msgRepository.findByWindowIdAndEventId(windowId, eventId);
			if (msg != null)
			{
				message = msg.getMessage();
				messageCache.put(key, message); // cache for future requests
			} else
			{
				message = "Message not found";
			}
		}
		return message;
	}

}
